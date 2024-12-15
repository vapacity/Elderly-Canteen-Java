package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.*;
import com.javaee.elderlycanteen.dto.cart.*;
import com.javaee.elderlycanteen.dto.finance.DeductBalanceResponseDto;
import com.javaee.elderlycanteen.dto.order.OrderInfoDto;
import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.entity.Cart;
import com.javaee.elderlycanteen.entity.CartItem;
import com.javaee.elderlycanteen.entity.OrderInfo;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.utils.DateUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.javaee.elderlycanteen.utils.DateUtils.getCurrentDate;

@Service
@Transactional
public class CartService {

    private final AccountDao accountDao;
    private final CartDao cartDao;
    private final CartItemDao cartItemDao;

    private final FinanceService financeService;
    private final WeekMenuService weekMenuService;
    private final OrderInfoService orderInfoService;
    private final SystemLogsService systemLogsService;


    @Autowired
    public CartService(AccountDao accountDao, CartDao cartDao,CartItemDao cartItemDao,
                       FinanceService financeService, WeekMenuService weekMenuService,
                       OrderInfoService orderInfoService, SystemLogsService systemLogsService) {
        this.accountDao = accountDao;
        this.cartDao = cartDao;
        this.cartItemDao = cartItemDao;

        this.financeService = financeService;
        this.weekMenuService = weekMenuService;
        this.orderInfoService = orderInfoService;
        this.systemLogsService = systemLogsService;

    }

    public CartResponseDto createCart(Integer accountId) throws ParseException {
        Date date = getCurrentDate();
        // 查找创建时间为今天的购物车
        List<Cart> existedCarts = this.cartDao.getCartsByAccountIdAndCreatedTime(accountId, date);
        // 查看所有用户今天已存在的购物车
        for (Cart cart : existedCarts) {
            List<OrderInfo> orderInfos = this.orderInfoService.getOrderInfoByCartId(cart.getCartId());
            if(orderInfos.isEmpty()){
                // 返回今天创建的购物车
                CartResponseDto.CartResponse cartResponse = new CartResponseDto.CartResponse(cart.getCartId(),cart.getCreatedTime(),cart.getUpdatedTime());
                CartResponseDto cartResponseDto = new CartResponseDto(cartResponse,"cart already existed!",Boolean.TRUE);

                return cartResponseDto;
            }
        }

        // 未找到已存在的购物车，创建新购物车
        Cart cart = new Cart();

        cart.setAccountId(accountId);
        cart.setCreatedTime(date);
        cart.setUpdatedTime(date);

        this.cartDao.insert(cart);

        CartResponseDto.CartResponse cartResponse = new CartResponseDto.CartResponse(cart.getCartId(),cart.getCreatedTime(),cart.getUpdatedTime());
        CartResponseDto cartResponseDto = new CartResponseDto(cartResponse,"cart created successfully!",Boolean.TRUE);

        return cartResponseDto;
    }

    public CartResponseDto deleteCart(Integer accountId) {
        // 查找与accountId的所有购物车
        List<Cart> carts = this.cartDao.getCartsByAccountId(accountId);
        if(!carts.isEmpty()){
            for (Cart cart : carts) {
                List<OrderInfo> orderInfos = this.orderInfoService.getOrderInfoByCartId(cart.getCartId());
                if(orderInfos.isEmpty()){
                    // 如果购物车不在订单中，则删除该购物车
                    this.cartDao.deleteCartByCartId(cart.getCartId());
                    // 返回成功消息
                    CartResponseDto.CartResponse cartResponse = new CartResponseDto.CartResponse(cart.getCartId(),cart.getCreatedTime(),cart.getUpdatedTime());
                    CartResponseDto responseDto = new CartResponseDto(cartResponse,"cart deleted successfully!",Boolean.TRUE);

                    return responseDto;
                }
            }
            // 如果所有购物车都关联了订单，则返回错误信息
            CartResponseDto responseDto = new CartResponseDto(new CartResponseDto.CartResponse(),"all carts have been associated with orders!",Boolean.FALSE);

            return responseDto;
        }else{
            // 没有找到购物车，返回错误信息
            CartResponseDto responseDto = new CartResponseDto(new CartResponseDto.CartResponse(),"No shopping cart associated with the user was found!",Boolean.FALSE);

            return responseDto;
        }
    }

    public Cart getCartByCartId(Integer cartId) {
        Cart cart = this.cartDao.getCartByCartId(cartId);
        if(cart == null) {
            throw new ServiceException("No cart found!");
        }
        return cart;
    }

    public CartItemResponseDto ensureCart(EnsureCartRequestDto Dto,Integer accountId) throws ParseException {
        // 验证CartId是否存在
        Cart cart = this.cartDao.getCartByCartIdAndAccountId(Dto.getCartId(),accountId);
        if(cart == null) {
            throw new ServiceException("Cart not found!");
        }

        // 验证CartId是否在OrderInfo表中
        this.orderInfoService.getOrderInfoByCartId(cart.getCartId());

        // 获取全部购物车项
        List<CartItem> cartItems = this.cartItemDao.getCartItemByCartId(Dto.getCartId());
        if(cartItems == null){
            throw new ServiceException("cart item is null or cartId is wrong");
        }
        // 调用库存管理服务检查并减少库存
        for (CartItem cartItem : cartItems) {
            Boolean repoReduced = this.weekMenuService.checkAndReduceRestock(cartItem.getDishId(),cartItem.getWeek(),cartItem.getQuantity());
            if(!repoReduced) {
                throw new ServiceException("Restock is not enough!");
            }
        }

        // 调用财务管理服务扣除用户余额
        Double totalPrice = this.orderInfoService.calculateTotalPrice(cartItems);
        DeductBalanceResponseDto deductBalanceResponseDto = this.financeService.DeductBalance(accountId,totalPrice);
        // 扣除余额失败
        if(deductBalanceResponseDto.success == false) {
            CartItemResponseDto responseDto = new CartItemResponseDto(Boolean.FALSE,deductBalanceResponseDto.msg);
            return responseDto;
        }

        //调用订单管理服务生成订单
        OrderInfoDto orderInfoDto = this.orderInfoService.createOrder(Dto.getCartId(),accountId,
                deductBalanceResponseDto.financeId,
                Dto.getDeliverOrDining(),cartItems,
                Optional.ofNullable(Dto.getNewAddress()), Optional.ofNullable(Dto.getRemark()));

        if(orderInfoDto.getSuccess()==Boolean.FALSE) {
            CartItemResponseDto responseDto = new CartItemResponseDto(Boolean.FALSE,orderInfoDto.getMsg());
            return responseDto;
        }

        // 更新默认地址
        Account account = this.accountDao.getAccountById(accountId);
        if(Dto.getSetDefaultAdd()==Boolean.TRUE) {
            Integer ret = this.accountDao.updateAccountAddress(accountId,account.getAddress());
            if(ret != 1) {
                throw new ServiceException("Fail to update account address!");
            }
        }

        // 返回更新成功信息
        CartItemResponseDto responseDto = new CartItemResponseDto(Boolean.TRUE,"Order process successfully!");
        return responseDto;
    }

    public ClearCartResponseDto clearCart(NormalRequestDto Dto) {
        // 检查购物车是否存在
        this.getCartByCartId(Dto.getCartId());

        // 检查是否关联订单
        List<OrderInfo> orderInfos = this.orderInfoService.getOrderInfoByCartId(Dto.getCartId());
        if(!orderInfos.isEmpty()) {
            throw new ServiceException("Cart is associated with order!");
        }

        //删除购物车的购物车项
        this.cartItemDao.deleteCartItemByCartId(Dto.getCartId());

        ClearCartResponseDto responseDto = new ClearCartResponseDto("clear cart successfully!",Boolean.TRUE);

        return responseDto;
    }

    public Boolean DeleteUnassociatedCarts() throws ParseException {

        Date date = getCurrentDate();

        // 查找所有创建日期不为今天的购物车
        List<Cart> carts = this.cartDao.getUnassociatedCarts(date);
        List<Cart> cartsToDelete = new ArrayList<Cart>();

        for (Cart cart : carts) {
            // 检查每个购物车是否未关联订单
            List<OrderInfo> orderInfos = this.orderInfoService.getOrderInfoByCartId(cart.getCartId());
            if(orderInfos.isEmpty()) {
                cartsToDelete.add(cart);
            }
        }

        // 删除符合条件的购物车
        for (Cart cart : cartsToDelete) {
            // 删除该购物车的所有购物车项
            this.cartItemDao.deleteCartItemByCartId(cart.getCartId());
            // 删除购物车本身
            this.cartDao.deleteCartByCartId(cart.getCartId());
        }

        // 添加系统日志
        this.systemLogsService.addSystemLog("Safe","The expired shopping cart has been removed!");

        return Boolean.TRUE;
    }

}
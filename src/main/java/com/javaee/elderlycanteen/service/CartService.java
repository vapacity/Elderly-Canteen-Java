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
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final AccountDao accountDao;
    private final CartDao cartDao;
    private final CartItemDao cartItemDao;

    private final FinanceService financeService;
    private final WeekMenuService weekMenuService;
    private final OrderInfoService orderInfoService;


    @Autowired
    public CartService(AccountDao accountDao, CartDao cartDao,CartItemDao cartItemDao, FinanceService financeService, WeekMenuService weekMenuService,OrderInfoService orderInfoService) {
        this.accountDao = accountDao;
        this.cartDao = cartDao;
        this.cartItemDao = cartItemDao;

        this.financeService = financeService;
        this.weekMenuService = weekMenuService;
        this.orderInfoService = orderInfoService;

    }

    // 新增购物车
    public Integer addCart(Cart cart) {
        Integer ret = this.cartDao.insert(cart);
        if(ret != 1) {
            throw new ServiceException("Fail to create cart!");
        }
        return ret;
    }

    //查找全部购物车
    public List<Cart> getAllCart() {
        List<Cart> carts = this.cartDao.findAll();
        if(carts == null) {
            throw new ServiceException("No carts found!");
        }
        return carts;
    }


    //删除购物车
    public Integer deleteCart(Integer id) {
        Integer ret = this.cartDao.deleteById(id);
        if(ret != 1) {
            throw new ServiceException("Fail to delete cart!");
        }
        return ret;
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
            CartItemResponseDto responseDto = new CartItemResponseDto();
            responseDto.setSuccess(Boolean.FALSE);
            responseDto.setMsg(deductBalanceResponseDto.msg);
            return responseDto;
        }

        //调用订单管理服务生成订单
        OrderInfoDto orderInfoDto = this.orderInfoService.createOrder(Dto.getCartId(),accountId,
                deductBalanceResponseDto.financeId,
                Dto.getDeliverOrDining(),cartItems,
                Optional.ofNullable(Dto.getNewAddress()), Optional.ofNullable(Dto.getRemark()));

        if(orderInfoDto.getSuccess()==Boolean.FALSE) {
            CartItemResponseDto responseDto = new CartItemResponseDto();
            responseDto.setSuccess(Boolean.FALSE);
            responseDto.setMsg(orderInfoDto.getMsg());
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
        CartItemResponseDto responseDto = new CartItemResponseDto();
        responseDto.setMsg("Order process successfully!");
        responseDto.setSuccess(Boolean.TRUE);
        return responseDto;
    }

    public ClearCartResponseDto clearCart(NormalRequestDto Dto) {
        ClearCartResponseDto clearCartResponseDto = new ClearCartResponseDto();
        return clearCartResponseDto;
    }

}
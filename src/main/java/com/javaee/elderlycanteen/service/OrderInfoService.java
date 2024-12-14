package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.AccountDao;
import com.javaee.elderlycanteen.dao.DeliverOrderDao;
import com.javaee.elderlycanteen.dao.OrderInfoDao;
import com.javaee.elderlycanteen.dto.order.OrderDish;
import com.javaee.elderlycanteen.dto.order.OrderInfoDto;
import com.javaee.elderlycanteen.dto.order.OrderItem;
import com.javaee.elderlycanteen.dto.register.Menu;
import com.javaee.elderlycanteen.dto.register.MenuResponseDto;
import com.javaee.elderlycanteen.dto.volServe.AccessOrderResponseDto;
import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.entity.CartItem;
import com.javaee.elderlycanteen.entity.DeliverOrder;
import com.javaee.elderlycanteen.entity.OrderInfo;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.javaee.elderlycanteen.utils.DateUtils.getCurrentDate;

@Transactional
@Service
public class OrderInfoService {

    private final AccountDao accountDao;
    private final DeliverOrderDao deliverOrderDao;
    private final OrderInfoDao orderInfoDao;

    private final WeekMenuService weekMenuService;


    @Autowired
    public OrderInfoService(AccountDao accountDao, DeliverOrderDao deliverOrderDao, OrderInfoDao orderInfoDao,WeekMenuService weekMenuService) {
        this.accountDao = accountDao;
        this.deliverOrderDao = deliverOrderDao;
        this.orderInfoDao = orderInfoDao;

        this.weekMenuService = weekMenuService;

    }

    public Integer insertOrderInfo(OrderInfo orderInfo) {
        Integer ret = this.orderInfoDao.insertOrderInfo(orderInfo);
        if (ret != 1) {
            throw new ServiceException("insert order info error!");
        }
        return ret;
    }

    //验证cartId是否在OrderInfo中，若存在则报错
    public void getOrderInfoByCartId(Integer cartId){
        List<OrderInfo> orderInfos = this.orderInfoDao.getOrderInfoByCartId(cartId);
        if(!orderInfos.isEmpty()) {
            System.out.println(orderInfos);
            throw new ServiceException("Order info exist!");
        }
    }

    public Double calculateTotalPrice(List<CartItem> cartItems) throws ParseException {
        Double totalPrice = 0.0;
        MenuResponseDto todayMenu = this.weekMenuService.getTodayMenu();

        // 将今日菜单转换为Map，以便于快速查找
        Map<Integer, Menu> todayMenuItems = todayMenu.getMenu().stream()
                .collect(Collectors.toMap(Menu::getDishId, menu -> menu));

        // 遍历购物车项，计算总价
        for (CartItem cartItem : cartItems) {
            // 查找菜品在今日菜单中的信息
            Menu menuItem = todayMenuItems.get(cartItem.getDishId());
            if (menuItem != null) {
                // 使用折扣价（如果有），否则使用正常价格
//                Double itemPrice = menuItem.getDiscountPrice() > 0 ? menuItem.getDiscountPrice() : menuItem.getDishPrice();
                Double itemPrice = Optional.ofNullable(menuItem.getDiscountPrice())
                        .filter(price -> price > 0)
                        .orElse(menuItem.getDishPrice());
                Double itemTotal = cartItem.getQuantity() * itemPrice;
                totalPrice += itemTotal;
            } else {
                // 如果某个菜品不在今日菜单中，可以选择抛出异常或者继续处理
                System.out.println("Today's Menu: " + todayMenuItems);
                throw new ServiceException("No." + cartItem.getDishId() + " is not on Today's menu, so the price cannot be calculated");
            }
        }
        return totalPrice;
    }

    public OrderInfoDto createOrder(Integer cartId, Integer accountId, Integer financeId,
                                    Boolean deliverOrDining, List<CartItem> cartItems,
                                    Optional<String> newAddress,
                                    Optional<String> remark) throws ParseException {
        // 初始化变量，用于计算总价
        Double totalPrice=0.0;
        List<OrderDish> orderDishes = new ArrayList<OrderDish>();
        totalPrice = this.calculateTotalPrice(cartItems);

        // 检查地址是否为空
        Account account = this.accountDao.getAccountById(accountId);
        if(account.getAddress().isEmpty() && newAddress.isEmpty()) {
            OrderInfoDto orderInfoDto = new OrderInfoDto();
            orderInfoDto.setMsg("account address is empty");
            orderInfoDto.setSuccess(Boolean.FALSE);
        }
        // 设置新地址
        String address = (newAddress != null) ? String.valueOf(newAddress) : account.getAddress();

        Double bonus=0.0;
        if(account.getIdentity()=="senior") bonus = totalPrice*0.2;

        // 生成订单记录
        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setDeliverOrDining(deliverOrDining?"D":"I");
        orderInfo.setCartId(cartId);
        orderInfo.setStatus("to be confirmed");
        orderInfo.setBonus(bonus);
        orderInfo.setRemark(remark.orElse("null"));
        orderInfo.setFinanceId(financeId);
        // 插入数据
        this.insertOrderInfo(orderInfo);

        // 生成配送订单
        if(deliverOrDining){
            DeliverOrder deliverOrder = new DeliverOrder();

            deliverOrder.setOrderId(orderInfo.getOrderId());
            deliverOrder.setCartId(cartId);
            deliverOrder.setDeliverPhone("pending");
            deliverOrder.setCustomerPhone(account.getPhoneNum());
            deliverOrder.setCusAddress(address);
            deliverOrder.setDeliverStatus("Pending");

            // 添加数据
            Integer ret = this.deliverOrderDao.insertDeliverOrder(deliverOrder);
            if (ret != 1) {
                throw new ServiceException("insert deliver order error!");
            }
        }

        // 创建并返回OrderInfoDto
        OrderInfoDto orderInfoDto = new OrderInfoDto();
        OrderItem orderItem = new OrderItem();

        orderInfoDto.setMsg("order created successfully");
        orderInfoDto.setSuccess(Boolean.TRUE);

        orderItem.setOrderId(orderInfo.getOrderId());
        orderItem.setCusAddress(address);
        orderItem.setDeliverOrDining(deliverOrDining);
        orderItem.setDeliverStatus(deliverOrDining?"pending orders":"eat in the hall");
        orderItem.setMoney(totalPrice-bonus);
        orderItem.setOrderDishes(orderDishes);
        orderItem.setRemark(orderInfo.getRemark());
        orderItem.setStatus(orderInfo.getStatus());
        orderItem.setSubsidy(bonus);
        orderItem.setUpdatedTime(getCurrentDate());

        orderInfoDto.setResponse(orderItem);

        return orderInfoDto;
    }
}
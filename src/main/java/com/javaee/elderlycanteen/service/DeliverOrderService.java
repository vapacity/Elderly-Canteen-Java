package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.*;
import com.javaee.elderlycanteen.dto.order.NormalResponseDto;
import com.javaee.elderlycanteen.dto.order.OrderDish;
import com.javaee.elderlycanteen.dto.volServe.AccessOrderResponseDto;
import com.javaee.elderlycanteen.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.javaee.elderlycanteen.enumeration.DeliverOrderStatusEnum.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.javaee.elderlycanteen.enumeration.DeliverOrderStatusEnum.*;

@Service
@Transactional
public class DeliverOrderService {

    private final AccountDao accountDao;
    private final CartDao cartDao;
    private final CartItemDao cartItemDao;
    private final DeliverOrderDao deliverOrderDao;
    private final DeliverVDao deliverVDao;
    private final DishDao dishDao;
    private final FinanceDao financeDao;
    private final OrderInfoDao orderInfoDao;
    private final VolunteerDao volunteerDao;
    private final WeekMenuDao weekMenuDao;

    @Autowired
    public DeliverOrderService(AccountDao accountDao, CartDao cartDao, CartItemDao cartItemDao, DeliverOrderDao deliverOrderDao, DeliverVDao deliverVDao,
                               FinanceDao financeDao, DishDao dishDao, OrderInfoDao orderInfoDao, VolunteerDao volunteerDao, WeekMenuDao weekMenuDao) {
        this.accountDao = accountDao;
        this.cartDao = cartDao;
        this.cartItemDao = cartItemDao;
        this.deliverOrderDao = deliverOrderDao;
        this.deliverVDao = deliverVDao;
        this.dishDao = dishDao;
        this.financeDao = financeDao;
        this.orderInfoDao = orderInfoDao;
        this.volunteerDao = volunteerDao;
        this.weekMenuDao = weekMenuDao;
    }

    public AccessOrderResponseDto getAcceptableOrder(){

        // 查找所有待接单的DeliverOrder
        List<DeliverOrder> deliverOrders = this.deliverOrderDao.getDeliverOrderByDeliverStatus(DELIVER_PENDING.getDescription());
        if(deliverOrders.isEmpty()){
            return new AccessOrderResponseDto(
                    "no pending order",
                    null,
                    Boolean.TRUE
            );
        }

        // 创建List存储所有的Response
        List<AccessOrderResponseDto.Response> responses = new ArrayList<>();
        for(DeliverOrder deliverOrder : deliverOrders){
            // 获取订单信息
            OrderInfo orderInfo = this.orderInfoDao.getOrderInfoById(deliverOrder.getOrderId());
            if(orderInfo == null) continue;

            // 获取购物车信息
            Cart cart = this.cartDao.getCartByCartId(orderInfo.getCartId());
            if(cart == null) continue;

            // 获取购物车中的所有项目
            List<CartItem> cartItems = this.cartItemDao.getCartItemByCartId(cart.getCartId());
            if(cartItems == null) continue;

            // 组装订单菜品的信息
            List<OrderDish> orderDishes = new ArrayList<>();
            for(CartItem cartItem : cartItems){
                Dish dish = this.dishDao.getDishById(cartItem.getDishId());
                WeekMenu weekMenu= this.weekMenuDao.findWeekMenuByWeekAndDishId(cartItem.getWeek(),cartItem.getDishId());
                if(dish!=null){
                    orderDishes.add(new OrderDish(
                            dish.getDishName(),
                            dish.getImageUrl(),
                            weekMenu.getDisPrice()==0?dish.getPrice():weekMenu.getDisPrice(),
                            cartItem.getQuantity()
                    ));
                }
            }
            Finance finance = this.financeDao.getFinanceById(orderInfo.getFinanceId());

            // 构建Response
            AccessOrderResponseDto.Response response = AccessOrderResponseDto.Response.builder()
                    .orderId(orderInfo.getOrderId())
                    .cusAddress(deliverOrder.getCusAddress())
                    .deliverOrDining(orderInfo.getDeliverOrDining().equals("D"))
                    .deliverStatus(deliverOrder.getDeliverStatus())
                    .money(finance.getPrice())
                    .orderDishes(orderDishes)
                    .remark(orderInfo.getRemark() == null ? "no remark" : orderInfo.getRemark())
                    .status(orderInfo.getStatus())
                    .subsidy(orderInfo.getBonus())
                    .updatedTime(finance.getFinanceDate())
                    .build();

            responses.add(response);

        }

        return new AccessOrderResponseDto(
                "get acceptable order successfully!",
                responses,
                Boolean.TRUE
        );
    }

    public NormalResponseDto acceptOrder(Integer orderId, Integer accountId){
        // 查找志愿者信息
        System.out.println("accept order start!!!!!!!!!");
        Volunteer volunteer = this.volunteerDao.getByAccountId(accountId);

        if(volunteer == null) {
            System.out.println("volunteer is null!!!!!!!!!!");
            return new NormalResponseDto(
                    Boolean.FALSE,
                    "account is not a volunteer"
            );
        }
        if(volunteer.getAvailable()!='Y'){
            System.out.println(volunteer);
            return new NormalResponseDto(
                    Boolean.FALSE,
                    "volunteer has already delivered"
            );
        }

        // 找到外卖订单
        DeliverOrder deliverOrder = this.deliverOrderDao.getDeliverOrderByOrderId(orderId);
        System.out.println(deliverOrder);
        // 找到送餐者信息
        Account deliverAccount = this.accountDao.getAccountById(accountId);
        System.out.println(deliverAccount);

        DeliverV deliverV = DeliverV.builder()
                .orderId(orderId)
                .volunteerId(accountId)
                .build();
        this.deliverVDao.insertDeliverV(deliverV);

        if(!deliverOrder.getDeliverStatus().equals("Pending")){
            return new NormalResponseDto(
                    Boolean.FALSE,
                    "order not pending"
            );
        }

        this.deliverOrderDao.updateDeliverDeliverStatus(orderId, DELIVER_DELIVERED.getDescription());
        this.deliverOrderDao.updateDeliverDeliverPhone(orderId,deliverAccount.getPhoneNum());
        this.volunteerDao.updateVolunterrAvailable(accountId,'N');

        return new NormalResponseDto(
                Boolean.TRUE,
                "deliver order successfully!"
        );
    }

    public NormalResponseDto confirmDelivered(Integer orderId, Integer accountId){
        // 查找订单是否已经被接单
        System.out.println("confirm delivered start!!!!!!!!!!!!!!");
        System.out.println(orderId);
        DeliverOrder deliverOrder = this.deliverOrderDao.getDeliverOrderByOrderId(orderId);
        System.out.println(deliverOrder);
        DeliverV deliverV = this.deliverVDao.getDeliverVByOrderId(orderId);
        System.out.println(deliverV);
        if(!deliverOrder.getDeliverStatus().equals(DELIVER_DELIVERED.getDescription())){
            return new NormalResponseDto(
                    Boolean.FALSE,
                    "order not delivered"
            );
        }
        this.deliverOrderDao.updateDeliverDeliverStatus(orderId, DELIVER_RECEIVED.getDescription());

        // 修改配送员信息
        Volunteer volunteer = this.volunteerDao.getByAccountId(accountId);
        if(volunteer.getAvailable()!='Y'){
            this.volunteerDao.updateVolunterrAvailable(accountId,'Y');
        }

        return new NormalResponseDto(
                Boolean.TRUE,
                "receive order successfully!"
        );
    }

    public AccessOrderResponseDto getOrderByDeliverStatus(Integer accountId, String deliverStatus){
        // 查找所有deliverStatus为deliverStatus的DeliverOrder记录
        System.out.println("get Order By Deliver Status!!!!!!!!!!!!!");
        System.out.println(accountId+" "+deliverStatus);
        List<DeliverOrder> deliverOrders = this.deliverOrderDao.getDeliverOrderByDeliverStatus(deliverStatus);
        if(Objects.equals(deliverStatus, DELIVER_RECEIVED.getDescription())){
            System.out.println(DELIVER_REVIEWED.getDescription());
            List<DeliverOrder> addDeliverOrders = this.deliverOrderDao.getDeliverOrderByDeliverStatus(DELIVER_REVIEWED.getDescription());
            deliverOrders.addAll(addDeliverOrders);

        }

        if(deliverOrders.isEmpty()){
            return new AccessOrderResponseDto(
                    deliverStatus+" order not found",
                    null,
                    Boolean.TRUE
            );
        }

        // 筛选出deliverId在deliverV表中与accountId匹配的订单
        List<DeliverOrder> filteredOrders = new ArrayList<>();
        for(DeliverOrder deliverOrder : deliverOrders){
            DeliverV deliverV = this.deliverVDao.getDeliverVByOrderId(deliverOrder.getOrderId());
            if(deliverV!=null && Objects.equals(deliverV.getVolunteerId(), accountId)){
                filteredOrders.add(deliverOrder);
            }
        }

        // 如果没有找到符合条件的订单，返回错误信息
        if(filteredOrders.isEmpty()){
            return new AccessOrderResponseDto(
                    "no " + deliverStatus + " order",
                    null,
                    Boolean.TRUE
            );
        }

        // 创建一个List来存储所有的Response项
        List<AccessOrderResponseDto.Response> responses = new ArrayList<>();

        for (DeliverOrder deliverOrder : filteredOrders) {
            OrderInfo orderInfo = this.orderInfoDao.getOrderInfoById(deliverOrder.getOrderId());
            if(orderInfo==null) continue;

            // 获取购物车信息
            Cart cart = this.cartDao.getCartByCartId(orderInfo.getCartId());
            if(cart==null) continue;

            // 获取购物车中的所有项目
            List<CartItem> cartItems = this.cartItemDao.getCartItemByCartId(cart.getCartId());
            if(cartItems==null) continue;

            // 组装订单菜品信息
            List<OrderDish> orderDishes = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
                Dish dish = this.dishDao.getDishById(cartItem.getDishId());
                WeekMenu weekMenu = this.weekMenuDao.findWeekMenuByWeekAndDishId(cartItem.getWeek(), dish.getDishId());
                orderDishes.add(
                        OrderDish.builder()
                                .dishName(dish.getDishName())
                                .picture(dish.getImageUrl())
                                .price(dish.getPrice())
                                .quantity(cartItem.getQuantity())
                                .build()
                );
            }

            Finance finance = this.financeDao.getFinanceById(orderInfo.getFinanceId());

            // 构建Response
            AccessOrderResponseDto.Response response = AccessOrderResponseDto.Response.builder()
                    .orderId(orderInfo.getOrderId())
                    .cusAddress(deliverOrder.getCusAddress())
                    .deliverOrDining(Objects.equals(orderInfo.getDeliverOrDining(), "D"))
                    .deliverStatus(deliverOrder.getDeliverStatus())
                    .money(finance.getPrice())
                    .orderDishes(orderDishes)
                    .remark(orderInfo.getRemark()==null?"no remark":orderInfo.getRemark())
                    .status(orderInfo.getStatus())
                    .subsidy(orderInfo.getBonus())
                    .updatedTime(finance.getFinanceDate())
                    .build();

            responses.add(response);
        }

        return new AccessOrderResponseDto(
                "get" + deliverStatus + " orders successfully!",
                responses,
                Boolean.TRUE
        );
    }
}
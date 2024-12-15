package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.*;
import com.javaee.elderlycanteen.dto.order.*;
import com.javaee.elderlycanteen.dto.register.Menu;
import com.javaee.elderlycanteen.dto.register.MenuResponseDto;
import com.javaee.elderlycanteen.entity.*;
import com.javaee.elderlycanteen.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.javaee.elderlycanteen.enumeration.AccountIdentityEnum.SENIOR;
import static com.javaee.elderlycanteen.enumeration.DeliverOrDiningEnum.DELIVER;
import static com.javaee.elderlycanteen.enumeration.DeliverOrDiningEnum.DINING;
import static com.javaee.elderlycanteen.enumeration.DeliverOrderStatusEnum.*;
import static com.javaee.elderlycanteen.enumeration.DeliverOrderStatusEnum.PENDING;
import static com.javaee.elderlycanteen.enumeration.FinanceTypeEnum.ORDER;
import static com.javaee.elderlycanteen.enumeration.OrderStatusEnum.*;
import static com.javaee.elderlycanteen.utils.DateUtils.getCurrentDate;

@Transactional
@Service
public class OrderInfoService {

    private final AccountDao accountDao;
    private final CartDao cartDao;
    private final CartItemDao cartItemDao;
    private final DeliverOrderDao deliverOrderDao;
    private final DeliverReviewDao deliverReviewDao;
    private final DeliverVDao deliverVDao;
    private final DishDao dishDao;
    private final FinanceDao financeDao;
    private final OrderInfoDao orderInfoDao;
    private final OrderReviewDao orderReviewDao;
    private final WeekMenuDao weekMenuDao;

    private final WeekMenuService weekMenuService;


    @Autowired
    public OrderInfoService(AccountDao accountDao, CartDao cartDao,CartItemDao cartItemDao,
                            DeliverOrderDao deliverOrderDao, DishDao dishDao, DeliverReviewDao deliverReviewDao,
                            DeliverVDao deliverVDao,
                            FinanceDao financeDao, OrderInfoDao orderInfoDao,OrderReviewDao orderReviewDao,
                            WeekMenuDao weekMenuDao, WeekMenuService weekMenuService) {
        this.accountDao = accountDao;
        this.cartDao = cartDao;
        this.cartItemDao = cartItemDao;
        this.deliverOrderDao = deliverOrderDao;
        this.deliverReviewDao = deliverReviewDao;
        this.deliverVDao = deliverVDao;
        this.dishDao = dishDao;
        this.financeDao = financeDao;
        this.orderInfoDao = orderInfoDao;
        this.orderReviewDao = orderReviewDao;
        this.weekMenuDao = weekMenuDao;

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
    public List<OrderInfo> getOrderInfoByCartId(Integer cartId){
        List<OrderInfo> orderInfos = this.orderInfoDao.getOrderInfoByCartId(cartId);
        if(!orderInfos.isEmpty()) {
            return null;
        }
        return orderInfos;
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
        List<OrderDish> orderDishes = new ArrayList<>();
        totalPrice = this.calculateTotalPrice(cartItems);

        // 检查地址是否为空
        Account account = this.accountDao.getAccountById(accountId);
        if(account.getAddress().isEmpty() && newAddress.isEmpty()) {
            return new OrderInfoDto(
                    Boolean.FALSE,
                    "account address is empty",
                    null
            );
        }
        // 设置新地址
        String address = (newAddress != null) ? String.valueOf(newAddress) : account.getAddress();

        Double bonus=0.0;
        if(account.getIdentity()==SENIOR.getDescription()) bonus = totalPrice*0.2;

        // 生成订单记录
        OrderInfo orderInfo = OrderInfo.builder()
                .deliverOrDining(deliverOrDining?DELIVER.getDescription(): DINING.getDescription())
                .cartId(cartId)
                .status(TOBECONFIRMED.getDescription())
                .bonus(bonus)
                .remark(remark.orElse("no remark"))
                .financeId(financeId)
                .build();
        // 插入数据
        this.insertOrderInfo(orderInfo);

        // 生成配送订单
        if(deliverOrDining){
            DeliverOrder deliverOrder = DeliverOrder.builder()
                    .orderId(orderInfo.getOrderId())
                    .cartId(cartId)
                    .deliverPhone("Pending")
                    .customerPhone(account.getPhoneNum())
                    .cusAddress(address)
                    .deliverStatus(PENDING.getDescription())
                    .build();

            // 添加数据
            Integer ret = this.deliverOrderDao.insertDeliverOrder(deliverOrder);
            if (ret != 1) {
                throw new ServiceException("insert deliver order error!");
            }
        }

        // 创建并返回OrderInfoDto
        OrderItem orderItem = OrderItem.builder()
                .orderId(orderInfo.getOrderId())
                .cusAddress(address)
                .money(totalPrice-bonus)
                .deliverOrDining(deliverOrDining)
                .deliverStatus(deliverOrDining? PENDING.getDescription() : DINING.getDescription())
                .orderDishes(orderDishes)
                .remark(orderInfo.getRemark())
                .status(orderInfo.getStatus())
                .subsidy(bonus)
                .updatedTime(getCurrentDate())
                .build();

        OrderInfoDto orderInfoDto = new OrderInfoDto(
                Boolean.TRUE,
                "order created successfully",
                orderItem
        );
        return orderInfoDto;
    }

    public GetOrderResponseDto getHistoryOrderInfo(Integer accountId) {
        // 查找所有与此用户相关的finance记录
        List<Finance> finances = this.financeDao.getFinanceByAccountIdAndFinanceType(accountId,ORDER.getDescription());
        if (finances.isEmpty()) {
            return new GetOrderResponseDto(Boolean.TRUE,"No history orders!",new OrderItem[0]);
        }
        List<OrderItem> orderItems = new ArrayList<>();

        for (Finance finance : finances) {
            // 通过financeId查找orderInfo
            List<OrderInfo> orderInfos = this.orderInfoDao.getOrderInfoByFinanceId(finance.getFinanceId());
            if (orderInfos.isEmpty()) continue;
            OrderInfo orderInfo = orderInfos.get(0);
            // 通过cartId获取购物车信息
            Cart cart = this.cartDao.getCartByCartId(orderInfo.getCartId());
            // 获取购物车中的所有项目
            List<CartItem> cartItems = this.cartItemDao.getCartItemByCartId(cart.getCartId());

            // 组装orderDishes信息
            List<OrderDish> orderDishes = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
                Dish dish = this.dishDao.getDishById(cartItem.getDishId());
                if (dish == null) continue;
                WeekMenu weekMenu = this.weekMenuDao.findWeekMenuByWeekAndDishId(cartItem.getWeek(), cartItem.getDishId());
                if (weekMenu == null) continue;
                orderDishes.add(OrderDish.builder()
                                .dishName(dish.getDishName())
                                .picture(dish.getImageUrl())
                                .price(weekMenu.getDisPrice() == null || weekMenu.getDisPrice()==0?dish.getPrice():weekMenu.getDisPrice())
                                .quantity(cartItem.getQuantity())
                        .build());
            }

            DeliverOrder deliverOrder = this.deliverOrderDao.getDeliverOrderByOrderId(orderInfo.getOrderId());

            // 构建OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .orderId(orderInfo.getOrderId())
                    .cusAddress(deliverOrder==null? DINING.getDescription():deliverOrder.getCusAddress())
                    .deliverStatus(deliverOrder==null? DINING.getDescription():deliverOrder.getDeliverStatus())
                    .deliverOrDining(Objects.equals(orderInfo.getDeliverOrDining(), DELIVER.getDescription()))
                    .orderDishes(orderDishes)
                    .remark(orderInfo.getRemark()==null?"no remark":orderInfo.getRemark())
                    .money(finance.getPrice())
                    .status(orderInfo.getStatus())
                    .subsidy(orderInfo.getBonus())
                    .updatedTime(finance.getFinanceDate())
                    .build();

            orderItems.add(orderItem);
        }

        // 构建GetOrderResponseDto，并将OrderItem列表转换为数组返回
        GetOrderResponseDto responseDto = new GetOrderResponseDto(Boolean.TRUE,"get history order successfully!",orderItems.toArray(new OrderItem[orderItems.size()]));
        return responseDto;
    }

    public NormalResponseDto confirmOrder(Integer orderId, Integer accountId) {
        OrderInfo orderInfo = this.orderInfoDao.getOrderInfoById(orderId);
        if (orderInfo == null){
            return new NormalResponseDto(Boolean.FALSE,"OrderId does not have an associated order!");
        }
        // 更新订单状态
        this.orderInfoDao.updateOrderStatus(orderId, CONFIRMED.getDescription());

        // 自动增加评价
        OrderReview orderReview = new OrderReview(orderId,5.0,"no review");
        this.orderReviewDao.insertOrderReview(orderReview);

        if(orderInfo.getDeliverOrDining()== DELIVERED.getDescription()){
            DeliverOrder deliverOrder = this.deliverOrderDao.getDeliverOrderByOrderId(orderId);
            if(deliverOrder==null){
                return new NormalResponseDto(Boolean.FALSE,"OrderId does not have an associated deliverOrder!");
            }
            this.deliverOrderDao.updateDeliverDeliverStatus(orderId,DELIVERED.getDescription());
            DeliverReview deliverReview = new DeliverReview(orderId,5.0,"no review");
            this.deliverReviewDao.insertDeliverReview(deliverReview);
        }

        return new NormalResponseDto(Boolean.TRUE,"confirm order successfully!");
    }

    public GetODMsgResponseDto GetOrderDeliverMsg(Integer orderId) {
        // 查询是否有志愿者
        DeliverV volunteer = this.deliverVDao.getDeliverVByOrderId(orderId);
        if(volunteer==null){
            return new GetODMsgResponseDto("no volunteer!",new GetODMsgResponseDto.VolunteerMsg(),Boolean.TRUE);
        }

        // 查找志愿者信息
        Account volAccount = this.accountDao.getAccountById(volunteer.getVolunteerId());
        if(volAccount==null){
            return new GetODMsgResponseDto("database error! volunteer id exists: "+ volunteer.getVolunteerId()+" but volunteer account not found!",
                    new GetODMsgResponseDto.VolunteerMsg(),Boolean.FALSE);
        }

        // 返回志愿者信息
        return new GetODMsgResponseDto(
                "get order deliver message successfully!",
                new GetODMsgResponseDto.VolunteerMsg(
                        volAccount.getAccountId(),
                        volAccount.getName()
                ),
                Boolean.TRUE
        );
    }

    public NormalResponseDto submitDiningReview(ReviewSubmissionDto Dto){
        // 查找对应订单
        OrderInfo orderInfo = this.orderInfoDao.getOrderInfoById(Dto.getOrderId());
        if (orderInfo == null){
            return new NormalResponseDto(Boolean.FALSE,"order not found!");
        }

        // 查找是否存在评价
        OrderReview existReview = this.orderReviewDao.getOrderReviewById(Dto.getOrderId());
        if(existReview==null){
            // 新增评价
            this.orderReviewDao.insertOrderReview(new OrderReview(
                    Dto.getOrderId(),
                    Dto.getCStars(),
                    Dto.getCReviewText()
            ));
            this.orderInfoDao.updateOrderStatus(Dto.getOrderId(), REVIEWED.getDescription());

            return new NormalResponseDto(Boolean.TRUE,"review successfully!");
        } else{
            existReview.setCReviewText(Dto.getCReviewText());
            existReview.setCStars(Dto.getCStars());
            this.orderReviewDao.updateOrderReview(existReview);
            this.orderInfoDao.updateOrderStatus(Dto.getOrderId(), REVIEWED.getDescription());
            return new NormalResponseDto(Boolean.TRUE,"review successfully!");
        }
    }

    public ReviewResponseDto getDiningReview(Integer orderId){
        OrderReview orderReview = this.orderReviewDao.getOrderReviewById(orderId);
        if(orderReview==null){
            return new ReviewResponseDto(Boolean.TRUE,"no review in database!",new ArrayList<>());
        }

        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        ReviewResponseDto.ReviewResponseData reviewResponseData = new ReviewResponseDto.ReviewResponseData();
        List<ReviewResponseDto.ReviewResponseData> reviewResponseDatas = new ArrayList<>();

        reviewResponseDto.setMsg("get dining review successfully!");
        reviewResponseDto.setSuccess(Boolean.TRUE);

        reviewResponseData.setCReviewText(orderReview.getCReviewText());
        reviewResponseData.setCStars(orderReview.getCStars());

        reviewResponseDatas.add(reviewResponseData);

        reviewResponseDto.setResponse(reviewResponseDatas);

        return reviewResponseDto;
    }

    public NormalResponseDto submitDeliverReview(ReviewSubmissionDto Dto){
        // 查找对应订单
        OrderInfo orderInfo = this.orderInfoDao.getOrderInfoById(Dto.getOrderId());
        if (orderInfo == null){
            return new NormalResponseDto(Boolean.FALSE,"order not found!");
        }

        // 查找是否存在评价
        OrderReview existReview = this.orderReviewDao.getOrderReviewById(Dto.getOrderId());
        if(existReview==null){
            // 新增订单评价
            this.orderReviewDao.insertOrderReview(new OrderReview(
                    Dto.getOrderId(),
                    Dto.getCStars(),
                    Dto.getCReviewText()
            ));
            this.orderInfoDao.updateOrderStatus(Dto.getOrderId(), REVIEWED.getDescription());
        } else{
            existReview.setCReviewText(Dto.getCReviewText());
            existReview.setCStars(Dto.getCStars());
            this.orderReviewDao.updateOrderReview(existReview);
            this.orderInfoDao.updateOrderStatus(Dto.getOrderId(), REVIEWED.getDescription());
        }

        DeliverReview existDeliverReview = this.deliverReviewDao.getDeliverReviewByOrderId(Dto.getOrderId());
        if(existDeliverReview==null){
            // 新增外卖评价
            this.deliverReviewDao.insertDeliverReview(new DeliverReview(
                    Dto.getOrderId(),
                    Dto.getDStars(),
                    Dto.getDReviewText()
            ));
            this.deliverOrderDao.updateDeliverDeliverStatus(Dto.getOrderId(), REVIEWED.getDescription());
        }else{
            existDeliverReview.setDReviewText(Dto.getDReviewText());
            existDeliverReview.setDStars(Dto.getDStars());
            this.deliverReviewDao.updateDeliverReview(existDeliverReview);
            this.deliverOrderDao.updateDeliverDeliverStatus(Dto.getOrderId(), REVIEWED.getDescription());
        }
        return new NormalResponseDto(Boolean.TRUE,"deliver review successfully!");
    }

    public ReviewResponseDto getDeliverReview(Integer orderId){
        OrderReview orderReview = this.orderReviewDao.getOrderReviewById(orderId);
        DeliverReview deliverReview = this.deliverReviewDao.getDeliverReviewByOrderId(orderId);
        if(orderReview==null){
            return new ReviewResponseDto(Boolean.TRUE,"no order review in database!",new ArrayList<>());
        }
        if(deliverReview==null){
            ReviewResponseDto.ReviewResponseData reviewResponseData = new ReviewResponseDto.ReviewResponseData();
            List<ReviewResponseDto.ReviewResponseData> reviewResponseDatas = new ArrayList<>();

            reviewResponseData.setCReviewText(orderReview.getCReviewText());
            reviewResponseData.setCStars(orderReview.getCStars());

            reviewResponseDatas.add(reviewResponseData);

            return new ReviewResponseDto(Boolean.TRUE,"no deliver review in database!",reviewResponseDatas);
        }

        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        ReviewResponseDto.ReviewResponseData reviewResponseData = new ReviewResponseDto.ReviewResponseData();
        List<ReviewResponseDto.ReviewResponseData> reviewResponseDatas = new ArrayList<>();

        reviewResponseDto.setMsg("get dining review successfully!");
        reviewResponseDto.setSuccess(Boolean.TRUE);

        reviewResponseData.setCReviewText(orderReview.getCReviewText());
        reviewResponseData.setCStars(orderReview.getCStars());
        reviewResponseData.setDReviewText(Optional.ofNullable(deliverReview.getDReviewText()));
        reviewResponseData.setDStars(Optional.ofNullable(deliverReview.getDStars()));

        reviewResponseDatas.add(reviewResponseData);

        reviewResponseDto.setResponse(reviewResponseDatas);

        return reviewResponseDto;
    }

    public OrderInfoDto getOrderInfoById(Integer orderId){
        OrderInfo orderInfo = this.orderInfoDao.getOrderInfoById(orderId);
        if(orderInfo==null){
            return new OrderInfoDto(Boolean.TRUE,"order not found!",null);
        }

        // 获取购物车信息
        Cart cart = this.cartDao.getCartByCartId(orderInfo.getCartId());
        if(cart==null){
            return new OrderInfoDto(Boolean.TRUE,"cart not found!",null);
        }

        // 获取购物车中所有项目
        List<CartItem> cartItems = this.cartItemDao.getCartItemByCartId(orderInfo.getCartId());
        if(cartItems==null){
            return new OrderInfoDto(Boolean.TRUE,"cart item not found!",null);
        }

        // 获取菜品详情
        List<OrderDish> orderDishes = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Dish dish = this.dishDao.getDishById(cartItem.getDishId());
            WeekMenu weekMenu = this.weekMenuDao.findWeekMenuByWeekAndDishId(cartItem.getWeek(),cartItem.getDishId());
            if(dish!=null){
                orderDishes.add(new OrderDish(
                        dish.getDishName(),
                        dish.getImageUrl(),
                        cartItem.getQuantity()*weekMenu.getDisPrice(),
                        cartItem.getQuantity()
                ));
            }
        }

        // 获取配送信息
        DeliverOrder deliverOrder = this.deliverOrderDao.getDeliverOrderByOrderId(orderId);
        Finance finance = this.financeDao.getFinanceById(orderInfo.getFinanceId());
        if(finance==null){
            return new OrderInfoDto(Boolean.TRUE,"finance not found!",null);
        }

        // 构建OrderItem并返回
        OrderItem orderItem = new OrderItem(
                orderInfo.getOrderId(),
                deliverOrder!=null?deliverOrder.getCusAddress(): DINING.getDescription(),
                orderInfo.getDeliverOrDining()== DELIVERED.getDescription(),
                deliverOrder!=null?deliverOrder.getDeliverStatus(): DINING.getDescription(),
                finance.getPrice(),
                orderDishes,
                orderInfo.getRemark().isEmpty()?"no remarks":orderInfo.getRemark(),
                orderInfo.getStatus(),
                orderInfo.getBonus(),
                finance.getFinanceDate()
        );

        return new OrderInfoDto(
                Boolean.TRUE,
                "get order successfully",
                orderItem
        );
    }

    public IdentityResponseDto getIdentityInOrder(Integer orderId, Integer accountId) {
        OrderInfo orderInfo = this.orderInfoDao.getOrderInfoById(orderId);
        if (orderInfo==null){
            return new IdentityResponseDto(Boolean.FALSE,"order not found!",null);
        }

        Cart cart = this.cartDao.getCartByCartId(orderInfo.getCartId());
        if(cart==null){
            return new IdentityResponseDto(Boolean.FALSE,"cart not found!",null);
        }
        if(orderInfo.getDeliverOrDining()=="I"){
            if(cart.getAccountId()==accountId){
                return new IdentityResponseDto(Boolean.TRUE,"placed order",
                        new IdentityResponseDto.IdentityDto(Boolean.FALSE,Boolean.TRUE));
            }else{
                return new IdentityResponseDto(Boolean.FALSE,"accountId and order not match!",null);
            }
        }

        DeliverV deliverVol = this.deliverVDao.getDeliverVByOrderId(orderId);
        DeliverOrder deliverOrder = this.deliverOrderDao.getDeliverOrderByOrderId(orderId);
        if(deliverOrder==null){
            return new IdentityResponseDto(Boolean.TRUE,"order not found!",
                    new IdentityResponseDto.IdentityDto(Boolean.FALSE,Boolean.FALSE));
        }
        if(deliverVol==null){
            if(cart.getAccountId()==accountId){
                return new IdentityResponseDto(Boolean.TRUE,"placed order",
                        new IdentityResponseDto.IdentityDto(Boolean.TRUE,Boolean.FALSE));
            }else{
                return new IdentityResponseDto(Boolean.TRUE,"order not assigned to delivery man",
                        new IdentityResponseDto.IdentityDto(Boolean.FALSE,Boolean.FALSE));
            }
        }else{
            if(deliverVol.getVolunteerId()==accountId && cart.getAccountId()==accountId){
                return new IdentityResponseDto(Boolean.TRUE,"delivery man and placed order",
                        new IdentityResponseDto.IdentityDto(Boolean.TRUE,Boolean.TRUE));
            }else if(deliverVol.getVolunteerId()==accountId){
                return new IdentityResponseDto(Boolean.TRUE,"delivery man",
                        new IdentityResponseDto.IdentityDto(Boolean.TRUE,Boolean.FALSE));
            }else{
                if(cart.getAccountId()==accountId){
                    return new IdentityResponseDto(Boolean.TRUE,"placed order",
                            new IdentityResponseDto.IdentityDto(Boolean.FALSE,Boolean.TRUE));
                }else{
                    return new IdentityResponseDto(Boolean.FALSE,"account and order not match!",null);
                }
            }
        }
    }
}
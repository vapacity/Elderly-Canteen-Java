package com.javaee.elderlycanteen.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.javaee.elderlycanteen.dao.CartDao;
import com.javaee.elderlycanteen.dao.CartItemDao;
import com.javaee.elderlycanteen.dao.DishDao;
import com.javaee.elderlycanteen.dao.WeekMenuDao;
import com.javaee.elderlycanteen.dto.cart.CartItemRequestDto;
import com.javaee.elderlycanteen.dto.cart.CartItemResponseDto;
import com.javaee.elderlycanteen.dto.cart.CartItemsDto;
import com.javaee.elderlycanteen.dto.cart.DeleteRequestDto;
import com.javaee.elderlycanteen.entity.Cart;
import com.javaee.elderlycanteen.entity.CartItem;
import com.javaee.elderlycanteen.entity.Dish;
import com.javaee.elderlycanteen.entity.WeekMenu;
import com.javaee.elderlycanteen.exception.InvalidInputException;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.google.gson.Gson;
import static com.javaee.elderlycanteen.utils.DateUtils.getCurrentDate;
import static com.javaee.elderlycanteen.utils.DateUtils.getDayOfWeek;

@Service
public class CartItemService {

    private final CartItemDao cartItemDao;
    private final CartDao cartDao;
    private final DishDao dishDao;
    private final WeekMenuDao weekMenuDao;

    private final OrderInfoService orderInfoService;


    @Autowired
    public CartItemService(CartItemDao cartItemDao, CartDao cartDao, DishDao dishDao, WeekMenuDao weekMenuDao, OrderInfoService orderInfoService) {

        this.cartItemDao = cartItemDao;
        this.cartDao = cartDao;
        this.dishDao = dishDao;
        this.weekMenuDao = weekMenuDao;

        this.orderInfoService = orderInfoService;

    }

    public Integer addCartItem(CartItem cartItem){
        Integer ret = this.cartItemDao.addCartItem(cartItem);
        if(ret != 1){
            throw new ServiceException("insert cart item error");
        }
        return ret;
    }

    public List<CartItem> getAllCartItem(){
        List<CartItem> cartItems = this.cartItemDao.getAllCartItems();
        if(cartItems == null){
            throw new ServiceException("cart item is null or get all cart item error");
        }
        return cartItems;
    }

    public List<CartItem> getCartItemByCartId(Integer cartId){
        List<CartItem> cartItems = this.cartItemDao.getCartItemByCartId(cartId);
        if(cartItems == null){
            throw new ServiceException("cart item is null or cartId is wrong");
        }
        return cartItems;
    }

    public Integer setCartItemQuantity(Integer cartId, Integer dishId, Date week, Integer quantity){
        Integer ret = this.cartItemDao.updateCartItemQuantity(cartId,dishId,week, quantity);
        if(ret != 1){
            throw new ServiceException("update cart item quantity error");
        }
        return ret;
    }

    public Integer updateCartUpdatedTime(Integer cartId, Date updatedTime){
        Integer ret = this.cartDao.updateCartUpdatedTime(cartId, updatedTime);
        if(ret != 1){
            throw new ServiceException("update cart item updated time error");
        }
        return ret;
    }

    public Integer deleteCartItemByPrimaryKey(Integer cartId, Integer dishId, Date week){
        Integer ret = this.cartItemDao.deleteCartItemByPrimaryKey(cartId,dishId,week);
        if(ret != 1) {
            throw new ServiceException("delete cart item error");
        }
        return ret;
    }

    public CartItemResponseDto updateCartItem(CartItemRequestDto Dto) throws ParseException {
        // 验证购物车是否存在
        Cart cart = this.cartDao.getCartByCartId(Dto.getCartId());
        if(cart == null) {
            throw new InvalidInputException("Cart not exist!");
        }
        System.out.println(cart);

        // 验证 DishId 是否在今日菜单中
        Date date = getCurrentDate();

        WeekMenu weekMenu = this.weekMenuDao.findWeekMenuByWeekAndDishId(date,Dto.getDishId());
        if(weekMenu == null) {
            throw new InvalidInputException("Week menu not exist!");
        }
        System.out.println(weekMenu);
        // 检查库存是否足够
        Integer restock = this.weekMenuDao.findStockByDishIdAndWeek(Dto.getDishId(),date);
        if(restock <= 0) {
            throw new InvalidInputException("Restock not enough!(restock <= 0)");
        }else if(Dto.getQuantity()>restock) {
            throw new InvalidInputException("Restock not enough!(quantity > restock)");
        }
        System.out.println(restock);

        // 添加或更新购物车项
        CartItem cartItemEntity = this.cartItemDao.getCartItemByCartIdAndDishId(Dto.getCartId(),Dto.getDishId());
        if(cartItemEntity == null) {
            CartItem newCartItem = new CartItem(Dto.getCartId(),Dto.getDishId(),date,Dto.getQuantity());
            this.addCartItem(newCartItem);
            System.out.println("insert cartItem success!");
        }else{
            this.setCartItemQuantity(Dto.getCartId(),Dto.getDishId(),date,Dto.getQuantity());
            System.out.println("update!"+cartItemEntity);
        }

        // 更新购物车更新时间
        this.updateCartUpdatedTime(Dto.getCartId(),date);
        System.out.println("update Cart UpdatedTime!");

        // 返回更新成功信息
        CartItemResponseDto responseDto = new CartItemResponseDto(Boolean.TRUE,"update successfully!");
        return responseDto;
    }

    public CartItemResponseDto deleteCartItem(DeleteRequestDto Dto, Integer accountId) throws ParseException {
        //验证cartId是否存在且属于accountId
        Cart cart = this.cartDao.getCartByCartIdAndAccountId(Dto.getCartId(),accountId);
        if(cart == null) {
            throw new InvalidInputException("Cart not exist or Account does not match Cart!");
        }
        System.out.println(cart);

        //验证dishId是否在购物车中
        Date date = getCurrentDate();

        CartItem cartItem = this.cartItemDao.getCartItemByPrimaryKey(Dto.getCartId(),Dto.getCartId(),date);
        if(cartItem == null) {
            throw new InvalidInputException("Cart item not exist!");
        }
        System.out.println(cartItem);

        //删除购物车项
        this.deleteCartItemByPrimaryKey(Dto.getCartId(),Dto.getCartId(),date);
        System.out.println("delete CartItem successfully!");

        // 更新购物车更新时间
        this.updateCartUpdatedTime(Dto.getCartId(),date);
        System.out.println("update Cart UpdatedTime!");

        // 返回更新成功信息
        CartItemResponseDto responseDto = new CartItemResponseDto(Boolean.TRUE,"delete successfully!");
        return responseDto;
    }

    public CartItemsDto getCartItems(Integer cartId, Integer accountId) throws ParseException {
        // 验证CartId是否存在
        Cart cart = this.cartDao.getCartByCartIdAndAccountId(cartId,accountId);
        if(cart == null) {
            System.out.println(cartId+" "+accountId);
            throw new ServiceException("Cart not found!");
        }

        // 验证CartId是否在OrderInfo表中
        this.orderInfoService.getOrderInfoByCartId(cart.getCartId());

        List<CartItem> cartItems = this.cartItemDao.getCartItemByCartId(cartId);
        if(cartItems == null){
            CartItemsDto cartItemsDto = new CartItemsDto(new ArrayList<>(),"Cart is null!",Boolean.FALSE);
            return cartItemsDto;
        }

        // 创建返回的Menu列表
        List<CartItemsDto.Menu> menus = new ArrayList<CartItemsDto.Menu>();

        for (CartItem cartItem : cartItems) {
            Date date = getCurrentDate();
            WeekMenu weekMenu = this.weekMenuDao.findWeekMenuByDishIdAndWeekAndDay(cartItem.getDishId(),cartItem.getWeek(),getDayOfWeek(date));
            if(weekMenu.getDisPrice()!=null) {
                Double discountPrice = weekMenu.getDisPrice();
            }else{
                Double discountPrice = 0.0;
            }
            Dish dish = this.dishDao.getDishById(cartItem.getDishId());
            if(dish!=null) {
                CartItemsDto.Menu menu = new CartItemsDto.Menu();

                menu.setDishId(dish.getDishId());
                menu.setDishName(dish.getDishName());
                menu.setDishPrice(dish.getPrice());
                menu.setDiscountPrice(dish.getPrice());
                menu.setImageUrl(dish.getImageUrl());
                menu.setQuantity(cartItem.getQuantity());

                menus.add(menu);
            }

        }

        // 返回购物车项目信息
        CartItemsDto cartItemsDto = new CartItemsDto(menus,"get CartItems successfully!",Boolean.TRUE);

        return cartItemsDto;
    }

    public CartItemResponseDto addCartItemByAudio(String commend, Integer cartId) throws ParseException {
        // TODO: 2021/12/20 增加语音识别功能

        Map<Integer, String> menuItems = new HashMap<>();
        List<WeekMenu> weekMenus = weekMenuDao.findWeekMenuByWeek(getCurrentDate(),getDayOfWeek(getCurrentDate()));
        if(weekMenus!=null){
            for(WeekMenu weekMenu:weekMenus){
                Integer dishId = weekMenu.getDishId();
                String dishName = dishDao.getDishById(dishId).getDishName();
                menuItems.put(dishId,dishName);
            }
        }
        System.out.println(menuItems);
        try {
            URL url = new URL("http://127.0.0.1:5000/process_order");

            // 创建 HTTP 连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            // 使用 Gson 将 Map 转换为 JSON 字符串
            Gson gson = new Gson();
            String menuJson = gson.toJson(menuItems); // 将 Map 转换为 JSON 字符串

            // 构造请求数据
            JsonObject data = new JsonObject();
            data.addProperty("menu_items_json", menuJson);  // 使用 JSON 字符串
            data.addProperty("order_text", commend);

            // 发送请求数据
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = data.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 获取响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 解析 JSON 响应
            JsonObject responseJson = JsonParser.parseString(response.toString()).getAsJsonObject();

            // 遍历响应的菜品和数量
            for (String dishIdStr : responseJson.keySet()) {
                int dishId = Integer.parseInt(dishIdStr); // 将 String 转换为 int
                int quantity = responseJson.get(dishIdStr).getAsInt(); // 获取数量

                // 调用 func 函数
                this.updateCartItem(new CartItemRequestDto(cartId, dishId, quantity));
            }

            // 返回解析后的结果
            // 你可以根据实际需求处理 Flask 返回的结果并返回一个具体的 DTO。
            return new CartItemResponseDto(Boolean.TRUE,"add successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
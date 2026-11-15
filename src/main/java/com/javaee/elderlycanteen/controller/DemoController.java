package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dao.CartDao;
import com.javaee.elderlycanteen.dao.OrderInfoDao;
import com.javaee.elderlycanteen.dto.cart.*;
import com.javaee.elderlycanteen.dto.login.LoginRequestDto;
import com.javaee.elderlycanteen.dto.order.OrderItem;
import com.javaee.elderlycanteen.dto.register.Menu;
import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.service.*;
import com.javaee.elderlycanteen.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Profile("demo")
@RestController
@RequestMapping("/demo-api")
@RequiredArgsConstructor
public class DemoController {
    private final AccountService accounts;
    private final WeekMenuService menus;
    private final CartService carts;
    private final CartItemService items;
    private final OrderInfoService orders;
    private final CartDao cartDao;
    private final OrderInfoDao orderDao;

    @PostMapping("/account/login")
    public Map<String, Object> login(@RequestBody LoginRequestDto credentials) {
        Account account;
        try {
            account = accounts.login(credentials);
        } catch (RuntimeException e) {
            return Map.of("loginSuccess", false, "msg", "手机号或密码不正确");
        }
        String token = JWTUtils.getToken(Map.of("accountId", account.getAccountId().toString(),
                "accountName", account.getAccountName(), "identity", account.getIdentity()));
        return Map.of("loginSuccess", true, "response", Map.of("token", token,
                "identity", account.getIdentity(), "accountName", account.getAccountName()));
    }

    @GetMapping("/account/getPersonInfo")
    public Map<String, Object> profile(@RequestHeader("Authorization") String token) {
        return Map.of("getSuccess", true, "response", accounts.getPersonInfo(accountId(token)).getResponse());
    }

    @GetMapping("/order/getMenuToday")
    public Object menu() throws ParseException {
        return Map.of("menu", menus.getTodayMenu().getMenu().stream().map(DemoController::legacyDish).toList());
    }

    private static Map<String, Object> legacyDish(Menu menu) {
        return Map.of("dishId", menu.getDishId(), "dishName", menu.getDishName(),
                "dishPrice", menu.getDishPrice(), "price", menu.getDishPrice(),
                "discountPrice", Objects.requireNonNullElse(menu.getDiscountPrice(), 0.0),
                "imageUrl", menu.getImageUrl(), "picture", menu.getImageUrl(),
                "category", String.valueOf(menu.getCategory()), "sale", menu.getSales(), "stock", menu.getStock());
    }

    @GetMapping({"/HomePage/dishes", "/HomePage/getDayDiscount"})
    public Object homepageMenu() throws ParseException {
        return Map.of("response", menus.getTodayMenu().getMenu().stream().map(DemoController::legacyDish).toList());
    }

    @GetMapping("/HomePage/getWeekmenuAndWeekDiscount")
    public Object weekMenu() throws ParseException {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Menu dish : menus.getTodayMenu().getMenu()) {
            var row = new HashMap<>(legacyDish(dish));
            row.put("week", today.toString());
            rows.add(row);
        }
        return Map.of("response", rows);
    }

    @GetMapping("/HomePage/getReviews")
    public Object reviews() {
        return Map.of("response", List.of());
    }

    @GetMapping("/Donate/getDonationList")
    public Object donations() {
        return Map.of("responce", List.of());
    }

    @PostMapping("/cart/createCart")
    public synchronized Object createCart(@RequestHeader("Authorization") String token) throws ParseException {
        return carts.createCart(accountId(token));
    }

    @GetMapping("/cart/getCartItem{cartId}")
    public Object cartItems(@PathVariable Integer cartId, @RequestHeader("Authorization") String token) throws ParseException {
        int id = accountId(token);
        checkCart(cartId, id);
        return items.getCartItems(cartId, id);
    }

    @PostMapping("/cart/updateCartItem")
    public synchronized Object update(@RequestBody CartItemRequestDto request, @RequestHeader("Authorization") String token) throws ParseException {
        checkCart(request.getCartId(), accountId(token));
        if (request.getDishId() == null || request.getQuantity() == null || request.getQuantity() < 1) {
            return new CartItemResponseDto(false, "请选择有效数量");
        }
        try {
            return items.updateCartItem(request);
        } catch (RuntimeException e) {
            if (String.valueOf(e.getMessage()).startsWith("Restock not enough")) {
                return new CartItemResponseDto(false, "库存不足");
            }
            throw e;
        }
    }

    @DeleteMapping("/cart/deleteCartItem")
    public synchronized Object delete(@RequestBody DeleteRequestDto request, @RequestHeader("Authorization") String token) throws ParseException {
        int id = accountId(token);
        checkCart(request.getCartId(), id);
        return items.deleteCartItem(request, id);
    }

    @PostMapping("/cart/clearCart")
    public synchronized Object clear(@RequestBody NormalRequestDto request, @RequestHeader("Authorization") String token) {
        checkCart(request.getCartId(), accountId(token));
        return carts.clearCart(request);
    }

    @PostMapping("/cart/ensureCart")
    public synchronized Object checkout(@RequestBody EnsureCartRequestDto request, @RequestHeader("Authorization") String token) throws ParseException {
        int id = accountId(token);
        checkCart(request.getCartId(), id);
        if (request.getNewAddress() == null || request.getNewAddress().isBlank()) {
            request.setNewAddress(accounts.getAccountById(id).getAddress());
        }
        if (Boolean.TRUE.equals(request.getDeliverOrDining()) && request.getNewAddress().isBlank()) {
            return new CartItemResponseDto(false, "请填写配送地址");
        }
        try {
            return carts.ensureCart(request, id);
        } catch (ServiceException e) {
            String msg = e.getMessage();
            return new CartItemResponseDto(false, msg.contains("restock") ? "库存不足，请返回购物车修改数量"
                    : msg.contains("余额不足") ? msg : "下单未完成，请检查购物车后重试");
        }
    }

    @GetMapping("/order/getPastOrder")
    public Object history(@RequestHeader("Authorization") String token) {
        OrderItem[] records = orders.getHistoryOrderInfo(accountId(token)).getResponse();
        Arrays.sort(records, Comparator.comparing(OrderItem::getOrderId).reversed());
        for (OrderItem record : records) localize(record);
        return Map.of("success", true, "response", records);
    }

    @GetMapping("/order/getOrderMsg")
    public Object order(@RequestParam("OrderId") Integer orderId, @RequestHeader("Authorization") String token) {
        return Map.of("success", true, "response", ownedOrder(orderId, token));
    }

    @GetMapping("/order/getIdentityInOrder")
    public Object identity(@RequestParam Integer orderId, @RequestHeader("Authorization") String token) {
        ownedOrder(orderId, token);
        return Map.of("success", true, "response", Map.of("isOwner", true, "isDeliver", false));
    }

    private static void localize(OrderItem record) {
        if ("To be confirmed".equals(record.getStatus())) record.setStatus("待确认");
        if ("Pending".equals(record.getDeliverStatus())) record.setDeliverStatus("待接单");
    }

    private OrderItem ownedOrder(Integer orderId, String token) {
        return Arrays.stream(orders.getHistoryOrderInfo(accountId(token)).getResponse())
                .filter(order -> orderId.equals(order.getOrderId()))
                .peek(DemoController::localize)
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void checkCart(Integer cartId, int accountId) {
        if (cartId == null || cartDao.getCartByCartIdAndAccountId(cartId, accountId) == null
                || !orderDao.getOrderInfoByCartId(cartId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private static int accountId(String header) {
        try {
            if (header.startsWith("Bearer ")) return JWTUtils.getTokenInfo(header.substring(7)).getAccountId();
        } catch (RuntimeException ignored) {
            // Expired or invalid credentials.
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}

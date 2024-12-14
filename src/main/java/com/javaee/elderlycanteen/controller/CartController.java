package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.cart.CartItemResponseDto;
import com.javaee.elderlycanteen.dto.cart.ClearCartResponseDto;
import com.javaee.elderlycanteen.dto.cart.EnsureCartRequestDto;
import com.javaee.elderlycanteen.dto.cart.NormalRequestDto;
import com.javaee.elderlycanteen.entity.Cart;
import com.javaee.elderlycanteen.entity.TokenInfo;
import com.javaee.elderlycanteen.exception.InvalidInputException;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.service.CartService;
import com.javaee.elderlycanteen.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 新增购物车

    @PostMapping("/addCart")
    public ResponseEntity<Integer> addCart(@RequestBody Cart cart) {
        try {
            System.out.println("Controller: "+cart);
            Integer result = cartService.addCart(cart);
            return ResponseEntity.ok(result);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 查找全部购物车
    @GetMapping("/getAllCart")
    public ResponseEntity<List<Cart>> getAllCarts() {
        try {
            List<Cart> carts = cartService.getAllCart();
            return ResponseEntity.ok(carts);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    // 删除购物车
    @DeleteMapping("/deleteCartById/{id}")
    public ResponseEntity<Integer> deleteCart(@PathVariable Integer id) {
        try {
            Integer result = cartService.deleteCart(id);
            return ResponseEntity.ok(result);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/ensureCart")
    public CartItemResponseDto ensureCart(@RequestBody EnsureCartRequestDto ensureCartRequestDto,
                                          @RequestHeader(name="Authorization",required = false) String token) throws ParseException {
        Map<String, Object> response = new HashMap<>();
        try {
            // 验证 token
            token = token.substring(7); // 去掉 "Bearer " 前缀
            if (token == null || token.isEmpty()) {
                throw new InvalidInputException("token is null or empty!");
            }
            TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
            Integer accountId = tokenInfo.getAccountId();

            if (accountId == null) {
                throw new InvalidInputException("account is unauthorized!");
            }
            CartItemResponseDto responseDto = this.cartService.ensureCart(ensureCartRequestDto,accountId);
            return responseDto;

        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            throw new ServiceException(e.getMessage());
        }
    }

    @PostMapping("/clearCart")
    public ClearCartResponseDto clearCart(@RequestBody NormalRequestDto normalRequestDto,
                                          @RequestHeader(name="Authorization",required = false) String token){
        Map<String, Object> response = new HashMap<>();
        try {
            // 验证 token
            token = token.substring(7); // 去掉 "Bearer " 前缀
            if (token == null || token.isEmpty()) {
                throw new InvalidInputException("token is null or empty!");
            }
            TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
            Integer accountId = tokenInfo.getAccountId();

            if (accountId == null) {
                throw new InvalidInputException("account is unauthorized!");
            }
            ClearCartResponseDto clearCartResponseDto = this.cartService.clearCart(normalRequestDto);
            return clearCartResponseDto;
        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            throw new ServiceException(e.getMessage());
        }
    }
}
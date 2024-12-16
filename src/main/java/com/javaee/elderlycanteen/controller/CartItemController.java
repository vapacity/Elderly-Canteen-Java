package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.cart.*;
import com.javaee.elderlycanteen.entity.CartItem;
import com.javaee.elderlycanteen.entity.TokenInfo;
import com.javaee.elderlycanteen.exception.InvalidInputException;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.service.CartItemService;
import com.javaee.elderlycanteen.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
<<<<<<< Updated upstream
@RequestMapping("/cartItems")
=======
@RequestMapping("api/cartItems")
@Transactional
>>>>>>> Stashed changes
public class CartItemController {

    private final CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping("/getAllCartItem")
    public List<CartItem> getAllCartItem(){
        List<CartItem> cartItems = cartItemService.getAllCartItem();
        return cartItems;
    }

    @PostMapping("/updateCartItem")
    public CartItemResponseDto updateCartItem(@RequestBody CartItemRequestDto cartItemRequestDto,
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

            CartItemResponseDto responseDto = this.cartItemService.updateCartItem(cartItemRequestDto);
            return responseDto;

        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            throw new ServiceException(e.getMessage());
        }
    }

    @DeleteMapping("/deleteCartItem")
    public CartItemResponseDto deleteCartItem(@RequestBody DeleteRequestDto deleteRequestDto,
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

            CartItemResponseDto responseDto = this.cartItemService.deleteCartItem(deleteRequestDto,accountId);
            return responseDto;

        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            throw new ServiceException(e.getMessage());
        }
    }

    @GetMapping("/getCartItems")
    public CartItemsDto getCartItems(@RequestParam Integer cartId,
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

            CartItemsDto cartItemsDto = this.cartItemService.getCartItems(cartId,accountId);
            return cartItemsDto;

        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            throw new ServiceException(e.getMessage());
        }
    }

}
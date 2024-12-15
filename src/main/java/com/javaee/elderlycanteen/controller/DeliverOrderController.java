package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.cart.CartItemResponseDto;
import com.javaee.elderlycanteen.dto.order.NormalResponseDto;
import com.javaee.elderlycanteen.dto.volServe.AcceptRequestDto;
import com.javaee.elderlycanteen.dto.volServe.AccessOrderResponseDto;
import com.javaee.elderlycanteen.entity.TokenInfo;
import com.javaee.elderlycanteen.exception.InvalidInputException;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.service.DeliverOrderService;
import com.javaee.elderlycanteen.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.javaee.elderlycanteen.enumeration.DeliverOrderStatusEnum.DELIVERED;
import static com.javaee.elderlycanteen.enumeration.DeliverOrderStatusEnum.RECEIVED;

@RestController
@RequestMapping("/deliverOrders")
@Transactional
public class DeliverOrderController {

    private final DeliverOrderService deliverOrderService;

    @Autowired
    public DeliverOrderController(DeliverOrderService deliverOrderService) {
        this.deliverOrderService = deliverOrderService;
    }

    @GetMapping("/getAcceptableOrder")
    public AccessOrderResponseDto getAcceptableOrder() {
        AccessOrderResponseDto responseDto = this.deliverOrderService.getAcceptableOrder();
        if (responseDto.getSuccess()==Boolean.FALSE) {
            throw new ServiceException(responseDto.getMsg());
        }
        return responseDto;
    }

    @PostMapping("/postAcceptOrder")
    public NormalResponseDto acceptOrder(@RequestBody AcceptRequestDto Dto,
                                         @RequestHeader(name="Authorization",required = false) String token) {
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

            NormalResponseDto responseDto = this.deliverOrderService.acceptOrder(Dto.getOrderId(),accountId);
            if (responseDto.getSuccess()==Boolean.FALSE) {
                throw new ServiceException(responseDto.getMsg());
            }
            return responseDto;

        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            throw new ServiceException(e.getMessage());
        }
    }

    @PostMapping("/postConfirmDelivered")
    public NormalResponseDto confirmDelivered(@RequestBody AcceptRequestDto Dto,
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

            NormalResponseDto responseDto = this.deliverOrderService.confirmDelivered(Dto.getOrderId(),accountId);
            if (responseDto.getSuccess()==Boolean.FALSE) {
                throw new ServiceException(responseDto.getMsg());
            }
            return responseDto;

        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            throw new ServiceException(e.getMessage());
        }
    }

    @GetMapping("/getAcceptedOrder")
    public AccessOrderResponseDto getAcceptedOrder(@RequestHeader(name="Authorization",required = false) String token) {
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

            AccessOrderResponseDto responseDto = this.deliverOrderService.getOrderByDeliverStatus(accountId,DELIVERED.getDescription());
            if (responseDto.getSuccess()==Boolean.FALSE) {
                throw new ServiceException(responseDto.getMsg());
            }
            return responseDto;

        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            throw new ServiceException(e.getMessage());
        }
    }

    @GetMapping("/getFinishedOrder")
    public AccessOrderResponseDto getFinishedOrder(@RequestHeader(name="Authorization",required = false) String token) {
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

            AccessOrderResponseDto responseDto = this.deliverOrderService.getOrderByDeliverStatus(accountId, RECEIVED.getDescription());
            if (responseDto.getSuccess()==Boolean.FALSE) {
                throw new ServiceException(responseDto.getMsg());
            }
            return responseDto;

        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            throw new ServiceException(e.getMessage());
        }
    }
}
package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.cart.CartResponseDto;
import com.javaee.elderlycanteen.dto.cart.NormalRequestDto;
import com.javaee.elderlycanteen.dto.order.*;
import com.javaee.elderlycanteen.dto.register.MenuResponseDto;
import com.javaee.elderlycanteen.entity.TokenInfo;
import com.javaee.elderlycanteen.exception.InvalidInputException;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.service.OrderInfoService;
import com.javaee.elderlycanteen.service.WeekMenuService;
import com.javaee.elderlycanteen.utils.JWTUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orderInfos")
public class OrderInfoController {

    private final WeekMenuService weekMenuService;
    private final OrderInfoService orderInfoService;

    @Autowired
    public OrderInfoController(WeekMenuService weekMenuService, OrderInfoService orderInfoService) {
        this.weekMenuService = weekMenuService;
        this.orderInfoService = orderInfoService;
    }

    @GetMapping("/getManuToday")
    public MenuResponseDto getManuToday() throws ParseException {
        MenuResponseDto responseDto = this.weekMenuService.getTodayMenu();
        return responseDto;
    }

    @GetMapping("/getPastOrder")
    public GetOrderResponseDto getPastOrder(@RequestHeader(name="Authorization",required = false) String token){
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

            GetOrderResponseDto responseDto = this.orderInfoService.getHistoryOrderInfo(accountId);
            if (responseDto.getSuccess() == Boolean.FALSE) {
                throw new ServiceException(responseDto.getMsg());
            }
            return responseDto;
        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            throw new ServiceException(e.getMessage());
        }
    }

    @PostMapping("/confirmOrder")
    public NormalResponseDto confirmOrder(@RequestBody NormalOrderRequestDto Dto,
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

            NormalResponseDto responseDto = this.orderInfoService.confirmOrder(Dto.getOrderId(),accountId);
            if (responseDto.getSuccess() == Boolean.FALSE) {
                throw new ServiceException(responseDto.getMsg());
            }
            return responseDto;
        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            throw new ServiceException(e.getMessage());
        }
    }
    @GetMapping("/getOrderDeliverMsg")
    public GetODMsgResponseDto GetOrderDeliverMsg(@RequestParam Integer orderId){
        GetODMsgResponseDto responseDto = this.orderInfoService.GetOrderDeliverMsg(orderId);
        if (responseDto.getSuccess() == Boolean.FALSE) {
            throw new ServiceException(responseDto.getMsg());
        }
        return responseDto;
    }

    @PostMapping("/postDiningReview")
    public NormalResponseDto submitDiningReview(@RequestBody ReviewSubmissionDto Dto){
        if(Dto==null||Dto.getOrderId()==null||Dto.getCStars()<1||Dto.getCStars()>5){
            return new NormalResponseDto(Boolean.FALSE,"input invalid!");
        }
        if(Dto.getCReviewText().length()>50){
            return new NormalResponseDto(Boolean.FALSE,"input too long, no more than 50!");
        }
        NormalResponseDto responseDto = this.orderInfoService.submitDiningReview(Dto);
        if(responseDto.getSuccess() == Boolean.FALSE){
            throw new ServiceException(responseDto.getMsg());
        }
        return responseDto;
    }

    @GetMapping("/getDiningComment")
    public ReviewResponseDto GetDiningReview(@RequestParam Integer orderId){
        ReviewResponseDto responseDto = this.orderInfoService.getDiningReview(orderId);
        if(responseDto.getSuccess() == Boolean.FALSE){
            throw new ServiceException(responseDto.getMsg());
        }
        return responseDto;
    }

    @PostMapping("/postDeliverComment")
    public NormalResponseDto submitDeliverReview(@RequestBody ReviewSubmissionDto Dto){
        if(Dto==null||Dto.getOrderId()==null||Dto.getCStars()<1||Dto.getCStars()>5||Dto.getDStars()<1||Dto.getDStars()>5){
            return new NormalResponseDto(Boolean.FALSE,"input invalid!");
        }
        if(Dto.getCReviewText().length()>50||Dto.getDReviewText().length()>50){
            return new NormalResponseDto(Boolean.FALSE,"input too long, no more than 50!");
        }
        NormalResponseDto responseDto = this.orderInfoService.submitDeliverReview(Dto);
        if(responseDto.getSuccess() == Boolean.FALSE){
            throw new ServiceException(responseDto.getMsg());
        }
        return responseDto;
    }

    @GetMapping("/getDeliverComment")
    public ReviewResponseDto GetDeliverComment(@RequestParam Integer orderId){
        ReviewResponseDto responseDto = this.orderInfoService.getDeliverReview(orderId);
        if(responseDto.getSuccess() == Boolean.FALSE){
            throw new ServiceException(responseDto.getMsg());
        }
        return responseDto;
    }

    @GetMapping("/getOrderMsg")
    public OrderInfoDto GetOrderMsg(@RequestParam Integer orderId){
        OrderInfoDto orderInfoDto = this.orderInfoService.getOrderInfoById(orderId);
        if(orderInfoDto.getSuccess() == Boolean.FALSE){
            throw new ServiceException(orderInfoDto.getMsg());
        }
        return orderInfoDto;
    }

    @GetMapping("/getIdentityInOrder")
    public IdentityResponseDto GetIdentityInOrder(@RequestParam Integer orderId,
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

            IdentityResponseDto responseDto = this.orderInfoService.getIdentityInOrder(orderId, accountId);
            if (responseDto.getSuccess() == Boolean.FALSE) {
                throw new ServiceException(responseDto.getMsg());
            }
            return responseDto;
        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            throw new ServiceException(e.getMessage());
        }
    }
}
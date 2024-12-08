package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.OrderReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderReviews")
public class OrderReviewController {

    private final OrderReviewService orderReviewService;

    @Autowired
    public OrderReviewController(OrderReviewService orderReviewService) {
        this.orderReviewService = orderReviewService;
    }

    // 在这里添加特定的API端点方法
}
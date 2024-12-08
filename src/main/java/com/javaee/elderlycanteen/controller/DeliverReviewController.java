package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.DeliverReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deliverReviews")
public class DeliverReviewController {

    private final DeliverReviewService deliverReviewService;

    @Autowired
    public DeliverReviewController(DeliverReviewService deliverReviewService) {
        this.deliverReviewService = deliverReviewService;
    }

    // 在这里添加特定的API端点方法
}
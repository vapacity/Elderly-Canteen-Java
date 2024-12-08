package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.VolReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/volReviews")
public class VolReviewController {

    private final VolReviewService volReviewService;

    @Autowired
    public VolReviewController(VolReviewService volReviewService) {
        this.volReviewService = volReviewService;
    }

    // 在这里添加特定的API端点方法
}
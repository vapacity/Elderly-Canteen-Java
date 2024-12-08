package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.DeliverVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deliverVs")
public class DeliverVController {

    private final DeliverVService deliverVService;

    @Autowired
    public DeliverVController(DeliverVService deliverVService) {
        this.deliverVService = deliverVService;
    }

    // 在这里添加特定的API端点方法
}
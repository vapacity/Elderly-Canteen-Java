package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.DeliverOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deliverOrders")
public class DeliverOrderController {

    private final DeliverOrderService deliverOrderService;

    @Autowired
    public DeliverOrderController(DeliverOrderService deliverOrderService) {
        this.deliverOrderService = deliverOrderService;
    }

    // 在这里添加特定的API端点方法
}
package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.DeliverEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deliverEmployees")
public class DeliverEmployeeController {

    private final DeliverEmployeeService deliverEmployeeService;

    @Autowired
    public DeliverEmployeeController(DeliverEmployeeService deliverEmployeeService) {
        this.deliverEmployeeService = deliverEmployeeService;
    }

    // 在这里添加特定的API端点方法
}
package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.PayWageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payWages")
public class PayWageController {

    private final PayWageService payWageService;

    @Autowired
    public PayWageController(PayWageService payWageService) {
        this.payWageService = payWageService;
    }

    // 在这里添加特定的API端点方法
}
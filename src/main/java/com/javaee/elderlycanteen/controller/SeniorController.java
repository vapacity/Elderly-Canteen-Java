package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.SeniorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seniors")
public class SeniorController {

    private final SeniorService seniorService;

    @Autowired
    public SeniorController(SeniorService seniorService) {
        this.seniorService = seniorService;
    }

    // 在这里添加特定的API端点方法
}
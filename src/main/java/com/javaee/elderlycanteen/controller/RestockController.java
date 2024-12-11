package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.RestockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restocks")
public class RestockController {

    private final RestockService restockService;

    @Autowired
    public RestockController(RestockService restockService) {
        this.restockService = restockService;
    }

    // 在这里添加特定的API端点方法
}
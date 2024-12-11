package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.WeekMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weekMenus")
public class WeekMenuController {

    private final WeekMenuService weekMenuService;

    @Autowired
    public WeekMenuController(WeekMenuService weekMenuService) {
        this.weekMenuService = weekMenuService;
    }

    // 在这里添加特定的API端点方法
}
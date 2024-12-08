package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.ActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actionLogs")
public class ActionLogController {

    private final ActionLogService actionLogService;

    @Autowired
    public ActionLogController(ActionLogService actionLogService) {
        this.actionLogService = actionLogService;
    }

    // 在这里添加特定的API端点方法
}
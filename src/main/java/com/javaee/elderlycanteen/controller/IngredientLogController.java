package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.IngredientLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ingredientLogs")
public class IngredientLogController {

    private final IngredientLogService ingredientLogService;

    @Autowired
    public IngredientLogController(IngredientLogService ingredientLogService) {
        this.ingredientLogService = ingredientLogService;
    }

    // 在这里添加特定的API端点方法
}
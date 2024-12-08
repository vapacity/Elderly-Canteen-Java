package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.FormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/formulas")
public class FormulaController {

    private final FormulaService formulaService;

    @Autowired
    public FormulaController(FormulaService formulaService) {
        this.formulaService = formulaService;
    }

    // 在这里添加特定的API端点方法
}
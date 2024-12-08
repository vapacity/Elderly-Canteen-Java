package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.FormulaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormulaService {

    private final FormulaDao formulaDao;

    @Autowired
    public FormulaService(FormulaDao formulaDao) {
        this.formulaDao = formulaDao;
    }

    // 在这里添加特定的业务逻辑方法
}
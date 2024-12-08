package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.IngredientLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientLogService {

    private final IngredientLogDao ingredientLogDao;

    @Autowired
    public IngredientLogService(IngredientLogDao ingredientLogDao) {
        this.ingredientLogDao = ingredientLogDao;
    }

    // 在这里添加特定的业务逻辑方法
}
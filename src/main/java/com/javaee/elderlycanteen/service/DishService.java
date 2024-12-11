package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.DishDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishService {

    private final DishDao dishDao;

    @Autowired
    public DishService(DishDao dishDao) {
        this.dishDao = dishDao;
    }

    // 在这里添加特定的业务逻辑方法
}
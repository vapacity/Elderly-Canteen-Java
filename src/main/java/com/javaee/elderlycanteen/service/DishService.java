package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.CategoryDao;
import com.javaee.elderlycanteen.dao.DishDao;
import com.javaee.elderlycanteen.dto.dish.DishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishService {

    private final DishDao dishDao;
    private final CategoryDao categoryDao;
    @Autowired
    public DishService(DishDao dishDao, CategoryDao categoryDao) {
        this.dishDao = dishDao;
        this.categoryDao = categoryDao;
    }

    // 在这里添加特定的业务逻辑方法
    public DishDto searchDish(String dishName, String categoryName){
        categoryDao.getCategoryByName(categoryName);
        dishDao.getDishByName(dishName);
    }
}
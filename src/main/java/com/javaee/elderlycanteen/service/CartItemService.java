package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.CartItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final CartItemDao cartItemDao;

    @Autowired
    public CartItemService(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    // 在这里添加特定的业务逻辑方法
}
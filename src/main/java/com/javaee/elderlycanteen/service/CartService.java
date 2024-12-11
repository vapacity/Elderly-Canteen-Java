package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.CartDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartDao cartDao;

    @Autowired
    public CartService(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    // 在这里添加特定的业务逻辑方法
}
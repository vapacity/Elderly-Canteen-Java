package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.OrderInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderInfoService {

    private final OrderInfoDao orderInfoDao;

    @Autowired
    public OrderInfoService(OrderInfoDao orderInfoDao) {
        this.orderInfoDao = orderInfoDao;
    }

    // 在这里添加特定的业务逻辑方法
}
package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.OrderReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderReviewService {

    private final OrderReviewDao orderReviewDao;

    @Autowired
    public OrderReviewService(OrderReviewDao orderReviewDao) {
        this.orderReviewDao = orderReviewDao;
    }

    // 在这里添加特定的业务逻辑方法
}
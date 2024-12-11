package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.DeliverOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliverOrderService {

    private final DeliverOrderDao deliverOrderDao;

    @Autowired
    public DeliverOrderService(DeliverOrderDao deliverOrderDao) {
        this.deliverOrderDao = deliverOrderDao;
    }

    // 在这里添加特定的业务逻辑方法
}
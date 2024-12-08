package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.DeliverVDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliverVService {

    private final DeliverVDao deliverVDao;

    @Autowired
    public DeliverVService(DeliverVDao deliverVDao) {
        this.deliverVDao = deliverVDao;
    }

    // 在这里添加特定的业务逻辑方法
}
package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.DeliverReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliverReviewService {

    private final DeliverReviewDao deliverReviewDao;

    @Autowired
    public DeliverReviewService(DeliverReviewDao deliverReviewDao) {
        this.deliverReviewDao = deliverReviewDao;
    }

    // 在这里添加特定的业务逻辑方法
}
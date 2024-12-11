package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.VolReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VolReviewService {

    private final VolReviewDao volReviewDao;

    @Autowired
    public VolReviewService(VolReviewDao volReviewDao) {
        this.volReviewDao = volReviewDao;
    }

    // 在这里添加特定的业务逻辑方法
}
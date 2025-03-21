package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.PayWageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayWageService {

    private final PayWageDao payWageDao;

    @Autowired
    public PayWageService(PayWageDao payWageDao) {
        this.payWageDao = payWageDao;
    }

    // 在这里添加特定的业务逻辑方法
}
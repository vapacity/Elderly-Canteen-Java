package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.FinanceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinanceService {

    private final FinanceDao financeDao;

    @Autowired
    public FinanceService(FinanceDao financeDao) {
        this.financeDao = financeDao;
    }

    // 在这里添加特定的业务逻辑方法
}
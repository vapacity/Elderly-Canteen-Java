package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.DeliverEmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliverEmployeeService {

    private final DeliverEmployeeDao deliverEmployeeDao;

    @Autowired
    public DeliverEmployeeService(DeliverEmployeeDao deliverEmployeeDao) {
        this.deliverEmployeeDao = deliverEmployeeDao;
    }

    // 在这里添加特定的业务逻辑方法
}
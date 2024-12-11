package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.RestockDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestockService {

    private final RestockDao restockDao;

    @Autowired
    public RestockService(RestockDao restockDao) {
        this.restockDao = restockDao;
    }

    // 在这里添加特定的业务逻辑方法
}
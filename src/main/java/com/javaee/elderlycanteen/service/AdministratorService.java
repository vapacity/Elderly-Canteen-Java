package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.AdministratorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {

    private final AdministratorDao administratorDao;

    @Autowired
    public AdministratorService(AdministratorDao administratorDao) {
        this.administratorDao = administratorDao;
    }

    // 在这里添加特定的业务逻辑方法
}
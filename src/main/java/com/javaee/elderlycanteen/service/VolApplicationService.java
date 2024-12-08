package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.VolApplicationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VolApplicationService {

    private final VolApplicationDao volApplicationDao;

    @Autowired
    public VolApplicationService(VolApplicationDao volApplicationDao) {
        this.volApplicationDao = volApplicationDao;
    }

    // 在这里添加特定的业务逻辑方法
}
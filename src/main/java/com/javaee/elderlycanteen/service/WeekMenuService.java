package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.WeekMenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeekMenuService {

    private final WeekMenuDao weekMenuDao;

    @Autowired
    public WeekMenuService(WeekMenuDao weekMenuDao) {
        this.weekMenuDao = weekMenuDao;
    }

    // 在这里添加特定的业务逻辑方法
}
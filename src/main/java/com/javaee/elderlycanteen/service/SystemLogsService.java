package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.SystemLogsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemLogsService {

    private final SystemLogsDao systemLogsDao;

    @Autowired
    public SystemLogsService(SystemLogsDao systemLogsDao) {
        this.systemLogsDao = systemLogsDao;
    }

    // 在这里添加特定的业务逻辑方法
}
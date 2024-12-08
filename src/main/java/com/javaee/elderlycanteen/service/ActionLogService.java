package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.ActionLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionLogService {

    private final ActionLogDao actionLogDao;

    @Autowired
    public ActionLogService(ActionLogDao actionLogDao) {
        this.actionLogDao = actionLogDao;
    }

    // 在这里添加特定的业务逻辑方法
}
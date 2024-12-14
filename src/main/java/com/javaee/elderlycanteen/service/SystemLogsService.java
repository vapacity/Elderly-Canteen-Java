package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.SystemLogsDao;
import com.javaee.elderlycanteen.entity.SystemLogs;
import com.javaee.elderlycanteen.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

import static com.javaee.elderlycanteen.utils.DateUtils.getCurrentDate;

@Service
public class SystemLogsService {

    private final SystemLogsDao systemLogsDao;


    @Autowired
    public SystemLogsService(SystemLogsDao systemLogsDao) {

        this.systemLogsDao = systemLogsDao;
    }

    public void addSystemLog(String logLevel, String message) throws ParseException {
        this.systemLogsDao.insert(logLevel, message, getCurrentDate());
    }
}
package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.VolunteerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VolunteerService {

    private final VolunteerDao volunteerDao;

    @Autowired
    public VolunteerService(VolunteerDao volunteerDao) {
        this.volunteerDao = volunteerDao;
    }

    // 在这里添加特定的业务逻辑方法
}
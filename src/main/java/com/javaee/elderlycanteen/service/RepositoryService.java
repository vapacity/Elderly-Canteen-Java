package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.DishDao;
import com.javaee.elderlycanteen.dao.RepositoryDao;
import com.javaee.elderlycanteen.dao.WeekMenuDao;
import com.javaee.elderlycanteen.entity.Dish;
import com.javaee.elderlycanteen.entity.WeekMenu;
import com.javaee.elderlycanteen.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RepositoryService {

    private final RepositoryDao repositoryDao;

    @Autowired
    public RepositoryService(RepositoryDao repositoryDao) {
        this.repositoryDao = repositoryDao;
    }


}
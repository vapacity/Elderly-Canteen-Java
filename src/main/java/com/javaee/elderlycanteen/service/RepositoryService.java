package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.RepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService {

    private final RepositoryDao repositoryDao;

    @Autowired
    public RepositoryService(RepositoryDao repositoryDao) {
        this.repositoryDao = repositoryDao;
    }

    // 在这里添加特定的业务逻辑方法
}
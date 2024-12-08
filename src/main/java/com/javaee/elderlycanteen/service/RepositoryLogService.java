package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.RepositoryLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryLogService {

    private final RepositoryLogDao repositoryLogDao;

    @Autowired
    public RepositoryLogService(RepositoryLogDao repositoryLogDao) {
        this.repositoryLogDao = repositoryLogDao;
    }

    // 在这里添加特定的业务逻辑方法
}
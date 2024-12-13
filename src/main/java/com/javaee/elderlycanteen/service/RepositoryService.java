package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.RepositoryDao;
import com.javaee.elderlycanteen.dto.repository.AllRepoResponseDto;
import com.javaee.elderlycanteen.entity.Repository;
import com.javaee.elderlycanteen.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepositoryService {

    private final RepositoryDao repositoryDao;

    @Autowired
    public RepositoryService(RepositoryDao repositoryDao) {
        this.repositoryDao = repositoryDao;
    }

//    public AllRepoResponseDto searchAllRepos(String name) {
//        // 仓库项目列表
//        List<Repository> repos = new ArrayList<Repository>();
//        if (name == null) {
//            repos = repositoryDao.searchAllRepos();
//        }
//        else{
//            repos = repositoryDao.searchAllReposByName(name);
//        }
//
//        if (repos.isEmpty()) {
//            throw new NotFoundException("No repository found with name: " + name)
//        }
//
//        return repositoryDao.searchAllRepos(name);
//    }

}
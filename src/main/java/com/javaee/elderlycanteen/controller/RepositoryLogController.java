package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.RepositoryLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repositoryLogs")
public class RepositoryLogController {

    private final RepositoryLogService repositoryLogService;

    @Autowired
    public RepositoryLogController(RepositoryLogService repositoryLogService) {
        this.repositoryLogService = repositoryLogService;
    }

    // 在这里添加特定的API端点方法
}
package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.repository.AllRepoResponseDto;
import com.javaee.elderlycanteen.dto.repository.RepoRequestDto;
import com.javaee.elderlycanteen.dto.repository.RepoResponseDto;
import com.javaee.elderlycanteen.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/repo")
public class RepositoryController {

    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    // 在这里添加特定的API端点
    @GetMapping("/search")
    public AllRepoResponseDto getAllIngredients(@RequestParam(value = "name", required = false) String name) {
        return repositoryService.searchAllRepos(name);
    }

    @PutMapping("/update")
    public RepoResponseDto updateRepo(RepoRequestDto dto){
        return repositoryService.updateRepo(dto);
    }

}
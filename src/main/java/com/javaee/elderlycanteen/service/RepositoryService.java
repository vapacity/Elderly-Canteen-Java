package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.IngredientDao;
import com.javaee.elderlycanteen.dao.RepositoryDao;
import com.javaee.elderlycanteen.dto.repository.AllRepoResponseDto;
import com.javaee.elderlycanteen.dto.repository.RepoRequestDto;
import com.javaee.elderlycanteen.dto.repository.RepoResponseDto;
import com.javaee.elderlycanteen.entity.Repository;
import com.javaee.elderlycanteen.exception.NotFoundException;
import com.javaee.elderlycanteen.exception.ServiceException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RepositoryService {

    private final RepositoryDao repositoryDao;
    private final IngredientDao ingredientDao;
    @Autowired
    public RepositoryService(RepositoryDao repositoryDao, IngredientDao ingredientDao) {
        this.repositoryDao = repositoryDao;
        this.ingredientDao = ingredientDao;
    }

    public AllRepoResponseDto searchAllRepos(String name) {
        // 仓库项目列表
        List<Repository> repos = new ArrayList<Repository>();
        if (name == null) {
            repos = repositoryDao.selectAll();
        }
        else{
            repos = repositoryDao.searchAllReposByNameVague(name);
        }

        if (repos.isEmpty()) {
            throw new NotFoundException("No repository found with name: " + name);
        }
        // repo封装为RepoDto
        AllRepoResponseDto response = new AllRepoResponseDto();
        response.setIngredients(new ArrayList<AllRepoResponseDto.RepoDto>());
        for (Repository repo : repos) {
            Integer ingredientId = repo.getIngredientId();
            String ingredientName = ingredientDao.getIngredientById(ingredientId).getIngredientName();

            AllRepoResponseDto.RepoDto repoDto = new AllRepoResponseDto.RepoDto(
                    repo.getRemainAmount(),
                    repo.getExpirationTime(),
                    repo.getHighConsumption(),
                    ingredientId,
                    ingredientName);
            response.getIngredients().add(repoDto);
        }
        // 封装返回数据
        response.setSuccess(true);
        response.setMessage("Successfully get all repositories.");
        return response;
    }

    public RepoResponseDto updateRepo(RepoRequestDto dto){
        Integer ingredientId = dto.getIngredientId();
        String ingredientName = ingredientDao.getIngredientById(ingredientId).getIngredientName();
        Integer remainAmount = dto.getAmount();
        Integer grade = dto.getGrade();
        Date oldExpiry = dto.getOldExpiry();
        Date newExpiry = dto.getNewExpiry();

        if (ingredientId == null || oldExpiry == null){
            throw new ServiceException("IngredientId or oldExpiry is null.");
        }

        Repository existingRepo = repositoryDao.selectByIngredientIdAndExpiry(ingredientId, oldExpiry);
        if (existingRepo == null){
            throw new NotFoundException("No repository found with id: " + ingredientId);
        }
//        // 仓库项目列表
//        Repository repo = repositoryDao.selectById(dto.getId());
//        if (repo == null) {
        return null;
    }
}
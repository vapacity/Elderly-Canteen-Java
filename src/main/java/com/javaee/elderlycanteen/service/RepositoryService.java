package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.FinanceDao;
import com.javaee.elderlycanteen.dao.IngredientDao;
import com.javaee.elderlycanteen.dao.RepositoryDao;
import com.javaee.elderlycanteen.dao.RestockDao;
import com.javaee.elderlycanteen.dto.repository.*;
import com.javaee.elderlycanteen.entity.Finance;
import com.javaee.elderlycanteen.entity.Ingredient;
import com.javaee.elderlycanteen.entity.Repository;
import com.javaee.elderlycanteen.entity.Restock;
import com.javaee.elderlycanteen.exception.NotFoundException;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.utils.DateUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RepositoryService {

    private final RepositoryDao repositoryDao;
    private final IngredientDao ingredientDao;
    private final FinanceDao financeDao;
    private final RestockDao restockDao;

    @Autowired
    public RepositoryService(RepositoryDao repositoryDao, IngredientDao ingredientDao, FinanceDao financeDao, RestockDao restockDao) {
        this.repositoryDao = repositoryDao;
        this.ingredientDao = ingredientDao;
        this.financeDao = financeDao;
        this.restockDao = restockDao;
    }

    public AllRepoResponseDto searchAllRepos(String name) {
        // 仓库项目列表
        List<Repository> repos = new ArrayList<Repository>();
        if (name == null) {
            repos = repositoryDao.selectAll();
        } else {
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

    public RepoResponseDto updateRepo(RepoRequestDto dto) {
        Integer ingredientId = dto.getIngredientId();
        String ingredientName = ingredientDao.getIngredientById(ingredientId).getIngredientName();
        Integer remainAmount = dto.getAmount();
        Integer grade = dto.getGrade();
        Date oldExpiry = dto.getOldExpiry();
        Date newExpiry = dto.getNewExpiry();

        if (ingredientId == null || oldExpiry == null) {
            throw new ServiceException("IngredientId or oldExpiry is null.");
        }

        Repository existingRepo = repositoryDao.selectByIngredientIdAndExpiry(ingredientId, oldExpiry);
        if (existingRepo == null) {
            throw new NotFoundException("No repository found with id: " + ingredientId);
        }
        repositoryDao.deleteByIngredientIdAndExpiry(ingredientId, oldExpiry);
        repositoryDao.insert(new Repository(ingredientId, remainAmount, grade, newExpiry));
        RepoResponseDto response = new RepoResponseDto();
        response.setSuccess(true);
        response.setMessage("Successfully updated repository.");
        return response;

    }

    public RepoResponseDto deleteRepo(Integer ingreId, Date expiry) {
        if (ingreId == null || expiry == null) {
            throw new ServiceException("IngredientId or expiry is null.");
        }
        repositoryDao.deleteByIngredientIdAndExpiry(ingreId, expiry);
        RepoResponseDto response = new RepoResponseDto();
        response.setSuccess(true);
        response.setMessage("Successfully deleted repository.");
        return response;
    }

    public RestockResponseDto restock(RestockRequestDto dto,Integer accountId) throws ParseException {
        // 获得dto信息
        Integer ingredientId = dto.getIngredientId();
        Integer amount = dto.getAmount();
        Date expiry = dto.getExpiry();
        Double price = dto.getPrice();

        Ingredient existingIngrent = ingredientDao.getIngredientById(ingredientId);
        if (existingIngrent == null) {
            throw new NotFoundException("No ingredient found with id: " + ingredientId);
        }
        String ingredientName = existingIngrent.getIngredientName();
        if (amount <= 0 || price <= 0)
            throw new ServiceException("Amount or price is invalid.");

        Date now = DateUtils.getCurrentDate();
        if (expiry.before(now)) {
            throw new ServiceException("Expiry date is before current date.");
        }

        Repository existingRepo = repositoryDao.selectByIngredientIdAndExpiry(ingredientId, expiry);
        if (existingRepo == null) {
            repositoryDao.insert(new Repository(ingredientId, amount, 0, expiry));
        } else {
            repositoryDao.updateRemainAmount(ingredientId, expiry, amount + existingRepo.getRemainAmount());
        }
        Finance finance = new Finance(0, "Restock", now, price,"out",accountId,accountId,null,"待处理");
        financeDao.insertFinance(finance);
        Finance latestFinance = financeDao.getLatestFinance();
        Restock restock =new Restock(latestFinance.getFinanceId(),ingredientId,accountId,amount,price);

        restockDao.insertRestock(restock);
        RestockResponseDto response = new RestockResponseDto();
        response.setSuccess(true);
        response.setMessage("Successfully restocked ingredient.");
        response.setData(new RestockResponseDto.RestockData(
                accountId,amount,expiry,finance.getFinanceId(),
                        0,ingredientId,ingredientName,price,DateUtils.getCurrentDate()));
        return response;
    }
}



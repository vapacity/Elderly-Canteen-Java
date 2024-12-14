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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public RestockResponseDto restock(RestockRequestDto dto, Integer accountId) throws ParseException {
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

        // 插入仓库
        Repository existingRepo = repositoryDao.selectByIngredientIdAndExpiry(ingredientId, expiry);
        if (existingRepo == null) {
            repositoryDao.insert(new Repository(ingredientId, amount, 0, expiry));
        } else {
            repositoryDao.updateRemainAmount(ingredientId, expiry, amount + existingRepo.getRemainAmount());
        }

        // 插入finance
        Finance finance = new Finance(0, "Restock", now, price, "0", accountId, accountId, null, "待处理");
        financeDao.insertFinance(finance);
        Finance latestFinance = financeDao.getLatestFinance();

        // 插入进货表restock
        Restock restock = new Restock(latestFinance.getFinanceId(), ingredientId, accountId, amount, price);
        restockDao.insertRestock(restock);

        // 封装返回数据
        RestockResponseDto response = new RestockResponseDto();
        response.setSuccess(true);
        response.setMessage("Successfully restocked ingredient.");
        response.setData(new RestockResponseDto.RestockData(
                accountId, amount, expiry, finance.getFinanceId(),
                0, ingredientId, ingredientName, price, DateUtils.getCurrentDate()));
        return response;
    }

    public AllRestockResponseDto getAllRestocks() {
        List<Restock> restocks = restockDao.selectAll();
        if (restocks.isEmpty()) {
            throw new NotFoundException("No restock found.");
        }
        AllRestockResponseDto response = new AllRestockResponseDto();
        response.setRestocks(new ArrayList<AllRestockResponseDto.Restocks>());
        for (Restock restock : restocks) {
            Integer financeId = restock.getFinanceId();
            Finance finance = financeDao.getFinanceById(financeId);
            Date restockDate = finance.getFinanceDate();
            // 此处有bug，expiry貌似没有在表中存，先放着，之后再说
            Date expiry = finance.getFinanceDate();
            Integer ingredientId = restock.getIngredientId();
            String ingredientName = ingredientDao.getIngredientById(ingredientId).getIngredientName();
            Double price = restock.getPrice();
            Integer accountId = finance.getAccountId();
            Integer amount = restock.getQuantity();

            AllRestockResponseDto.Restocks restockDto = new AllRestockResponseDto.Restocks(
                    accountId, amount,restockDate, expiry,financeId,ingredientId,ingredientName,price);
            response.getRestocks().add(restockDto);
        }
        response.setSuccess(true);
        response.setMessage("Successfully get all restocks.");
        return response;


    }

    // TODO: 仓库的自动化管理，比如过期的仓库自动清理，库存低于某个值自动补货等
}




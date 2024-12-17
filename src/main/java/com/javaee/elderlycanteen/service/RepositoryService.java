package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.*;

import com.javaee.elderlycanteen.dto.repository.*;
import com.javaee.elderlycanteen.entity.*;
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

import static com.javaee.elderlycanteen.enumeration.FinanceInOrOutEnum.IN;
import static com.javaee.elderlycanteen.enumeration.FinanceInOrOutEnum.OUT;
import static com.javaee.elderlycanteen.enumeration.FinanceStatusEnum.PENDING;


import static com.javaee.elderlycanteen.enumeration.FinanceInOrOutEnum.IN;
import static com.javaee.elderlycanteen.enumeration.FinanceInOrOutEnum.OUT;
import static com.javaee.elderlycanteen.enumeration.FinanceStatusEnum.PENDING;
import static com.javaee.elderlycanteen.enumeration.LogLevelEnum.*;
import static com.javaee.elderlycanteen.utils.DateUtils.getCurrentDate;
import static com.javaee.elderlycanteen.utils.DateUtils.getDayOfWeek;


@Service
public class RepositoryService {

    private final RepositoryDao repositoryDao;
    private final IngredientDao ingredientDao;
    private final FinanceDao financeDao;
    private final RestockDao restockDao;
    private final WeekMenuDao weekMenuDao;
    private final SystemLogsDao systemLogsDao;
    private final DishDao dishDao;
    private final FormulaDao formulaDao;
    @Autowired
    public RepositoryService(RepositoryDao repositoryDao,
                             IngredientDao ingredientDao,
                             FinanceDao financeDao,
                             RestockDao restockDao,
                             WeekMenuDao weekMenuDao,
                             SystemLogsDao systemLogsDao,
                             DishDao dishDao,
                             FormulaDao formulaDao) {
        this.repositoryDao = repositoryDao;
        this.ingredientDao = ingredientDao;
        this.financeDao = financeDao;
        this.restockDao = restockDao;
        this.weekMenuDao = weekMenuDao;
        this.systemLogsDao = systemLogsDao;
        this.dishDao = dishDao;
        this.formulaDao = formulaDao;
    }
    @Transactional
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
    @Transactional
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
    @Transactional
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

        Date now = getCurrentDate();
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
        Finance finance = new Finance(0, "Restock", now, price, OUT.getValue(), accountId, accountId, null, PENDING.getDescription());
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
                0, ingredientId, ingredientName, price, getCurrentDate()));
        return response;
    }
    @Transactional
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
    // TODO: 自动化功能用定时任务实现，比如每天凌晨1点执行一次，目前仅使用手动调用
    @Transactional
    public Boolean removeExpiredRepos() throws ParseException {
        List<Repository> repos = repositoryDao.selectAll();

        for (Repository repo : repos) {
            if (repo.getExpirationTime().before(getCurrentDate())) {
                repositoryDao.deleteByIngredientIdAndExpiry(repo.getIngredientId(), repo.getExpirationTime());
                systemLogsDao.insert(new SystemLogs(0,MEDIUM.getLevel(),"Expired repository removed: " + repo.toString(), getCurrentDate()));
            }
        }
        return true;
    }
    /*
    ** 根据输入的需要份数和菜品id，从仓库中根据先过期先使用原则，从仓库中减少对应数量的原料，并返回实际减少的份数
    ** 返回执行是否成功，减少时写入log
     */

    @Transactional
    public Boolean reduceRepos(Integer dishId, Integer amount) throws ParseException {
        // 1. 获取菜品信息
        Dish dish = dishDao.getDishById(dishId);
        if (dish == null) {
            throw new NotFoundException("No dish found with id: " + dishId);
        }

        // 2. 获取菜品的配方信息
        List<Formula> formulas = formulaDao.getFormulasById(dishId);
        if (formulas.isEmpty()) {
            throw new NotFoundException("No formula found for dish with id: " + dishId);
        }

        // 3. 遍历配方，减少每种食材的库存
        for (Formula formula : formulas) {
            // 计算所需的食材数量
            Integer totalRequiredQuantity = amount * formula.getAmount();

            // 4. 获取对应食材的库存
            List<Repository> repos = repositoryDao.selectByIdOrderByExpiry(formula.getIngredientId());

            // 6. 减少库存
            for (Repository repo : repos) {
                if (repo.getRemainAmount() >= totalRequiredQuantity) {
                    // 库存足够，直接减少
                    Integer oldRemainAmount = repo.getRemainAmount();
                    Integer newRemainAmount = repo.getRemainAmount() - totalRequiredQuantity;
                    repo.setRemainAmount(newRemainAmount);
                    totalRequiredQuantity = 0;

                    systemLogsDao.insert(new SystemLogs(0,LOW.getLevel(),
                            "Ingredient " + formula.getIngredientId() + " reduced from " + oldRemainAmount + " to " + newRemainAmount,
                            getCurrentDate()));
                } else {
                    // 库存不足，减少剩余库存
                    totalRequiredQuantity -= repo.getRemainAmount();
                    repo.setRemainAmount(0);
                    systemLogsDao.insert(new SystemLogs(0,LOW.getLevel(),
                            "Ingredient " + formula.getIngredientId() + " reduced from " + repo.getRemainAmount() + " to 0", getCurrentDate()));
                }

                // 更新食材库存
                repositoryDao.update(repo);

                // 如果所需数量已经满足，跳出循环
                if (totalRequiredQuantity == 0) {
                    break;
                }
            }

            // 如果所有食材库存总和仍然不足以满足需求，抛出异常
            if (totalRequiredQuantity > 0) {
                systemLogsDao.insert(new SystemLogs(0,HIGH.getLevel(),"Not enough stock for ingredient " + formula.getIngredientId(), getCurrentDate()));
                throw new ServiceException("Not enough stock for ingredient " + formula.getIngredientId());
            }
        }

        // 所有库存减少成功，返回 true
        return true;
    }

    @Transactional
    public Integer calculateMaxPortion(Integer dishId){
        Dish dish = dishDao.getDishById(dishId);
        if (dish == null) {
            throw new NotFoundException("No dish found with id: " + dishId);
        }

        List<Formula> formulas = formulaDao.getFormulasById(dishId);
        if (formulas.isEmpty()) {
            throw new NotFoundException("No formula found.");
        }
        int maxPortion = 0;
        for(Formula formula : formulas){
            Ingredient ingredient =  ingredientDao.getIngredientById(formula.getIngredientId());
            Integer remainAmount = repositoryDao.getRemainAmountByIngredientId(ingredient.getIngredientId());

            maxPortion = (int) Math.floor(remainAmount / formula.getAmount());
        }
        return maxPortion;
    }

    @Transactional
    public List<ReduceResponseDto> replenishStock(Date date, Integer requestAmount) throws ParseException {
        List<ReduceResponseDto> reduceResponseDtos = new ArrayList<>();
        List<WeekMenu> weekMenus = weekMenuDao.findWeekMenuByWeek(date, getDayOfWeek(date));

        if (weekMenus.isEmpty()) {
            throw new NotFoundException("No week menu found.");
        }

        // 遍历 WeekMenu 项目
        for (WeekMenu weekMenu : weekMenus) {
            // 计算最大可支持份数
            int maxPortions = calculateMaxPortion(weekMenu.getDishId());

            // 设置销售数量为 0
            weekMenu.setSales(0);

            // 初始化返回的 DTO
            ReduceResponseDto reduceResponseDto = new ReduceResponseDto();
            Dish dish = dishDao.getDishById(weekMenu.getDishId());
            reduceResponseDto.setDishName(dish.getDishName());
            reduceResponseDto.setQuantity(maxPortions);

            // 减少食材库存的成本
            List<ReduceResponseDto.ReduceIngreDto> ingredientCosts = new ArrayList<>();

            // 处理库存不足的情况
            if (maxPortions < requestAmount) {
                weekMenu.setStock(maxPortions);
                systemLogsDao.insert(new SystemLogs(0,MEDIUM.getLevel(), "今日配料库存不足，" + dish.getDishName() + " 的库存为零", getCurrentDate()));
                System.out.println("库存不足，" + weekMenu.getDishId() + " 的库存调整为 " + weekMenu.getStock() + " 份");
            } else {
                weekMenu.setStock(requestAmount);
                systemLogsDao.insert(new SystemLogs(0,LOW.getLevel(), dish.getDishName() + " 的库存已自动设置为" + weekMenu.getStock() + "份", getCurrentDate()));
            }

            // 更新 WeekMenu 中的库存
            weekMenuDao.update(weekMenu);

            // 减少食材库存
            boolean success = reduceRepos(weekMenu.getDishId(), weekMenu.getStock());
            if (!success) {
                throw new ServiceException("库存减少失败，无法支持 " + weekMenu.getStock() + " 份 " + weekMenu.getDishId());
            }

            // 获取食材信息并填充到 DTO 中
            List<Formula> formulas = formulaDao.getFormulasById(weekMenu.getDishId());
            for (Formula formula : formulas) {
                // 获取食材名称、减少的数量和剩余数量
                Ingredient ingredient = ingredientDao.getIngredientById(formula.getIngredientId());
                // Repository repository = repositoryDao.selectById(formula.getIngredientId());
                ReduceResponseDto.ReduceIngreDto reduceIngreDto = new ReduceResponseDto.ReduceIngreDto();
                reduceIngreDto.setIngredientName(ingredient.getIngredientName());
                reduceIngreDto.setIngredientQuantity(formula.getAmount() * weekMenu.getStock());
                //reduceIngreDto.setRemainingQuantity(repository.getRemainAmount());

                ingredientCosts.add(reduceIngreDto);
            }

            // 将该菜品的库存减少信息添加到返回的列表中
            reduceResponseDto.setIngredientCost(ingredientCosts);
            reduceResponseDtos.add(reduceResponseDto);
        }

        // 返回包含所有菜品和食材信息的 DTO 列表
        return reduceResponseDtos;
    }


}








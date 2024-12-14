package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.AccountDao;
import com.javaee.elderlycanteen.dao.FinanceDao;
import com.javaee.elderlycanteen.dao.SeniorDao;
import com.javaee.elderlycanteen.dto.finance.DeductBalanceResponseDto;
import com.javaee.elderlycanteen.dto.finance.FinanceResponseDto;
import com.javaee.elderlycanteen.dto.finance.FinanceTotalsResponseDto;
import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.entity.Finance;
import com.javaee.elderlycanteen.entity.Senior;
import com.javaee.elderlycanteen.exception.NotFoundException;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.javaee.elderlycanteen.utils.DateUtils.getCurrentDate;

@Service
public class FinanceService {

    private final AccountDao accountDao;
    private final FinanceDao financeDao;
    private final SeniorDao seniorDao;

    private final AccountService accountService;
    private final SeniorService seniorService;


    @Autowired
    public FinanceService(AccountDao accountDao, FinanceDao financeDao,SeniorDao seniorDao,AccountService accountService,SeniorService seniorService) {
        this.accountDao = accountDao;
        this.financeDao = financeDao;
        this.seniorDao = seniorDao;

        this.accountService = accountService;
        this.seniorService = seniorService;
    }

    public DeductBalanceResponseDto DeductBalance(Integer accountId,Double totalPrice) throws ParseException {
        // 获取用户信息
        Account account = this.accountDao.getAccountById(accountId);
        if(account == null){
            throw new ServiceException("No matched account found");
        }

        Double bonus = 0.0;

        if(account.getIdentity()== "senior"){
            // 获取老人信息
            Senior senior =this.seniorDao.selectByAccountId(accountId);
            if(senior == null){
                throw new ServiceException("No matched Senior found");
            }

            // 计算补贴
            bonus = account.getMoney()*0.2;
            if(bonus > senior.getSubsidy()) bonus = 0.0;
            totalPrice = totalPrice - bonus;
            this.seniorService.updateSeniorSubsidy(accountId,senior.getSubsidy()-bonus);
        }

        // 检查余额是否足够
        if(account.getMoney()<totalPrice){
            DeductBalanceResponseDto responseDto = new DeductBalanceResponseDto();
            responseDto.msg="insufficient balance!";
            responseDto.success=false;
            return responseDto;
        }

        // 扣除余额
        this.accountService.updateAccountMoney(accountId,account.getMoney()-totalPrice);

        //记录财务信息到Finance表中
        Finance finance = new Finance();

        finance.setAccountId(accountId);
        finance.setFinanceType("Order");
        finance.setFinanceDate(getCurrentDate());
        finance.setPrice(totalPrice);
        finance.setInOrOut("0");
        finance.setStatus("pending");
        finance.setAdministratorId(accountId);


        this.financeDao.insertFinance(finance);

        DeductBalanceResponseDto responseDto = new DeductBalanceResponseDto();
        responseDto.msg="success";
        responseDto.success=true;
        responseDto.bonus= bonus.describeConstable();
        responseDto.financeId=finance.getFinanceId();

        return responseDto;
    }

    public FinanceResponseDto getAllFinanceInfo(String financeType, String inOrOut, String financeDate,
                                                String financeId, String accountId, String status) {
        // 调用 MyBatis 查询
        List<Finance> finances = financeDao.getAllFinanceInfo(financeType, inOrOut, financeDate, financeId, accountId, status);

        // 如果没有找到数据
        if (finances.isEmpty()) {
            return new FinanceResponseDto(null, false,"未找到符合条件的财务信息" );
        }

        // 转换成响应数据格式
        List<FinanceResponseDto.FinanceResponseData> financeResponseDataList = finances.stream()
                .map(finance -> new FinanceResponseDto.FinanceResponseData(
                        finance.getFinanceId(),
                        finance.getFinanceType(),
                        finance.getFinanceDate(),
                        finance.getPrice(),
                        finance.getInOrOut(),
                        finance.getAccountId(),
                        finance.getAdministratorId(),
                        null,
                        finance.getStatus()))
                .collect(Collectors.toList());

        // 返回结果
        return new FinanceResponseDto(financeResponseDataList,true, "财务信息检索成功");
    }

    public FinanceResponseDto auditFinance(Integer id, String status) {
        // 根据 id 查询 Finance 信息
        Finance finance = financeDao.getFinanceById(id);
        if(finance == null){
            throw new NotFoundException("No matched finance found");
        }
        // 更新 Finance 状态
        finance.setStatus(status);
        financeDao.updateFinance(finance);

        // 返回结果
        FinanceResponseDto responseDto = new FinanceResponseDto();
        responseDto.msg="status 更新为{status}";
        responseDto.success=true;
        return responseDto;
    }

    public FinanceTotalsResponseDto getTotal() {
        // 查询所有 Finance 信息
//        List<Finance> finances = financeDao.getAllFinanceInfo(null, null);
        return null;
    }
}
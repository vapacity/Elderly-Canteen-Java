package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.AccountDao;
import com.javaee.elderlycanteen.dao.FinanceDao;
import com.javaee.elderlycanteen.dao.SeniorDao;
import com.javaee.elderlycanteen.dto.finance.DeductBalanceResponseDto;
import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.entity.Finance;
import com.javaee.elderlycanteen.entity.Senior;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

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
}
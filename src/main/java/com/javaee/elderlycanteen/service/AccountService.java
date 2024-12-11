package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.AccountDao;
import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.dto.LoginRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class AccountService {

    private final AccountDao accountDao;

    @Autowired
    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account login(LoginRequestDto loginRequestDto) {
        Account account = accountDao.login(loginRequestDto.getAccountId(), loginRequestDto.getPassword());
        if (account == null) {
            throw new RuntimeException("登录失败，账户或密码错误!");
        }
        return account;
    }

    public Account getAccountById(String accountId) {
        Account account = accountDao.getAccountById(accountId);
        if (account == null) {
            throw new RuntimeException("未找到账户信息！");
        }
        return account;
    }

    public Integer addAccount(Account account) {
        return accountDao.insertAccount(account);
    }
}
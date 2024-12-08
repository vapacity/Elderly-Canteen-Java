package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.AccountMapper;
import com.javaee.elderlycanteen.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountMapper accountDao;

    @Autowired
    public AccountService(AccountMapper accountDao) {
        this.accountDao = accountDao;
    }

    // 在这里添加特定的业务逻辑方法
    public Integer getAccountNumById(Long id){
        return this.accountDao.findUserByAccountId(id);
    }
}
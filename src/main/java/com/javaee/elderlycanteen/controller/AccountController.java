package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.entity.User;
import com.javaee.elderlycanteen.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // 在这里添加特定的API端点方法
    @GetMapping("/id/{id}")
    public Account getAccountById(@PathVariable int id) {
        Account account = new Account();
        account.setAccountId("12");
        account.getAccountId();
        account.setAccountName("Tom's Account");
        return account;
        //        return this.accountService.getAccountById(id);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(this.accountService.getAllAccounts());
    }

    @GetMapping("/user/{userId}")
    public User getUserByAccountId(@PathVariable int userId) {
        User user = new User();
        user.age = 10;
        user.name = "Tom";
        user.email = "tom@gmail.com";
        return user;
    }
}
package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Integer> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(this.accountService.getAccountNumById(id));
    }
}
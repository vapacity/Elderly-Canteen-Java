package com.javaee.elderlycanteen.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.service.AccountService;
import com.javaee.elderlycanteen.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 用户登录接口
     *
     * @param accountId 用户账户 ID
     * @param password  用户密码
     * @return 包含 JWT token 的响应
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String accountId, @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 验证用户身份
            Account account = accountService.login(accountId, password);

            // 创建 JWT payload
            Map<String, String> payload = new HashMap<>();
            payload.put("accountId", account.getAccountId());
            payload.put("accountname", account.getAccountName());

            // 生成 JWT token
            String token = JWTUtils.getToken(payload);

            // 返回成功响应
            response.put("state", true);
            response.put("msg", "登录成功！");
            response.put("token", token);
            return ResponseEntity.ok(response); // 使用ResponseEntity包装响应体并设置状态码为200
        } catch (RuntimeException e) {
            // 返回错误响应
            response.put("state", false);
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 设置状态码为401
        }
    }

    @PostMapping("/test")
    public ResponseEntity<Map<String, Object>> test(@RequestHeader("token") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 验证 token
            DecodedJWT decodedJWT = JWTUtils.verify(token);

            // 获取 token 中的信息
            String accountId = decodedJWT.getClaim("accountId").asString();
            String accountName = decodedJWT.getClaim("accountname").asString();

            // 返回验证成功的信息
            response.put("state", true);
            response.put("msg", "请求成功");
            response.put("accountId", accountId);
            response.put("accountname", accountName);
            return ResponseEntity.ok(response); // 返回成功响应
        } catch (Exception e) {
            // 捕获各种异常并返回错误信息
            response.put("state", false);
            response.put("msg", "Token 无效或已过期！");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 返回错误响应
        }
    }
}

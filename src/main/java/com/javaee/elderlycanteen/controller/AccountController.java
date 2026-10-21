package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.annotation.CheckAccountIdentity;
import com.javaee.elderlycanteen.dto.OTP.GetOTPRequestDto;
import com.javaee.elderlycanteen.dto.OTP.GetOTPResponseDto;
import com.javaee.elderlycanteen.dto.OTP.VerifyOTPRequestDto;
import com.javaee.elderlycanteen.dto.OTP.VerifyOTPResponseDto;
import com.javaee.elderlycanteen.dto.account.AccountDto;
import com.javaee.elderlycanteen.dto.authentication.AuthenticationRequestDto;
import com.javaee.elderlycanteen.dto.authentication.AuthenticationResponseDto;
import com.javaee.elderlycanteen.dto.login.LoginRequestDto;
import com.javaee.elderlycanteen.dto.personInfo.*;
import com.javaee.elderlycanteen.dto.register.RegisterRequestDto;
import com.javaee.elderlycanteen.dto.register.RegisterResponseDto;
import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.entity.TokenInfo;
import com.javaee.elderlycanteen.exception.NotFoundException;
import com.javaee.elderlycanteen.minio.MinioService;
import com.javaee.elderlycanteen.service.AccountService;
import com.javaee.elderlycanteen.service.OtpService;
import com.javaee.elderlycanteen.service.RemoteAvatarService;
import com.javaee.elderlycanteen.utils.JWTUtils;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

import com.javaee.elderlycanteen.dto.login.LoginRequestIdDto;

@RestController
@RequestMapping("accounts")
public class AccountController {

    private final AccountService accountService;
    private final MinioService minioService;
    private final RemoteAvatarService remoteAvatarService;
    private final OtpService otpService;

    @Autowired
    public AccountController(AccountService accountService, MinioService minioService,
                             RemoteAvatarService remoteAvatarService, OtpService otpService) {
        this.accountService = accountService;
        this.minioService = minioService;
        this.remoteAvatarService = remoteAvatarService;
        this.otpService = otpService;
    }


    /**
     * 用户登录接口
     *
     * @param loginRequestDto 登录验证DTO
     * @return 包含 JWT token 的响应
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto loginRequestDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 验证用户身份
            Account account = accountService.login(loginRequestDto);

            // 创建 JWT payload
            Map<String, String> payload = new HashMap<>();
            payload.put("accountId", account.getAccountId().toString());
            payload.put("accountName", account.getAccountName());
            payload.put("identity", account.getIdentity());

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


    @PostMapping("/register")
    public RegisterResponseDto register(@RequestParam(value = "accountName", required = false) String accountName,
                                        @RequestParam(value = "password", required = false) String password,
                                        @RequestParam(value = "phoneNum", required = false) String phoneNum,
                                        @RequestParam(value = "gender", required = false) String gender,
                                        @RequestParam(value = "birthDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthDate,
                                        @Parameter(name = "上传的图片",required = true)
                                        @RequestParam(value = "avatar",required = false) MultipartFile avatar) throws Exception {
        //把图片转换成url路径并上传到桶
        String fileName = avatar.getOriginalFilename();
        minioService.uploadFile(fileName,avatar);
        //把信息封装到RegisterRequestDto
        RegisterRequestDto registerRequest = new RegisterRequestDto();
        registerRequest.setUserName(accountName);
        registerRequest.setPhone(phoneNum);
        registerRequest.setPassword(password);
        registerRequest.setGender(gender);
        registerRequest.setBirthDate(birthDate);
        //传入Service层
        return accountService.register(registerRequest ,  fileName);

    }

    @GetMapping("/getPersonInfo")
    public PersonInfoResponseDto getPersonInfo(@RequestHeader(name = "Authorization", required = false) String token) {
        // 获取accountId
        if (token == null) {
            throw new NotFoundException("token is null");
        }
        token = token.substring(7);
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
        Integer accountId = tokenInfo.getAccountId();

        //返回查询结果，是一个PersonInfoResponseDto对象
        return accountService.getPersonInfo(accountId);
    }

    @PostMapping("/alterPersonInfo")
    public PersonInfoResponseDto alterPersonInfo(@RequestHeader(name = "Authorization", required = false) String token,
                                                 @Parameter(name = "上传的图片", required = false)
                                                 @RequestParam("avatar") MultipartFile avatar,
                                                 @RequestParam(value = "accountName", required = false) String accountName,
                                                 @RequestParam(value = "phoneNum", required = false) String phoneNum,
                                                 @RequestParam(value = "gender", required = false) String gender,
                                                 @RequestParam(value = "birthDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthDate,
                                                 @RequestParam(value = "address", required = false) String address,
                                                 @RequestParam(value = "name", required = false) String name
    ) throws Exception {

        // 获取accountId
        if (token == null) {
            throw new NotFoundException("token is null");
        }
        token = token.substring(7);
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
        Integer accountId = tokenInfo.getAccountId();
        //把图片转换成url路径并上传到桶
        String fileName = avatar.getOriginalFilename();
        minioService.uploadFile(fileName, avatar);
        //把用户信息封装成PersonInfoRequestDto对象
        PersonInfoRequestDto personInfo = new PersonInfoRequestDto(accountName, phoneNum, gender, birthDate, address, name);
        return accountService.alterPersonInfo(personInfo, accountId, fileName);
    }

    @GetMapping("/getAllAccount")
    public List<AccountDto> getAllAccount() {

        return accountService.getAllAccount();
    }

    @PostMapping("/nameAuthenticate")
    public AuthenticationResponseDto nameAuthenticate(@RequestHeader(name = "Authorization", required = false) String token,
                                                      AuthenticationRequestDto input) {
        //获取accountId
        if (token == null) {
            throw new NotFoundException("token is null");
        }
        token = token.substring(7);
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
        Integer accountId = tokenInfo.getAccountId();

        return accountService.nameAuthenticate(accountId, input);
    }

    //修改密码
    @PostMapping("/changePassword")
    public ResponseEntity<Void> changePassword(@RequestHeader(name = "Authorization", required = false) String token, String password) {
        // 获取accountId
        if (token == null) {
            throw new NotFoundException("token is null");
        }
        token = token.substring(7);
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
        Integer accountId = tokenInfo.getAccountId();
        return accountService.changePassword(accountId, password);
    }

    //更改密码
    @PostMapping("/alterPassword")
    public ResponseEntity<Void> alterPassword(@RequestHeader(name = "Authorization", required = false) String token, PasswordRequestDto request) {
        // 获取accountId
        if (token == null) {
            throw new NotFoundException("token is null");
        }
        token = token.substring(7);
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
        Integer accountId = tokenInfo.getAccountId();
        boolean isOldPassword = accountService.VerifyPassword(request.getOldPassword(), accountId);
        if (!isOldPassword) {
            throw new RuntimeException("原密码不正确");
        }

        return accountService.changePassword(accountId, request.getNewPassword());
    }

    //修改手机号
    @PostMapping("/changePhone")
    public PhoneResponseDto changePhone(@RequestHeader(name = "Authorization", required = false) String token, PhoneRequestDto request) {
        if (token == null) {
            throw new NotFoundException("token is null");
        }
        token = token.substring(7);
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
        Integer accountId = tokenInfo.getAccountId();
        return accountService.changePhone(accountId, request.getNewPhoneNum());
    }

    //用户注销
    @DeleteMapping("/deleteUser")
    public ResponseEntity<Void> deleteUser(@RequestHeader(name = "Authorization", required = false) String token) {
        if (token == null) {
            throw new NotFoundException("token is null");
        }
        token = token.substring(7);
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
        Integer accountId = tokenInfo.getAccountId();

        return accountService.deleteUser(accountId);
    }

    @PostMapping("/prePaid")
    public ResponseEntity<Void> prepaid(@RequestHeader(name = "Authorization", required = false) String token, Double money) {
        if (token == null) {
            throw new NotFoundException("token is null");
        }
        token = token.substring(7);
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
        Integer accountId = tokenInfo.getAccountId();

        return accountService.creditAccount(accountId, money);
    }

    /**
     * SEC-LAB-B1：公开 JWT 验签公钥，便于客户端/合作方校验 token。
     */
    @GetMapping("/publicKey")
    public ResponseEntity<Map<String, String>> publicKey() {
        return ResponseEntity.ok(Map.of("publicKey", JWTUtils.getPublicKeyPem()));
    }

    /**
     * SEC-LAB-A2：从远程 URL 导入头像。
     */
    @PostMapping("/avatar/import")
    public ResponseEntity<Map<String, String>> importAvatar(
            @RequestHeader(name = "Authorization", required = false) String token,
            @RequestBody Map<String, String> body) throws Exception {
        if (token == null) {
            throw new NotFoundException("token is null");
        }
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token.substring(7));
        String fileUrl = remoteAvatarService.importAvatar(body.get("url"), tokenInfo.getAccountId());
        return ResponseEntity.ok(Map.of("url", fileUrl));
    }

    /**
     * SEC-LAB-B2：发送手机验证码。
     */
    @PostMapping("/otp/send")
    public GetOTPResponseDto sendOtp(@RequestBody GetOTPRequestDto request) {
        boolean sent = otpService.send(request.getPhoneNum());
        GetOTPResponseDto response = new GetOTPResponseDto();
        response.setSuccess(sent);
        response.setMsg(sent ? "OTP sent" : "phone number not found");
        return response;
    }

    /**
     * SEC-LAB-B2：校验验证码并重置密码。
     */
    @PostMapping("/otp/verify")
    public VerifyOTPResponseDto<Void> verifyOtp(@RequestBody VerifyOTPRequestDto request) {
        boolean verified = otpService.verify(
                request.getPhoneNum(),
                request.getCode(),
                request.getNewPhoneNum(),
                request.getNewPassword());
        VerifyOTPResponseDto<Void> response = new VerifyOTPResponseDto<>();
        response.setSuccess(verified);
        response.setMsg(verified ? "password reset successfully" : "invalid or expired code");
        return response;
    }
}

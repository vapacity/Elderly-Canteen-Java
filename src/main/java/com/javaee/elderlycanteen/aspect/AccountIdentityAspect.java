package com.javaee.elderlycanteen.aspect;

import com.javaee.elderlycanteen.annotation.CheckAccountIdentity;
import com.javaee.elderlycanteen.entity.TokenInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import com.javaee.elderlycanteen.utils.JWTUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Aspect
@Component
public class AccountIdentityAspect {
    @Autowired
    private HttpServletRequest request;

    // 修正Pointcut：不带参数的方法
    @Pointcut("@annotation(checkAccountIdentity)")
    public void checkAccountIdentityPointcut(CheckAccountIdentity checkAccountIdentity) {
        // Pointcut定义为空方法
    }

    // 在目标方法执行之前进行拦截
    @Before("checkAccountIdentityPointcut(checkAccountIdentity)")
    public void checkAccountIdentityBefore(JoinPoint joinPoint, CheckAccountIdentity checkAccountIdentity) throws IllegalAccessException {
        String token = request.getHeader("Authorization");  // 从请求头获取Token

        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalAccessException("Token is missing or invalid");
        }

        token = token.substring(7);  // 去掉 Bearer 前缀
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);

        String identity = tokenInfo.getIdentity();  // 从 token 中获取 account identity

        // 解析 token 获取 account identity
        Optional<String> accountIdentity = Optional.ofNullable(identity);

        if (!accountIdentity.isPresent()) {
            throw new IllegalAccessException("Token is invalid or does not contain account identity");
        }

        // 检查 token 中的 account identity 是否与注解中的 value 匹配
        if (!accountIdentity.get().equals(checkAccountIdentity.identity())) {
            throw new IllegalAccessException("Account identity mismatch");
        }
    }
}

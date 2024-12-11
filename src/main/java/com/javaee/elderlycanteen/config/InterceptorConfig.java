package com.javaee.elderlycanteen.config;

import com.javaee.elderlycanteen.utils.JWTInterceptors;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptors())
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns("/accounts/login")  // 排除登录接口
                .excludePathPatterns("/swagger-ui.html")  // 排除 Swagger UI 首页
                .excludePathPatterns("/swagger-ui/**")  // 排除 Swagger UI 路径
                .excludePathPatterns("/swagger-resources/**")  // 排除 Swagger 资源
                .excludePathPatterns("/v3/api-docs/**")  // 排除 Swagger OpenAPI 3 API 文档
                .excludePathPatterns("/webjars/**")  // 排除 Swagger JS、CSS 文件
                .excludePathPatterns("/favicon.ico");  // 排除 favicon 请求
    }

}

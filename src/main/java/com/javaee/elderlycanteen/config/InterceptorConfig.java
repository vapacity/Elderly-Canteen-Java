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
                .addPathPatterns("/**")  // 其他接口token验证
                .excludePathPatterns("/accounts/login");  // 所有用户都放行
    }
}

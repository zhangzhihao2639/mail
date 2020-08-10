package com.imooc.mall.config;

import com.imooc.mall.interceptor.UserLoginInceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error","/user/login","/user/register","/products","/category","/products/*");
    }
}

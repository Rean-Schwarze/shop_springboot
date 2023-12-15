package com.rean.shopspring.config;

import com.rean.shopspring.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录接口和注册接口不拦截
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login","/user/register","/upload",
                "/home/category/head","/home/new","/home/banner","/home/category","/goods","/category/sub/filter",
                "/category/goods/temporary");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")   //所有方法都做处理跨域
                .allowedOrigins("http://localhost:5173")  //允许跨域的请求头
                .allowedMethods("*")  //允许通过的请求方法
                .allowedHeaders("*");  //允许的请求头
    }

}

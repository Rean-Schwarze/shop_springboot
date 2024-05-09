package com.rean.shopspring.config;

import com.rean.shopspring.interceptors.AdminInterceptor;
import com.rean.shopspring.interceptors.LoginInterceptor;
import com.rean.shopspring.interceptors.SellerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private SellerInterceptor sellerInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录接口和注册接口不拦截
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login","/user/register","/upload",
                "/home/category/head","/home/new","/home/banner","/home/category","/home/goods","/goods","/category/sub/filter",
                "/category/goods/temporary","/pay/**","/paycallback","/seller/login","/admin/login");
        registry.addInterceptor(sellerInterceptor).addPathPatterns("/seller/**","/upload/seller/**").excludePathPatterns("/seller/login");
        registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/**").excludePathPatterns("/admin/login");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")   //所有方法都做处理跨域
                .allowedOriginPatterns("*")  //允许跨域的请求头
                .allowedMethods("*")  //允许通过的请求方法
                .allowCredentials(true)// 允许cookie等凭证
                .allowedHeaders("*");  //允许的请求头
    }

}

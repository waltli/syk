package com.sbolo.syk.fetch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sbolo.syk.common.tools.ConfigUtils;
import com.sbolo.syk.fetch.interceptor.LoginInterceptor;

@Configuration
public class FetchWebMvcConfig implements WebMvcConfigurer {

	/**
     * 自己定义的拦截器类
     * @return
     */
    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**").excludePathPatterns("/test111", "/test222", "/login-work", "/login", "/assets/**", "/temp/**", "/error");
    }
	
}

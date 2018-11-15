package com.sbolo.syk.view.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.view.interceptor.LoginInterceptor;

@Configuration
public class ViewWebMvcConfig implements WebMvcConfigurer {

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
//        registry.addInterceptor(loginInterceptor()).addPathPatterns("/msg/**").excludePathPatterns("/msg/gets");
    }
	
}

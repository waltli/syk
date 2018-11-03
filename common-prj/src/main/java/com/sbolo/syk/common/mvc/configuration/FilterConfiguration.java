package com.sbolo.syk.common.mvc.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sbolo.syk.common.mvc.filter.XSSFilter;

@Configuration
public class FilterConfiguration {
    @Bean
    public FilterRegistrationBean<XSSFilter> filterRegist() {
        FilterRegistrationBean<XSSFilter> frBean = new FilterRegistrationBean<XSSFilter>();
        frBean.setFilter(new XSSFilter());
        frBean.addUrlPatterns("/*");
        return frBean;
    }
}

package com.sbolo.syk.common.mvc.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sbolo.syk.common.mvc.filter.XSSFilter;
import com.sbolo.syk.common.tools.ConfigUtils;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//配置静态资源路由
		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/webapp/assets/");
		//配置绝对路径映射
		String fsMapping = ConfigUtils.getPropertyValue("fs.mapping");
		String fsDir = ConfigUtils.getPropertyValue("fs.dir");
		registry.addResourceHandler(fsMapping+"/**").addResourceLocations("file:"+fsDir+"/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}
	
//	@Bean
//    public FilterRegistrationBean<XSSFilter> filterRegist() {
//        FilterRegistrationBean<XSSFilter> frBean = new FilterRegistrationBean<XSSFilter>();
//        frBean.setFilter(new XSSFilter());
//        frBean.addUrlPatterns("/*");
//        return frBean;
//    }
	
}

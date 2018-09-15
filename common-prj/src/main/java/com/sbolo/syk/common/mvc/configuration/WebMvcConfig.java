package com.sbolo.syk.common.mvc.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sbolo.syk.common.tools.ConfigUtil;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//配置静态资源路由
		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/webapp/assets/");
		//配置绝对路径映射
		String fsMapping = ConfigUtil.getPropertyValue("fs.mapping");
		String fsDir = ConfigUtil.getPropertyValue("fs.dir");
		registry.addResourceHandler(fsMapping+"/**").addResourceLocations("file:"+fsDir+"/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}
	
}

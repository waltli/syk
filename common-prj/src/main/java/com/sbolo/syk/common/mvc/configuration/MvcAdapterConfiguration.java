package com.sbolo.syk.common.mvc.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.sbolo.syk.common.tools.ConfigUtil;

@Configuration
public class MvcAdapterConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//配置静态资源路由
		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/webapp/assets/");
		super.addResourceHandlers(registry);
	}
	
	/**
	 * 配置首页的controller
	 */
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }

	/*拦截器配置*/
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//注册自定义拦截器，添加拦截路径和排除拦截路径    
//        registry.addInterceptor(new LoginInfoInterceptor()).addPathPatterns("/**").excludePathPatterns("/login");
		super.addInterceptors(registry);
	}
	
	
	/*过滤器配置*/
//	@Bean
//	public FilterRegistrationBean registration(SsoAuthenticationFilter filter) {
//		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
//		registration.setEnabled(false);
//		return registration;
//	}
	
//	@Bean
//    public FilterRegistrationBean myFilterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new SessionFilter());
//        registration.addUrlPatterns("/biz/*");
//        registration.setOrder(Integer.MAX_VALUE);
//        return registration;
//    }
	
}

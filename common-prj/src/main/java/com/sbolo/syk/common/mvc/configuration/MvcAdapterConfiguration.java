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
	String htmlMapping = ConfigUtil.getPropertyValue("velocity.htmlmapping");
	String tempHtmlMapping = ConfigUtil.getPropertyValue("velocity.tempHtmlmapping");
	String htmlPath = ConfigUtil.getPropertyValue("velocity.htmlpath");
	String tempPath = ConfigUtil.getPropertyValue("velocity.tempHtmlpath");

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(ConfigUtil.getPropertyValue("fs.mapping") + "/**")
				.addResourceLocations("file:" + ConfigUtil.getPropertyValue("fs.dir") + "/");
		if (StringUtils.isNotBlank(htmlMapping) && StringUtils.isNotBlank(tempHtmlMapping) && StringUtils.isNotBlank(htmlPath)) {
			registry.addResourceHandler(htmlMapping + "/**").addResourceLocations("file:" + htmlPath + "/");
			registry.addResourceHandler(tempHtmlMapping + "/**").addResourceLocations("file:" + tempPath + "/");
		}
		super.addResourceHandlers(registry);
	}
	
	/**
	 * 配置首页的controller
	 */
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
		String homePageUri = "/";
		String testServerUri = "/"+ConfigUtil.getPropertyValue("testService.name");
		String homePageName = ConfigUtil.getPropertyValue("homePage.viewName");
		String testServerName = ConfigUtil.getPropertyValue("testService.viewName");
		registry.addViewController(homePageUri).setViewName("redirect:"+htmlMapping+"/"+homePageName);
		registry.addViewController(testServerUri).setViewName("redirect:"+htmlMapping+"/"+testServerName);
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE );
        super.addViewControllers(registry);
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

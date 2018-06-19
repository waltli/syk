package com.sbolo.syk.common.mvc.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {
	@Bean
	public Docket webApi() {
		return new Docket(DocumentationType.SWAGGER_2)
		        .groupName("接口文档")
		        .apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.sbolo.syk.common"))
				.paths(PathSelectors.any())
//				.apis(RequestHandlerSelectors.any())
//				.paths(Predicates.or(PathSelectors.regex("/biz/*")))
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("系统")
				.description("服务为：FE为  http://127.0.0.1:32601/fe-prj/swagger-ui.html; MA为：http://127.0.0.1:32602/ma-prj/swagger-ui.html")
				.termsOfServiceUrl("")
				.version("1.0").build();
	}

}
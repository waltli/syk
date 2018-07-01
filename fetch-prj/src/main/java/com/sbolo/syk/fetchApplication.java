package com.sbolo.syk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
@EnableSwagger2
@MapperScan(basePackages = "com.sbolo.syk.*.mapper")
public class fetchApplication extends SpringBootServletInitializer {
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(fetchApplication.class);
    }
	
	public static void main(String[] args) {
        SpringApplication.run(fetchApplication.class,args);
    }
}

package com.sbolo.syk.common.mvc.configuration;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.sbolo.syk.common.mvc.configuration.properties.DruidDataSourceProperties;

@Configuration
@EnableConfigurationProperties(DruidDataSourceProperties.class)
public class DruidDataSourceConfiguration {
	
	@Autowired
	private DruidDataSourceProperties properties;
	
	@Bean
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource  
    public DataSource dataSource() throws SQLException{  
        DruidDataSource datasource = new DruidDataSource();  
          
        datasource.setUrl(properties.getUrl());  
        datasource.setUsername(properties.getUsername());  
        datasource.setPassword(properties.getPassword());  
        datasource.setDriverClassName(properties.getDriverClassName());  
          
        //configuration  
        datasource.setInitialSize(properties.getInitialSize());  
        datasource.setMinIdle(properties.getMinIdle());  
        datasource.setMaxActive(properties.getMaxActive());  
        datasource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());  
        datasource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());  
        datasource.setValidationQuery(properties.getValidationQuery());  
        datasource.setTestWhileIdle(properties.getTestWhileIdle());  
        datasource.setTestOnBorrow(properties.getTestOnBorrow());  
        datasource.setTestOnReturn(properties.getTestOnReturn());  
        datasource.setPoolPreparedStatements(properties.getPoolPreparedStatements());  
        datasource.setMaxPoolPreparedStatementPerConnectionSize(properties.getMaxPoolPreparedStatementPerConnectionSize());  
        datasource.setFilters(properties.getFilters());  
        datasource.init();
        return datasource;  
    }  
}

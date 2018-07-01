package com.sbolo.syk.fetch.config;  
  
import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.SqlServerDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.source.ClassPathSourceFactory;
import com.sbolo.syk.common.mvc.configuration.properties.DruidDataSourceProperties;
@Configuration
@EnableConfigurationProperties(DruidDataSourceProperties.class)
public class ActiveRecordPluginConfig {
	@Autowired
	private DruidDataSourceProperties druidDataSourceProperties;
    @Bean
    public DataSource getDataSource() {  
  
        DruidPlugin druidPlugin = new DruidPlugin(druidDataSourceProperties.getUrl(), druidDataSourceProperties.getUsername(),
        		druidDataSourceProperties.getPassword(),druidDataSourceProperties.getDriverClassName());  
       
        druidPlugin.set(druidDataSourceProperties.getInitialSize(), druidDataSourceProperties.getMinIdle(), druidDataSourceProperties.getMaxActive());
        // 加强数据库安全  
//        WallFilter wallFilter = new WallFilter();  
//        wallFilter.setDbType("mysql");  
//        druidPlugin.addFilter(wallFilter);  
        
        // 添加 StatFilter 才会有统计数据  
        // druidPlugin.addFilter(new StatFilter());  
        // 必须手动调用start  
        
        druidPlugin.start();  
  
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);  
        arp.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);  
        arp.setShowSql(true);  
//        arp.setDialect(new SqlServerDialect());
        arp.getEngine().setSourceFactory(new ClassPathSourceFactory());  
        
        //arp.addSqlTemplate("/sql/all_sqls.sql");  
        
        // 必须手动调用start  
        arp.start();  
        return druidPlugin.getDataSource();  
    }  
  
}  
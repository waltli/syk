//package com.sbolo.syk.common.mvc.configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//
//import javax.sql.DataSource;
//
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.chrhc.mybatis.locker.interceptor.OptimisticLocker;
//import com.github.pagehelper.PageInterceptor;
//
//@Configuration
//public class MybatisConfiguration {
//	
//	@Autowired
//	private MybatisProperties properties;
//	
//	@Bean
//    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception{
//    	SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//    	sqlSessionFactoryBean.setDataSource(dataSource);
//    	sqlSessionFactoryBean.setTypeAliasesPackage(properties.getTypeAliasesPackage());
//    	sqlSessionFactoryBean.setMapperLocations(properties.resolveMapperLocations());
//    	
//    	sqlSessionFactoryBean.setPlugins(pageInterceptor());
//    	return sqlSessionFactoryBean.getObject();
//    }
//    
//    private Interceptor[] pageInterceptor(){
//    	List<Interceptor> interceptors = new ArrayList<>();
//    	
//    	PageInterceptor pageInterceptor = new PageInterceptor();
//    	Properties properties = new Properties();
//    	properties.setProperty("reasonable", "true");
//    	pageInterceptor.setProperties(properties);
//    	interceptors.add(pageInterceptor);
//    	
//    	OptimisticLocker locker = new OptimisticLocker();
//    	Properties properties2 = new Properties();
//    	properties2.setProperty("versionField", "upVer");
//    	properties2.setProperty("versionColumn", "up_ver");
//    	locker.setProperties(properties2);
//    	interceptors.add(locker);
//    	
//    	
//    	return interceptors.toArray(new Interceptor[]{});
//    }
//}

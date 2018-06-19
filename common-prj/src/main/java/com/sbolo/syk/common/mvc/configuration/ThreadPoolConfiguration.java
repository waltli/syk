package com.sbolo.syk.common.mvc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.sbolo.syk.common.mvc.configuration.properties.ThreadPoolProperties;

@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolConfiguration {
	
	@Autowired
	private ThreadPoolProperties threadPoolProperties;
	
	
	@Bean(name="threadPool")
	public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(threadPoolProperties.getCorePoolSize());//核心线程池数
		pool.setMaxPoolSize(threadPoolProperties.getMaxPoolSize()); // 最大线程
		pool.setQueueCapacity(threadPoolProperties.getQueueCapacity());//队列容量
		pool.setAllowCoreThreadTimeOut(threadPoolProperties.getAllowCoreThreadTimeOut());
		pool.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy()); //队列满，线程被拒绝执行策略
		return pool;
	}
}

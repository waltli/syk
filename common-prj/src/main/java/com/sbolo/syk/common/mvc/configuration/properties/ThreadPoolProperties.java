package com.sbolo.syk.common.mvc.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.threadpool")
public class ThreadPoolProperties {
    private Integer corePoolSize;
	private Integer maxPoolSize;
	private Integer queueCapacity;
	private Boolean allowCoreThreadTimeOut;
	public Integer getCorePoolSize() {
		return corePoolSize;
	}
	public void setCorePoolSize(Integer corePoolSize) {
		this.corePoolSize = corePoolSize;
	}
	public Integer getMaxPoolSize() {
		return maxPoolSize;
	}
	public void setMaxPoolSize(Integer maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	public Integer getQueueCapacity() {
		return queueCapacity;
	}
	public void setQueueCapacity(Integer queueCapacity) {
		this.queueCapacity = queueCapacity;
	}
	public Boolean getAllowCoreThreadTimeOut() {
		return allowCoreThreadTimeOut;
	}
	public void setAllowCoreThreadTimeOut(Boolean allowCoreThreadTimeOut) {
		this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
	}
	
	
}

package com.sbolo.syk.fetch.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spider")
public class SpiderProperties {
	private  StartUrlProperties startUrl;

	public StartUrlProperties getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(StartUrlProperties startUrl) {
		this.startUrl = startUrl;
	}
	
	
}

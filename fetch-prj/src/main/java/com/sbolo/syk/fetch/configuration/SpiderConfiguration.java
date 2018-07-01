package com.sbolo.syk.fetch.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sbolo.syk.common.mvc.configuration.properties.DruidDataSourceProperties;
import com.sbolo.syk.fetch.configuration.properties.SpiderProperties;
import com.sbolo.syk.fetch.pipeline.FilterPipeline;
import com.sbolo.syk.fetch.processor.SixVHaoProcessor;
import com.sbolo.syk.fetch.spider.Downloader;
import com.sbolo.syk.fetch.spider.PageProcessor;
import com.sbolo.syk.fetch.spider.Pipeline;
import com.sbolo.syk.fetch.spider.Spider;

@Configuration
@EnableConfigurationProperties(SpiderProperties.class)
public class SpiderConfiguration {

	@Autowired
	private SpiderProperties properties;
	
	@Bean
	public PageProcessor sixVHaoProcessor() {
		SixVHaoProcessor sixVHaoProcessor = new SixVHaoProcessor();
		sixVHaoProcessor.setStartUrl(properties.getStartUrl().getSixVHao());
		return sixVHaoProcessor;
	}
	
	@Bean
	public Pipeline filterPipeline() {
		FilterPipeline filterPipeline = new FilterPipeline();
		return filterPipeline;
	}
	
	@Bean
	public Spider spider() {
		List<PageProcessor> listProcessor = new ArrayList<>();
		listProcessor.add(sixVHaoProcessor());
		Spider spider = new Spider(listProcessor, filterPipeline());
		return spider;
	}
	
	
}

package com.sbolo.syk.fetch.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.fetch.pipeline.MyPipeline;
import com.sbolo.syk.fetch.processor.SixVHaoProcessor;
import com.sbolo.syk.fetch.spider.Downloader;
import com.sbolo.syk.fetch.spider.PageProcessor;
import com.sbolo.syk.fetch.spider.Pipeline;
import com.sbolo.syk.fetch.spider.Spider;

@Configuration
public class SpiderConfiguration {
	
	@Bean
	public PageProcessor sixVHaoProcessor() {
		SixVHaoProcessor sixVHaoProcessor = new SixVHaoProcessor();
		sixVHaoProcessor.setStartUrl(ConfigUtil.getPropertyValue("spider.url.sixVHao"));
		return sixVHaoProcessor;
	}
	
	@Bean
	public Pipeline myPipeline() {
		MyPipeline myPipeline = new MyPipeline();
		return myPipeline;
	}
	
	@Bean
	public Downloader downloader() {
		return new Downloader();
	}
	
	@Bean
	public Spider spider() {
		List<PageProcessor> listProcessor = new ArrayList<>();
		listProcessor.add(sixVHaoProcessor());
		Spider spider = new Spider(listProcessor, myPipeline(), downloader());
		return spider;
	}
	
	
}

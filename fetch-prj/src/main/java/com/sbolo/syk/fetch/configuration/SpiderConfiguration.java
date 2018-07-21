package com.sbolo.syk.fetch.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	public Spider spider() {
		List<PageProcessor> listProcessor = new ArrayList<>();
		SixVHaoProcessor sixVHaoProcessor = new SixVHaoProcessor();
		sixVHaoProcessor.setStartUrl(ConfigUtil.getPropertyValue("spider.url.sixVHao"));
		listProcessor.add(sixVHaoProcessor);
		MyPipeline pipeline = new MyPipeline();
		Downloader downloader = new Downloader();
		Spider spider = new Spider(listProcessor, pipeline, downloader);
		return spider;
	}
	
	
}

package com.sbolo.syk.fetch.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.sbolo.syk.common.tools.ConfigUtils;
import com.sbolo.syk.fetch.distinct.MyDistinct;
import com.sbolo.syk.fetch.pipeline.FilePipeline;
import com.sbolo.syk.fetch.pipeline.DBPipeline;
import com.sbolo.syk.fetch.processor.MeiJuTTProcessor;
import com.sbolo.syk.fetch.processor.SixVHaoProcessor;
import com.sbolo.syk.fetch.spider.Distinct;
import com.sbolo.syk.fetch.spider.Downloader;
import com.sbolo.syk.fetch.spider.PageProcessor;
import com.sbolo.syk.fetch.spider.Pipeline;
import com.sbolo.syk.fetch.spider.Spider;

@Configuration
public class SpiderConfiguration {
	
	@Bean
	public PageProcessor sixVHaoProcessor() {
		SixVHaoProcessor sixVHaoProcessor = new SixVHaoProcessor();
		sixVHaoProcessor.setStartUrl(ConfigUtils.getPropertyValue("spider.url.sixVHao"));
		return sixVHaoProcessor;
	}
	
	@Bean
	public PageProcessor meiJuTTProcessor() {
		MeiJuTTProcessor meiJuTTProcessor = new MeiJuTTProcessor();
		meiJuTTProcessor.setStartUrl(ConfigUtils.getPropertyValue("spider.url.meijutt"));
		return meiJuTTProcessor;
	}
	
	@Bean
	public Distinct myDistinct() {
		MyDistinct myDistinct = new MyDistinct();
		return myDistinct;
	}
	
	@Bean
	public Pipeline filePipeline() {
		FilePipeline filePipeline = new FilePipeline();
		return filePipeline;
	}
	
	@Bean
	public Pipeline dbPipeline() {
		DBPipeline dbPipeline = new DBPipeline();
		return dbPipeline;
	}
	
	@Bean
	public Downloader downloader() {
		return new Downloader();
	}
	
	@Bean
	public Spider spider() {
		List<PageProcessor> listProcessor = new ArrayList<>();
		listProcessor.add(sixVHaoProcessor());
		listProcessor.add(meiJuTTProcessor());
		List<Pipeline> listPipeline = new ArrayList<>();
		listPipeline.add(filePipeline());
		listPipeline.add(dbPipeline());
		Spider spider = new Spider(listProcessor, myDistinct(), listPipeline, downloader());
		return spider;
	}
	
	
}

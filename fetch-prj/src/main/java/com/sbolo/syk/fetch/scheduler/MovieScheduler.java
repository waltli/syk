package com.sbolo.syk.fetch.scheduler;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sbolo.syk.fetch.spider.Spider;

public class MovieScheduler {
	private static final Logger log = LoggerFactory.getLogger(MovieScheduler.class);
	
	@Resource
	private Spider spider;
	
	public void goSpider(){
		try {
			spider.run();
		} catch (Exception e) {
			log.error("",e);
		}
	}
}

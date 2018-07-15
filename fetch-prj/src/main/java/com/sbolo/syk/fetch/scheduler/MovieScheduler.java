package com.sbolo.syk.fetch.scheduler;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sbolo.syk.fetch.spider.Spider;

@Component
public class MovieScheduler {
	private static final Logger log = LoggerFactory.getLogger(MovieScheduler.class);
	
	@Resource
	private Spider spider;
	
//	@Scheduled(cron="0/5 * * * * ? ")
	public void goSpider(){
//		try {
//			spider.run();
//		} catch (Exception e) {
//			log.error("",e);
//		}
	}
}

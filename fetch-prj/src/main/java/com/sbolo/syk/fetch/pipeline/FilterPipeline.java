package com.sbolo.syk.fetch.pipeline;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.sbolo.syk.fetch.dao.MovieResourceEntityMapper;
import com.sbolo.syk.fetch.service.MovieInfoBizService;
import com.sbolo.syk.fetch.service.ResourceInfoBizService;
import com.sbolo.syk.fetch.spider.Pipeline;

@Component
public class FilterPipeline implements Pipeline {
	
	private static final Logger log = LoggerFactory.getLogger(FilterPipeline.class);
	
	@Override
	public void process(Map<String, Object> fields) throws Exception {
//		log.info("新增movieInfo条数："+adMovies.size());
//		log.info("修改movieInfo条数："+mdMovies.size());
//		log.info("labels条数："+adLabels.size());
//		log.info("locations条数："+adLocations.size());
//		log.info("新增resourceInfo条数："+adResources.size());
//		log.info("修改resourceInfo条数："+mdResources.size());
//		log.info("bachAdd completion");
	}
	
	@Override
	public void after() {
	}
	
	

}

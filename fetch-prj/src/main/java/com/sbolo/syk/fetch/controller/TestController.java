package com.sbolo.syk.fetch.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.service.ResourceInfoService;
import com.sbolo.syk.fetch.spider.Spider;


@Controller
public class TestController {
	
	private static final Logger log = LoggerFactory.getLogger(TestController.class);
	
	@Resource
	private Spider spider;
	
	@Resource
	private ResourceInfoService resourceInfoService;
	
	@GetMapping("test111")
	@ResponseBody
	public RequestResult<String> test(){
		try {
			spider.run();
		} catch (Exception e) {
			log.error("",e);
		}
		
//		ResourceInfoEntity optimalResource = resourceInfoService.getOptimalResource("");
		
		return new RequestResult<String>("到达");
	}
}

package com.sbolo.syk.fetch.controller;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.GrapicmagickUtils;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.service.MovieInfoService;
import com.sbolo.syk.fetch.service.ResourceInfoService;
import com.sbolo.syk.fetch.spider.Spider;


@Controller
public class TestController {
	
	private static final Logger log = LoggerFactory.getLogger(TestController.class);
	
	@Resource
	private Spider spider;
	
	@Resource
	private ResourceInfoService resourceInfoService;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPool;
	
	@Resource
	private MovieInfoService movieInfoService;
	
	@GetMapping("test111")
	@ResponseBody
	public RequestResult<String> test() throws ParseException{
//		movieInfoService.getOneByPureNameAndYear("亿万少年俱乐部", DateUtil.str2Date("2018", "yyyy"));
//		
//		resourceInfoService.getOptimalResource("m178267591232");
		try {
			spider.run();
		} catch (Exception e) {
			log.error("",e);
		}
//		
//		ResourceInfoEntity optimalResource = resourceInfoService.getOptimalResource("");
		
		return new RequestResult<String>("到达");
	}
	
	@GetMapping("test222")
	@ResponseBody
	public RequestResult<String> test222() throws Exception{
		String url = "https://tu.66vod.net/2018/3503.jpg";
    	byte[] bytes = HttpUtils.getBytes(url);
    	InputStream resourceAsStream = this.getClass().getResourceAsStream("/img/mark.png");
    	byte[] byteArray = IOUtils.toByteArray(resourceAsStream);
		byte[] watermark = GrapicmagickUtils.watermark(bytes, byteArray);
		String tempDir = ConfigUtil.getPropertyValue("fs.temp.dir");
		FileUtils.saveFile(watermark, tempDir, "test", "jpg");
		return new RequestResult<String>("到达");
	}
}

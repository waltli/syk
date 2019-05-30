package com.sbolo.syk.view.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbolo.syk.common.ui.ResultApi;
import com.sbolo.syk.view.service.ResourceInfoService;

@Controller
public class TestController {
	private static final Logger log = LoggerFactory.getLogger(TestController.class);
	@GetMapping("test111")
	public String test111() {
		return "message/main.html";
	}
	
	@GetMapping("json")
	@ResponseBody
	public Map<String, Object> json(){
		Map<String, Object> m = new HashMap<>();
		m.put("tocken", "123");
		return m;
	}
	
	@GetMapping("test")
	@ResponseBody
	public ResultApi<String> test(){
		File file = new File("/etc/shadow");
		if(!file.exists()) {
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>文件不存在");
			return new ResultApi<String>("文件不存在！");
		}
		if(!file.canRead()) {
			return new ResultApi<String>("文件不可读！");
		}
		
		if(!file.canWrite()) {
			return new ResultApi<String>("文件不可写！");
		}
		
		if(!file.canExecute()) {
			return new ResultApi<String>("文件不可执行！");
		}
		boolean delete = file.delete();
		return new ResultApi<String>("成功");
	}
}

package com.sbolo.syk.fetch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.jfinal.log.Log;
import com.sbolo.syk.common.ui.RequestResult;

import lombok.extern.slf4j.Slf4j;

@Controller
public class TestController {
	
	@GetMapping("test111")
	public String test(){
		return null;
	}
}

package com.sbolo.syk.view.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

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
}

package com.sbolo.syk.view.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbolo.syk.common.mvc.controller.BaseController;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.common.vo.MigrateVO;
import com.sbolo.syk.view.service.MigrateService;

@RestController
@RequestMapping("migrate")
public class MigrateController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(MigrateController.class);
	
	@Autowired
	private MigrateService migrateService;
	
	@PostMapping("write")
	public RequestResult<String> write(@RequestBody MigrateVO vo) {
		migrateService.write(vo);
		return new RequestResult<>("success!");
	}
}

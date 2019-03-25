package com.sbolo.syk.fetch.controller;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbolo.syk.common.enums.MovieCategoryEnum;
import com.sbolo.syk.common.enums.MovieTagEnum;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.tools.ConfigUtils;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.GrapicmagickUtils;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.ui.ResultApi;
import com.sbolo.syk.fetch.entity.MovieDictEntity;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.service.MigrateService;
import com.sbolo.syk.fetch.service.MovieDictService;
import com.sbolo.syk.fetch.service.MovieInfoService;
import com.sbolo.syk.fetch.service.ResourceInfoService;
import com.sbolo.syk.fetch.spider.Spider;
import com.sbolo.syk.fetch.tool.DoubanUtils;
import com.sbolo.syk.fetch.vo.MovieDictVO;
import com.sbolo.syk.fetch.vo.MovieInfoVO;


@Controller
@RequestMapping("test")
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
	
	@Resource
	private MigrateService migrateService;
	
	@Autowired
	private MovieInfoMapper movieInfoMapper;
	
	@Autowired
	private MovieDictService movieDictService;
	
	@GetMapping("111")
	@ResponseBody
	public ResultApi<String> test() throws ParseException{
		try {
			spider.run();
		} catch (Exception e) {
			log.error("",e);
		}
		return new ResultApi<String>("到达");
	}
	
	@GetMapping("222")
	@ResponseBody
	public ResultApi<String> test222() throws Exception {
		migrateService.migrate();
		return new ResultApi<String>("到达");
	}
	
	@GetMapping("333")
	@ResponseBody
	public ResultApi<String> test333() throws Exception{
		String url = "https://tu.66vod.net/2018/3503.jpg";
    	byte[] bytes = HttpUtils.getBytes(url);
    	InputStream resourceAsStream = this.getClass().getResourceAsStream("/img/mark.png");
    	byte[] byteArray = IOUtils.toByteArray(resourceAsStream);
		byte[] watermark = GrapicmagickUtils.watermark(bytes, byteArray);
		String tempDir = ConfigUtils.getPropertyValue("fs.temp.dir");
		FileUtils.saveFile(watermark, tempDir, "test", "jpg");
		return new ResultApi<String>("到达");
	}
	
	@GetMapping("tag")
	@ResponseBody
	public ResultApi<String> tag() throws Exception{
		List<MovieInfoEntity> selectByCategoryAndNullTag = movieInfoMapper.selectByCategoryAndNullTag(MovieCategoryEnum.tv.getCode());
		
		if(selectByCategoryAndNullTag == null || selectByCategoryAndNullTag.size() == 0) {
			throw new BusinessException("selectByCategoryAndNullTag is null");
		}
		
		List<MovieInfoEntity> toupList = new ArrayList<>();
		
		for(MovieInfoEntity entity : selectByCategoryAndNullTag) {
			MovieInfoEntity newEntity = new MovieInfoEntity();
			newEntity.setPrn(entity.getPrn());
			String tagLocation = DoubanUtils.getTagLocation(MovieCategoryEnum.tv.getCode(), entity.getLocations());
			String tag = MovieTagEnum.getTagByLocation(tagLocation);
			newEntity.setTag(tag);
			toupList.add(newEntity);
		}
		
		if(toupList.size() == 0) {
			throw new BusinessException("toupList.size == 0");
		}
		
		movieDictService.junitUP(toupList);
		return new ResultApi<String>("到达");
	}
	
	@GetMapping("dict")
	@ResponseBody
	public ResultApi<String> Dict() throws Exception{
		List<MovieInfoEntity> selectAll = movieInfoMapper.selectAll();
		
		List<MovieInfoVO> po2vos = VOUtils.po2vo(selectAll, MovieInfoVO.class);
		
		List<MovieDictVO> distDicts = movieDictService.distDict(po2vos, new Date());
		
		List<MovieDictEntity> dictAll = VOUtils.po2vo(distDicts, MovieDictEntity.class);
		
		if(dictAll.size() == 0) {
			throw new BusinessException("dictAll.size == 0");
		}
		
		movieDictService.junitInsert(dictAll);
		return new ResultApi<String>("到达");
	}
}

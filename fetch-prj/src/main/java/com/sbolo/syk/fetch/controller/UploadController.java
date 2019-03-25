package com.sbolo.syk.fetch.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.enums.MovieCategoryEnum;
import com.sbolo.syk.common.tools.ConfigUtils;
import com.sbolo.syk.common.ui.ResultApi;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

@Controller
public class UploadController {
	private static final Logger log = LoggerFactory.getLogger(UploadController.class);
	
	@RequestMapping(value="upload", method=RequestMethod.POST)
	public void uploadFile(@RequestParam("file") MultipartFile myfile, 
			@RequestParam(value="mi", required=false) String moviePrn,
			Integer fileType, 
			HttpServletRequest request, 
			HttpServletResponse response){
		ResultApi<Map<String, Object>> result = null;
		InputStream is = null;
		try {
			if(myfile.isEmpty()){
	        	throw new Exception("上传文件获取失败！");
			}
        	if(fileType == null){
        		throw new Exception("内部错误，未指定文件类型，请联系管理员！");
        	}
        	is = myfile.getInputStream();
        	byte[] bytes = IOUtils.toByteArray(is);
        	String fileName = myfile.getOriginalFilename();
        	String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        	String subDir = null;
        	String uri = null;
        	Map<String, Object> map = new HashMap<>();
        	if(fileType == CommonConstants.icon_v){
        		subDir = FetchUtils.saveTempIcon(bytes, suffix);
        	}else if(fileType == CommonConstants.poster_v){
        		subDir = FetchUtils.saveTempPoster(bytes, suffix);
        	}else if(fileType == CommonConstants.photo_v){
        		subDir = FetchUtils.saveTempPhoto(bytes, suffix);
        	}else if(fileType == CommonConstants.shot_v){
        		subDir = FetchUtils.saveTempShot(bytes, suffix);
        	}else if(fileType == CommonConstants.torrent_v){
        		subDir = FetchUtils.saveTempTorrent(bytes, suffix);
        		ResourceInfoVO resourceVO = FetchUtils.buildResouceInfoFromName(fileName, MovieCategoryEnum.tv.getCode(), null, null);
        		map.put("resource", resourceVO);
        	}
        	map.put("subDir", subDir);
        	uri = ConfigUtils.getPropertyValue("fs.temp.mapping")+subDir;
        	map.put("uri", uri);
        	result = new ResultApi<>(map);
		} catch (Exception e) {
			result = ResultApi.error(e);
			log.error("",e);
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("关闭流出错",e);
				}
			}
		}
		try {
			String json = JSON.toJSONString(result);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(json);
		} catch (IOException e) {
			log.error("",e);
		}  
	}
}

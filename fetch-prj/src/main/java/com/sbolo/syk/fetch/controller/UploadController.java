package com.sbolo.syk.fetch.controller;

import java.io.IOException;
import java.io.InputStream;

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
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.fetch.tool.FetchUtils;

@Controller
public class UploadController {
	private static final Logger log = LoggerFactory.getLogger(UploadController.class);
	
	@RequestMapping(value="upload", method=RequestMethod.POST)
	public void uploadFile(@RequestParam("file") MultipartFile myfile, 
			@RequestParam(value="mi", required=false) String moviePrn,
			Integer fileType, 
			HttpServletRequest request, 
			HttpServletResponse response){
		RequestResult<String> result = null;
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
        	String uri = null;
        	if(fileType == CommonConstants.icon_v){
        		uri = FetchUtils.saveTempIcon(bytes, suffix);
        	}else if(fileType == CommonConstants.poster_v){
        		uri = FetchUtils.saveTempPoster(bytes, suffix);
        	}else if(fileType == CommonConstants.photo_v){
        		uri = FetchUtils.saveTempPhoto(bytes, suffix);
        	}else if(fileType == CommonConstants.shot_v){
        		uri = FetchUtils.saveTempShot(bytes, suffix);
        	}else if(fileType == CommonConstants.torrent_v){
        		uri = FetchUtils.saveTempTorrent(bytes, suffix);
        		FetchUtils.getResouceInfoFromName(fileName, category, season, totalEpisode);
        		
        	}
		} catch (Exception e) {
			result.setRequestResult(false);
			result.setError(e.getMessage());
		} finally {
			if(is != null) {
				is.close();
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

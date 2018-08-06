package com.sbolo.syk.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.constants.CommonConstants;
import com.sbolo.syk.service.MovieService;
import com.sbolo.syk.service.ResourceService;
import com.sbolo.syk.tools.StringUtil;
import com.sbolo.syk.tools.Utils;
import com.sbolo.syk.ui.AjaxResult;

@Controller
public class UploadController {
	private static final Logger log = LoggerFactory.getLogger(UploadController.class);
	
	@Resource
	private MovieService movieService;
	
	@Resource
	private ResourceService resourceService;
	
	@RequestMapping(value="upload", method=RequestMethod.POST)
	public void uploadFile(@RequestParam("file") MultipartFile myfile, 
			Integer fileType, 
			HttpServletRequest request, 
			HttpServletResponse response){
		AjaxResult result = new AjaxResult(true);
		try {
			if(myfile.isEmpty()){
	        	throw new Exception("上传文件获取失败！");
	        }else{
	        	if(fileType == null){
	        		throw new Exception("内部错误，未指定文件类型，请联系管理员！");
	        	}
	        	
	        	String fileName = myfile.getOriginalFilename();
	        	String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
	        	if(fileType == CommonConstants.icon_v){
	        		InputStream is = myfile.getInputStream();
	        		String ownPath = "temp/icon";
	        		String tempPath = request.getSession().getServletContext().getRealPath(ownPath);
	        		String newName = StringUtil.getId(CommonConstants.pic_s)+"."+suffix;
	        		Utils.uploadPic(is, tempPath, newName, suffix, CommonConstants.icon_width, CommonConstants.icon_height);
	        		result.put("uri", ownPath+"/"+newName);
	        	}else if(fileType == CommonConstants.poster_v){
	        		InputStream is = myfile.getInputStream();
	        		String ownPath = "temp/poster";
	        		String tempPath = request.getSession().getServletContext().getRealPath(ownPath);
	        		String newName = StringUtil.getId(CommonConstants.pic_s)+"."+suffix;
	        		Utils.uploadPic(is, tempPath, newName, suffix, CommonConstants.photo_width, CommonConstants.photo_height);
	        		result.put("uri", ownPath+"/"+newName);
	        	}else if(fileType == CommonConstants.photo_v){
	        		InputStream is = myfile.getInputStream();
	        		String ownPath = "temp/photo";
	        		String tempPath = request.getSession().getServletContext().getRealPath(ownPath);
	        		String newName = StringUtil.getId(CommonConstants.pic_s)+"."+suffix;
	        		Utils.uploadPic(is, tempPath, newName, suffix, CommonConstants.photo_width, CommonConstants.photo_height);
	        		result.put("uri", ownPath+"/"+newName);
	        	}else if(fileType == CommonConstants.torrent_v){
	        		byte[] bytes = myfile.getBytes();
	        		String ownPath = "temp/torrent";
	        		String tempPath = request.getSession().getServletContext().getRealPath(ownPath);
	        		String newName = StringUtil.getId(CommonConstants.file_s)+"."+suffix;
	        		Utils.saveFile(bytes, tempPath, newName);
	        		result.put("uri", ownPath+"/"+newName);
	        		
	        		resourceService.fetchResouceInfo(fileName, bytes, result);
	        		
	        	}
	        }
		} catch (Exception e) {
			result.setRequestResult(false);
			result.setError(e.getMessage());
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

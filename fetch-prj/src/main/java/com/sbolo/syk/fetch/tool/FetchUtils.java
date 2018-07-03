package com.sbolo.syk.fetch.tool;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

public class FetchUtils {
	/**
     * 当原先resource被替换后，删除原resource留下的文件
     * @param resource
     */
    public static void deleteResourceFile(ResourceInfoVO resource){
    	String formalDir = ConfigUtil.getPropertyValue("fs.formal.dir");
		String printscreenJson = resource.getPrintscreenUriJson();
		
		if(StringUtils.isNotBlank(printscreenJson)){
			List<String> printscreenUriList = JSON.parseArray(printscreenJson, String.class);
			for(String printscreenUri:printscreenUriList){
				String printscreenPath = formalDir+"/"+printscreenUri;
				FileUtils.deleteFile(printscreenPath);
			}
		}
		
		String downloadLink = resource.getDownloadLink();
		if(StringUtil.isLocalLink(downloadLink)){
			String torrentPath = formalDir+"/"+downloadLink;
			FileUtils.deleteFile(torrentPath);
		}
	}
    
    /**
     * 删除movie相关的文件
     * 因电影图片可以不用更新，故该方法暂时未使用
     * @param resource
     */
    public static void deleteMovieFile(MovieInfoVO movie){
    	String formalDir = ConfigUtil.getPropertyValue("fs.formal.dir");
		String iconUri = movie.getIconUri();
		if(StringUtils.isNotBlank(iconUri)) {
			String iconPath = formalDir+"/"+iconUri;
			FileUtils.deleteFile(iconPath);
		}
		
		String posterUriJson = movie.getPosterUriJson();
		if(StringUtils.isNotBlank(posterUriJson)) {
			List<String> posterUriList = JSON.parseArray(posterUriJson, String.class);
			for(String posterUri : posterUriList) {
				String posterPath = formalDir + "/" + posterUri;
				FileUtils.deleteFile(posterPath);
			}
		}
	}
}

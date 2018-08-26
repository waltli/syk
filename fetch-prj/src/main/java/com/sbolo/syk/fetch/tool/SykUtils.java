package com.sbolo.syk.fetch.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieQualityEnum;
import com.sbolo.syk.common.constants.MovieResolutionConstant;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.tools.BucketUtils;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.GrapicmagickUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

public class SykUtils {
	
	public static String uploadIcon4Uri(String iconTempUri) throws Exception {
		if(StringUtils.isNotBlank(iconTempUri)){
			String iconTempDir = ConfigUtil.getPropertyValue("icon.temp.dir");
			String suffix = iconTempUri.substring(iconTempUri.lastIndexOf(".")+1);
			String iconTempPath = iconTempDir+"/"+iconTempUri;
			InputStream is = null;
			try {
				File file = new File(iconTempPath);
				if(file.exists()) {
					is = new FileInputStream(file);
					byte[] bytes = IOUtils.toByteArray(is);
					String targetDir = ConfigUtil.getPropertyValue("bucket.formal.icon");
					int fixWidth = CommonConstants.icon_width;
					int fixHeight = CommonConstants.icon_height;
					String formalName = StringUtil.getId(CommonConstants.pic_s);
					String uri = SykUtils.downPicAndFixWithDist(bytes, targetDir, formalName, suffix, fixWidth, fixHeight);
					return uri;
				}
			} finally {
				if(is != null) {
					is.close();
					is = null;
				}
			}
		}
		return null;
	}
	
	public static String uploadIcon4Url(String iconUrl) throws Exception {
		if(StringUtils.isNotBlank(iconUrl)){
			String targetDir = ConfigUtil.getPropertyValue("bucket.formal.icon");
			int fixWidth = CommonConstants.icon_width;
			int fixHeight = CommonConstants.icon_height;
			String formalName = StringUtil.getId(CommonConstants.pic_s);
			String suffix = iconUrl.substring(iconUrl.lastIndexOf(".")+1);
			String uri = SykUtils.downPicAndFixWithDist(iconUrl, targetDir, formalName, suffix, fixWidth, fixHeight);
			return uri;
		}
		return null;
	}
	
	public static String uploadPhoto4Uri(List<String> photoTempUriList) throws Exception {
		List<String> photoUriList = new ArrayList<>();
		for(String photoTempUri : photoTempUriList) {
			String photoTempDir = ConfigUtil.getPropertyValue("photo.temp.dir");
			String suffix = photoTempUri.substring(photoTempUri.lastIndexOf(".")+1);
			String photoTempPath = photoTempDir+"/"+photoTempUri;
			InputStream is = null;
			try {
				File file = new File(photoTempPath);
				if(file.exists()) {
					is = new FileInputStream(file);
					byte[] bytes = IOUtils.toByteArray(is);
					String targetDir = ConfigUtil.getPropertyValue("bucket.formal.photo");
					int fixWidth = CommonConstants.photo_width;
					int fixHeight = CommonConstants.photo_height;
					String formalName = StringUtil.getId(CommonConstants.pic_s);
					String uri = SykUtils.downPicAndFixWithDist(bytes, targetDir, formalName, suffix, fixWidth, fixHeight);
					photoUriList.add(uri);
				}
			} catch (Exception e) {
				
			} finally {
				if(is != null) {
					is.close();
					is = null;
				}
			}
		}
		
		if(photoUriList != null && photoUriList.size() > 0){
			String photoUriStr = JSON.toJSONString(photoUriList);
			return photoUriStr;
		}
			
		return null;
	}
	
	public static String uploadPoster4Uri(List<String> posterTempUriList) throws Exception {
		List<String> posterUriList = new ArrayList<>();
		for(String posterTempUri : posterTempUriList) {
			String posterTempDir = ConfigUtil.getPropertyValue("poster.temp.dir");
			String suffix = posterTempUri.substring(posterTempUri.lastIndexOf(".")+1);
			String posterTempPath = posterTempDir+"/"+posterTempUri;
			InputStream is = null;
			try {
				File file = new File(posterTempPath);
				if(file.exists()) {
					is = new FileInputStream(file);
					byte[] bytes = IOUtils.toByteArray(is);
					String targetDir = ConfigUtil.getPropertyValue("bucket.formal.poster");
					int fixWidth = CommonConstants.poster_width;
					int fixHeight = CommonConstants.poster_height;
					String formalName = StringUtil.getId(CommonConstants.pic_s);
					String uri = SykUtils.downPicAndFixWithDist(bytes, targetDir, formalName, suffix, fixWidth, fixHeight);
					posterUriList.add(uri);
				}
			} catch (Exception e) {
				
			} finally {
				if(is != null) {
					is.close();
					is = null;
				}
			}
		}
		
		if(posterUriList != null && posterUriList.size() > 0){
			String posterUriStr = JSON.toJSONString(posterUriList);
			return posterUriStr;
		}
			
		return null;
	}
	
	public static String uploadShot4Uri(List<String> shotTempUriList) throws Exception {
		List<String> shotUriList = new ArrayList<>();
		for(String shotTempUri : shotTempUriList) {
			String shotTempDir = ConfigUtil.getPropertyValue("shot.temp.dir");
			String suffix = shotTempUri.substring(shotTempUri.lastIndexOf(".")+1);
			String shotTempPath = shotTempDir+"/"+shotTempUri;
			InputStream is = null;
			try {
				File file = new File(shotTempPath);
				if(file.exists()) {
					is = new FileInputStream(file);
					byte[] bytes = IOUtils.toByteArray(is);
					String targetDir = ConfigUtil.getPropertyValue("bucket.formal.shot");
					int fixWidth = CommonConstants.shot_width;
					int fixHeight = CommonConstants.shot_height;
					String formalName = StringUtil.getId(CommonConstants.pic_s);
					String uri = SykUtils.downPicAndFixWithDist(bytes, targetDir, formalName, suffix, fixWidth, fixHeight);
					shotUriList.add(uri);
				}
			} catch (Exception e) {
				
			} finally {
				if(is != null) {
					is.close();
					is = null;
				}
			}
		}
		
		if(shotUriList != null && shotUriList.size() > 0){
			String shotUriStr = JSON.toJSONString(shotUriList);
			return shotUriStr;
		}
			
		return null;
	}
	
	public static String uploadTorrent4Uri(String downloadLinkTemp, String torrentName) throws Exception {
		if(StringUtils.isNotBlank(downloadLinkTemp)){
			String torrentTempDir = ConfigUtil.getPropertyValue("torrent.temp.dir");
			String suffix = downloadLinkTemp.substring(downloadLinkTemp.lastIndexOf(".")+1);
			String targetDir = ConfigUtil.getPropertyValue("bucket.formal.torrent");
			String downloadLink = BucketUtils.upload2Dir(new File(torrentTempDir), targetDir, torrentName, suffix);
			return downloadLink;
		}
		return null;
	}
	
	
	/**
	 * 下载文件并返回uri
	 * @param url
	 * @param targetDir
	 * @param fileName
	 * @param suffix
	 * @return
	 * @throws Exception
	 */
	public static String downTorrentWithDist(String url, byte[] bytes, String targetDir, String fileName, String suffix) throws Exception {
		if(bytes == null) {
			bytes = HttpUtils.getBytes(url);
		}
		String uri = BucketUtils.upload2Dir(bytes, targetDir, fileName, suffix);
		return uri;
	}
	
	/**
	 * 下载图片并修正图片大小，返回uri
	 * @param url
	 * @param targetDir
	 * @param fileName
	 * @param suffix
	 * @param fixWidth
	 * @param fixHeight
	 * @return
	 * @throws Exception
	 */
	public static String downPicAndFixWithDist(String url, String targetDir, String fileName, String suffix, int fixWidth, int fixHeight) throws Exception {
		byte[] imageBytes = HttpUtils.getBytes(url);
		imageBytes = GrapicmagickUtils.descale(imageBytes, fixWidth, fixHeight);
		String uri = BucketUtils.upload2Dir(imageBytes, targetDir, fileName, suffix);
		return uri;
	}
	
	public static String downPicAndFixWithDist(byte[] bytes, String targetDir, String fileName, String suffix, int fixWidth, int fixHeight) throws Exception {
		byte[] imageBytes = GrapicmagickUtils.descale(bytes, fixWidth, fixHeight);
		String uri = BucketUtils.upload2Dir(imageBytes, targetDir, fileName, suffix);
		return uri;
	}
	
	/**
	 * 下载图片并修正图片大小以及添加水印，返回uri
	 * @param url
	 * @param targetDir
	 * @param fileName
	 * @param suffix
	 * @param fixWidth
	 * @param fixHeight
	 * @return
	 * @throws Exception
	 */
	public static String downPicAndFixMarkWithDist(String url, String targetDir, String fileName, String suffix, int fixWidth, int fixHeight) throws Exception {
		InputStream markStream = null;
		try {
			byte[] imageBytes = HttpUtils.getBytes(url);
			imageBytes = GrapicmagickUtils.descale(imageBytes, fixWidth, fixHeight);
			markStream = SykUtils.class.getResourceAsStream("/img/mark.png");
			if(markStream != null) {
				byte[] markBytes = IOUtils.toByteArray(markStream);
				imageBytes = GrapicmagickUtils.watermark(imageBytes, markBytes);
			}
			String uri = BucketUtils.upload2Dir(imageBytes, targetDir, fileName, suffix);
			return uri;
		} finally {
			if(markStream != null) {
				markStream.close();
				markStream = null;
			}
		}
	}
	
	/**
	 * 重新组建种子文件名
	 * @param fetchResource
	 * @return
	 */
	public static String getTorrentName(ResourceInfoVO fetchResource) {
		StringBuffer fileName = new StringBuffer(fetchResource.getPureName().replaceAll(" ", "."));
		if(fetchResource.getEpisodeStart() != null){
			fileName.append(".第").append(fetchResource.getEpisodeStart())
			.append("-").append(fetchResource.getEpisodeEnd()).append("集");
		}else if(fetchResource.getEpisodeEnd() != null){
			fileName.append(".第").append(fetchResource.getEpisodeEnd()).append("集");
		}
		if(StringUtils.isNotBlank(fetchResource.getQuality())){
			fileName.append(".").append(fetchResource.getQuality());
		}
		if(StringUtils.isNotBlank(fetchResource.getResolution())){
			fileName.append(".").append(fetchResource.getResolution());
		}
		String subtitleNotice = "";
		if(StringUtils.isNotBlank(fetchResource.getSubtitle())){
			subtitleNotice = fetchResource.getSubtitle();
		}
		fileName.append(".").append(subtitleNotice).append(CommonConstants.local_sign);
		return fileName.toString();
	}
	
	/**
     * 计算资源resource的得分情况
     * 方便进行资源的好坏的比较
     * @param quality
     * @param resolution
     * @return
     */
    public static int translateDefinitionIntoScore(String quality, String resolution){
		
		if(StringUtils.isBlank(quality) && StringUtils.isBlank(resolution)){
			return 0;
		}
		if(StringUtils.isBlank(quality)){
			return MovieResolutionConstant.getPureResolutionScoreByKey(resolution);
		}
		
		if(StringUtils.isBlank(resolution)){
			return MovieQualityEnum.getScoreByName(quality);
		}
		
		BigDecimal rat = new BigDecimal(0.69);
		BigDecimal qualityScore = new BigDecimal(MovieQualityEnum.getScoreByName(quality));
		BigDecimal resolutionScore = new BigDecimal(MovieResolutionConstant.getResolutionScoreByKey(resolution));
		BigDecimal definition = qualityScore.add(resolutionScore).multiply(rat).setScale(0,BigDecimal.ROUND_HALF_UP);
		
		return definition.intValue();
	}
}

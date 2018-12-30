package com.sbolo.syk.common.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

public class CommonConstants {
	public static final Map<Integer, String> timeFormat = new HashMap<Integer, String>();
	
	public static final String SEPARATOR = "#Y";
	
	public static final Integer insert = 1;
	public static final Integer update = 2;
	//还需第二次处理的
	public static final Integer waiting = 3;
	public static final Integer abandon = 4;
	
	public static final String movie_key = "movieInfo";
	public static final String resource_key = "resourceInfo";
	public static final String label_mapping_key = "label";
	public static final String location_mapping_key = "location";
	public static final String domain = "[www.cyvdo.com]";
	
	//图片裁剪的尺寸，比实际需要大了1px，以为在裁剪的时候有误差
	public static final int icon_width = 183;  
	public static final int icon_height = 273;
	public static final int photo_width = 478;
	public static final int photo_height = 273;
	public static final int poster_width = 630;  
	public static final int poster_height = 354;
	public static final int shot_width = 630;
	public static final int shot_height = 354;
	
	//上传下载文件时判断文件类型标识
	public static final int icon_v = 1;
	public static final int poster_v = 2;
	public static final int photo_v = 3;
	public static final int shot_v = 4;
	public static final int torrent_v = 5;
	
	//生成文件名和唯一ID标识
	public static final String movie_s = "m";
	public static final String resource_s = "r";
	public static final String comment_s = "c";
	public static final String hot_s = "h";
	public static final String label_s = "b";
	public static final String location_s = "a";
	public static final String tag_s = "t";
	public static final String pic_s = "p";
	public static final String file_s = "f";
	public static final String message_s = "g";
	public static final String user_s = "u";
	
	public static final int rt_update = 2;
	public static final int rt_add2update = 3;
	
	public static final String CERT_S = "c";
	public static final String PIC_S = "p";
	public static final String DOC_S = "d";
	public static final String OTH_S = "o";
	public static final String INST_S = "i";
	
	public static final String movie_default_icon = "movie_default_large.png";
	public static final String tv_default_icon = "tv_default_large.png";
	
	public static final String USER = "user";
	public static final String USERNAME = "username";
	
	public static final String DICT = "dict_";
	
	public static final String DICT_TOP = "0";
	
	/**
	 * 当前正在使用的过滤标识
	 */
	public static final String USE_FILTER_CHAR = "";
	
	static{
		timeFormat.put(4, "yyyy");
		timeFormat.put(6, "yyyy-MM");
		timeFormat.put(7, "yyyy-MM");
		timeFormat.put(8, "yyyy-MM-dd");
		timeFormat.put(9, "yyyy-MM-dd");
		timeFormat.put(10, "yyyy-MM-dd");
		timeFormat.put(19, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String getTimeFormat(String timeStr){
		if(StringUtils.isBlank(timeStr)){
			return null;
		}
		return timeFormat.get(timeStr.length());
	}
}

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
	public static final Integer abandon = 3;
	
	public static final String movie_key = "movieInfo";
	public static final String resource_key = "resourceInfo";
	public static final String label_mapping_key = "label";
	public static final String location_mapping_key = "location";
	public static final String local_sign = "[www.chanying.cc]";
	
	//图片裁剪的尺寸，比实际需要大了1px，以为在裁剪的时候有误差
	public static final int icon_width = 183;  
	public static final int icon_height = 273;
	public static final int photo_width = 478;
	public static final int photo_height = 273;
	
	//上传下载文件时判断文件类型标识
	public static final int icon_v = 1;
	public static final int poster_v = 2;
	public static final int photo_v = 3;
	public static final int torrent_v = 4;
	
	//生成文件名和唯一ID标识
	public static final String movie_s = "m";
	public static final String resource_s = "r";
	public static final String comment_s = "c";
	public static final String hot_s = "h";
	public static final String label_s = "b";
	public static final String location_s = "a";
	public static final String pic_s = "p";
	public static final String file_s = "f";
	
	public static final int rt_update = 2;
	public static final int rt_add2update = 3;
	
	public static final String CERT_S = "c";
	public static final String PIC_S = "p";
	public static final String DOC_S = "d";
	public static final String OTH_S = "o";
	public static final String INST_S = "i";
	
	/**
	 * 乐观锁数据库列名
	 */
	public static final String UP_VERSION_COLUMN_NAME = "up_ver";
	
	/**
	 * id列名
	 */
	public static final String ID_NAME = "id";
	
	/**
	 * prn数据库列名
	 */
	public static final String PRN = "prn";
	
	/**
	 * 当前正在使用的过滤标识
	 */
	public static final String USE_FILTER_CHAR = "";
	
	/**
	 * 状态列名
	 */
	public static final String ST_NAME = "st";
	
	static{
		timeFormat.put(4, "yyyy");
		timeFormat.put(6, "yyyy-MM");
		timeFormat.put(7, "yyyy-MM");
		timeFormat.put(8, "yyyy-MM-dd");
		timeFormat.put(9, "yyyy-MM-dd");
		timeFormat.put(10, "yyyy-MM-dd");
	}
	
	public static String getTimeFormat(String timeStr){
		if(StringUtils.isBlank(timeStr)){
			return null;
		}
		return timeFormat.get(timeStr.length());
	}
}

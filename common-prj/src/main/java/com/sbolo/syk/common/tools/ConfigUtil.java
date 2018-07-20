package com.sbolo.syk.common.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.sbolo.syk.common.exception.BusinessException;

/**
 * 
 * @author lihaopeng
 * @date 2015年10月8日
 * @version 1.0.0
 * @description desc
 */
public class ConfigUtil {

	private static Properties properties;
	
	static{
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void init() throws IOException{
		String[] configArr = new String[]{"/config_gp.properties", "/config_g.properties", "/config_p.properties"};
		
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		properties = new Properties();
		
		for(String config : configArr) {
			try {
				inputStream = ConfigUtil.class.getResourceAsStream(config);
				if(inputStream != null){
					inputStreamReader = new InputStreamReader(inputStream, "utf-8");
					properties.load(inputStreamReader);
				}
			}finally {
				if(inputStreamReader != null) {
					inputStreamReader.close();
					inputStreamReader = null;
				}
				if(inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			}
		}
	}
	
	
	/**
	 * 获取配置文件的值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getPropertyValue(String key,String defaultValue){
		String value = getPropertyValue(key);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * 获取配置文件的值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getPropertyValue(String key){
		Object valueObj = properties.get(key);
		if (valueObj == null) {
			return null;
		}
		
		String value = String.valueOf(valueObj);
		
		if (StringUtils.isBlank(value)) {
			return null;
		}
		
		Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher m = p.matcher(valueObj.toString());
		if(!m.find()){
			return value;
		}
		String inner = m.group();
		String innerKey = m.group(1);
		String innerValue = getPropertyValue(innerKey);
		if(StringUtils.isBlank(innerValue)) {
			return value;
		}
		value = value.replace(inner, innerValue);
		return value;
	}
}

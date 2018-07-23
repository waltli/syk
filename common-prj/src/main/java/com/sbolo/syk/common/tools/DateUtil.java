package com.sbolo.syk.common.tools;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.sbolo.syk.common.constants.TimeDirectEnum;
import com.sbolo.syk.common.exception.BusinessException;

public class DateUtil {
	public static String date2Str(Date time, String format){
		if(time == null || format == null){
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(time);
	}
	
	public static String date2Str(Date time){
		return date2Str(time, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static Date str2Date(String dateStr, String format) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.parse(dateStr);
	}
	
	public static Date str2Date(String dateStr) throws ParseException{
		return str2Date(dateStr, TimeDirectEnum.NORMAL);
	}
	
	public static Date str2Date(String dateStr, TimeDirectEnum direct) throws ParseException{
		dateStr = dateWipe(dateStr, direct, true);
		String format = getFormat(dateStr);
		return str2Date(dateStr, format);
	}
	
	public static Date str2Date(String dateStr, String format, TimeDirectEnum direct) throws ParseException{
		dateStr = dateWipe(dateStr, direct, true);
		return str2Date(dateStr, format);
	}
	
	/**
	 * 
	 * @param dateStr
	 * @param direct
	 * @param isRape 如果为false，只有在没有时分秒时才会按照direct的方式来填充
	 * @return
	 * @throws ParseException
	 */
	public static Date str2Date(String dateStr, TimeDirectEnum direct, Boolean isRape) throws ParseException{
		dateStr = dateWipe(dateStr, direct, isRape);
		String format = getFormat(dateStr);
		return str2Date(dateStr, format);
	}
	
	/**
	 * 
	 * @param dateStr
	 * @param format
	 * @param direct
	 * @param isRape 如果为false，只有在没有时分秒时才会按照direct的方式来填充
	 * @return
	 * @throws ParseException
	 */
	public static Date str2Date(String dateStr, String format, TimeDirectEnum direct, Boolean isRape) throws ParseException{
		dateStr = dateWipe(dateStr, direct, isRape);
		return str2Date(dateStr, format);
	}
	
	public static String getFormat(String dateStr){
		dateStr = dateStr.trim();
		String dateFormat = null;
		if(Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$").matcher(dateStr).find()){
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}else if(Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$").matcher(dateStr).find()){
			dateFormat = "yyyy-MM-dd";
		}else if(Pattern.compile("^\\d{4}-\\d{1,2}$").matcher(dateStr).find()) {
			dateFormat = "yyyy-MM";
		}else if(Pattern.compile("^\\d{4}$").matcher(dateStr).find()) {
			Integer year = Integer.valueOf(dateStr);
			if(year < 1969 || year > 2099) {
				throw new BusinessException("时间年份"+year+" 超过了有效年份: 1969-2099");
			}
			dateFormat = "yyyy";
		}
		else {
			throw new BusinessException("时间格式错误，请符合：yyyy-MM-dd HH:mm:ss, yyyy-MM-dd, yyyy-MM, yyyy");
		}
		return dateFormat;
	}
	
	public static String dateWipe(String dateStr, TimeDirectEnum direct, Boolean isRape){
		dateStr = dateStr.trim();
		String reg = "^(\\d{4}-\\d{1,2}-\\d{1,2})$";
		
		if(isRape){
			reg = "^(\\d{4}-\\d{1,2}-\\d{1,2})( \\d{1,2}:\\d{1,2}:\\d{1,2})?$";
		}
		
		if(direct.equals(TimeDirectEnum.UP)){
			dateStr = dateStr.replaceAll(reg, "$1 "+TimeDirectEnum.UP.getCode());
		}else if(direct.equals(TimeDirectEnum.DOWN)){
			dateStr = dateStr.replaceAll(reg, "$1 "+TimeDirectEnum.DOWN.getCode());
		}
		return dateStr;
		
	}
}

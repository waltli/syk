package com.sbolo.syk.common.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import com.sbolo.syk.common.enums.TimeDirectEnum;
import com.sbolo.syk.common.exception.BusinessException;

public class DateUtil {
	private static final String defaultFormat = "yyyy-MM-dd HH:mm:ss";
	
	public static String date2Str(Date time){
		return date2Str(time, defaultFormat);
	}
	
	public static String date2Str(Date time, TimeDirectEnum direct) throws ParseException{
		String format = direct.getFormat();
		if(direct.getCode() == TimeDirectEnum.DOWN_IN_YEAR.getCode()) {
			time = yearDown(time);
		}else if(direct.getCode() == TimeDirectEnum.UP_IN_YEAR.getCode()) {
			time = yearUp(time);
		}else if(direct.getCode() == TimeDirectEnum.DOWN_IN_MONTH.getCode()) {
			time = monthDown(time);
		}else if(direct.getCode() == TimeDirectEnum.UP_IN_MONTH.getCode()) {
			time = monthUp(time);
		}else if(direct.getCode() == TimeDirectEnum.DOWN_IN_DAY.getCode()) {
			time = dayDown(time);
		}else if(direct.getCode() == TimeDirectEnum.UP_IN_DAY.getCode()) {
			time = dayUp(time);
		}else if(direct.getCode() == TimeDirectEnum.DOWN_IN_HOUR.getCode()) {
			time = hourDown(time);
		}else if(direct.getCode() == TimeDirectEnum.UP_IN_HOUR.getCode()) {
			time = hourUp(time);
		}else if(direct.getCode() == TimeDirectEnum.DOWN_IN_MINUTE.getCode()) {
			time = minuteDown(time);
		}else if(direct.getCode() == TimeDirectEnum.UP_IN_MINUTE.getCode()) {
			time = minuteUp(time);
		}else {
			format = defaultFormat;
		}
		
		return date2Str(time, format);
	}
	
	public static String date2Str(Date time, String format){
		if(time == null || format == null){
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(time);
	}
	
	public static Date str2Date(String dateStr) throws ParseException{
		String guessFormat = getFormat(dateStr);
		return str2Date(dateStr, guessFormat);
	}
	
	public static Date str2Date(String dateStr, TimeDirectEnum direct) throws ParseException{
		String format = direct.getFormat();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = sdf.parse(dateStr);
		
		if(direct.getCode() == TimeDirectEnum.DOWN_IN_YEAR.getCode()) {
			return yearDown(date);
		}else if(direct.getCode() == TimeDirectEnum.UP_IN_YEAR.getCode()) {
			return yearUp(date);
		}else if(direct.getCode() == TimeDirectEnum.DOWN_IN_MONTH.getCode()) {
			return monthDown(date);
		}else if(direct.getCode() == TimeDirectEnum.UP_IN_MONTH.getCode()) {
			return monthUp(date);
		}else if(direct.getCode() == TimeDirectEnum.DOWN_IN_DAY.getCode()) {
			return dayDown(date);
		}else if(direct.getCode() == TimeDirectEnum.UP_IN_DAY.getCode()) {
			return dayUp(date);
		}else if(direct.getCode() == TimeDirectEnum.DOWN_IN_HOUR.getCode()) {
			return hourDown(date);
		}else if(direct.getCode() == TimeDirectEnum.UP_IN_HOUR.getCode()) {
			return hourUp(date);
		}else if(direct.getCode() == TimeDirectEnum.DOWN_IN_MINUTE.getCode()) {
			return minuteDown(date);
		}else if(direct.getCode() == TimeDirectEnum.UP_IN_MINUTE.getCode()) {
			return minuteUp(date);
		}
		
		return str2Date(dateStr);
	}
	
	public static Date str2Date(String dateStr, String format) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.parse(dateStr);
	}
	
	public static Date yearDown(Date date) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
        cale.set(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        return cale.getTime();
	}
	
	public static Date yearUp(Date date) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.set(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        
		cale.add(Calendar.YEAR, 1);
        cale.add(Calendar.SECOND, -1);
        return cale.getTime();
	}
	
	public static Date monthDown(Date date) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.set(Calendar.DAY_OF_MONTH, 1);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.SECOND, 0);
        return cale.getTime();
	}
	
	public static Date monthUp(Date date) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.set(Calendar.DAY_OF_MONTH, 1);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.SECOND, 0);
		
		cale.add(Calendar.MONTH, 1); 
        cale.add(Calendar.SECOND, -1);
        return cale.getTime();
	}
	
	public static Date dayDown(Date date) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        return cale.getTime();
	}
	
	public static Date dayUp(Date date) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        
		cale.add(Calendar.DAY_OF_MONTH, 1);
        cale.add(Calendar.SECOND, -1);
        return cale.getTime();
	}
	
	public static Date hourDown(Date date) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        return cale.getTime();
	}
	
	public static Date hourUp(Date date) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
		
		cale.add(Calendar.HOUR_OF_DAY, 1);
        cale.add(Calendar.SECOND, -1);
        return cale.getTime();
	}
	
	public static Date minuteDown(Date date) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.set(Calendar.SECOND, 0);
        return cale.getTime();
	}
	
	public static Date minuteUp(Date date) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.set(Calendar.SECOND, 0);
		
		cale.add(Calendar.MINUTE, 1);
        cale.add(Calendar.SECOND, -1);
        return cale.getTime();
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
}

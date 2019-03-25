package com.sbolo.syk.common.tools;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieResolutionConstant;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.enums.MovieQualityEnum;
import com.sbolo.syk.common.exception.BusinessException;

public class Utils {
	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
	
	/**
	 * 判断一个类是否为基本数据类型。
	 * 
	 * @param clazz
	 *            要判断的类。
	 * @return true 表示为基本数据类型。
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isMinimumType(Class clazz) {
		return (clazz.isPrimitive() || clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class)
				|| clazz.equals(Long.class) || clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(Character.class) || clazz.equals(Short.class) || clazz.equals(BigDecimal.class)
				|| clazz.equals(BigInteger.class) || clazz.equals(Boolean.class) || clazz.equals(Date.class));
	}
	
	public static byte[] objectToByte(Object obj) throws IOException {  
	    try (ByteArrayOutputStream bo = new ByteArrayOutputStream();
	    		ObjectOutputStream oo = new ObjectOutputStream(bo); ) {
	    	byte[] bytes = null;
	        oo.writeObject(obj);
	        bytes = bo.toByteArray();
	        return bytes;  
	    }
	}
	
	public static Object byteToObject(byte[] bytes) {
		Object obj = null;
		try (ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
				ObjectInputStream oi = new ObjectInputStream(bi);) {
	
			obj = oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
        return obj;
    }
	
	public static String getServerName(){
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		for(int i=2; i<stacks.length; i++){
			StackTraceElement stack = stacks[i];
			String className = stack.getClassName();
			Pattern p = Pattern.compile("com\\.qzsoft\\.lims\\.(\\w{2,4})\\.");
			Matcher m = p.matcher(className);
			if(m.find()){
				return m.group(1);
			}
		}
		throw new BusinessException("serverName获取失败！");
	}
	
	/**  
     * 生成32位编码  
     * @return string  
     */    
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }
    
	public static String getTimeStr(String content){
		for(int i=0; i<RegexConstant.list_release_time.size(); i++){
			Matcher m = RegexConstant.list_release_time.get(i).matcher(content);
			if(m.find()){
				return m.group();
			}
		}
		return null;
	}
	
    public static String getCharset(String contentType) {
        Matcher matcher = Pattern.compile(RegexConstant.pattern_for_charset).matcher(contentType);
        if (matcher.find()) {
            String charset = matcher.group(1);
            if (Charset.isSupported(charset)) {
                return charset;
            }
        }
        return null;
    }
	
	
	public static String unicode2Chinese(String unicode){
		String newStr = null;
		try {
			newStr = URLDecoder.decode(unicode,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error("",e);
		}
		return newStr;
	}
	
	
	/** 
     * 将数字转换成中文数字 
     * @author Prosper 
     * 
     */  
    public static String int2ChineseNumber(String i){
    	Matcher m = Pattern.compile(RegexConstant.cn_number).matcher(i);
    	if(m.find()){
    		return i;
    	}
        String[] zh = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};    
        String[] unit = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十"};    
          
        String str = "";  
        StringBuffer sb = new StringBuffer(i);  
        sb = sb.reverse();  
        int r = 0;  
        int l = 0;  
        for (int j = 0; j < sb.length(); j++)  
        {  
            /** 
             * 当前数字 
             */  
            r = Integer.valueOf(sb.substring(j, j+1));  
              
            if (j != 0)  
                /** 
                 * 上一个数字 
                 */  
                l = Integer.valueOf(sb.substring(j-1, j));  
              
            if (j == 0)  
            {  
                if (r != 0 || sb.length() == 1)  
                    str = zh[r];  
                continue;  
            }  
              
            if (j == 1 || j == 2 || j == 3 || j == 5 || j == 6 || j == 7 || j == 9)  
            {  
                if (r != 0)  
                    str = zh[r] + unit[j] + str;  
                else if (l != 0)  
                    str = zh[r] + str;  
                continue;  
            }  
              
            if (j == 4 || j == 8)  
            {  
                str =  unit[j] + str;  
                if ((l != 0 && r == 0) || r != 0)  
                    str = zh[r] + str;  
                continue;  
            }  
        }  
        return str;  
    } 
	
	public static int chineseNumber2Int(String chineseNumber){
		Matcher m = Pattern.compile("^\\d+$").matcher(chineseNumber);
		if(m.find()){
			return Integer.valueOf(chineseNumber);
		}
		int result = 0;
		int temp = 1;//存放一个单位的数字如：十万
		int count = 0;//判断是否有chArr
		char[] cnArr = new char[]{'一','二','三','四','五','六','七','八','九'};
		char[] chArr = new char[]{'十','百','千','万','亿'};
		for (int i = 0; i < chineseNumber.length(); i++) {
			boolean b = true;//判断是否是chArr
			char c = chineseNumber.charAt(i);
			for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
				if (c == cnArr[j]) {
					if(0 != count){//添加下一个单位之前，先把上一个单位值添加到结果中
						result += temp;
						temp = 1;
						count = 0;
					}
					// 下标+1，就是对应的值
					temp = j + 1;
					b = false;
					break;
				}
			}
			if(b){//单位{'十','百','千','万','亿'}
				for (int j = 0; j < chArr.length; j++) {
					if (c == chArr[j]) {
						switch (j) {
						case 0:
							temp *= 10;
							break;
						case 1:
							temp *= 100;
							break;
						case 2:
							temp *= 1000;
							break;
						case 3:
							temp *= 10000;
							break;
						case 4:
							temp *= 100000000;
							break;
						default:
							break;
						}
						count++;
					}
				}
			}
			if (i == chineseNumber.length() - 1) {//遍历到最后一个字符
				result += temp;
			}
		}
		return result;
	}
	
	public static String encode(String str, String charset) {
		StringBuffer b = new StringBuffer();
		try {
			String zhPattern = "[\u4e00-\u9fa5]+|[\\[\\]]|\\s";
			Pattern p = Pattern.compile(zhPattern);
			Matcher m = p.matcher(str);
			while (m.find()) {
				m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
			}
			m.appendTail(b);
		} catch (Exception e) {
			LOG.error("",e);
		}
		return b.toString();
	}
	
	public static boolean containsOne(List<String> all, List<String> sub){
		for(String s : sub){
			if(all.contains(s)){
				return true;
			}
		}
		return false;
	}
	
	public static String getHostUrl(String host, String uri){
		if(!uri.startsWith("http://") && !uri.startsWith("https://")){
			if(!uri.startsWith("/")){
				uri = "/"+uri;
			}
			uri = host+uri;
		}
		return uri;
	}
	
	//base64编码
	public static String base64(String str, String charset) throws UnsupportedEncodingException{
		byte[] data = str.getBytes(charset);
		
		char[] base64EncodeChars = new char[] { 
	        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 
	        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 
	        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 
	        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 
	        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
	        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 
	        'w', 'x', 'y', 'z', '0', '1', '2', '3', 
	        '4', '5', '6', '7', '8', '9', '+', '/' }; 
		
        StringBuffer sb = new StringBuffer(); 
        int len = data.length; 
        int i = 0; 
        int b1, b2, b3; 
        while (i < len) { 
            b1 = data[i++] & 0xff; 
            if (i == len) 
            { 
                sb.append(base64EncodeChars[b1 >>> 2]); 
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]); 
                sb.append("=="); 
                break; 
            } 
            b2 = data[i++] & 0xff; 
            if (i == len) 
            { 
                sb.append(base64EncodeChars[b1 >>> 2]); 
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]); 
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]); 
                sb.append("="); 
                break; 
            } 
            b3 = data[i++] & 0xff; 
            sb.append(base64EncodeChars[b1 >>> 2]); 
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]); 
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]); 
            sb.append(base64EncodeChars[b3 & 0x3f]); 
        } 
        String encoded = sb.toString(); 
        return encoded;    
	}
}

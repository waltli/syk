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
import com.sbolo.syk.common.constants.MovieQualityEnum;
import com.sbolo.syk.common.constants.MovieResolutionConstant;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.constants.TimeDirectEnum;
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
	    byte[] bytes = null;
	    ByteArrayOutputStream bo = null;
	    ObjectOutputStream oo = null;
	    try {
	        // object to bytearray  
	        bo = new ByteArrayOutputStream();  
	        oo = new ObjectOutputStream(bo);  
	        oo.writeObject(obj);
	        bytes = bo.toByteArray();
	    }finally {
	    	if(bo != null){
				bo.close();
	    	}
	    	if(oo !=null){
	    		oo.close(); 
	    	}
		}
	    return bytes;  
	}
	
	public static Object byteToObject(byte[] bytes) {
		Object obj = null;
		try {
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);
	
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
	
	public static void uploadPic(InputStream picStream, String targetPathStr, String fileName, String suffix, int width, int height) throws IOException{
		Image srcImg = ImageIO.read(picStream);
		
		Integer widthCopy = width;
		Integer heightCopy = height;
		
//		int oldWidth = srcImg.getWidth(null);  
//        int oldHeight = srcImg.getHeight(null);
//        
//        BigDecimal scaleBig = getImageScale(oldHeight, oldWidth, heightCopy, widthCopy);
//        
//        widthCopy = new BigDecimal(oldWidth).multiply(scaleBig).intValue();
//    	heightCopy = new BigDecimal(oldHeight).multiply(scaleBig).intValue();
    	
    	File targetPath = new File(targetPathStr);
		if(!targetPath.exists()){
			targetPath.mkdirs();
		}
		
//        Image scaledImage = srcImg.getScaledInstance(widthCopy, heightCopy, Image.SCALE_SMOOTH);
        BufferedImage buffImg = new BufferedImage(widthCopy, heightCopy, BufferedImage.TYPE_INT_RGB);
        buffImg.getGraphics().drawImage(srcImg , 0, 0, null);
        ImageIO.write(buffImg, suffix, new File(targetPathStr+"/"+fileName));
	}
	
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
	
	private static BigDecimal getImageScale(int oldHeight, int oldWidth, int height, int width){
		BigDecimal oldHeightBig = new BigDecimal(oldHeight);
        BigDecimal oldWidthBig = new BigDecimal(oldWidth);
        BigDecimal heightBig = new BigDecimal(height);
        BigDecimal widthBig = new BigDecimal(width);
        BigDecimal scaleBig = new BigDecimal(1);
        
        if(oldHeight > height && oldWidth > width){
        	Integer heightDiff = oldHeight-height;
        	Integer widthDiff = oldWidth-width;
        	if(heightDiff > widthDiff){
	        	scaleBig = heightBig.divide(oldHeightBig, 2, BigDecimal.ROUND_HALF_DOWN);
        	}else {
	        	scaleBig = widthBig.divide(oldWidthBig, 2, BigDecimal.ROUND_HALF_DOWN);
        	}
        }else if(oldHeight > height){
        	scaleBig = heightBig.divide(oldHeightBig, 2, BigDecimal.ROUND_HALF_DOWN);
        }else if(oldWidth > width){
        	scaleBig = widthBig.divide(oldWidthBig, 2, BigDecimal.ROUND_HALF_DOWN);
        }
        return scaleBig;
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
	
	
	 /**
	 * 向指定 URL发送POST方法的请求     
	 * @param url 发送请求的 URL    
	 * @param params 请求的参数集合     
	 * @return 远程资源的响应结果
	 */
	//@SuppressWarnings("unused")
	public static String sendPost(String url, Map<String, String> params)  throws Exception{
        OutputStreamWriter out = null;
        BufferedReader in = null;        
        StringBuilder result = new StringBuilder(); 
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn =(HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
           
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数            
            if (params != null) {
        		StringBuilder param = new StringBuilder(); 
	          	for (Map.Entry<String, String> entry : params.entrySet()) {
	        	  if(param.length()>0){
	        		  param.append("&");
	        	  }	        	  
	        	  param.append(entry.getKey());
	        	  param.append("=");
	        	  param.append(entry.getValue());		        	  
	          }
	          out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            // 断开连接
            conn.disconnect();
        }/* catch (Exception e) {            
            e.printStackTrace();
        }*/
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result.toString();
    }
	
	 /**
	 * 需要认证的POST方法的请求     
	 * @param url 发送请求的 URL    
	 * @param params 请求的参数集合     
	 * @return 远程资源的响应结果
	 */
	@SuppressWarnings("unused")
	public static String sendPost2(String url, Map<String, Object> params,String authorization)  throws Exception{
       OutputStreamWriter out = null;
       BufferedReader in = null;
       HttpURLConnection conn = null;
       StringBuilder result = new StringBuilder(); 
       try {
           URL realUrl = new URL(url.trim());
           conn =(HttpURLConnection) realUrl.openConnection();
           // 发送POST请求必须设置如下两行
           conn.setDoInput(true);
           conn.setDoOutput(true);
           int code = conn.getResponseCode();
           // POST方法
           conn.setRequestMethod("POST");
           // 设置通用的请求属性
           conn.setRequestProperty("accept", "*/*");
           conn.setRequestProperty("connection", "Keep-Alive");
           //认证
           if(authorization != null){
           	conn.setRequestProperty("Authorization", authorization);
           }
           conn.setRequestProperty("user-agent",
                   "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
           conn.setRequestProperty("Content-Type", "application/json");
           conn.connect();
           // 获取URLConnection对象对应的输出流
           out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
           // 发送请求参数            
           if (params != null) {
       		String jsonString = JSON.toJSONString(params);
       		out.write(jsonString);
           }
           // flush输出流的缓冲
           out.flush();
           // 定义BufferedReader输入流来读取URL的响应
           
           InputStream inputStream = conn.getInputStream();
           String str = "";
           in = new BufferedReader(
                   new InputStreamReader(inputStream, "UTF-8"));
           String line;
           while ((line = in.readLine()) != null) {
               result.append(line);
           }
           // 断开连接
           conn.disconnect();
       } /*catch (Exception e) {
           e.printStackTrace();
       }*/
       //使用finally块来关闭输出流、输入流
       finally{
           try{
               if(out!=null){
                   out.close();
               }
               if(in!=null){
                   in.close();
               }
               if(conn != null){
            	   conn.disconnect();
               }
           }
           catch(IOException ex){
               ex.printStackTrace();
           }
       }
       String resultStr = result.toString();
       return resultStr;
   }
	
//	public static String sendPost(String url, Map<String, Object> params,String authorization) {
//		OutputStreamWriter out = null;
//      BufferedReader in = null;        
//      StringBuilder result = new StringBuilder(); 
//      HttpURLConnection conn = null;
//      try {
//          URL realUrl = new URL(url.trim());
//          conn =(HttpURLConnection) realUrl.openConnection();
//          // 发送POST请求必须设置如下两行
//          conn.setDoOutput(true);
//          conn.setDoInput(true);
//          // POST方法
//          conn.setRequestMethod("POST");
//          // 设置通用的请求属性
//          conn.setRequestProperty("accept", "*/*");
//          conn.setRequestProperty("connection", "Keep-Alive");
//          //认证
//          if(authorization != null){
//          	conn.setRequestProperty("Authorization", authorization);
//          }
//          conn.setRequestProperty("user-agent",
//                  "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//          conn.setRequestProperty("Content-Type", "application/json");
//          conn.connect();
//          // 获取URLConnection对象对应的输出流
//          out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
//          // 发送请求参数            
//          if (params != null) {
//      		String jsonString = JSON.toJSONString(params);
//      		out.write(jsonString);
//          }
//          // flush输出流的缓冲
//          out.flush();
//          // 定义BufferedReader输入流来读取URL的响应
//          
//          InputStream inputStream = conn.getInputStream();
//          String str = "";
//          in = new BufferedReader(
//                  new InputStreamReader(inputStream, "UTF-8"));
//          String line;
//          while ((line = in.readLine()) != null) {
//              result.append(line);
//          }
//          // 断开连接
//          //conn.disconnect();
//      } /*catch (Exception e) {            
//          e.printStackTrace();
//      }*/
//      //使用finally块来关闭输出流、输入流
//      finally{
//          try{
//              if(out!=null){
//                  out.close();
//              }
//              if(in!=null){
//                  in.close();
//              }
//              if(conn !=null){
//              	conn.disconnect();
//              }
//          }
//          catch(IOException ex){
//              ex.printStackTrace();
//          }
//      }
//      String resultStr = result.toString();
//      return resultStr;
//	}
	
	
	
	@SuppressWarnings("unused")
	public static String sendPost(String url, Map<String, Object> params,String authorization) throws Exception{
        OutputStreamWriter out = null;
        BufferedReader in = null;        
        StringBuilder result = new StringBuilder(); 
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url.trim());
            conn =(HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            //认证
            if(authorization != null){
            	conn.setRequestProperty("Authorization", authorization);
            }
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数            
            if (params != null) {
        		String jsonString = JSON.toJSONString(params);
        		out.write(jsonString);
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            int responseCode = conn.getResponseCode();
            if(responseCode == 409){
            	LOG.error("---------------------请求activiti响应码为409----------------------");
            }
            InputStream inputStream = conn.getInputStream();
            String str = "";
            in = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            // 断开连接
            //conn.disconnect();
        } /*catch (Exception e) {            
            e.printStackTrace();
        }*/
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
                if(conn !=null){
                	conn.disconnect();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        String resultStr = result.toString();
        return resultStr;
    }
	
	@SuppressWarnings("unused")
	public static String sendPost2(String url, String jsonString,String authorization)  throws Exception{
        OutputStreamWriter out = null;
        BufferedReader in = null;        
        StringBuilder result = new StringBuilder(); 
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url.trim());
            conn =(HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            //认证
            if(authorization != null){
            	conn.setRequestProperty("Authorization", authorization);
            }
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数            
            if (jsonString != null && !"".equals(jsonString)) {
        		out.write(jsonString);
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            
            InputStream inputStream = conn.getInputStream();
            String str = "";
            in = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            // 断开连接
            //conn.disconnect();
        } /*catch (Exception e) {            
            e.printStackTrace();
        }*/
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
                if(conn !=null){
                	conn.disconnect();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        String resultStr = result.toString();
        return resultStr;
    }
	
	@SuppressWarnings("unused")
	public static String sendPut(String url, Map<String, Object> params,String authorization)  throws Exception{
        OutputStreamWriter out = null;
        BufferedReader in = null;        
        HttpURLConnection conn = null;
        StringBuilder result = new StringBuilder(); 
        try {
            URL realUrl = new URL(url.trim());
            conn =(HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("PUT");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            //认证
            if(authorization != null){
            	conn.setRequestProperty("Authorization", authorization);
            }
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数            
            if (params != null) {
        		String jsonString = JSON.toJSONString(params);
        		out.write(jsonString);
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            
            InputStream inputStream = conn.getInputStream();
            String str = "";
            in = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            // 断开连接
            //conn.disconnect();
        } /*catch (Exception e) {            
            e.printStackTrace();
        }*/
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
                if(conn != null){
                	conn.disconnect();
                }
            }
            catch(IOException ex){
            	 if(conn != null){
                 	conn.disconnect();
                 }
                ex.printStackTrace();
            }
        }
        String resultStr = result.toString();
        return resultStr;
    }
	
	/**
    * 从网络获取json数据,(String byte[})
    * @param path
    * @return
    */
   public static String sendGet(String path,String authorization)  throws Exception{
   	InputStream is = null;
   	ByteArrayOutputStream baos = null;
   	HttpURLConnection urlConnection =null;
       try {
           URL url = new URL(path.trim());
           //打开连接
           urlConnection = (HttpURLConnection) url.openConnection();
           //认证
           if(authorization != null){
           	urlConnection.setRequestProperty("Authorization", authorization);
           }
           int status = urlConnection.getResponseCode();
           if(200 == status){
               //得到输入流
               is =urlConnection.getInputStream();
               baos = new ByteArrayOutputStream();
               byte[] buffer = new byte[1024];
               int len = 0;
               while(-1 != (len = is.read(buffer))){
                   baos.write(buffer,0,len);
                   baos.flush();
               }
               //urlConnection.disconnect();
               return baos.toString("utf-8");
           }
       }  /*catch (IOException e) {
           e.printStackTrace();
	    }*/  finally{
	        try{
	            if(baos!=null){
	            	baos.close();
	            }
	            if(is!=null){
	            	is.close();
	            }
	            if(urlConnection != null){
	            	urlConnection.disconnect();
	            }
	        }
	        catch(IOException ex){
	        	 if(urlConnection != null){
	        		 urlConnection.disconnect();
	                }
	            ex.printStackTrace();
	        }
	    }

       return null;
   }

	public static String sendDelete(String httpUrl, String authorization)  throws Exception{
		InputStream is = null;
	   	ByteArrayOutputStream baos = null;
	   	HttpURLConnection urlConnection = null;
	       try {
	           URL url = new URL(httpUrl.trim());
	           //打开连接
	           urlConnection = (HttpURLConnection) url.openConnection();
	           //认证
	           if(authorization != null){
	           	urlConnection.setRequestProperty("Authorization", authorization);
	           }
	           urlConnection.setRequestMethod("DELETE");
	           urlConnection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	           int status = urlConnection.getResponseCode();
	           if(200 == status){
	               //得到输入流
	               is =urlConnection.getInputStream();
	               baos = new ByteArrayOutputStream();
	               byte[] buffer = new byte[1024];
	               int len = 0;
	               while(-1 != (len = is.read(buffer))){
	                   baos.write(buffer,0,len);
	                   baos.flush();
	               }
	               urlConnection.disconnect();
	               return baos.toString("utf-8");
	           }
	       }  /*catch (IOException e) {
	           e.printStackTrace();
		    }*/  finally{
		        try{
		            if(baos!=null){
		            	baos.close();
		            }
		            if(is!=null){
		            	is.close();
		            }
		            if(urlConnection != null){
		            	urlConnection.disconnect();
	                }
		        }
		        catch(IOException ex){
		            ex.printStackTrace();
		        }
		    }

		return null;
	}

	public static String sendJsonPost(String httpUrl, String jsonString, String authorization) throws Exception{
		 OutputStreamWriter out = null;
	        BufferedReader in = null;        
	        StringBuilder result = new StringBuilder(); 
	        HttpURLConnection conn = null;
	        try {
	            URL realUrl = new URL(httpUrl.trim());
	            conn =(HttpURLConnection) realUrl.openConnection();
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // POST方法
	            conn.setRequestMethod("POST");
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            //认证
	            if(authorization != null){
	            	conn.setRequestProperty("Authorization", authorization);
	            }
	            conn.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            conn.setRequestProperty("Content-Type", "application/json");
	            conn.connect();
	            // 获取URLConnection对象对应的输出流
	            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
	            // 发送请求参数            
	            if (jsonString != null && !"".equals(jsonString)) {
	        		out.write(jsonString);
	            }
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            
	            InputStream inputStream = conn.getInputStream();
	            String str = "";
	            in = new BufferedReader(
	                    new InputStreamReader(inputStream, "UTF-8"));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result.append(line);
	            }
	            // 断开连接
	            //conn.disconnect();
	        } /*catch (Exception e) {
	        	e.printStackTrace();
	        }*/
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	                if(conn != null){
	                	conn.disconnect();
	                }
	            }
	            catch(IOException ex){
	            	 if(conn != null){
	                 	conn.disconnect();
	                 }
	                ex.printStackTrace();
	            }
	        }
	        String resultStr = result.toString();
	        return resultStr;
	}
	
	@SuppressWarnings("unused")
	public static String sendPutJson(String url, String jsonString,String authorization) throws Exception{
        OutputStreamWriter out = null;
        BufferedReader in = null;        
        HttpURLConnection conn = null;
        StringBuilder result = new StringBuilder(); 
        try {
            URL realUrl = new URL(url.trim());
            conn =(HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("PUT");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            //认证
            if(authorization != null){
            	conn.setRequestProperty("Authorization", authorization);
            }
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数            
            if (jsonString != null && !"".equals(jsonString)) {
        		out.write(jsonString);
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            
            InputStream inputStream = conn.getInputStream();
            String str = "";
            in = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            // 断开连接
            //conn.disconnect();
        } /*catch (Exception e) {            
            e.printStackTrace();
        }*/
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
                if(conn != null){
                	conn.disconnect();
                }
            }
            catch(IOException ex){
            	 if(conn != null){
                 	conn.disconnect();
                 }
                ex.printStackTrace();
            }
        }
        String resultStr = result.toString();
        return resultStr;
    }
}

package com.sbolo.syk.common.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.http.HttpUtils;

public class FileUtils {
	private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
	
	public static void saveFile(File file, String targetPathStr, String fileName, String suffix) throws IOException {
		try (InputStream input = new FileInputStream(file);) {
    		saveFile(IOUtils.toByteArray(input), targetPathStr, fileName, suffix);
		}
    }
	
	public static void saveFile(byte[] content, String targetDir, String fileName, String suffix) throws IOException{
		File file = new File(targetDir);
		if(!file.exists()) {
			file.mkdirs();
		}
		//创建一个文件输出流
		try (FileOutputStream output = new FileOutputStream(targetDir + "/" + fileName + "."+suffix);) {
			IOUtils.write(content, output);
		}
	}
	
	public static void deleteFile(String filePath) {
    	File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
    }
	
	public static byte[] getBytes(File file) throws Exception {
		InputStream is = null;
		try {
			if(!file.exists()) {
				throw new IOException("The file: "+file.getPath() +" is not exists!");
			}
			is = new FileInputStream(file);
			return IOUtils.toByteArray(is);
		} finally {
			if(is != null) {
				is.close();
				is = null;
			}
		}
	}
	
	public static String getTypeDir(String type) {
		String subDir = null;
		if("icon".equals(type)) {
			subDir = "/icon";
		}else if("poster".equals(type)) {
			subDir = "/poster";
		}else if("photo".equals(type)) {
			subDir = "/photo";
		}else if("shot".equals(type)) {
			subDir = "/shot";
		}else if("torrent".equals(type)) {
			subDir = "/torrent";
		}else if("date".equals(type)) {
			subDir = "/"+DateUtil.date2Str(new Date(), "yyyyMM");
		}else {
			subDir = "/other";
		}
		return subDir;
	}
	
	/**
	 * 按比例修正图片大小
	 * @param content
	 * @param newWidth
	 * @param newHeight
	 * @param suffix
	 * @return
	 * @throws IOException
	 */
	public static byte[] imageFix(byte[] content, int newWidth, int newHeight, String suffix) throws IOException{
		ByteArrayInputStream byteArrayInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		Graphics2D graphics = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(content);
			byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.setUseCache(false);
			Image srcImg = ImageIO.read(byteArrayInputStream);
			
			int imgWidth = srcImg.getWidth(null);  
	        int imgHeight = srcImg.getHeight(null);
	        
	        //获取比例
	        BigDecimal scaleBig = getImageScale(imgWidth, imgHeight, newWidth, newHeight);
	        newWidth = new BigDecimal(imgWidth).multiply(scaleBig).intValue();
	        newHeight = new BigDecimal(imgHeight).multiply(scaleBig).intValue();
			
	        Image scaledImage = srcImg.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	        BufferedImage bufImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
	        graphics = bufImg.createGraphics();
	        graphics.drawImage(scaledImage , 0, 0, null);
	        
	        ImageIO.write(bufImg, suffix, byteArrayOutputStream);
	        return byteArrayOutputStream.toByteArray();
		} finally {
			if(graphics != null) {
				graphics.dispose();
			}
			if(byteArrayOutputStream != null) {
				byteArrayOutputStream.close();
			}
			if(byteArrayInputStream != null) {
				byteArrayInputStream.close();
			}
		}
	}
	
	/**
	 * 给图片右下角添加水印
	 * @param content
	 * @param text
	 * @param font
	 * @param color
	 * @param background
	 * @param x
	 * @param y
	 * @param suffix
	 * @return
	 * @throws IOException
	 */
	public static byte[] imageMark(byte[] content, String suffix) throws IOException {
		ByteArrayInputStream byteArrayInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		Graphics2D graphics2D = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(content);
			byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.setUseCache(false);
			Image srcImg = ImageIO.read(byteArrayInputStream);
			
			int imgWidth = srcImg.getWidth(null);  
	        int imgHeight = srcImg.getHeight(null);
	        
	        //创建新的画板
			BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
			graphics2D = bufImg.createGraphics();
			// 去除锯齿(当设置的字体过大的时候,会出现锯齿)
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			//将原图画到新的画板上
			graphics2D.drawImage(srcImg, 0, 0, imgWidth, imgHeight, null);
			
			//水印文字
	        String word = "cyvdo.com";
	        //創建字體
	        Font font = new Font("微软雅黑", Font.BOLD, 20);
			
			//获取文字长宽
			FontRenderContext context = graphics2D.getFontRenderContext();     
	        Rectangle2D bounds = font.getStringBounds(word, context);
	        int wordWidth = (int) bounds.getWidth();
	        int wordHeight = (int) bounds.getHeight();
			
	        //設置矩形x,y軸坐標
	        int rectX = imgWidth-wordWidth-1;
	        int rectY = imgHeight-wordHeight-1;
	        //設置畫筆顏色
	        graphics2D.setColor(Color.black);
	        //使用畫筆顏色填充一塊矩形
			graphics2D.fillRect(rectX,rectY, wordWidth, wordHeight);
			
			//設置文字x,y軸坐標
			int wordX = imgWidth-wordWidth-2;
			int wordY = imgHeight-8;
			//设置畫筆顏色
			graphics2D.setColor(Color.white);
			//设置畫筆字體
			graphics2D.setFont(font);
	        //使用畫筆將文字寫入圖片指定位置
			graphics2D.drawString(word,wordX,wordY);
			
			ImageIO.write(bufImg, suffix, byteArrayOutputStream);
	        return byteArrayOutputStream.toByteArray();
		} finally {
			if(graphics2D != null) {
				graphics2D.dispose();
			}
			if(byteArrayOutputStream != null) {
				byteArrayOutputStream.flush();
				byteArrayOutputStream.close();
			}
			if(byteArrayInputStream != null) {
				byteArrayInputStream.close();
			}
		}
	}
	
	/**
	 * 将文件大小计算至最大单位
	 * 
	 * @param source
	 * @return
	 */
	public static String unitUp(String source){
        if(source != null){
        	BigDecimal val = new BigDecimal(source);
        	BigDecimal s = new BigDecimal(1024);
        	String unit = "B";
        	if(val.compareTo(s)>0){
        		val = val.divide(s);
        		unit = "KB";
        	}
        	if(val.compareTo(s)>0){
        		val = val.divide(s);
        		unit = "MB";
        	}
        	if(val.compareTo(s)>0){
        		val = val.divide(s);
        		unit = "GB";
        	}
        	if(val.compareTo(s)>0){
        		val = val.divide(s);
        		unit = "TB";
        	}
        	return val.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+unit;
        }else {
        	return null;
        }
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
	
	public static Set<String> txt2Set(InputStream txtStream) throws Exception{
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(txtStream, "utf-8");
			br = new BufferedReader(isr);
			
			String lineTxt = null;
			Set<String> set = new HashSet<>();
			while ((lineTxt = br.readLine()) != null) {
				set.add(lineTxt);
		    }
			return set;
		} finally {
			if(br != null) {
				br.close();
			}
			
			if(isr != null) {
				isr.close();
			}
		}
	}
}

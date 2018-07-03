package com.sbolo.syk.common.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	        // 加水印
			BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
			graphics2D = bufImg.createGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 去除锯齿(当设置的字体过大的时候,会出现锯齿)
			graphics2D.drawImage(srcImg, 0, 0, imgWidth, imgHeight, null);
			graphics2D.setColor(Color.PINK);
			graphics2D.setFont(graphics2D.getFont().deriveFont(34f));
			graphics2D.fill(graphics2D.getFontMetrics().getStringBounds("chanying.cc",graphics2D) );
			graphics2D.setColor(Color.BLUE);
			graphics2D.drawString("chanying.cc",imgWidth-100,imgHeight-50);
//			graphics.setColor(Color.blue);
//			graphics.setFont(new Font("微软雅黑", Font.BOLD, 50)); // 设置字体
//			graphics.drawString("chanying.cc", imgWidth - 145, imgHeight); // 设置ps上去的文字的坐标位置和文字的内容
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
}

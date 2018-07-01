package com.sbolo.syk.common.tools;

import java.awt.Image;
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
	
	public static byte[] imageFix(byte[] content, int newWidth, int newHeight, String suffix) throws IOException{
		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();) {
			ImageIO.setUseCache(false);
			Image srcImg = ImageIO.read(byteArrayInputStream);
			Integer widthCopy = newWidth;
			Integer heightCopy = newHeight;
			
			int oldWidth = srcImg.getWidth(null);  
	        int oldHeight = srcImg.getHeight(null);
	        
	        BigDecimal scaleBig = getImageScale(oldHeight, oldWidth, heightCopy, widthCopy);
	        
	        widthCopy = new BigDecimal(oldWidth).multiply(scaleBig).intValue();
	    	heightCopy = new BigDecimal(oldHeight).multiply(scaleBig).intValue();
			
	        Image scaledImage = srcImg.getScaledInstance(widthCopy, heightCopy, Image.SCALE_SMOOTH);
	        BufferedImage buffImg = new BufferedImage(widthCopy, heightCopy, BufferedImage.TYPE_INT_RGB);
	        buffImg.getGraphics().drawImage(scaledImage , 0, 0, null);
	        
	        ImageIO.write(buffImg, suffix, byteArrayOutputStream);
	        return byteArrayOutputStream.toByteArray();
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

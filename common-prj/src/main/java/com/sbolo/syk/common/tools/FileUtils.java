package com.sbolo.syk.common.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.exception.BusinessException;

public class FileUtils {
	private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
	
	public static String saveFile(File file, String targetPathStr, String fileName) throws IOException {
    	InputStream input = new FileInputStream(file);
    	return saveFile(input, targetPathStr, fileName);
    }
    
    
    public static String saveFile(InputStream input, String targetPathStr, String fileName) throws IOException {
    	byte[] content = IOUtils.toByteArray(input);
    	return saveFile(content, targetPathStr, fileName);
    }
	
	public static String saveFile(byte[] content, String targetPathStr, String fileName) throws IOException{
		if(StringUtils.isBlank(fileName)){
			throw new BusinessException("The fileName is null");
		}
		File targetPath = new File(targetPathStr);
		if(!targetPath.exists()){
			targetPath.mkdirs();
		}
		RandomAccessFile raf = null;
		File file = new File(targetPathStr+"/"+fileName);
		try {
			raf = new RandomAccessFile(file,"rw");
			raf.write(content);
		}catch(IOException e){
			if(file.exists()){
				file.delete();
			}
			throw e;
		} finally {
			if(raf != null){
				raf.close();
				raf = null;
			}
		}
		return fileName;
	}
	
	public static String parseFileUri(String uri) {
		String serverName = Utils.getServerName();
		FileTypeVO fileType = getFileType(uri);
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ConfigUtil.getPropertyValue("fs.formal.mapping")).append("/")
			.append(serverName).append("/")
			.append(fileType.getFileType()).append("/")
			.append(uri);
		return sBuffer.toString();
	}
	
//	public static String parseEditorFileUri(String content){
//		Pattern p = Pattern.compile("(?i)<\\s*img.*?src\\s*=\\s*\"(.*?)\"");
//		Matcher m = p.matcher(content);
//		while (m.find()) {
//			String uri = m.group(1);
//			String formalUri = parseFileUri(uri);
//			if(StringUtils.isBlank(formalUri)){
//				continue;
//			}
//			content = content.replace(uri, formalUri);
//		}
//		return content;
//	}
//	
//	public static String copyEditorFile2Formal(String content) throws IOException {
//		Pattern p = Pattern.compile("(?i)<\\s*img.*?src\\s*=\\s*\"(.*?)\"");
//		Matcher m = p.matcher(content);
//		while (m.find()) {
//			String url = m.group(1);
//			String formalUri = copyFile2Formal(url);
//			if(StringUtils.isBlank(formalUri)){
//				continue;
//			}
//			content = content.replace(url, formalUri);
//		}
//		return content;
//	}
	
	public static String copyFile2Formal(String uri) throws IOException{
		if(StringUtils.isBlank(uri)){
			return null;
		}
		String tempMapping = ConfigUtil.getPropertyValue("fs.temp.mapping");
		if(!uri.contains(tempMapping)){
			return uri;
		}
		String serverName = Utils.getServerName();
		String fileName = uri.substring(uri.lastIndexOf("/")+1);
		String tempDir = ConfigUtil.getPropertyValue("fs.temp.dir");
		LOG.info(tempDir+"+++++");
		
		File file = new File(tempDir+"/"+fileName);
		if(!file.exists()){
			throw new BusinessException("copyFile2Formal:"+uri+". 文件不存在！");
		}
		FileTypeVO fileType = getFileType(fileName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String month = sdf.format(new Date());
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(ConfigUtil.getPropertyValue("fs.formal.dir")).append("/")
			.append(serverName).append("/")
			.append(fileType.getFileType()).append("/")
			.append(month);
		saveFile(file, sBuffer.toString(), fileName);
		return month+"/"+fileName;
	}
	
	
	public static FileTypeVO getFileType(String uri) {
		String fileType = uri;
		if(uri.lastIndexOf(".") > -1){
			fileType = uri.substring(uri.lastIndexOf(".")+1);
		}
		
		Pattern p = Pattern.compile(RegexConstant.pic);
		Matcher m = p.matcher(fileType);
		if(m.find()){
			return new FileTypeVO("pic", CommonConstants.PIC_S);
		}
		
		p = Pattern.compile(RegexConstant.doc);
		m = p.matcher(fileType);
		if(m.find()){
			return new FileTypeVO(fileType, CommonConstants.DOC_S);
		}
		
		return new FileTypeVO("oth", CommonConstants.OTH_S);
	}
	
	
	public static class FileTypeVO{
		private String fileType;
		private String fileFlag;
		
		FileTypeVO(String fileType, String fileFlag){
			this.fileType = fileType;
			this.fileFlag = fileFlag;
		}
		
		public String getFileType() {
			return fileType;
		}
		public void setFileType(String fileType) {
			this.fileType = fileType;
		}
		public String getFileFlag() {
			return fileFlag;
		}
		public void setFileFlag(String fileFlag) {
			this.fileFlag = fileFlag;
		}
	}
	
}

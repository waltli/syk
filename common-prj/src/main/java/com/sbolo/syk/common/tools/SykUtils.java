package com.sbolo.syk.common.tools;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.sbolo.syk.common.http.HttpUtils;

public class SykUtils {
	/**
	 * 下载文件并返回uri
	 * @param url
	 * @param targetDir
	 * @param fileName
	 * @param suffix
	 * @return
	 * @throws Exception
	 */
	public static String downTorrentWithDist(String url, byte[] bytes, String targetDir, String fileName, String suffix) throws Exception {
		if(bytes == null) {
			bytes = HttpUtils.getBytes(url);
		}
		String uri = BucketUtils.upload2Dir(bytes, targetDir, fileName, suffix);
		return uri;
	}
	
	/**
	 * 下载图片并修正图片大小，返回uri
	 * @param url
	 * @param targetDir
	 * @param fileName
	 * @param suffix
	 * @param fixWidth
	 * @param fixHeight
	 * @return
	 * @throws Exception
	 */
	public static String downPicAndFixWithDist(String url, String targetDir, String fileName, String suffix, int fixWidth, int fixHeight) throws Exception {
		byte[] imageBytes = HttpUtils.getBytes(url);
		imageBytes = GrapicmagickUtils.descale(imageBytes, fixWidth, fixHeight);
		String uri = BucketUtils.upload2Dir(imageBytes, targetDir, fileName, suffix);
		return uri;
	}
	
	/**
	 * 下载图片并修正图片大小以及添加水印，返回uri
	 * @param url
	 * @param targetDir
	 * @param fileName
	 * @param suffix
	 * @param fixWidth
	 * @param fixHeight
	 * @return
	 * @throws Exception
	 */
	public static String downPicAndFixMarkWithDist(String url, String targetDir, String fileName, String suffix, int fixWidth, int fixHeight) throws Exception {
		InputStream markStream = null;
		try {
			byte[] imageBytes = HttpUtils.getBytes(url);
			imageBytes = GrapicmagickUtils.descale(imageBytes, fixWidth, fixHeight);
			markStream = SykUtils.class.getResourceAsStream("/img/mark.png");
			if(markStream != null) {
				byte[] markBytes = IOUtils.toByteArray(markStream);
				imageBytes = GrapicmagickUtils.watermark(imageBytes, markBytes);
			}
			String uri = BucketUtils.upload2Dir(imageBytes, targetDir, fileName, suffix);
			return uri;
		} finally {
			if(markStream != null) {
				markStream.close();
				markStream = null;
			}
		}
	}
}

package com.sbolo.syk.common.tools;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.DeleteObjectsRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.DeleteObjectsRequest.KeyVersion;
import com.qcloud.cos.region.Region;

public class BucketUtils {
	private static final Logger log = LoggerFactory.getLogger(BucketUtils.class);
	
	private static String bucketName;
	private static COSClient cosClient;
	private static Boolean hasOpened = false;
	private static AtomicInteger bucketAlive = new AtomicInteger();  //还在链接bucket的个数
	
	public static void openBucket() {
		if(!hasOpened) {
			String secretId = ConfigUtils.getPropertyValue("bucket.secretId");
			String secretKey = ConfigUtils.getPropertyValue("bucket.secretKey");
			String region = ConfigUtils.getPropertyValue("bucket.region");
			BucketUtils.bucketName = ConfigUtils.getPropertyValue("bucket.name");
			// 1 初始化用户身份信息(secretId, secretKey)
			COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
			// 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
			ClientConfig clientConfig = new ClientConfig(new Region(region));
			// 3 生成cos客户端
			cosClient = new COSClient(cred, clientConfig);
			hasOpened = true;
		}
	}
	
	public static void upload(byte[] bytes, String targetDir, String fileName, String suffix) throws Exception {
		bucketAlive.incrementAndGet();
		InputStream sbs = null;
		try {
			if(!hasOpened) {
				limitOpen();
			}
			sbs = new ByteArrayInputStream(bytes);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(bytes.length);
			String key = targetDir+"/"+fileName+"."+suffix;
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, sbs, metadata);
			cosClient.putObject(putObjectRequest);
		} finally {
			bucketAlive.decrementAndGet();
			if(sbs != null) {
				sbs.close();
			}
		}
		
	}
	
	public static void deletes(List<String> keys) throws Exception {
		bucketAlive.incrementAndGet();
		try {
			if(!hasOpened) {
				limitOpen();
			}
			List<KeyVersion> keyVers = new ArrayList<>();
			for(String key : keys) {
				if(StringUtils.isBlank(key)) {
					continue;
				}
				KeyVersion kv = new KeyVersion(key);
				keyVers.add(kv);
			}
			if(keyVers.size() > 0) {
				DeleteObjectsRequest d = new DeleteObjectsRequest(bucketName);
				d.setKeys(keyVers);
				cosClient.deleteObjects(d);
			}
			
		} finally {
			bucketAlive.decrementAndGet();
		}
	}
	
	public static void delete(String key) throws Exception {
		List<String> keys = new ArrayList<>();
		keys.add(key);
		deletes(keys);
	}
	
	public static void closeBucket() {
		if(hasOpened) {
			cosClient.shutdown();
			cosClient = null;
			hasOpened = false;
		}
	}
	
	public static boolean hasOpened() {
		return hasOpened;
	}
	
	public static synchronized void limitOpen() {
		if(!hasOpened) {
			openBucket();
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						do {
							Thread.sleep(30000);  //等待3分钟再关闭bucket
						} while (bucketAlive.get() > 0);  //3分钟后任然有bucket链接则继续等待
						closeBucket();
					} catch (InterruptedException e) {
						log.error("线程已被中断",e);
					}
				}
			});
			t.start();
		}
	}
}

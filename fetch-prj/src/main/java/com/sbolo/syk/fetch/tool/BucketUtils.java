package com.sbolo.syk.fetch.tool;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.DeleteObjectsRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.model.DeleteObjectsRequest.KeyVersion;
import com.qcloud.cos.model.DeleteObjectsResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import com.sbolo.syk.common.tools.ConfigUtil;

public class BucketUtils {
	
	private static String bucketName;
	private static COSClient cosClient;
	
	public static void openBucket(String bucketName) {
		if(cosClient == null) {
			String secretId = ConfigUtil.getPropertyValue("bucket.secretId");
			String secretKey = ConfigUtil.getPropertyValue("bucket.secretKey");
			String region = ConfigUtil.getPropertyValue("bucket.region");
			BucketUtils.bucketName = bucketName;
			// 1 初始化用户身份信息(secretId, secretKey)
			COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
			// 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
			ClientConfig clientConfig = new ClientConfig(new Region(region));
			// 3 生成cos客户端
			cosClient = new COSClient(cred, clientConfig);
		}
	}
	
	public static void upload(byte[] bytes, String targetDir, String fileName, String suffix) throws Exception {
		if(cosClient == null) {
			throw new Exception("cosClient has not be opened yet!");
		}
		InputStream sbs = new ByteArrayInputStream(bytes);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(bytes.length);
		String key = targetDir+"/"+fileName+"."+suffix;
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, sbs, metadata);
		PutObjectResult putResult = cosClient.putObject(putObjectRequest);
	}
	
	public static void deletes(List<String> uris) throws Exception {
		if(cosClient == null) {
			throw new Exception("cosClient has not be opened yet!");
		}
		List<KeyVersion> keyVers = new ArrayList<>();
		for(String uri : uris) {
			KeyVersion k = new KeyVersion(uri);
			keyVers.add(k);
		}
		DeleteObjectsRequest d = new DeleteObjectsRequest(bucketName);
		d.setKeys(keyVers);
		DeleteObjectsResult deleteResults = cosClient.deleteObjects(d);
	}
	
	public static void closeBucket() {
		if(cosClient != null) {
			cosClient.shutdown();
			cosClient = null;
		}
	}
	
	
	
}

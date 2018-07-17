package com.sbolo.syk.fetch.tool;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;

public class BucketUtils {
	
	private static COSClient cosClient;
	
	public static COSClient instance() {
		if(cosClient != null ) {
			return cosClient;
		}
		// 1 初始化用户身份信息(secretId, secretKey)
		COSCredentials cred = new BasicCOSCredentials("AKIDEPQGC0y7TTKBA4LD4mJfuyOjsrgQOhKm", "Ss9LNFvX1LUhsyulV6jR5e3HOTVLKof2");
		// 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
		ClientConfig clientConfig = new ClientConfig(new Region("ap-chengdu"));
		// 3 生成cos客户端
		cosClient = new COSClient(cred, clientConfig);
		return cosClient;
	}
	
	public static void shutdown() {
		cosClient.shutdown();
		cosClient = null;
	}
	
	
	
}

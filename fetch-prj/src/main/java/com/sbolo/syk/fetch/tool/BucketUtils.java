package com.sbolo.syk.fetch.tool;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import com.sbolo.syk.common.tools.ConfigUtil;

public class BucketUtils {
	
	public static COSClient getCOSClient() {
		String secretId = ConfigUtil.getPropertyValue("bucket.secretId");
		String secretKey = ConfigUtil.getPropertyValue("bucket.secretKey");
		String region = ConfigUtil.getPropertyValue("bucket.region");
		// 1 初始化用户身份信息(secretId, secretKey)
		COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
		// 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
		ClientConfig clientConfig = new ClientConfig(new Region(region));
		// 3 生成cos客户端
		COSClient cosClient = new COSClient(cred, clientConfig);
		return cosClient;
	}
}

package com.sbolo.syk.fetch.tool;

import java.io.File;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.tools.StringUtil;

public class Test {
	public static void main(String[] args) {
		COSClient cosClient = BucketUtils.instance();
		// bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
		String bucketName = "syk-1253786918";
		// 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
		// 大文件上传请参照 API 文档高级 API 上传
		File localFile = new File("C:\\Users\\YY\\Desktop\\123.png");
		// 指定要上传到 COS 上对象键
		// 对象键（Key）是对象在存储桶中的唯一标识。例如，在对象的访问域名 `bucket1-1250000000.cos.ap-guangzhou.myqcloud.com/doc1/pic1.jpg` 中，对象键为 doc1/pic1.jpg, 详情参考 [对象键](https://cloud.tencent.com/document/product/436/13324)
		String key = "test/"+StringUtil.getId(CommonConstants.pic_s)+".jpg";
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
		PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
		cosClient.shutdown();
		String key2 = "test/"+StringUtil.getId(CommonConstants.pic_s)+".jpg";
		PutObjectRequest putObjectRequest2 = new PutObjectRequest(bucketName, key2, localFile);
		cosClient.putObject(putObjectRequest);
	}
}

package com.sbolo.syk.fetch.tool;

import java.io.File;
import java.util.List;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectId;
import com.qcloud.cos.model.COSObjectIdBuilder;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.tools.MD5Utils;
import com.sbolo.syk.common.tools.StringUtil;

public class Test {
	public static void main(String[] args) {
		COSClient cosClient = BucketUtils.instance();
		// bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
		String bucketName = "syk-1253786918";
		// 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
		// 大文件上传请参照 API 文档高级 API 上传
		File localFile = new File("D:\\fs\\formal\\shot\\201807\\p174187555920.jpg");
		// 指定要上传到 COS 上对象键
		// 对象键（Key）是对象在存储桶中的唯一标识。例如，在对象的访问域名 `bucket1-1250000000.cos.ap-guangzhou.myqcloud.com/doc1/pic1.jpg` 中，对象键为 doc1/pic1.jpg, 详情参考 [对象键](https://cloud.tencent.com/document/product/436/13324)
		String key = "test1/"+StringUtil.getId(CommonConstants.pic_s)+".jpg";
		ObjectListing listObjects = cosClient.listObjects(bucketName, "test1/");
		List<COSObjectSummary> objectSummaries = listObjects.getObjectSummaries();
		for(COSObjectSummary os : objectSummaries) {
			String fileMD5 = MD5Utils.getFileMD5(localFile);
			String eTag = os.getETag();
			String key2 = os.getKey();
			System.out.println();
		}
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
		PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
		cosClient.shutdown();
	}
}

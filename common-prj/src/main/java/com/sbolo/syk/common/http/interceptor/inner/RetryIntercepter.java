package com.sbolo.syk.common.http.interceptor.inner;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 重试拦截器
 */
public class RetryIntercepter implements Interceptor {

	private static final Logger log = LoggerFactory.getLogger(RetryIntercepter.class);
	
	private int maxRetry;//最大重试次数

    public RetryIntercepter(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    @SuppressWarnings("resource")
	@Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        for(int retryNum=0;retryNum<=maxRetry;retryNum++){
        	try {
        		response = chain.proceed(request);
        		
        		//get the bytes once, and once submit a exception it will retry!
        		ResponseBody rb = response.body();
        		MediaType contentType = rb.contentType();
        		ResponseBody newRb = ResponseBody.create(contentType, rb.bytes());
        		response = response.newBuilder().body(newRb).build();
        		
        		if(retryNum>0){
        			log.error("retry success!");
        		}
        		break;
        	} catch (IOException e) {
        		if(retryNum < maxRetry){
        			log.error("retry {}/{}, address: {}, method: {}, exception: {}, message: {}", retryNum+1, maxRetry, request.url(), request.method(), e.getClass().getName(), e.getMessage());
        			try {
						Thread.sleep(3500);
					} catch (InterruptedException e1) {
						log.error(e1.getMessage());
					}
        		}else if(retryNum == maxRetry){
        			log.error("retried {}, abandon request: {}, method: {}, exception: {}, message: {}", maxRetry, request.url(), request.method(), e.getClass().getName(), e.getMessage());
        			throw e;
        		}
        	}
        }
        return response;
    }
}
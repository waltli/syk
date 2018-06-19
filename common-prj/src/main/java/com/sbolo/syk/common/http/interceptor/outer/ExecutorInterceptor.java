package com.sbolo.syk.common.http.interceptor.outer;

import java.io.IOException;

import com.sbolo.syk.common.http.interceptor.OuterChain;
import com.sbolo.syk.common.http.interceptor.OuterInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExecutorInterceptor implements OuterInterceptor {
	
	private OkHttpClient okHttpClient;
	
	public ExecutorInterceptor(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
	}

	@Override
	public Response intercept(OuterChain oChain) throws IOException {
		Request request = oChain.getRequest();
		return okHttpClient.newCall(request).execute();
	}

//	public void setOkHttpClient(OkHttpClient okHttpClient) {
//		this.okHttpClient = okHttpClient;
//	}

}

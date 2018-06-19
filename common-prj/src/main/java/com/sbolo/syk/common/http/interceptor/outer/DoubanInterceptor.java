package com.sbolo.syk.common.http.interceptor.outer;

import java.io.IOException;

import com.sbolo.syk.common.http.interceptor.OuterChain;
import com.sbolo.syk.common.http.interceptor.OuterInterceptor;
import com.sbolo.syk.common.tools.StringUtil;

import okhttp3.Request;
import okhttp3.Response;

public class DoubanInterceptor implements OuterInterceptor {

	@Override
	public Response intercept(OuterChain oChain) throws IOException {
		Request request = oChain.getRequest();
		if("movie.douban.com".equals(request.url().host())){
			request = request.newBuilder().addHeader("Cookie", "bid="+StringUtil.randomString(11)).build();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return oChain.proceed(request);
	}

}

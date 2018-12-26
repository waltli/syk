package com.sbolo.syk.common.http.interceptor.outer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.http.interceptor.OuterChain;
import com.sbolo.syk.common.http.interceptor.OuterInterceptor;
import com.sbolo.syk.common.tools.StringUtil;

import okhttp3.Request;
import okhttp3.Response;

public class UserAgentInterceptor implements OuterInterceptor {
	
	public static final List<String> userAgents = new ArrayList<>();
	
	static {
		userAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
		userAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134");
		userAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
		userAgents.add("Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; MANMJS; rv:11.0) like Gecko");
		userAgents.add("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		userAgents.add("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0");
		userAgents.add("Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; MANMJS; rv:11.0) like Gecko Core/1.63.5603.400 QQBrowser/10.1.1775.400");
		userAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 OPR/57.0.3098.106");
	}

	@Override
	public Response intercept(OuterChain oChain) throws IOException {
		Request request = oChain.getRequest();
		Random random = new Random();
		int nextInt = random.nextInt(userAgents.size());
		String userAgent = userAgents.get(nextInt);
		request = request.newBuilder().addHeader("User-Agent", userAgent).build();
		return oChain.proceed(request);
	}

}

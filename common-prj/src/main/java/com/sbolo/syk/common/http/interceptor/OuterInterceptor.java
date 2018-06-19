package com.sbolo.syk.common.http.interceptor;

import java.io.IOException;

import okhttp3.Response;

public interface OuterInterceptor {
	Response intercept(OuterChain oChain) throws IOException;
}

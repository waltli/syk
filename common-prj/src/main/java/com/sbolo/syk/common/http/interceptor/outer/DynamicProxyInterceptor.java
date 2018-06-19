package com.sbolo.syk.common.http.interceptor.outer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sbolo.syk.common.http.HttpClient;
import com.sbolo.syk.common.http.interceptor.OuterChain;
import com.sbolo.syk.common.http.interceptor.OuterInterceptor;
import com.sbolo.syk.common.http.proxy.StatefulProxy;

import okhttp3.Request;
import okhttp3.Response;

public class DynamicProxyInterceptor implements OuterInterceptor {
	private static final Logger log = LoggerFactory.getLogger(DynamicProxyInterceptor.class);
	
	private HttpClient httpClient;
	private List<StatefulProxy> listProxies;
	private Lock lock = new ReentrantLock();
	
	public DynamicProxyInterceptor(HttpClient httpClient, List<StatefulProxy> listProxies) {
		this.httpClient = httpClient;
		if(listProxies == null){
			listProxies = new ArrayList<StatefulProxy>();
		}
		this.listProxies = listProxies;
	}
	
	@Override
	public Response intercept(OuterChain oChain) throws IOException {
		Request request = oChain.getRequest();
		StatefulProxy beforProxy = httpClient.currProxy();
		Response response = null;
		int i = 0;
		do {
			try {
				response = oChain.proceed(request);
				if(response.code() != 403){
					return response;
				}
				if(listProxies.size() == 0){
					return response;
				}
				resetProxy(beforProxy, listProxies.get(i));
			} catch (IOException e) {
				if(listProxies.size() == 0){
					throw e;
				}
				resetProxy(beforProxy, listProxies.get(i));
			}
		} while (i++<listProxies.size());
		
		return response;
	}
	
	private void resetProxy(StatefulProxy beforProxy, StatefulProxy proxy){
		lock.lock();
		try {
			StatefulProxy afterProxy = httpClient.currProxy();
			if(beforProxy == null || afterProxy == null || beforProxy.equals(afterProxy)){
				log.info("use proxy by {}:{}", proxy.host(), proxy.port());
				httpClient.proxyWithRunning(proxy);
			}
		}finally {
			lock.unlock();
		}
	}

}

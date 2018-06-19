package com.sbolo.syk.common.http.interceptor;

import java.io.IOException;
import java.util.List;

import com.sbolo.syk.common.http.HttpClient;

import okhttp3.Request;
import okhttp3.Response;

public class OuterChain {
	private HttpClient httpClient;
	private Request request;
	private List<OuterInterceptor> oIntercepts;
	private int index;
	
	public OuterChain(HttpClient httpClient, Request request, int index, List<OuterInterceptor> oIntercepts){
		this.httpClient = httpClient;
		this.request = request;
		this.oIntercepts = oIntercepts;
		this.index = index;
	}
	
	public Response proceed(Request request) throws IOException{
		OuterChain next = new OuterChain(httpClient, request, index+1, oIntercepts);
		OuterInterceptor oInterceptor = oIntercepts.get(index);
		return oInterceptor.intercept(next);
	}
	
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}

	public List<OuterInterceptor> getoIntercepts() {
		return oIntercepts;
	}

	public void setoIntercepts(List<OuterInterceptor> oIntercepts) {
		this.oIntercepts = oIntercepts;
	}
	
}

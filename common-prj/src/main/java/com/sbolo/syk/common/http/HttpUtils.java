package com.sbolo.syk.common.http;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.exception.DownloadException;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.http.callback.HttpSendCallbackPure;
import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.Utils;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
	private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
	private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";

	public static void httpGet(String url, HttpSendCallbackPure callback) throws Exception {
		HttpClient httpClient = getHttpClient();
		Request request = buildRequest(url);
		Response response = null;
		try {
			response = httpClient.execute(request);
			if (!response.isSuccessful()) {
				log.error("请求响应失败，code: {}.  message: {}. url: {}", 
						response.code(), response.message(), response.request().url().url());
			}
			callback.onResponse(response);
		} finally {
			if (response != null) {
				response.close();
				response = null;
			}
		}
	}

	public static <T> HttpResult<T> httpGet(String url, HttpSendCallback<T> callback) {
		HttpClient httpClient = getHttpClient();
		Request request = buildRequest(url);
		Response response = null;
		HttpResult<T> result = new HttpUtils.HttpResult<T>();
		try {
			response = httpClient.execute(request);
			if (!response.isSuccessful()) {
				log.error("请求响应失败，code: {}.  message: {}. url: {}", 
						response.code(), response.message(), response.request().url().url());
			}
			result.setValue(callback.onResponse(response));
		} catch (Exception e) {
			result.setException(e);
			result.setHasException(true);
		} finally {
			if (response != null) {
				response.close();
				response = null;
			}
		}
		return result;
	}

	public static void httpPost(String url, Map<String, String> params, HttpSendCallbackPure callback)
			throws Exception {
		HttpClient httpClient = getHttpClient();
		FormBody.Builder builder = new FormBody.Builder();
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				builder.add(entry.getKey(), entry.getValue());
			}
		}
		FormBody body = builder.build();
		Request request = buildRequest(url, body);
		Response response = null;
		try {
			response = httpClient.execute(request);
			if (!response.isSuccessful()) {
				log.error("请求响应失败，code: {}.  message: {}. url: {}", 
						response.code(), response.message(), response.request().url().url());
			}
			callback.onResponse(response);
		} finally {
			if (response != null) {
				response.close();
				response = null;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static <T> HttpResult<T> httpPost(String url, Map params, HttpSendCallback<T> callback) {
		HttpClient httpClient = getHttpClient();
		FormBody.Builder builder = new FormBody.Builder();
		if (params != null) {
			Set keys = params.keySet();
			for (Object key : keys) {
				builder.add(String.valueOf(key), String.valueOf(params.get(key)));
			}
		}
		FormBody body = builder.build();
		Request request = buildRequest(url, body);
		Response response = null;
		HttpResult<T> result = new HttpUtils.HttpResult<T>();
		try {
			response = httpClient.execute(request);
			if (!response.isSuccessful()) {
				log.error("请求响应失败，code: {}.  message: {}. url: {}", 
						response.code(), response.message(), response.request().url().url());
			}
			result.setValue(callback.onResponse(response));
		} catch (Exception e) {
			result.setException(e);
			result.setHasException(true);
		} finally {
			if (response != null) {
				response.close();
				response = null;
			}
		}
		return result;
	}

	public static <T, PARAMS> HttpResult<T> httpPost(String url, PARAMS params, HttpSendCallback<T> callback) {
		HttpClient httpClient = getHttpClient();
		MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
		String paramStr = JSON.toJSONString(params);
		RequestBody body = RequestBody.create(mediaType, paramStr);
		Request request = buildRequest(url, body);
		Response response = null;
		HttpResult<T> result = new HttpUtils.HttpResult<T>();
		try {
			response = httpClient.execute(request);
			if (!response.isSuccessful()) {
				log.error("请求响应失败，code: {}.  message: {}. url: {}", 
						response.code(), response.message(), response.request().url().url());
			}
			result.setValue(callback.onResponse(response));
		} catch (Exception e) {
			result.setException(e);
			result.setHasException(true);
		} finally {
			if (response != null) {
				response.close();
				response = null;
			}
		}
		return result;
	}

	private static Request buildRequest(String url, RequestBody body) {
		Builder builder = new Request.Builder().url(url);
		builder.header("User-Agent", userAgent);
		if (body != null) {
			builder.post(body);
		}
		return builder.build();
	}

	private static Request buildRequest(String url) {
		return buildRequest(url, null);
	}

	public static final HttpClient getHttpClient() {
		return HttpClient.getHttpClient();
		// return HttpClient.getHttpClient().addOInterceptor(new
		// DoubanInterceptor());
	}
	public static String getResultJsonByPost(String url,Map<String,String> params){
		HttpResult<String> httpPost = HttpUtils.httpPost(url, params, new HttpSendCallback<String>() {

			@Override
			public String onResponse(Response response) throws Exception {
				if (!response.isSuccessful()) {
					throw new BusinessException("请求响应失败，code: " + response.code() + ". message: "
							+ response.message() + ". url: " + response.request().url().url());
				}
				String value = response.body().string();
				return value;
			}

		});
		String value = "";
		try {
			value = httpPost.getValue();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return value;
	}
	
	public static class HttpResult<V> {
		private V value;
		private Exception exception;
		private Boolean hasException = false;

		public V getValue() throws Exception {
			if (HasException()) {
				throw getException();
			}
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		public Exception getException() {
			return exception;
		}

		public void setException(Exception exception) {
			this.exception = exception;
		}

		public Boolean HasException() {
			return hasException;
		}

		public void setHasException(Boolean hasException) {
			this.hasException = hasException;
		}
	}
	
	/**
	 * 将网络文件转换为byte[]
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytes(String url) throws Exception {
		HttpResult<byte[]> result = HttpUtils.httpGet(url, new HttpSendCallback<byte[]>() {
			@Override
			public byte[] onResponse(Response response) throws Exception{
				if(!response.isSuccessful()){
					throw new DownloadException("下载文件失败："+url+"响应码："+response.code());
				}
				byte[] bodyBytes = response.body().bytes();
				return bodyBytes;
			}
		});
		return result.getValue();
	}
}

package com.sbolo.syk.common.tools;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.ui.RequestResult;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BizUtils {

	private static final Logger LOG = LoggerFactory.getLogger1(BizUtils.class);

	private static MediaType streamMedia = MediaType.parse("application/octet-stream");

	public static <T, REQUEST> RequestResult<T> getByForm(String url, REQUEST request, Class<T> responseClass) {
		try {
			url = buildUrl(url, request);
			HttpResult<RequestResult<T>> httpResult = HttpUtils.httpGet(url, new HttpSendCallback<RequestResult<T>>() {

				@Override
				public RequestResult<T> onResponse(Response response) throws Exception {
					if (!response.isSuccessful()) {
						throw new BusinessException("请求响应失败，code: " + response.code() + ". message: "
								+ response.message() + ". url: " + response.request().url().url());
					}

					RequestResult<T> result = parseResult(response.body(), responseClass);
					return result;
				}
			});
			return httpResult.getValue();
		} catch (Exception e) {
			LOG.error("", e);
			return RequestResult.error(e);
		}
	}

	public static <T, REQUEST> RequestResult<T> postByForm(String url, REQUEST request, Class<T> responseClass) {
		try {
			url = buildUrl(url, null);
			Map<String, String> params = getParamMap(request);
			HttpResult<RequestResult<T>> httpResult = HttpUtils.httpPost(url, params,
					new HttpSendCallback<RequestResult<T>>() {

						@Override
						public RequestResult<T> onResponse(Response response) throws Exception {
							if (!response.isSuccessful()) {
								throw new BusinessException("请求响应失败，code: " + response.code() + ". message: "
										+ response.message() + ". url: " + response.request().url().url());
							}
							RequestResult<T> result = parseResult(response.body(), responseClass);
							return result;
						}

					});
			return httpResult.getValue();
		} catch (Exception e) {
			LOG.error("", e);
			return RequestResult.error(e);
		}
	}

	public static <T, REQUEST> RequestResult<T> postByJson(String url, REQUEST request, Class<T> responseClass) {
		try {
			url = buildUrl(url, null);
			HttpResult<RequestResult<T>> httpResult = HttpUtils.httpPost(url, request,
					new HttpSendCallback<RequestResult<T>>() {

						@Override
						public RequestResult<T> onResponse(Response response) throws Exception {
							if (!response.isSuccessful()) {
								throw new BusinessException("请求响应失败，code: " + response.code() + ". message: "
										+ response.message() + ". url: " + response.request().url().url());
							}
							RequestResult<T> result = parseResult(response.body(), responseClass);
							return result;
						}

					});
			return httpResult.getValue();
		} catch (Exception e) {
			LOG.error("", e);
			return RequestResult.error(e);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> RequestResult<T> parseResult(ResponseBody responseBody, Class<T> responseClass)
			throws IOException {
		MediaType mediaType = responseBody.contentType();
		RequestResult<T> result = null;
		if (mediaType == null) {
			throw new BusinessException("服务端返回RequestResult为null！");
		}

		if (mediaType.type().equals(streamMedia.type()) && mediaType.subtype().equals(streamMedia.subtype())) {
			byte[] bytes = responseBody.bytes();
			result = (RequestResult<T>) Utils.byteToObject(bytes);
		} else {
			result = json2Result(responseBody.string(), responseClass);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <REQUEST> Map<String, String> getParamMap(REQUEST request)
			throws IllegalArgumentException, IllegalAccessException {
		if (request == null) {
			return null;
		}
		Map<String, String> params = new HashMap<>();
		if (request instanceof Map) {
			Map<Object, Object> temp = (Map) request;
			for (Map.Entry<Object, Object> entry : temp.entrySet()) {
				if (entry.getValue() == null) {
					continue;
				}
				params.put(entry.getKey().toString(), entry.getValue().toString());
			}
		} else {
			obj2Map(request, request.getClass(), params);
		}

		return params;
	}

	@SuppressWarnings("rawtypes")
	private static <REQUEST> void obj2Map(REQUEST request, Class clazz, Map<String, String> params)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fieldArr = clazz.getDeclaredFields();

		for (Field field : fieldArr) {
			field.setAccessible(true);
			if (field.get(request) == null) {
				continue;
			}
			params.put(field.getName(), field.get(request).toString());
		}

		if (clazz.getSuperclass() != Object.class) {
			obj2Map(request, clazz.getSuperclass(), params);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <REQUEST> String buildUrl(String url, REQUEST request)
			throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		String JID = "";
		try {
			Subject subject = SecurityUtils.getSubject();
			if (subject != null) {
				Session session = subject.getSession();
				if (session != null) {
					Object attribute = session.getAttribute("JID");
					if (attribute != null) {
						JID = attribute.toString();
					}
				}
			}
		} catch (Exception e) {
		}

		if (request == null && StringUtils.isBlank(JID)) {
			return url;
		}

		StringBuffer sbu = new StringBuffer();
		if (StringUtils.isNotBlank(JID)) {
			sbu.append("JID=").append(JID).append("&");
		}

		if (request != null) {
			if (request instanceof Map) {
				Map<Object, Object> temp = (Map) request;
				for (Map.Entry<Object, Object> entry : temp.entrySet()) {
					if (entry.getValue() == null) {
						continue;
					}
					sbu.append(entry.getKey().toString()).append("=")
							.append(URLEncoder.encode(entry.getValue().toString(), "utf-8")).append("&");
				}
			} else {
				Class clazz = request.getClass();
				Field[] fieldArr = clazz.getDeclaredFields();
				for (Field field : fieldArr) {
					field.setAccessible(true);
					if (field.get(request) == null) {
						continue;
					}
					sbu.append(field.getName()).append("=")
							.append(URLEncoder.encode(field.get(request).toString(), "utf-8")).append("&");
				}
			}
		}
		if (sbu.length() > 0) {
			sbu = sbu.replace(sbu.length() - 1, sbu.length(), "");
			url = url + "?" + sbu.toString();
		}
		return url;
	}

	private static <T> RequestResult<T> json2Result(String resultStr, Class<T> responseClass) {
		RequestResult<T> result = JSON.parseObject(resultStr, new TypeReference<RequestResult<T>>() {
		});
		List<T> list = result.getList();
		if (list != null && list.size() > 0 && list.get(0) instanceof JSONObject) {
			List<T> reloadList = JSON.parseArray(list.toString(), responseClass);
			result.setList(reloadList);
			result.setObj(reloadList.get(0));
		} else if (result.getObj() instanceof JSONObject) {
			T obj = JSON.parseObject(result.getObj().toString(), responseClass);
			result.setObj(obj);
		}
		return result;
	}

}

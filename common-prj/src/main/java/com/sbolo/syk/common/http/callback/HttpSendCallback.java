package com.sbolo.syk.common.http.callback;

import okhttp3.Response;

public interface HttpSendCallback<T> {
	public T onResponse(Response response) throws Exception;
}

package com.sbolo.syk.common.http.callback;

import okhttp3.Response;

public interface HttpSendCallbackPure {
	public void onResponse(Response response) throws Exception;
}

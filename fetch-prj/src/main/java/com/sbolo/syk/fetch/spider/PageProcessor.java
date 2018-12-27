package com.sbolo.syk.fetch.spider;

import java.util.Map;

public interface PageProcessor {
	void before();
	void process(Page page, Map<String, Object> fields) throws Exception;
	void after();
	String startUrl();
	default int getSleep() {
		return 1000;
	}
	default boolean needThread() {
		return true;
	}
}

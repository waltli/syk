package com.sbolo.syk.fetch.spider;

import java.util.Map;

public interface Pipeline {
	void process(Map<String, Object> fields) throws Exception;
	void after();
}

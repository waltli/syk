package com.sbolo.syk.fetch.spider;

import java.util.Map;

public interface Distinct {
	void process(Map<String, Object> fields) throws Exception;
	void before();
	void after();
}

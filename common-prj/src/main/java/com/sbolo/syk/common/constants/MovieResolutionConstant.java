package com.sbolo.syk.common.constants;

import java.util.HashMap;
import java.util.Map;

public class MovieResolutionConstant {
	private static final Map<String, Integer> resolutionFetchInt = new HashMap<String, Integer>();
	private static final Map<String, Integer> pureResolutionFetchInt = new HashMap<String, Integer>();
	
	//720P|1080P|480P|720|1080|480|2160P|2160|普清|高清|超清|清晰|4K
	static{
		resolutionFetchInt.put("4K", 5);
		resolutionFetchInt.put("2160P", 5);
		resolutionFetchInt.put("2160", 5);
		resolutionFetchInt.put("1080P", 4);
		resolutionFetchInt.put("1080", 4);
		resolutionFetchInt.put("720P", 3);
		resolutionFetchInt.put("720", 3);
		resolutionFetchInt.put("1280", 3);
		resolutionFetchInt.put("480P", 2);
		resolutionFetchInt.put("480", 2);
		resolutionFetchInt.put("超清", 4);
		resolutionFetchInt.put("高清", 3);
		resolutionFetchInt.put("普清", 2);
		resolutionFetchInt.put("清晰", 1);
		
		
		
		pureResolutionFetchInt.put("4K", 10);
		pureResolutionFetchInt.put("2160P", 10);
		pureResolutionFetchInt.put("2160", 10);
		pureResolutionFetchInt.put("1080P", 8);
		pureResolutionFetchInt.put("1080", 8);
		pureResolutionFetchInt.put("720P", 7);
		pureResolutionFetchInt.put("720", 7);
		pureResolutionFetchInt.put("1280", 7);
		pureResolutionFetchInt.put("480P", 6);
		pureResolutionFetchInt.put("480", 6);
		pureResolutionFetchInt.put("超清",8);
		pureResolutionFetchInt.put("高清", 7);
		pureResolutionFetchInt.put("普清", 6);
		pureResolutionFetchInt.put("清晰", 5);
	}
	
	
	public static int getResolutionScoreByKey (String key){
		Integer score = resolutionFetchInt.get(key.toUpperCase());
		if(score == null){
			return 0;
		}
		return score;
	}
	
	public static int getPureResolutionScoreByKey(String key){
		Integer score = pureResolutionFetchInt.get(key.toUpperCase());
		if(score == null){
			return 0;
		}
		return score;
	}
}

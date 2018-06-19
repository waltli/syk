package com.sbolo.syk.fetch.processor;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import okhttp3.Response;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.exception.MovieInfoFetchException;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.fetch.service.MovieInfoBizService;
import com.sbolo.syk.fetch.service.ResourceInfoBizService;
import com.sbolo.syk.fetch.vo.LinkInfoVO;
import com.sbolo.syk.fetch.vo.PureNameAndSeasonVO;

public class CommonProcessor {
	private static final Logger log = LoggerFactory.getLogger(CommonProcessor.class);
	private String startUrl;
	
	public String getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}
	
	@Resource
	private ThreadPoolTaskExecutor threadPool;
	
	@Resource
	private ResourceInfoBizService resourceInfoBizService;
	
	@Resource
	private MovieInfoBizService movieInfoBizService;
	
    public PureNameAndSeasonVO getPureNameAndSeason(String pureName, String fullName) throws MovieInfoFetchException{
    	
    	if(pureName == null && fullName == null){
    		throw new MovieInfoFetchException("pureName and fullName all are null!");
    	}

    	if(fullName == null){
    		fullName = "";
    	}
    	
    	if(pureName == null){
    		Matcher m = Pattern.compile(RegexConstant.pure_name).matcher(fullName);
    		if(!m.find()){
    			throw new MovieInfoFetchException("Failure to fetch pureName from \""+fullName+"\", so jump out of the page!");
    		}
    		pureName = m.group();
    	}else {
    		int pointIdx = pureName.indexOf(".");
    		if(pointIdx > -1){
    			pureName = pureName.substring(0, pointIdx).trim();
    			fullName += pureName.substring(pointIdx);
    		}
    		
    		int slashIdx = pureName.indexOf("/");
    		if(slashIdx > -1){
    			pureName = pureName.substring(0, slashIdx).trim();
    			fullName += pureName.substring(slashIdx);
    		}
    	}
    	
    	//filter pureName
    	String name = pureName;
    	pureName = Pattern.compile(RegexConstant.types).matcher(name).replaceAll("").trim();
		if(StringUtils.isBlank(pureName)){
			throw new MovieInfoFetchException("The pureName is null after delete types. from \""+name+"\"");
		}
    	
    	String desc = fullName;
    	if(!fullName.contains(pureName)){
    		desc = pureName+fullName;
    	}
    	
		Integer season = null;
		String cnSeason = null;
		for(int i=0; i<RegexConstant.list_season.size(); i++){
			Matcher m2 = RegexConstant.list_season.get(i).matcher(StringUtil.trimAll(desc));
			if(m2.find()){
				//防止名称和季数中间没有任何间隔 例：越狱第五季
				int seasonIdx = pureName.indexOf(m2.group());
				if(seasonIdx != -1){
					pureName = pureName.substring(0, seasonIdx).trim();
				}
				season = Utils.chineseNumber2Int(m2.group(1));
				cnSeason = "第"+Utils.int2ChineseNumber(m2.group(1))+"季";
				pureName = pureName +" "+cnSeason;
				break;
			}
		}
		return new PureNameAndSeasonVO(pureName, season, cnSeason);
    }
	
    
    
    
    protected void destroy(){
    }
    
    protected List<String> getPrecisionsByInfo(List<TextNode> textNodes) {
		String directDesc = "";
		String castDesc = "";
		for(TextNode textNode : textNodes) {
			String text = textNode.text();
			if(Pattern.compile(RegexConstant.DYtitle).matcher(text).find() && StringUtils.isBlank(directDesc)){
				directDesc = text;
			}else if(Pattern.compile(RegexConstant.YYtitle+"|"+RegexConstant.ZYtitle).matcher(text).find() && StringUtils.isBlank(castDesc)){
				castDesc = text;
			}
			if(StringUtils.isNotBlank(directDesc) && StringUtils.isNotBlank(castDesc)){
				break;
			}
		}
		List<String> precisions = getPrecisions(directDesc, castDesc);
		return precisions;
	}
    
    protected List<String> getPrecisions(String directDesc, String castDesc){
    	List<String> precisions = new ArrayList<String>();
    	String[] directNamesArr = null;
    	String[] castNamesArr = null;
    	if(StringUtils.isNotBlank(directDesc)){
    		String noTitleDirect = Pattern.compile(RegexConstant.DYtitle).matcher(directDesc).replaceAll("");
    		String directNames = StringUtil.trim(noTitleDirect);
    		directNamesArr = directNames.split(RegexConstant.slashSep);
    	}
    	if(StringUtils.isNotBlank(castDesc)){
    		String noTitleCast = Pattern.compile(RegexConstant.YYtitle+"|"+RegexConstant.ZYtitle).matcher(castDesc).replaceAll("");
    		String castNames = StringUtil.trim(noTitleCast);
        	castNamesArr = castNames.split(RegexConstant.slashSep);
    	}
    	
    	if(directNamesArr == null && castNamesArr == null){
    		return precisions;
    	}else if(castNamesArr == null || (directNamesArr != null && directNamesArr.length >= 2)){
    		getPrecisions(precisions, directNamesArr, 2);
    	}else if(directNamesArr == null){
    		getPrecisions(precisions, castNamesArr, 2);
    	}else {
    		getPrecisions(precisions, directNamesArr, 1);
    		getPrecisions(precisions, castNamesArr, 1);
    	}
		return precisions;
    }
    
    private void getPrecisions(List<String> precisions, String[] namesArr, int count){
    	for(String name : namesArr){
    		if(Pattern.compile("^"+RegexConstant.chinese).matcher(name).find()){
    			name = name.split(" ")[0].replace("・", "·");
    		}
    		precisions.add(name);
    		if(precisions.size() >= count){
				break;
			}
    	}
    }
}

package com.sbolo.syk.fetch.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sbolo.syk.common.exception.MovieInfoFetchException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.fetch.spider.Page;
import com.sbolo.syk.fetch.spider.PageProcessor;
import com.sbolo.syk.fetch.vo.GatherVO;
import com.sbolo.syk.fetch.vo.LinkInfoVO;
import com.sbolo.syk.fetch.vo.PureNameAndSeasonVO;

public class RarbtProcessor extends CommonProcessor implements PageProcessor {
	
	private static final Logger log = LoggerFactory.getLogger(RarbtProcessor.class);
	
	private String pageUrlReg = "http://www\\.rarbt\\.com/index\\.php/index/index/p/(\\d+)\\.html";
	private String detailUrlReg = "http://www\\.rarbt\\.com/subject/(\\d+)\\.html";
	private String toDownUrlReg = "http://www\\.rarbt\\.com/index\\.php/dow/index\\.html\\?id=\\d+\\&zz=\\d+(?:#(\\w{32}))?";

	@Override
	public void process(final Page page, Map<String, Object> fields) throws MovieInfoFetchException {
		Document document = page.getDocument();
		String url = page.getUrl().toString();
		if(Pattern.compile(pageUrlReg).matcher(url).find()){
			Elements page_elements = document.select("div.item.cl > div.title > p.tt.cl > a");
			List<String> detailUrls = page.links(page_elements, "href").subList(0, 15);
			log.info("本页影片总数：{}",detailUrls.size());
			page.addNewUrls(detailUrls);
		} else if(Pattern.compile(detailUrlReg).matcher(url).find()){
			String preDoubanUri = document.select("em.e_db").first().parent().attr("href");
			if(StringUtils.isBlank(preDoubanUri)){
				throw new MovieInfoFetchException("The preDoubanUri is not exists of url: "+ url);
			}
			String preDoubanUrl = Utils.getHostUrl(page.getHost(), preDoubanUri);
			
			String doubanUrl = getDoubanUrl(preDoubanUrl);
//			String directNames = document.select("ul.moviedteail_list > li:containsOwn(导演) > a").text("/");
//			String castNames = document.select("ul.moviedteail_list > li:containsOwn(主演) > a").text("/");
//			
//			List<String> precisions = getPrecisions(directNames, castNames);
			
			//获取资源信息
			Elements elementsResource = document.select("div.tinfo>a");
			List<LinkInfoVO> linkInfos = new ArrayList<LinkInfoVO>();
			for(int i=0; i<elementsResource.size(); i++){
				//先获取下载链接的描述
				Element elementResourceALabel = elementsResource.get(i);
				Element elementResourcePLabel = elementResourceALabel.child(0);
				String downloadLinkName = elementResourcePLabel.text();
				if(downloadLinkName.contains("论坛下载")){
					continue;
				}
				//再获取下载链接
				String toDownloadLink = Utils.getHostUrl(page.getHost(), elementResourceALabel.attr("href"));
				if(!Pattern.compile(toDownUrlReg).matcher(toDownloadLink).find()){
					//不符合将要下载的页面的链接则跳过本链接
					continue;
				}
				String downloadLink = getDownloadLink(toDownloadLink, page.getUrl());
				
				LinkInfoVO linkInfo = new LinkInfoVO();
				linkInfo.setName(downloadLinkName);
				linkInfo.setDownloadLink(downloadLink);
				linkInfos.add(linkInfo);
			}
			//获取资源截图
			Elements screensElements = document.select("img.zoom");
			if(screensElements.size() == 0){
				screensElements = document.select("div.sl.cl ~ div > p > img");
			}
			List<String> printscreens = page.links(screensElements, "src");
			
			GatherVO gather = new GatherVO(null, null, linkInfos, printscreens, url, doubanUrl);
			fields.put(url, gather);
		}
	}
	
	private String getDoubanUrl(final String preDoubanUrl) throws MovieInfoFetchException{
		HttpResult<String> result = HttpUtils.httpGet(preDoubanUrl, new HttpSendCallback<String>() {
			
			@Override
			public String onResponse(Response response) throws Exception {
				if(!response.isSuccessful()){
					throw new MovieInfoFetchException("Request to get doubanUrl page failed from "+preDoubanUrl+", code:"+response.code());
				}
				byte[] bytes = response.body().bytes();
				String content = new String(bytes, "utf-8");
				Matcher m = Pattern.compile("window\\.location\\.href='https?://movie\\.douban\\.com/subject/(\\d+)/?';").matcher(content);
				if(!m.find()){
					throw new MovieInfoFetchException("Can't fetch doubanUrl with Regex, from "+preDoubanUrl);
				}
				String doubanId = m.group(1);
				return "https://movie.douban.com/subject/"+doubanId+"/";
			}
		});
		try {
			return result.getValue();
		} catch (Exception e) {
			if(e instanceof MovieInfoFetchException){
				throw (MovieInfoFetchException) e;
			}
			throw new MovieInfoFetchException(e);
		}
		
	}
	
	private String getDownloadLink(String toDownloadLink, final String comefrom){
		String[] toDownloadLinkSplit = toDownloadLink.split("\\?");
		final String reqUrl = toDownloadLinkSplit[0];
		String[] paramSplit = toDownloadLinkSplit[1].split("\\&");
		String id = paramSplit[0].split("=")[1];
		String zz = "zz"+paramSplit[1].split("=")[1];
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("zz", zz);
		HttpResult<String> result = HttpUtils.httpPost(reqUrl, params, new HttpSendCallback<String>() {

			@Override
			public String onResponse(Response response)
					throws Exception {
				if(response.code() != 200 && response.code() != 302){
					throw new Exception("Get real download link failed from RARBT. code: "+response.code()+" from: "+comefrom);
				}
				return response.request().url().toString();
			}
		});
		String downloadLink = null;
		try {
			downloadLink = result.getValue();
		} catch (Exception e) {
			log.error(comefrom,e);
		}
		return downloadLink;
	}
	
	public static void main(String[] args) {
		
		Matcher m = Pattern.compile("https?://movie.douban.com/subject/(\\d+)/?").matcher("http://movie.douban.com/subject/26718346/");
	    if(m.find()){
	    	
	    	System.out.println(m.group());
	    }
	}

	@Override
	public void after() {
		this.destroy();
	}

	@Override
	public String startUrl() {
		return getStartUrl();
	}

	@Override
	public void before() {
	}
	

}

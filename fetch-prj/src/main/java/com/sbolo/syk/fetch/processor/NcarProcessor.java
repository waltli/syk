package com.sbolo.syk.fetch.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.exception.MovieInfoFetchException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.callback.HttpSendCallbackPure;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.fetch.spider.Page;
import com.sbolo.syk.fetch.spider.PageProcessor;
import com.sbolo.syk.fetch.vo.GatherVO;
import com.sbolo.syk.fetch.vo.LinkInfoVO;
import com.sbolo.syk.fetch.vo.PureNameAndSeasonVO;

public class NcarProcessor extends CommonProcessor implements PageProcessor {
	private static final Logger log = LoggerFactory.getLogger(NcarProcessor.class);
	public static Boolean isLogin = false;
	
	private String pageUrlReg = "http://bbs\\.ncar\\.cc/forum-oumeijuji-\\d\\.html";
	private String detailUrlReg = "http://bbs\\.ncar\\.cc/thread-\\d{5,6}-\\d-\\d\\.html";
	private String toDownUrlReg = "http://bbs\\.ncar\\.cc/forum\\.php\\?mod=attachment\\&aid=\\w+";
	@Override
	public void process(Page page, Map<String, Object> fields) throws MovieInfoFetchException {
		Document document = page.getDocument();
		String url = page.getUrl().toString();
		if(Pattern.compile(pageUrlReg).matcher(url).find()){
			Elements page_elements = document.select("#portal_block_763_content > div > dl > dt > a");
			List<String> detailUrls = page.links(page_elements, "href");
			log.info("本页影片总数：{}",detailUrls.size());
			page.addNewUrls(detailUrls);
		}else if(Pattern.compile(detailUrlReg).matcher(url).find()){
			String fullName = document.select("#thread_subject").first().text();
			Elements posts = document.select("#postlist > div:not(.pl)");
			Element firstPost = posts.first();
			String firstIdNum = firstPost.attr("id").substring(5);
			
			String auth = firstPost.select("#favatar"+firstIdNum+" > div.pi > div.authi > a").first().text();
			if(!"马克思·佩恩".equals(auth) && !"sakura樱".equals(auth)){
				log.info("此发布页不是\"马克思·佩恩\"或\"sakura樱\", 跳过此发布页. fullName: {}, auth: {}", fullName, auth);
				return;
			}
			//哥谭市 去除市
			fullName = Pattern.compile(RegexConstant.getan).matcher(fullName).replaceAll("");
			
			PureNameAndSeasonVO pureNameAndSeason = getPureNameAndSeason(null, fullName);
			
			Element postMessageElement = firstPost.select("#postmessage_"+firstIdNum).first();
			
			//获取精确字段
			List<TextNode> textNodes = postMessageElement.textNodes();
			List<String> precisions = getPrecisionsByInfo(textNodes);
			
			List<LinkInfoVO> linkInfos = new ArrayList<LinkInfoVO>();
			for(int i=posts.size()-1; i>=0; i--){
				Element post = posts.get(i);
				String idNum = post.attr("id").substring(5);
				Element postInfoElement = post.select("#postnum"+idNum).first();
				if(postInfoElement == null){
					log.info("{} 楼没有找到是否置顶贴:{}", i+1, page.getUrl());
					continue;
				}
				String postInfo = postInfoElement.ownText();
				if(!postInfo.contains("楼主") && !postInfo.contains("来自")){
					continue;
				}
				
				Element postElement = post.select("#postmessage_"+idNum).first();
				Elements showhides = postElement.select("div.showhide");
				for(Element showhide : showhides){
					List<LinkInfoVO> tempLinks = new ArrayList<LinkInfoVO>();
					String ownText = showhide.ownText();
					if(Pattern.compile("(?i)RMHD").matcher(ownText).find()){
						continue;
					}
					if(ownText.contains("熟肉")){
						getLinkInfo(showhide, tempLinks, page.getHost());
						if(tempLinks.size() > 0){
							linkInfos.addAll(tempLinks);
							break;
						}
					}
					if(ownText.contains("生肉")){
						getLinkInfo(showhide, tempLinks, page.getHost());
						if(tempLinks.size() > 0){
							linkInfos.addAll(tempLinks);
						}
					}
				}
			}
			GatherVO gather = new GatherVO(pureNameAndSeason.getPureName(), precisions, linkInfos, null, url, null);
			fields.put(url, gather);
		}
	}
	
	private void getLinkInfo(Element showhide, List<LinkInfoVO> linkInfos, String host){
		Elements resourcesElements = showhide.select("ignore_js_op > span > a:matchesOwn((?i)"+RegexConstant.torrent+")");
		boolean isbt = true;
		if(resourcesElements.size() <= 0){
			resourcesElements = showhide.select("a");
			isbt = false;
		}
		
		for(int n=0; n<resourcesElements.size(); n++){
			Element resourceElement = resourcesElements.get(n);
			String downloadLinkName = null;
			String downloadLink = null;
			LinkInfoVO linkInfo = new LinkInfoVO();
			if(isbt){
				downloadLink = Utils.getHostUrl(host, resourceElement.attr("href"));
				if(!Pattern.compile(toDownUrlReg).matcher(downloadLink).find()){
					//不符合将要下载的页面的链接则跳过本链接
					continue;
				}
				downloadLinkName = resourceElement.text();
				linkInfo.setName(downloadLinkName);
				//不是标准的.torrent后缀，所以添加个自定义后缀，后面解析好判断
				linkInfo.setDownloadLink(downloadLink+"#torrent");
				linkInfos.add(linkInfo);
			}else {
				List<Pattern> patterns = new ArrayList<Pattern>();
				patterns.add(Pattern.compile(RegexConstant.ed2k));
				patterns.add(Pattern.compile(RegexConstant.magnet));
				patterns.add(Pattern.compile(RegexConstant.baiduNet));
				downloadLinkName = showhide.ownText() + " " + resourceElement.text();
				String href = resourceElement.attr("href");
				for(int j=0; j<patterns.size(); j++){
					if(patterns.get(j).matcher(href).find()){
						downloadLink = href;
						linkInfo.setName(downloadLinkName);
						linkInfo.setDownloadLink(downloadLink);
						linkInfos.add(linkInfo);
					}
				}
			}
		}
	}

	@Override
	public void after() {
		isLogin = false;
		this.destroy();
	}

	@Override
	public String startUrl() {
		return getStartUrl();
	}

	@Override
	public void before() {
		try {
			if(!isLogin){
				Map<String, String> params = new HashMap<String, String>();
				params.put("username", "soorush");
				params.put("password", "moming30613");
				HttpUtils.httpPost("http://bbs.ncar.cc/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes", params, new HttpSendCallbackPure() {
					
					@Override
					public void onResponse(Response response) throws Exception {
					}
				}); 
			}
		}catch(Exception e){
			log.error("登录ncar",e);
		}
	}

}

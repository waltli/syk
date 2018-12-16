package com.sbolo.syk.fetch.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.fetch.spider.Page;
import com.sbolo.syk.fetch.spider.PageProcessor;
import com.sbolo.syk.fetch.spider.exception.AnalystException;
import com.sbolo.syk.fetch.spider.exception.SpiderException;
import com.sbolo.syk.fetch.vo.ConcludeVO;
import com.sbolo.syk.fetch.vo.LinkInfoVO;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.PureNameAndSeasonVO;

public class MeiJuTTProcessor extends ProcessorHelper implements PageProcessor {
private static final Logger log = LoggerFactory.getLogger(MeiJuTTProcessor.class);
	//http://www.meijutt.com/
	private String pageUrlReg = "^https?://www\\.meijutt\\.com/$";
	//http://www.meijutt.com/content/meiju23842.html
	private String detailUrlReg = "^https?://www\\.meijutt\\.com/content/meiju\\d{4,5}\\.html$";
	
	@Override
	public void before() {
		this.init();
	}

	@Override
	public void process(Page page, Map<String, Object> fields) throws Exception {
		Document document = page.getDocument();
		String url = page.getUrl().toString();
		if(Pattern.compile(pageUrlReg).matcher(url).find()){
			Element firstUrlElement = document.select("div.week-hot > ul > li > a").first();
			int size = 6;
			if(firstUrlElement == null) {
				size = 7;
			}
			Elements urls_elements = document.select("div.week-hot > ul > li > p > a");
			String firstLink = page.link(firstUrlElement, "href");
			List<String> detailUrls = page.links(urls_elements, "href", size);
			detailUrls.add(0, firstLink);
			log.info("本页影片总数：{}",detailUrls.size());
			page.addNewUrls(detailUrls);
		}else if(Pattern.compile(detailUrlReg).matcher(url).find()){
			String fullName = document.select("div.info-title > h1").first().text();
			String pureName = Pattern.compile("【.*?】").matcher(fullName).replaceAll("");
			pureName = pureNameFilter(pureName);
			PureNameAndSeasonVO pureNameAndSeason = getPureNameAndSeason(pureName, fullName);
			Element infoElement = document.select("div.o_r_contact > ul").first();
			List<String> precisions = null;
			if(infoElement != null){
				precisions = getPrecisionsByInfo(infoElement.html(), "<li>", "更多&gt;&gt;");
			}
			
			Elements labels = document.select("div.from-tabs > label");
			int seq = -1;
			if(labels != null && labels.size() > 0) {
				for(int i=0; i<labels.size(); i++) {
					Element label = labels.get(i);
					if(label.hasClass("down-ico")) {
						seq = i;
						break;
					}
				}
			}
			
			if(seq == -1) {
				throw new SpiderException("该资源没有找到含有中文字幕的资源，url: "+url);
			}
			
			Elements tabList = document.select("div.tabs-list");
			Element resourceParent = tabList.get(seq);
			if(resourceParent == null) {
				throw new SpiderException("该资源中没有对应seq, seq: "+seq+"，url: "+url);
			}
			Elements resourceElements = resourceParent.select("div.down_list > ul > li > p > strong > a");

			List<LinkInfoVO> linkInfos = new ArrayList<LinkInfoVO>();
			for(int i=0; i<resourceElements.size(); i++){
				Element elementResourceALabel = resourceElements.get(i);
				//再获取下载链接的描述
				String downloadLinkName = elementResourceALabel.text();
				
				//先获取下载链接
				String downloadLink = elementResourceALabel.attr("href");
				
				if(!Pattern.compile(RegexConstant.resource_protocol).matcher(downloadLink).find()){
					continue;
				}
				
				
				LinkInfoVO linkInfo = new LinkInfoVO();
				linkInfo.setName(downloadLinkName);
				linkInfo.setDownloadLink(downloadLink);
				linkInfo.setLinkDecoding("UTF-8");
				linkInfos.add(linkInfo);
			}
			
			MovieInfoVO fetchMovie = this.resolve(pureNameAndSeason, precisions, linkInfos, null, url, null);
			if(fetchMovie != null) {
				fields.put(url, fetchMovie); 
			}
		}else {
			log.warn(url+" 不符合meijutt的正则表达式，首页："+pageUrlReg+",详情页："+detailUrlReg);
		}
	}
	
	private String pureNameFilter(String pureName) throws SpiderException {
		Map<String, String> replaceMapping = new HashMap<>();
		replaceMapping.put("绝命律师", "风骚律师");
		replaceMapping.put("杀手中间人", "中间人先生");
		replaceMapping.put("碟中谍", "虎胆妙算");
		replaceMapping.put("新闻之争", "报社");
		
		List<String> skipMapping = new ArrayList<>();
		skipMapping.add("鲁保罗变装皇后秀");
		
		
		for(String skip : skipMapping) {
			if(pureName.contains(skip)) {
				throw new SpiderException("跳过-"+pureName);
			}
		}
		
		for(String key : replaceMapping.keySet()) {
			if(pureName.contains(key)) {
				String value = replaceMapping.get(key);
				pureName = pureName.replace(key, value);
				break;
			}
		}
		return pureName;
	}

	@Override
	public void after() {
		this.destroy();
	}

	@Override
	public String startUrl() {
		return getStartUrl();
	}
}

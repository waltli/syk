package com.sbolo.syk.fetch.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sbolo.syk.common.exception.MovieInfoFetchException;
import com.sbolo.syk.fetch.spider.Page;
import com.sbolo.syk.fetch.spider.PageProcessor;
import com.sbolo.syk.fetch.vo.GatherVO;
import com.sbolo.syk.fetch.vo.LinkInfoVO;
import com.sbolo.syk.fetch.vo.PureNameAndSeasonVO;

public class SixOneVCDProcessor extends CommonProcessor implements PageProcessor {
	
	private static final Logger log = LoggerFactory.getLogger(SixOneVCDProcessor.class);
	
	private String pageUrlReg = "http://www\\.61dyz\\.com$";
	private String detailUrlReg = "http://www\\.61dyz\\.com/zixun/\\w+/\\d{4}-\\d{1,2}/\\d{5,6}\\.html";
	@Override
	public void process(Page page, Map<String, Object> fields) throws MovieInfoFetchException {
		Document document = page.getDocument();
		String url = page.getUrl().toString();
		if(Pattern.compile(pageUrlReg).matcher(url).find()){
			Elements page_elements = document.select(".die_tv.die_movie>div.die_right > div.ranking>ul>li>a");
			List<String> detailUrls = page.links(page_elements, "href");
			log.info("本页影片总数：{}",detailUrls.size());
			page.addNewUrls(detailUrls);
		}else if(Pattern.compile(detailUrlReg).matcher(url).find()){
			String pureName = document.select("div.summary > h2").first().text();
			PureNameAndSeasonVO pureNameAndSeason = getPureNameAndSeason(pureName, null);
			
			//获取精确字段
			List<TextNode> textNodes = document.select("#die_wrap  div.details > .contents").first().textNodes();
			List<String> precisions = getPrecisionsByInfo(textNodes);
			
			//获取资源信息
			Elements elementsResource = document.select("div.downlist li");
			List<LinkInfoVO> linkInfos = new ArrayList<LinkInfoVO>();
			for(int i=0; i<elementsResource.size(); i++){
				//先获取下载链接的描述
				Element elementResource = elementsResource.get(i);
				String downloadLinkName = elementResource.select("p > strong").first().text();
				//再获取下载链接
				String downloadLink = elementResource.select("input[type=checkbox]").first().val();
				downloadLink = downloadLink.replace("I0JleW9uZCMj", "");
				
				LinkInfoVO linkInfo = new LinkInfoVO();
				linkInfo.setName(downloadLinkName);
				linkInfo.setDownloadLink(downloadLink);
				linkInfos.add(linkInfo);
				
			}
			GatherVO gather = new GatherVO(pureNameAndSeason.getPureName(), precisions, linkInfos, null, url, null);
			fields.put(url, gather);
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

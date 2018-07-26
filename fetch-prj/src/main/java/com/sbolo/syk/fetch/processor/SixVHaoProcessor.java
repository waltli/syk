package com.sbolo.syk.fetch.processor;

import java.util.ArrayList;
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

import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.exception.AnalystException;
import com.sbolo.syk.fetch.spider.Page;
import com.sbolo.syk.fetch.spider.PageProcessor;
import com.sbolo.syk.fetch.vo.ConcludeVO;
import com.sbolo.syk.fetch.vo.LinkInfoVO;
import com.sbolo.syk.fetch.vo.PureNameAndSeasonVO;

public class SixVHaoProcessor extends ProcessorHelper implements PageProcessor {
	private static final Logger log = LoggerFactory.getLogger(SixVHaoProcessor.class);
	
	private String pageUrlReg = "^http://www\\.6vhao\\.tv/$";
	private String detailUrlReg = "^http://www\\.6vhao\\.tv/\\w+/\\d{4}-\\d{1,2}-\\d{1,2}/\\w{4,6}\\.html$";
	
	@Override
	public void before() {
		this.init();
	}

	@Override
	public void process(Page page, Map<String, Object> fields) throws Exception {
		Document document = page.getDocument();
		String url = page.getUrl().toString();
		if(Pattern.compile(pageUrlReg).matcher(url).find()){
			Elements urls_elements = document.select("div.tnlist:contains(影片) > ul > li > a");
			List<String> detailUrls = page.links(urls_elements, "href", 7);
			log.info("本页影片总数：{}",detailUrls.size());
			page.addNewUrls(detailUrls);
		}else if(Pattern.compile(detailUrlReg).matcher(url).find()){
			String fullName = document.select(".contentinfo > h1 > a").first().text();
			String pureName = Pattern.compile("[【】]|(\\[.*?\\])").matcher(fullName).replaceAll("");
			PureNameAndSeasonVO pureNameAndSeason = getPureNameAndSeason(pureName, fullName);
			Element infoElement = document.select("#text > p:matches("+RegexConstant.DYtitle+"|"+RegexConstant.YYtitle+"|"+RegexConstant.ZYtitle+")").first();
			List<String> precisions = null;
			if(infoElement != null){
				List<TextNode> textNodes = infoElement.textNodes();
				precisions = getPrecisionsByInfo(textNodes);
			}
			
			Elements tableElements = document.select("#text > table");
			Element table = null;
			
			if(tableElements.size() == 0){
				throw new AnalystException("there is no resource, url: "+ page);
			}
			
			if(tableElements.size() > 1){
				for(int i=0; i<tableElements.size(); i++){
					Element tableTem = tableElements.get(i);
					String groupTitle = tableTem.select("table tr:nth-child(1) > td").text();
					if((StringUtils.isNotBlank(pureNameAndSeason.getCnSeason()) && 
							groupTitle.contains(pureNameAndSeason.getCnSeason())) || groupTitle.contains("国语")){
						table = tableTem;
						break;
					}
				}
			}
			
			if(table == null){
				table = tableElements.get(0);
			}
			
			Elements resourceElements = table.select("div#text > table tr > td > a");

			List<LinkInfoVO> linkInfos = new ArrayList<LinkInfoVO>();
			for(int i=0; i<resourceElements.size(); i++){
				Element elementResourceALabel = resourceElements.get(i);
				//再获取下载链接的描述
				String downloadLinkName = elementResourceALabel.text();
				if(downloadLinkName.contains("密码")){
					continue;
				}
				
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
			
			//获取资源截图
			Element screensElement = document.select("div#text").first();
			List<String> shots = new ArrayList<>();
			if(screensElement != null) {
				Elements pLabels = screensElement.children();
				boolean isPrintscreenStart = false;
				for(Element p : pLabels) {
					if(p.hasText()) {
						isPrintscreenStart = true;
						continue;
					}
					Element img = p.selectFirst("img");
					if(img != null && img.nodeName().equals("img") && isPrintscreenStart) {
						String link = page.link(img, "src");
						shots.add(link);
					}
				}
			}
			
			ConcludeVO conclude = this.resolve(pureNameAndSeason.getPureName(), precisions, linkInfos, shots, url, null);
			if(conclude != null) {
				fields.put(url, conclude);
			}
		}else {
			log.warn(url+" 不符合6vhao的正则表达式，首页："+pageUrlReg+",详情页："+detailUrlReg);
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

}

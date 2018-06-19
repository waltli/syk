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
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.fetch.spider.Page;
import com.sbolo.syk.fetch.spider.PageProcessor;
import com.sbolo.syk.fetch.vo.GatherVO;
import com.sbolo.syk.fetch.vo.LinkInfoVO;
import com.sbolo.syk.fetch.vo.PureNameAndSeasonVO;

public class BT4KProcessor extends CommonProcessor implements PageProcessor {
	private static final Logger log = LoggerFactory.getLogger(BT4KProcessor.class);
    private String pageUrlReg = "http://bbs\\.bt4k\\.com/searcher\\.php\\?sch_time=today";
    private String detailUrlReg = "http://bbs\\.bt4k\\.com/read-htm-tid-\\d{5,7}\\.html";
    private String toDownUrlReg = "http://bbs\\.bt4k\\.com/down\\.php\\?aid=\\d{5,7}";
    
	@Override
	public void process(Page page, Map<String, Object> fields) throws MovieInfoFetchException {
		Document document = page.getDocument();
		String url = page.getUrl().toString();
		if(Pattern.compile(pageUrlReg).matcher(url).find()){
			Elements urls_elements = document.select("div.dlA > dl > dt >a");
			List<String> detailUrls = page.links(urls_elements, "href");
			log.info("本页影片总数：{}",detailUrls.size());
			page.addNewUrls(detailUrls);
		}else if(Pattern.compile(detailUrlReg).matcher(url).find()){
			
			//BT4K有3个页面样式，官方，用户，连续剧，很好3个页面都可以用这一种方式取出
			Elements toDownloads = document.select ("#readfloor_tpc a[title=点击进入下载页面]");
			if(toDownloads.size() == 0){
				throw new MovieInfoFetchException("There is not pureName for this web!");
			}
			
			//获取精确字段
			List<TextNode> textNodes = document.select("div#read_tpc").first().textNodes();
			List<String> precisions = getPrecisionsByInfo(textNodes);
			
			
			String fullName = document.select("h1#subject_tpc").first().text().replaceAll("(?i)bt4k", "");
			String feedName = "";
			List<LinkInfoVO> links = new ArrayList<LinkInfoVO>();
			
			for(int i=0; i<toDownloads.size(); i++){
				Element toDownload = toDownloads.get(i);
				LinkInfoVO link = new LinkInfoVO();
				String name = toDownload.select("font[color=red]").first().text();
				feedName += (" "+name);
				
				if(toDownloads.size() == 1){
					name = name + " " + fullName;
				}
				
				link.setName(name);
				String downloadLink = Utils.getHostUrl(page.getHost(), toDownload.attr("href"));
				if(Pattern.compile(toDownUrlReg).matcher(downloadLink).find()){
					String aid = downloadLink.split("=")[1];
					downloadLink = "http://bbs.bt4k.com/job.php?action=download&aid="+aid;
				}
				//不是标准的.torrent后缀，所以添加个自定义后缀，后面解析好判断
				downloadLink+="#torrent";
				link.setDownloadLink(downloadLink);
				links.add(link);
			}
			fullName = feedName + " " + fullName;
			PureNameAndSeasonVO pureNameAndSeason = getPureNameAndSeason(null, fullName);
			
			//从1开始，BT4K第一张图，是宣传图
			Elements photosElements = document.select("div#read_tpc img[onclick]");
			List<String> printscreens = new ArrayList<String>();
			for(int i=1; i<photosElements.size(); i++){
				String photoUrl = Utils.getHostUrl(page.getHost(), photosElements.get(i).attr("src"));
				printscreens.add(photoUrl);
			}
			
			
			GatherVO gather = new GatherVO(pureNameAndSeason.getPureName(), precisions, links, printscreens, url, null);
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

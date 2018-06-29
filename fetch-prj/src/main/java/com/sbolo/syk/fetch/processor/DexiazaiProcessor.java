//package com.sbolo.syk.fetch.processor;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//import okhttp3.Response;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.nodes.TextNode;
//import org.jsoup.select.Elements;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.sbolo.syk.common.exception.MovieInfoFetchException;
//import com.sbolo.syk.common.http.HttpUtils;
//import com.sbolo.syk.common.http.HttpUtils.HttpResult;
//import com.sbolo.syk.common.http.callback.HttpSendCallback;
//import com.sbolo.syk.common.tools.Utils;
//import com.sbolo.syk.fetch.spider.Page;
//import com.sbolo.syk.fetch.spider.PageProcessor;
//import com.sbolo.syk.fetch.vo.GatherVO;
//import com.sbolo.syk.fetch.vo.LinkInfoVO;
//import com.sbolo.syk.fetch.vo.PureNameAndSeasonVO;
//
//public class DexiazaiProcessor extends CommonProcessor implements PageProcessor {
//	
//	private static final Logger log = LoggerFactory.getLogger(DexiazaiProcessor.class);
//    private String pageUrlReg = "http://www\\.dexiazai\\.cc/plus/list\\.php\\?tid=\\d{2}\\&TotalResult=\\d{4}\\&PageNo=\\d";
//    private String detailUrlReg = "http://www\\.dexiazai\\.cc/dianying/\\d{4}/\\d{4}/(\\d*).html";
//    private String toDownUrlReg = "http://www\\.dexiazai\\.cc/newdown/\\?pid=(\\d*)\\&linkn=\\d+(?:#(\\w{32}))?";
//
//    @Override
//	public void process(Page page, Map<String, Object> fields) throws MovieInfoFetchException {
//		Document document = page.getDocument();
//		String url = page.getUrl().toString();
//		if(Pattern.compile(pageUrlReg).matcher(url).find()){
//			Elements detailUrls_elements = document.select("div.main_top > span > a");
//			List<String> detailUrls = page.links(detailUrls_elements, "href");
//			log.info("本页影片总数：{}",detailUrls.size());
//			page.addNewUrls(detailUrls);
////			String nextPage = page.select("div.manu > a:containsOwn(下一页)").link("href");
////			page.addNewUrl(nextPage);
//		} else if(Pattern.compile(detailUrlReg).matcher(url).find()){
//			String descName = document.select("div.main_top > span > a").first().text();
//			descName = Pattern.compile("\\[站长推荐\\]").matcher(descName).replaceAll("");
//			String pureName = descName.split("\\]\\[")[1];
//			
//			PureNameAndSeasonVO pureNameAndSeason = getPureNameAndSeason(pureName, null);
//			
//			//获取精确字段
//			List<TextNode> textNodes = document.select("div#movieinfo").first().textNodes();
//			List<String> precisions = getPrecisionsByInfo(textNodes);
//			
//			//获取资源信息
//			Elements elementsResource = document.select("div.main_text > p > a");
//			final String charset = page.getCharset();
//			List<LinkInfoVO> linkInfos = new ArrayList<LinkInfoVO>();
//			for(int i=0; i<elementsResource.size(); i++){
//				Element elementResourceAlabel = elementsResource.get(i);
//				Element nameSpan = elementResourceAlabel.child(0);
//				String downloadLinkName = nameSpan.text();
//				final String toDownloadLink = Utils.getHostUrl(page.getHost(), elementResourceAlabel.attr("href"));
//				if(!Pattern.compile(toDownUrlReg).matcher(toDownloadLink).find()){
//					//不符合将要下载的页面的链接则跳过本链接
//					continue;
//				}
//				
//				HttpResult<String> result = HttpUtils.httpGet(toDownloadLink, new HttpSendCallback<String>() {
//					@Override
//					public String onResponse(Response response)
//							throws Exception {
//						if(!response.isSuccessful()){
//							return toDownloadLink;
//						}
//						byte[] bytes = response.body().bytes();
//						String content = new String(bytes, charset);
//						Document linkDoc = Jsoup.parse(content);
//						String downloadLink = linkDoc.select("textarea#ifeng2").text();
//						return downloadLink;
//					}
//				});
//				
//				String downloadLink = null;
//				try {
//					downloadLink = result.getValue();
//				} catch (Exception e) {
//					log.error(page.getUrl(),e);
//				}
//				LinkInfoVO linkInfo = new LinkInfoVO();
//				linkInfo.setName(downloadLinkName);
//				linkInfo.setDownloadLink(downloadLink);
//				linkInfos.add(linkInfo);
//			}
//			
//			//符合条件获取资源截图
//			Elements screens_elements = document.select("div#movieinfo > img");
//			if(screens_elements.size() == 0){
//				screens_elements = document.select("div#movieinfo > div > img");
//			}
//			List<String> printscreens = page.links(screens_elements, "src");
//			GatherVO gather = new GatherVO(pureNameAndSeason.getPureName(), precisions, linkInfos, printscreens, url, null);
//			fields.put(url, gather);
//		}
//	}
//    
//	public static void main(String[] args){
//		
//    }
//
//	@Override
//	public void after() {
//		this.destroy();
//	}
//
//	@Override
//	public String startUrl() {
//		return getStartUrl();
//	}
//
//	@Override
//	public void before() {
//	}
//
//}

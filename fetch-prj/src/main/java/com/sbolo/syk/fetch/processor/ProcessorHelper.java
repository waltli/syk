package com.sbolo.syk.fetch.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.fetch.exception.ResourceException;
import com.sbolo.syk.fetch.mapper.MovieFileIndexMapper;
import com.sbolo.syk.fetch.service.MovieDictService;
import com.sbolo.syk.fetch.service.MovieInfoService;
import com.sbolo.syk.fetch.service.ResourceInfoService;
import com.sbolo.syk.fetch.spider.exception.SpiderException;
import com.sbolo.syk.fetch.tool.DoubanUtils;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.vo.MovieArroundVO;
import com.sbolo.syk.fetch.vo.LinkInfoVO;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.PureNameAndSeasonVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

@Component
public class ProcessorHelper {
	private static final Logger log = LoggerFactory.getLogger(ProcessorHelper.class);

	private String startUrl;
	
	public String getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}
	
	@Resource
	private ResourceInfoService resourceInfoService;
	
	@Resource
	private MovieInfoService movieInfoService;
	
	@Resource
	private MovieFileIndexMapper movieFileIndexMapper;
	
	@Resource
	private MovieDictService movieDictService;
	
	
	protected void resolve(Map<String, Object> fields, PureNameAndSeasonVO pureNameAndSeanson, List<String> precisions, List<LinkInfoVO> links, List<String> shots, String comeFromUrl, String doubanUrl) throws Exception {
		if(links == null || links.size() == 0) {
			log.info("No donload link! movie: [{}] url: {}", pureNameAndSeanson.getPureName(), comeFromUrl);
			return;
		}
		
		Date thisTime = new Date();
		
		//判断豆瓣URL是否为空，因为有些网站已经将豆瓣URL贴上了
		if(StringUtils.isBlank(doubanUrl)){
			doubanUrl = DoubanUtils.getDoubanUrl(pureNameAndSeanson, precisions);
		}
		
		//从豆瓣中获取到准确的movie信息
		MovieInfoVO fetchMovie = DoubanUtils.fetchMovieFromDouban(doubanUrl, thisTime);
		fetchMovie.setComeFromUrl(comeFromUrl);
		
		//根据扫描的信息构建resource对象
		List<ResourceInfoVO> fetchResources = this.getResources(links, fetchMovie, shots, comeFromUrl, thisTime);
		fetchMovie.setResourceList(fetchResources);
		
		MovieArroundVO ma = new MovieArroundVO();
		ma.setInfo(fetchMovie);
		fields.put(comeFromUrl, ma);
	}
	
    public PureNameAndSeasonVO getPureNameAndSeason(String pureName, String fullName) throws SpiderException{
    	
    	if(pureName == null && fullName == null){
    		throw new SpiderException("pureName and fullName all are null!");
    	}

    	if(fullName == null){
    		fullName = "";
    	}
    	
    	if(pureName == null){
    		Matcher m = Pattern.compile(RegexConstant.pure_name).matcher(fullName);
    		if(!m.find()){
    			throw new SpiderException("Failure to fetch pureName from \""+fullName+"\", so jump out of the page!");
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
			throw new SpiderException("The pureName is null after delete types. from \""+name+"\"");
		}
    	
    	String desc = fullName;
    	if(!fullName.contains(pureName)){
    		desc = pureName+fullName;
    	}
    	
		Integer season = null;
		String cnSeason = null;
		String noSeasonName = pureName;
		for(int i=0; i<RegexConstant.list_season.size(); i++){
			Matcher m2 = RegexConstant.list_season.get(i).matcher(StringUtil.trimAll(desc));
			if(m2.find()){
				//防止名称和季数中间没有任何间隔 例：越狱第五季
				int seasonIdx = pureName.indexOf(m2.group());
				if(seasonIdx != -1){
					noSeasonName = pureName.substring(0, seasonIdx).trim();
				}
				season = Utils.chineseNumber2Int(m2.group(1));
				cnSeason = "第"+Utils.int2ChineseNumber(m2.group(1))+"季";
				pureName = noSeasonName +" "+cnSeason;
				break;
			}
		}
		return new PureNameAndSeasonVO(pureName, noSeasonName, season, cnSeason);
    }
    
    protected void destroy(){
    }
    
    protected void init() {
    }
    
    protected List<String> getPrecisionsByInfo(String infos, String separator, String filter) {
    	String[] filters = new String[] {filter};
    	return getPrecisionsByInfo(infos, separator, filters);
    }
    
    protected List<String> getPrecisionsByInfo(String infos, String separator) {
    	return getPrecisionsByInfo(infos, separator, new String[] {});
    }
    
    protected List<String> getPrecisionsByInfo(String infos, String separator, String[] filters) {
    	if(StringUtils.isBlank(infos)) {
    		return null;
    	}
    	
    	if(filters != null && filters.length > 0) {
    		for(String filter : filters) {
    			infos = infos.replace(filter, "");
    		}
    	}
		String[] texts = StringUtil.trimAll(infos.replaceAll(separator, CommonConstants.SEPARATOR)   //将separator更换为自定义的区别符
				.replaceAll("<.*?>", ""))   //去除所有的html标签
				.split(CommonConstants.SEPARATOR);
		String directDesc = "";
		String castDesc = "";
		for(String text : texts) {
			if(Pattern.compile(RegexConstant.DYtitle).matcher(text).find() && StringUtils.isBlank(directDesc)){
				directDesc = StringUtil.replaceHTML(text);
			}else if(Pattern.compile(RegexConstant.YYtitle+"|"+RegexConstant.ZYtitle).matcher(text).find() && StringUtils.isBlank(castDesc)){
				castDesc = StringUtil.replaceHTML(text);
			}
			if(StringUtils.isNotBlank(directDesc) && StringUtils.isNotBlank(castDesc)){
				break;
			}
		}
		List<String> precisions = getPrecisions(directDesc, castDesc);
		return precisions;
	}
    
//    protected List<String> getPrecisionsByInfo(List<TextNode> textNodes) {
//		String directDesc = "";
//		String castDesc = "";
//		for(TextNode textNode : textNodes) {
//			String text = textNode.text();
//			if(Pattern.compile(RegexConstant.DYtitle).matcher(text).find() && StringUtils.isBlank(directDesc)){
//				directDesc = text;
//			}else if(Pattern.compile(RegexConstant.YYtitle+"|"+RegexConstant.ZYtitle).matcher(text).find() && StringUtils.isBlank(castDesc)){
//				castDesc = text;
//			}
//			if(StringUtils.isNotBlank(directDesc) && StringUtils.isNotBlank(castDesc)){
//				break;
//			}
//		}
//		List<String> precisions = getPrecisions(directDesc, castDesc);
//		return precisions;
//	}
    
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
    			name = name.replaceAll("[a-zA-Z]+・?[a-zA-Z]+", "");    //某些网站将中文名和英文名一并写上了，所以去掉英文名
    			name = name.split(" ")[0].replace("・", "·");  //豆瓣采用的是中文点
    		}else {
    			name = name.replaceAll("・|·", " ");  //如果是英文，豆瓣没有点，所以更换为空格
    		}
    		precisions.add(name);
    		if(precisions.size() >= count){
				break;
			}
    	}
    }
    
    
	/**
	 * 根据爬取到的链接集合等信息
	 * 构建出ResourceInfo
	 * @param links 下载链接集合
	 * @param fetchMovie 电影信息
	 * @param printScreens 电影截图
	 * @param comeFromUrl 爬取的网站
	 * @return
	 */
	private List<ResourceInfoVO> getResources(List<LinkInfoVO> links, MovieInfoVO fetchMovie, List<String> shots, String comeFromUrl, Date thisTime){
		List<ResourceInfoVO> resources = new ArrayList<ResourceInfoVO>();
		String anotherName = fetchMovie.getAnotherName();
		for(LinkInfoVO link:links){
			if(!Pattern.compile(RegexConstant.resource_protocol).matcher(link.getDownloadLink()).find()){
				log.info("The link of \""+link.getDownloadLink()+"\", that doesn't matching protocol, Supported: ed2k,thunder,magnet,torrent or baiduNet");
				continue;
			}
			link.setName(stripEngName(link.getName(), anotherName));
			ResourceInfoVO fetchResource = null;
			try {
				fetchResource = buildResourceInfoFromLinkName(fetchMovie, link, comeFromUrl, thisTime);
				fetchResource.setShotOutUrlList(shots);
				resources.add(fetchResource);
			} catch (ResourceException e) {
				log.warn(e.getMessage());
			}
		}
		return resources;
	}
	
	/**
	 * 去除英文名字，因为英文名字会干扰....
	 * @param downloadLinkName
	 * @param anotherName
	 * @return
	 */
	private String stripEngName(String downloadLinkName, String anotherName){
    	if(StringUtils.isBlank(anotherName)){
    		return downloadLinkName;
    	}
    	String engNames = Pattern.compile(RegexConstant.not_word_blank).matcher(anotherName).replaceAll("");
    	String[] engNamesSplit = engNames.split(" ");
    	for(String word : engNamesSplit){
    		if(word.length() <= 1){
    			continue;
    		}
    		downloadLinkName = Pattern.compile("(?i)"+word).matcher(downloadLinkName).replaceAll("");
		}
    	return downloadLinkName;
    }
	
	/**
     * 根据下载链接的名字，拼装ResourceInfoVO
     * 涵盖字段：
     * resourcePrn,moviePrn,season,speed,definition,quality,resolution,subtitle,format,size,episode,createTime
     * 缺乏：
     * listPhotos,downloadLink,comeFromUrl
     * 
     * @param downloadLinkName
     * @return
	 * @throws ResourceException 
     */
    private ResourceInfoVO buildResourceInfoFromLinkName(MovieInfoVO fetchMovie, LinkInfoVO link, String comeFromUrl, Date thisTime) throws ResourceException{
    	String pureName = fetchMovie.getPureName();
    	Date releaseTime = fetchMovie.getReleaseTime();
    	Integer season = fetchMovie.getPresentSeason();
    	Integer category = fetchMovie.getCategory();
    	Integer totalEpisode = fetchMovie.getTotalEpisode();
    	String moviePrn = fetchMovie.getPrn();
    	
    	String downloadLinkName = link.getName();
    	String downloadLink = link.getDownloadLink();
    	String thunderDecoding = link.getLinkDecoding();
    	
    	//从name中获取resource的相关信息
    	ResourceInfoVO newResource = FetchUtils.buildResouceInfoFromName(downloadLinkName, category, season, totalEpisode);
    	
    	newResource.setMoviePrn(moviePrn);
    	newResource.setPureName(pureName);
    	newResource.setReleaseTime(releaseTime);
    	newResource.setComeFromUrl(comeFromUrl);
    	newResource.setDownloadLinkTemp(downloadLink);
    	
    	String resourcePrn = StringUtil.getId(CommonConstants.resource_s);
    	newResource.setPrn(resourcePrn);
    	newResource.setSpeed(5);
    	newResource.setSt(MovieStatusEnum.available.getCode());
    	
    	
    	if(Pattern.compile(RegexConstant.thunder).matcher(downloadLink).find()){
    		newResource.setThunderDecoding(thunderDecoding);
    	}
		
		newResource.setCreateTime(thisTime);
		return newResource;
    }
    
}

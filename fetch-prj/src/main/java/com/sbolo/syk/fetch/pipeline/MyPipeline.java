package com.sbolo.syk.fetch.pipeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.exception.AnalystException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.fetch.entity.MovieFileIndexEntity;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.MovieLabelEntity;
import com.sbolo.syk.fetch.entity.MovieLocationEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.mapper.MovieFileIndexMapper;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.mapper.MovieLabelMapper;
import com.sbolo.syk.fetch.mapper.MovieLocationMapper;
import com.sbolo.syk.fetch.mapper.ResourceInfoMapper;
import com.sbolo.syk.fetch.spider.Pipeline;
import com.sbolo.syk.fetch.tool.BucketUtils;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.vo.ConcludeVO;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.MovieLabelVO;
import com.sbolo.syk.fetch.vo.MovieLocationVO;
import com.sbolo.syk.fetch.vo.PicVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

@Component
public class MyPipeline implements Pipeline {
	
	private static final Logger log = LoggerFactory.getLogger(MyPipeline.class);
	
	private String doubanDefaultIcon = "movie_default_large.png";
	
	private ConcurrentMap<String, String> shotUrlMapping = new ConcurrentHashMap<String, String>();
	
	private ConcurrentMap<String, Integer> torrentNameMapping = new ConcurrentHashMap<String, Integer>(); 
	
	@Autowired
	private MovieInfoMapper movieInfoMapper;
	@Autowired
	private MovieLabelMapper movieLabelMapper;
	@Autowired
	private MovieLocationMapper movieLocationMapper;
	@Autowired
	private ResourceInfoMapper resourceInfoMapper;
	@Autowired
	private MovieFileIndexMapper movieFileIndexMapper;
	
	@Override
	@Transactional
	public void process(Map<String, Object> fields) throws Exception {
		//需要下载的文件进行处理
		List<MovieFileIndexEntity> fileIdxs = downAndGetFileIndex(fields);
		//写入DB
		writeDB(fields, fileIdxs);
	}
	
	private List<MovieFileIndexEntity> downAndGetFileIndex(Map<String, Object> fields) {
		List<MovieInfoVO> allFetchMovie = new ArrayList<>();
		List<ResourceInfoVO> allFetchResource = new ArrayList<>();
		
		Set<Entry<String,Object>> entrySet = fields.entrySet();
		for(Entry<String,Object> entry : entrySet) {
			ConcludeVO vo = (ConcludeVO) entry.getValue();
			allFetchMovie.add(vo.getFetchMovie());
			allFetchResource.addAll(vo.getFetchResources());
		}
		
		Map<String, MovieFileIndexEntity> pioneer = getPioneer(allFetchMovie, allFetchResource);
		List<MovieFileIndexEntity> newFileIdxList = new ArrayList<>();
		downloadAndSetMoviePic(allFetchMovie, pioneer, newFileIdxList, new Date());
		downloadAndSetResourceFile(allFetchResource, pioneer, newFileIdxList, new Date());
		return newFileIdxList;
	}
	
	private void writeDB(Map<String, Object> fields, List<MovieFileIndexEntity> fileIdxs) {
		List<MovieInfoEntity> addMovies = new ArrayList<>();
		List<MovieLabelEntity> addLabels = new ArrayList<>();
		List<MovieLocationEntity> addLocations = new ArrayList<>();
		List<ResourceInfoEntity> addResourceInfos = new ArrayList<>();
		List<MovieInfoEntity> updateMovies = new ArrayList<>();
		List<ResourceInfoEntity> updateResourceInfos = new ArrayList<>();
		
		Set<Entry<String,Object>> entrySet = fields.entrySet();
		for(Entry<String,Object> entry : entrySet) {
			ConcludeVO vo = (ConcludeVO) entry.getValue();
			MovieInfoVO fetchMovie = vo.getFetchMovie();
			List<ResourceInfoVO> fetchResources = vo.getFetchResources();
			try {
				if(fetchMovie.getAction() == CommonConstants.insert || 
						fetchMovie.getAction() == CommonConstants.update) {
					List<MovieLabelVO> labelList = fetchMovie.getLabelList();
					if(labelList != null && labelList.size() > 0) {
						List<MovieLabelEntity> labelEntityList = VOUtils.po2vo(labelList, MovieLabelEntity.class);
						addLabels.addAll(labelEntityList);
					}
					
					List<MovieLocationVO> locationList = fetchMovie.getLocationList();
					if(locationList != null && locationList.size() > 0) {
						List<MovieLocationEntity> locationEntityList = VOUtils.po2vo(locationList, MovieLocationEntity.class);
						addLocations.addAll(locationEntityList);
					}
				}
				
				if(fetchMovie.getAction() == CommonConstants.insert) {
					MovieInfoEntity movieEntity = VOUtils.po2vo(fetchMovie, MovieInfoEntity.class);
					addMovies.add(movieEntity);
				}else if(fetchMovie.getAction() == CommonConstants.update) {
					MovieInfoEntity movieEntity = VOUtils.po2vo(fetchMovie, MovieInfoEntity.class);
					updateMovies.add(movieEntity);
				}
				
				if(fetchResources != null && fetchResources.size() > 0) {
					for(ResourceInfoVO fetchResource : fetchResources) {
						if(fetchResource.getAction() == CommonConstants.insert) {
							ResourceInfoEntity resourceInfoEntity = VOUtils.po2vo(fetchResource, ResourceInfoEntity.class);
							addResourceInfos.add(resourceInfoEntity);
						}else if(fetchResource.getAction() == CommonConstants.update) {
							ResourceInfoEntity resourceInfoEntity = VOUtils.po2vo(fetchResource, ResourceInfoEntity.class);
							updateResourceInfos.add(resourceInfoEntity);
						}
					}
				}
			} catch (Exception e) {
				log.error("分解入库时出现错误", e);
				deleteFiles(fetchMovie, fetchResources);
				throw e;
			}
		}
		
		int insertMovieSize = 0;
		int updateMovieSize = 0;
		int insertLabelSize = 0;
		int insertLocationSize = 0;
		int insertResourceSize = 0;
		int updateResourceSize = 0;
		int insertFileIdxsSize = 0;
		if(addMovies.size() > 0) {
			insertMovieSize = movieInfoMapper.insertList(addMovies);
		}
		if(updateMovies.size() > 0) {
			updateMovieSize = movieInfoMapper.updateListByPrn(updateMovies);
		}
		if(addLabels.size() > 0) {
			insertLabelSize = movieLabelMapper.insertList(addLabels);
		}
		if(addLocations.size() > 0) {
			insertLocationSize = movieLocationMapper.insertList(addLocations);
		}
		if(addResourceInfos.size() > 0) {
			insertResourceSize = resourceInfoMapper.insertList(addResourceInfos);
		}
		if(updateResourceInfos.size() > 0) {
			updateResourceSize = resourceInfoMapper.updateListByPrn(updateResourceInfos);
		}
		if(fileIdxs.size() > 0) {
			insertFileIdxsSize = movieFileIndexMapper.insertList(fileIdxs);
		}
		
		log.info("新增movieInfo条数："+insertMovieSize);
		log.info("修改movieInfo条数："+updateMovieSize);
		log.info("labels条数："+insertLabelSize);
		log.info("locations条数："+insertLocationSize);
		log.info("新增resourceInfo条数："+insertResourceSize);
		log.info("修改resourceInfo条数："+updateResourceSize);
		log.info("movie file index："+insertFileIdxsSize);
		log.info("bachAdd completion");
	}
	
	private void deleteFiles(MovieInfoVO fetchMovie, List<ResourceInfoVO> fetchResources) {
		List<String> uris = new ArrayList<>();
		
		String iconUri = fetchMovie.getIconUri();
		List<String> posterUriList = fetchMovie.getPosterUriList();
		List<String> photoUriList = fetchMovie.getPhotoUriList();
		List<String> shotUriList = new ArrayList<>();
		List<String> torrentUriList = new ArrayList<>();
		for(ResourceInfoVO fetchResource : fetchResources) {
			List<String> shotUris = fetchResource.getShotUriList();
			shotUriList.addAll(shotUris);
			if(Pattern.compile(RegexConstant.torrent).matcher(fetchResource.getDownloadLink()).find()) {
				torrentUriList.add(fetchResource.getDownloadLink());
			}
		}
		if(StringUtils.isNotBlank(iconUri)) {
			uris.add(iconUri);
		}
		if(posterUriList != null && posterUriList.size() > 0) {
			uris.addAll(posterUriList);
		}
		
		if(photoUriList != null && photoUriList.size() > 0) {
			uris.addAll(photoUriList);
		}
		
		if(shotUriList != null && shotUriList.size() > 0) {
			uris.addAll(shotUriList);
		}
		
		if(torrentUriList != null && torrentUriList.size() > 0) {
			uris.addAll(torrentUriList);
		}
		
		try {
			BucketUtils.deletes(uris);
		} catch (Exception e) {
			log.error("删除文件时出现错误", e);
		}
	}
	
	/**
	 * 从DB中获取以前 已经上传过的文件映射关系
	 * @param fetchMovie
	 * @param filter2
	 * @return
	 */
	private Map<String, MovieFileIndexEntity> getPioneer(List<MovieInfoVO> fetchMovies, List<ResourceInfoVO> filter2){
		Set<String> urls = new HashSet<>();
		
		for(MovieInfoVO fetchMovie : fetchMovies) {
			String iconUrl = fetchMovie.getIconUrl();
			List<String> posterUrlList = fetchMovie.getPosterUrlList();
			List<String> photoUrlList = fetchMovie.getPhotoUrlList();
			
			if(StringUtils.isNotBlank(iconUrl)) {
				urls.add(iconUrl);
			}
			
			if(posterUrlList != null && posterUrlList.size() > 0) {
				urls.addAll(posterUrlList);
			}
			
			if(photoUrlList != null && photoUrlList.size() > 0) {
				urls.addAll(photoUrlList);
			}
		}
		
		for(ResourceInfoVO fetchResource : filter2) {
			String downLink = fetchResource.getDownloadLink();
			List<String> shotUrlList = fetchResource.getShotUrlList();
			if(Pattern.compile(RegexConstant.torrent).matcher(downLink).find()) {
				urls.add(downLink);
			}
			if(shotUrlList != null && shotUrlList.size() > 0) {
				urls.addAll(shotUrlList);
			}
		}
		
		Map<String, MovieFileIndexEntity> maps = new HashMap<>();
		if(urls != null && urls.size() > 0) {
			List<MovieFileIndexEntity> movieFileIndexList = movieFileIndexMapper.selectBatchBySourceUrl(urls);
			for(MovieFileIndexEntity entity : movieFileIndexList) {
				maps.put(entity.getSourceUrl(), entity);
			}
		}
		return maps;
	}
	
	/**
	 * 构建icon对象
	 * @param iconUrl
	 * @return
	 */
	private PicVO buildPic(String url, int picV) {
		if(StringUtils.isBlank(url)){
			return null;
		}
		String name = url.substring(url.lastIndexOf("/")+1);
		if(name.equals(doubanDefaultIcon)) {
			return null;
		}
		String suffix = name.substring(name.lastIndexOf(".")+1);
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		String picDir = null;
		Integer picWidth = null;
		Integer picHeight = null;
		switch (picV) {
		case CommonConstants.icon_v:
			picDir = ConfigUtil.getPropertyValue("bucket.icon");
			picWidth = CommonConstants.icon_width;
			picHeight = CommonConstants.icon_height;
			break;
		case CommonConstants.poster_v:
			picDir = ConfigUtil.getPropertyValue("bucket.poster");
			picWidth = CommonConstants.poster_width;
			picHeight = CommonConstants.poster_height;
			break;
		case CommonConstants.photo_v:
			picDir = ConfigUtil.getPropertyValue("bucket.photo");
			picWidth = CommonConstants.photo_width;
			picHeight = CommonConstants.photo_height;
			break;
		case CommonConstants.shot_v:
			picDir = ConfigUtil.getPropertyValue("bucket.shot");
			picWidth = CommonConstants.shot_width;
			picHeight = CommonConstants.shot_height;
			break;
		default:
			break;
		}
		return new PicVO(url, fileName, suffix, picV, picDir, picWidth, picHeight);
	}
	
	private List<PicVO> buildPicList(List<String> urlList, int picV) {
		if(urlList == null || urlList.size() == 0){
			return null;
		}
		
		List<PicVO> picList = new ArrayList<>();
		for(String url : urlList) {
			PicVO pic = this.buildPic(url, picV);
			picList.add(pic);
		}
		return picList;
	}
	
	/**
	 * 下载视频截图并设置
	 * 
	 * @param fetchResource 获取到的resource
	 * @throws AnalystException
	 */
	private void downloadAndSetResourceFile(List<ResourceInfoVO> filter2, 
			Map<String, MovieFileIndexEntity> pioneer, List<MovieFileIndexEntity> newFileIdxList, 
			Date thisTime) {
		
		for(ResourceInfoVO fetchResource : filter2) {
			List<String> shotUrlList = fetchResource.getShotUrlList();
			if(shotUrlList == null){
				return;
			}
			List<String> shotUriList = new ArrayList<>();
			for(String shotUrl : shotUrlList){
				try {
					String repeatedShotUri = shotUrlMapping.get(shotUrl);
					if(repeatedShotUri != null) {
						shotUriList.add(repeatedShotUri);
						continue;
					}
					String suffix = shotUrl.substring(shotUrl.lastIndexOf(".")+1);
					String fileName = StringUtil.getId(CommonConstants.pic_s);
					String shotUri = this.downPicAndFixMarkWithDist(shotUrl, ConfigUtil.getPropertyValue("bucket.shot"), fileName, suffix, pioneer, CommonConstants.shot_width, CommonConstants.shot_height, newFileIdxList, CommonConstants.shot_v, thisTime);
					shotUrlMapping.put(shotUrl, shotUri);
					shotUriList.add(shotUri);
				} catch (Exception e) {
					log.error(fetchResource.getComeFromUrl(),e);
				}
			}
			
			if(shotUriList.size() > 0){
				String shotUriJson = JSON.toJSONString(shotUriList);
				fetchResource.setShotUriJson(shotUriJson);
				fetchResource.setShotUriList(shotUriList);
			}
			
			try {
				//如果是種子文件則需要下載到服務器
				if(Pattern.compile(RegexConstant.torrent).matcher(fetchResource.getDownloadLink()).find()){
					String torrentName = getTorrentName(fetchResource);
					String suffix = fetchResource.getDownloadLink().substring(fetchResource.getDownloadLink().lastIndexOf(".")+1);
					String torrentDir = ConfigUtil.getPropertyValue("bucket.torrent");
					String uri = this.downTorrentWithDist(fetchResource.getDownloadLink(), fetchResource.getTorrentBytes(), torrentDir, torrentName, suffix, pioneer, newFileIdxList, CommonConstants.torrent_v, thisTime);
					fetchResource.setDownloadLink(uri);
				}
			} catch (Throwable e) {
				//假若失败，则resource还是存的原始的地址，所以不用删除
				log.error("下载种子文件失败！ url: "+ fetchResource.getDownloadLink(), e);
			}
		}
	}
	
	/**
	 * 重新组建种子文件名
	 * @param fetchResource
	 * @return
	 */
	private String getTorrentName(ResourceInfoVO fetchResource) {
		StringBuffer fileName = new StringBuffer(fetchResource.getPureName().replaceAll(" ", "."));
		if(fetchResource.getEpisodeStart() != null){
			fileName.append(".第").append(fetchResource.getEpisodeStart())
			.append("-").append(fetchResource.getEpisodeEnd()).append("集");
		}else if(fetchResource.getEpisodeEnd() != null){
			fileName.append(".第").append(fetchResource.getEpisodeEnd()).append("集");
		}
		if(StringUtils.isNotBlank(fetchResource.getQuality())){
			fileName.append(".").append(fetchResource.getQuality());
		}
		if(StringUtils.isNotBlank(fetchResource.getResolution())){
			fileName.append(".").append(fetchResource.getResolution());
		}
		String subtitleNotice = "";
		if(StringUtils.isNotBlank(fetchResource.getSubtitle())){
			subtitleNotice = fetchResource.getSubtitle();
		}
		fileName.append(".").append(subtitleNotice).append(CommonConstants.local_sign);
		
		//去除当前页面文件重名的可能性
		StringBuffer test = new StringBuffer(fileName);
		int count = 1;
		do{
			Integer in = torrentNameMapping.get(test.toString());
			if(in == null){
				fileName = test;
				break;
			}
			test = new StringBuffer(fileName);
			test.append("(").append(count).append(")");
			count++;
		} while(true);
		
		torrentNameMapping.put(fileName.toString(), 1);
		return fileName.toString();
	}
	
	/**
	 * 收集icon和poster
	 * 将收集到的icon和poster下载到服务器
	 * 将uri设置到fetchMovie中
	 * 
	 * @param fetchMovie
	 * @param picList
	 */
	private void downloadAndSetMoviePic(List<MovieInfoVO> fetchMovies, Map<String, MovieFileIndexEntity> pioneer, List<MovieFileIndexEntity> newFileIdxList, Date thisTime){
		for(MovieInfoVO fetchMovie : fetchMovies) {
			List<PicVO> picList = new ArrayList<>();
			//获取icon
			PicVO icon = buildPic(fetchMovie.getIconUrl(), CommonConstants.icon_v);
			//获取poster
			List<PicVO> posterList = buildPicList(fetchMovie.getPosterUrlList(), CommonConstants.poster_v);
			//获取photo
			List<PicVO> photoList = buildPicList(fetchMovie.getPhotoUrlList(), CommonConstants.photo_v);
			if(icon != null) {
				picList.add(icon);
			}
			if(posterList != null && posterList.size() > 0) {
				picList.addAll(posterList);
			}
			if(photoList != null && photoList.size() > 0) {
				picList.addAll(photoList);
			}
			List<String> posterUris = new ArrayList<String>();
			List<String> photoUris = new ArrayList<String>();
			for(PicVO pic:picList){
				try {
					String picUri = this.downPicAndFixWithDist(pic.getFetchUrl(), pic.getPicDir(), pic.getFileName(), pic.getSuffix(), pioneer, pic.getPicWidth(), pic.getPicHeight(), newFileIdxList, pic.getPicV(), thisTime);
					switch (pic.getPicV()) {
					case CommonConstants.icon_v:
						fetchMovie.setIconUri(picUri);
						break;
					case CommonConstants.poster_v:
						posterUris.add(picUri);
						break;
					case CommonConstants.photo_v:
						photoUris.add(picUri);
						break;
					default:
						break;
					}
				} catch (Exception e) {
					log.error("download poster wrong, url: {}", pic.getFetchUrl(), e);
				}
			}
			
			if(posterUris.size() > 0){
				String posterUriJson = JSON.toJSONString(posterUris);
				fetchMovie.setPosterUriList(posterUris);
				fetchMovie.setPosterUriJson(posterUriJson);
			}
			
			if(photoUris.size() > 0){
				String photoUriJson = JSON.toJSONString(photoUris);
				fetchMovie.setPhotoUriList(photoUris);
				fetchMovie.setPhotoUriJson(photoUriJson);
			}
		}
	}
	
	/**
	 * 下载文件并返回uri
	 * @param url
	 * @param targetDir
	 * @param fileName
	 * @param suffix
	 * @param pioneer
	 * @param newFileIdxList
	 * @param fileV
	 * @param thisTime
	 * @return
	 * @throws Exception
	 */
	private String downTorrentWithDist(String url, byte[] bytes, String targetDir, String fileName, String suffix, Map<String, MovieFileIndexEntity> pioneer, 
			List<MovieFileIndexEntity> newFileIdxList, Integer fileV, Date thisTime) throws Exception {
		MovieFileIndexEntity movieFileIndexEntity = pioneer.get(url.trim());
		if(movieFileIndexEntity != null) {
			return movieFileIndexEntity.getFixUri();
		}
		if(bytes == null) {
			bytes = HttpUtils.getBytes(url);
		}
		String uri = upoadBucketAndGetUri(bytes, targetDir, fileName, suffix);
		addMovieFileIdx(url, uri, fileV, thisTime, newFileIdxList);
		return uri;
	}
	
	/**
	 * 下载图片并修正图片大小，返回uri
	 * @param url
	 * @param targetDir
	 * @param fileName
	 * @param suffix
	 * @param pioneer
	 * @param fixWidth
	 * @param fixHeight
	 * @param newFileIdxList
	 * @param fileV
	 * @param thisTime
	 * @return
	 * @throws Exception
	 */
	private String downPicAndFixWithDist(String url, String targetDir, String fileName, String suffix, 
			Map<String, MovieFileIndexEntity> pioneer, int fixWidth, int fixHeight, 
			List<MovieFileIndexEntity> newFileIdxList, Integer fileV, Date thisTime) throws Exception {
		MovieFileIndexEntity movieFileIndexEntity = pioneer.get(url.trim());
		if(movieFileIndexEntity != null) {
			return movieFileIndexEntity.getFixUri();
		}
		byte[] bytes = HttpUtils.getBytes(url);
		byte[] imageFix = FileUtils.imageFix(bytes, fixWidth, fixHeight, suffix);
		String uri = upoadBucketAndGetUri(imageFix, targetDir, fileName, suffix);
		addMovieFileIdx(url, uri, fileV, thisTime, newFileIdxList);
		return uri;
	}
	
	/**
	 * 下载图片并修正图片大小以及添加水印，返回uri
	 * @param url
	 * @param targetDir
	 * @param fileName
	 * @param suffix
	 * @param pioneer
	 * @param fixWidth
	 * @param fixHeight
	 * @param newFileIdxList
	 * @param fileV
	 * @param thisTime
	 * @return
	 * @throws Exception
	 */
	private String downPicAndFixMarkWithDist(String url, String targetDir, String fileName, String suffix, 
			Map<String, MovieFileIndexEntity> pioneer, int fixWidth, int fixHeight, 
			List<MovieFileIndexEntity> newFileIdxList, Integer fileV, Date thisTime) throws Exception {
		MovieFileIndexEntity movieFileIndexEntity = pioneer.get(url.trim());
		if(movieFileIndexEntity != null) {
			return movieFileIndexEntity.getFixUri();
		}
		byte[] bytes = HttpUtils.getBytes(url);
		byte[] imageFix = FileUtils.imageFix(bytes, fixWidth, fixHeight, suffix);
		byte[] imageMark = FileUtils.imageMark(imageFix, suffix);
		String uri = upoadBucketAndGetUri(imageMark, targetDir, fileName, suffix);
		addMovieFileIdx(url, uri, fileV, thisTime, newFileIdxList);
		return uri;
	}
	
	private void addMovieFileIdx(String url, String uri, Integer fileV, Date thisTime, List<MovieFileIndexEntity> newFileIdxList) {
		MovieFileIndexEntity fileIdx = new MovieFileIndexEntity();
		fileIdx.setCreateTime(thisTime);
		fileIdx.setFileV(fileV);
		fileIdx.setFixUri(uri);
		fileIdx.setSourceUrl(url.trim());
		newFileIdxList.add(fileIdx);
	}
	
	private String upoadBucketAndGetUri(byte[] bytes, String targetDir, String fileName, String suffix) throws Exception {
		String subDir = DateUtil.date2Str(new Date(), "yyyyMM");
		String saveDir = targetDir+"/"+subDir;
		BucketUtils.upload(bytes, saveDir, fileName, suffix);
		String uri = saveDir + "/" + fileName + "." +suffix;
		return uri;
	}
	
	@Override
	public void after() {
		BucketUtils.closeBucket();
		shotUrlMapping.clear();
		torrentNameMapping.clear();
	}

	@Override
	public void before() {
		BucketUtils.openBucket(ConfigUtil.getPropertyValue("bucket.name"));
	}
	
	

}

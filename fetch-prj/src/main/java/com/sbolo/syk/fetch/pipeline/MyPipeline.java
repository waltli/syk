package com.sbolo.syk.fetch.pipeline;

import java.io.InputStream;
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

import org.apache.commons.io.IOUtils;
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
import com.sbolo.syk.common.tools.BucketUtils;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.GrapicmagickUtils;
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
	
	private ConcurrentMap<String, String> shotUrlMapping;
	
	private ConcurrentMap<String, Integer> torrentNameMapping; 
	
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
			String iconOutUrl = fetchMovie.getIconOutUrl();
			List<String> posterOutUrlList = fetchMovie.getPosterOutUrlList();
			List<String> photoOutUrlList = fetchMovie.getPhotoOutUrlList();
			
			if(StringUtils.isNotBlank(iconOutUrl)) {
				urls.add(iconOutUrl);
			}
			
			if(posterOutUrlList != null && posterOutUrlList.size() > 0) {
				urls.addAll(posterOutUrlList);
			}
			
			if(photoOutUrlList != null && photoOutUrlList.size() > 0) {
				urls.addAll(photoOutUrlList);
			}
		}
		
		for(ResourceInfoVO fetchResource : filter2) {
			String downLink = fetchResource.getDownloadLink();
			List<String> shotOutUrlList = fetchResource.getShotOutUrlList();
			if(Pattern.compile(RegexConstant.torrent).matcher(downLink).find()) {
				urls.add(downLink);
			}
			if(shotOutUrlList != null && shotOutUrlList.size() > 0) {
				urls.addAll(shotOutUrlList);
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
	 * 下载视频截图并设置
	 * 
	 * @param fetchResource 获取到的resource
	 * @throws AnalystException
	 */
	private void downloadAndSetResourceFile(List<ResourceInfoVO> filter2, 
			Map<String, MovieFileIndexEntity> pioneer, List<MovieFileIndexEntity> newFileIdxList, 
			Date thisTime) {
		
		for(ResourceInfoVO fetchResource : filter2) {
			List<String> shotOutUrlList = fetchResource.getShotOutUrlList();
			if(shotOutUrlList == null){
				return;
			}
			List<String> shotUriList = new ArrayList<>();
			for(String shotOutUrl : shotOutUrlList){
				try {
					String repeatedShotUri = shotUrlMapping.get(shotOutUrl);
					if(repeatedShotUri != null) {
						shotUriList.add(repeatedShotUri);
						continue;
					}
					
					String shotUri = "";
					MovieFileIndexEntity movieFileIndexEntity = pioneer.get(shotOutUrl.trim());
					if(movieFileIndexEntity != null) {
						shotUri = movieFileIndexEntity.getFixUri();
					}else {
						shotUri = FetchUtils.uploadShotGetUri(shotOutUrl);
						MovieFileIndexEntity fileIdx = this.buildFileIndex(shotOutUrl.trim(), shotUri, CommonConstants.shot_v, thisTime);
						newFileIdxList.add(fileIdx);
					}
					
					shotUrlMapping.put(shotOutUrl, shotUri);
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
					String torrentUri = "";
					MovieFileIndexEntity movieFileIndexEntity = pioneer.get(fetchResource.getDownloadLink().trim());
					if(movieFileIndexEntity != null) {
						torrentUri = movieFileIndexEntity.getFixUri();
					}else {
						String torrentName = FetchUtils.getTorrentName(fetchResource);
						//去除当前页面文件重名的可能性
						StringBuffer sb = new StringBuffer(torrentName);
						int count = 1;
						do{
							Integer in = torrentNameMapping.get(sb.toString());
							if(in == null){
								torrentName = sb.toString();
								break;
							}
							sb = new StringBuffer(torrentName);
							sb.append("(").append(count).append(")");
							count++;
						} while(true);
						torrentNameMapping.put(sb.toString(), 1);
						
						if(fetchResource.getTorrentBytes() == null) {
							torrentUri = FetchUtils.uploadTorrentGetUri(fetchResource.getDownloadLink(), torrentName);
						}else {
							String suffix = fetchResource.getDownloadLink().substring(fetchResource.getDownloadLink().lastIndexOf(".")+1);
							torrentUri = FetchUtils.uploadTorrentGetUri(fetchResource.getTorrentBytes(), torrentName, suffix);
						}
						MovieFileIndexEntity fileIdx = this.buildFileIndex(fetchResource.getDownloadLink(), torrentUri, CommonConstants.torrent_v, thisTime);
						newFileIdxList.add(fileIdx);
					}
					fetchResource.setDownloadLink(torrentUri);
				}
			} catch (Throwable e) {
				//假若失败，则resource还是存的原始的地址，所以不用删除
				log.error("下载种子文件失败！ url: "+ fetchResource.getDownloadLink(), e);
			}
		}
	}
	
	private MovieFileIndexEntity buildFileIndex(String sourceUrl, String uri, int fileV, Date thisTime) {
		MovieFileIndexEntity fileIdx = new MovieFileIndexEntity();
		fileIdx.setCreateTime(thisTime);
		fileIdx.setFileV(fileV);
		fileIdx.setFixUri(uri);
		fileIdx.setSourceUrl(sourceUrl);
		return fileIdx;
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
			picList.add(new PicVO(fetchMovie.getIconOutUrl(), CommonConstants.icon_v));
			//获取poster
			List<String> posterOutUrlList = fetchMovie.getPosterOutUrlList();
			for(String posterOutUrl : posterOutUrlList) {
				picList.add(new PicVO(posterOutUrl, CommonConstants.poster_v));
			}
			//获取photo
			List<String> photoUrlList = fetchMovie.getPhotoOutUrlList();
			for(String photoUrl : photoUrlList) {
				picList.add(new PicVO(photoUrl, CommonConstants.photo_v));
			}
			List<String> posterUris = new ArrayList<String>();
			List<String> photoUris = new ArrayList<String>();
			for(PicVO pic:picList){
				try {
					String picUri = "";
					MovieFileIndexEntity movieFileIndexEntity = pioneer.get(pic.getFetchUrl().trim());
					if(movieFileIndexEntity != null) {
						picUri = movieFileIndexEntity.getFixUri();
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
					}else {
						switch (pic.getPicV()) {
						case CommonConstants.icon_v:
							picUri = FetchUtils.uploadIconGetUri(pic.getFetchUrl());
							fetchMovie.setIconUri(picUri);
							break;
						case CommonConstants.poster_v:
							picUri = FetchUtils.uploadPosterGetUri(pic.getFetchUrl());
							posterUris.add(picUri);
							break;
						case CommonConstants.photo_v:
							picUri = FetchUtils.uploadPhotoGetUri(pic.getFetchUrl());
							photoUris.add(picUri);
							break;
						default:
							break;
						}
						//url去重入库
						MovieFileIndexEntity fileIdx = buildFileIndex(pic.getFetchUrl(), picUri, pic.getPicV(), thisTime);
						newFileIdxList.add(fileIdx);
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
	
	@Override
	public void after() {
		BucketUtils.closeBucket();
		if(shotUrlMapping != null) {
			shotUrlMapping.clear();
			shotUrlMapping = null;
		}
		if(torrentNameMapping != null) {
			torrentNameMapping.clear();
			torrentNameMapping = null;
		}
	}

	@Override
	public void before() {
		shotUrlMapping = new ConcurrentHashMap<String, String>();
		torrentNameMapping = new ConcurrentHashMap<String, Integer>();
		BucketUtils.openBucket();
	}
	
	

}

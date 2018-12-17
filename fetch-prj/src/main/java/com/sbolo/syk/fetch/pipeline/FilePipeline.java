package com.sbolo.syk.fetch.pipeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.BucketUtils;
import com.sbolo.syk.fetch.entity.MovieFileIndexEntity;
import com.sbolo.syk.fetch.mapper.MovieFileIndexMapper;
import com.sbolo.syk.fetch.spider.Pipeline;
import com.sbolo.syk.fetch.spider.exception.AnalystException;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.PicVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

@Component
public class FilePipeline implements Pipeline {
	
	private static final Logger log = LoggerFactory.getLogger(FilePipeline.class);
	
	private ConcurrentMap<String, String> shotUrlMapping;
	
	private ConcurrentMap<String, Integer> torrentNameMapping; 
	
	@Autowired
	private MovieFileIndexMapper movieFileIndexMapper;
	
	@SuppressWarnings("unchecked")
	@Override
	public void process(Map<String, Object> fields) throws Exception {
		
		Object movieObj = fields.get("finalMovies");
		Object resourceObj = fields.get("finalResources");
		
		if(movieObj == null) {
			return;
		}
		
		List<MovieInfoVO> finalMovies = (List<MovieInfoVO>) movieObj;
		List<ResourceInfoVO> finalResources = null;
		if(resourceObj != null) {
			finalResources = (List<ResourceInfoVO>) resourceObj;
		}
		
		Map<String, MovieFileIndexEntity> pioneer = getPioneer(finalMovies, finalResources);
		List<MovieFileIndexEntity> fileIdxs = new ArrayList<>();
		downloadAndSetMoviePic(finalMovies, pioneer, fileIdxs, new Date());
		downloadAndSetResourceFile(finalResources, pioneer, fileIdxs, new Date());
		fields.put("fileIdxs", fileIdxs);
	}
	
	/**
	 * 从DB中获取以前 已经上传过的文件映射关系
	 * @param fetchMovie
	 * @param filter2
	 * @return
	 */
	private Map<String, MovieFileIndexEntity> getPioneer(List<MovieInfoVO> fetchMovies, List<ResourceInfoVO> filter2){
		Set<String> urls = new HashSet<>();
		
		if(fetchMovies != null && fetchMovies.size() > 0) {
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
		}
		
		if(filter2 != null && filter2.size() > 0) {
			for(ResourceInfoVO fetchResource : filter2) {
				String downLink = fetchResource.getDownloadLinkTemp();
				List<String> shotOutUrlList = fetchResource.getShotOutUrlList();
				if(Pattern.compile(RegexConstant.torrent).matcher(downLink).find()) {
					urls.add(downLink);
				}
				if(shotOutUrlList != null && shotOutUrlList.size() > 0) {
					urls.addAll(shotOutUrlList);
				}
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
		
		if(filter2 == null || filter2.size() == 0) {
			return;
		}
		
		for(ResourceInfoVO fetchResource : filter2) {
			//处理shot
			List<String> shotOutUrlList = fetchResource.getShotOutUrlList();
			if(shotOutUrlList != null && shotOutUrlList.size() > 0){
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
			}
			
			try {
				String torrentUri = fetchResource.getDownloadLinkTemp();
				//如果是種子文件則需要下載到服務器
				if(Pattern.compile(RegexConstant.torrent).matcher(torrentUri).find()){
					MovieFileIndexEntity movieFileIndexEntity = pioneer.get(torrentUri.trim());
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
							torrentUri = FetchUtils.uploadTorrentGetUri(torrentUri, torrentName);
						}else {
							String suffix = torrentUri.substring(torrentUri.lastIndexOf(".")+1);
							torrentUri = FetchUtils.uploadTorrentGetUri(fetchResource.getTorrentBytes(), torrentName, suffix);
						}
						MovieFileIndexEntity fileIdx = this.buildFileIndex(fetchResource.getDownloadLinkTemp(), torrentUri, CommonConstants.torrent_v, thisTime);
						newFileIdxList.add(fileIdx);
					}
				}
				fetchResource.setDownloadLink(torrentUri);
			} catch (Throwable e) {
				//假若失败，则resource还是存的原始的地址，所以不用删除
				log.error("下载种子文件失败！ url: "+ fetchResource.getDownloadLinkTemp(), e);
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
		
		if(fetchMovies == null || fetchMovies.size() == 0) {
			return;
		}
		
		for(MovieInfoVO fetchMovie : fetchMovies) {
			List<PicVO> picList = new ArrayList<>();
			//获取icon
			if(StringUtils.isNotBlank(fetchMovie.getIconOutUrl())) {
				picList.add(new PicVO(fetchMovie.getIconOutUrl(), CommonConstants.icon_v));
			}
			//获取poster
			List<String> posterOutUrlList = fetchMovie.getPosterOutUrlList();
			if(posterOutUrlList != null && posterOutUrlList.size() > 0) {
				for(String posterOutUrl : posterOutUrlList) {
					picList.add(new PicVO(posterOutUrl, CommonConstants.poster_v));
				}
			}
			//获取photo
			List<String> photoUrlList = fetchMovie.getPhotoOutUrlList();
			if(photoUrlList != null && photoUrlList.size() > 0) {
				for(String photoUrl : photoUrlList) {
					picList.add(new PicVO(photoUrl, CommonConstants.photo_v));
				}
			}
			if(picList.size() == 0) {
				continue;
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

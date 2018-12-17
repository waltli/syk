package com.sbolo.syk.fetch.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.BucketUtils;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.fetch.entity.MovieDictEntity;
import com.sbolo.syk.fetch.entity.MovieFetchRecordEntity;
import com.sbolo.syk.fetch.entity.MovieFileIndexEntity;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.mapper.MovieDictMapper;
import com.sbolo.syk.fetch.mapper.MovieFetchRecordMapper;
import com.sbolo.syk.fetch.mapper.MovieFileIndexMapper;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.mapper.ResourceInfoMapper;
import com.sbolo.syk.fetch.spider.Pipeline;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.vo.MovieDictVO;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

@Component
public class DBPipeline implements Pipeline {
	
	private static final Logger log = LoggerFactory.getLogger(DBPipeline.class);
	
	@Autowired
	private MovieInfoMapper movieInfoMapper;
	@Autowired
	private ResourceInfoMapper resourceInfoMapper;
	@Autowired
	private MovieFileIndexMapper movieFileIndexMapper;
	@Autowired
	private MovieDictMapper movieDictMapper;
	@Autowired
	private MovieFetchRecordMapper movieFetchRecordMapper;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void process(Map<String, Object> fields) throws Exception {
		
		List<MovieInfoEntity> addMovies = new ArrayList<>();
		List<ResourceInfoEntity> addResourceInfos = new ArrayList<>();
		List<MovieInfoEntity> updateMovies = new ArrayList<>();
		List<ResourceInfoEntity> updateResourceInfos = new ArrayList<>();
		List<MovieDictEntity> addDicts = new ArrayList<>();
		
		Object movieObj = fields.get("finalMovies");
		Object resourceObj = fields.get("finalResources");
		Object DictObj = fields.get("finalDicts");
		Object idxObj = fields.get("fileIdxs");
		
		List<MovieInfoVO> finalMovies = null;
		List<ResourceInfoVO> finalResources = null;
		List<MovieDictVO> finalDicts = null;
		List<MovieFileIndexEntity> fileIdxs = null;
		
		if(movieObj == null) {
			log.warn("没有可用的影片需要插入！");
			return;
		}
		finalMovies = (List<MovieInfoVO>) movieObj;
		if(resourceObj != null) {
			finalResources = (List<ResourceInfoVO>) resourceObj;
		}
		if(DictObj != null) {
			finalDicts = (List<MovieDictVO>) DictObj;
		}
		if(idxObj != null) {
			fileIdxs = (List<MovieFileIndexEntity>) idxObj;
		}
		
		try {
			
			if(finalMovies != null && finalMovies.size() > 0) {
				for(MovieInfoVO finalMovie : finalMovies) {
					if(finalMovie.getAction() == CommonConstants.insert) {
						MovieInfoEntity movieEntity = VOUtils.po2vo(finalMovie, MovieInfoEntity.class);
						addMovies.add(movieEntity);
					}else if(finalMovie.getAction() == CommonConstants.update) {
						MovieInfoEntity movieEntity = VOUtils.po2vo(finalMovie, MovieInfoEntity.class);
						updateMovies.add(movieEntity);
					}
				}
			}
			
			if(finalResources != null && finalResources.size() > 0) {
				for(ResourceInfoVO finalResource : finalResources) {
					if(finalResource.getAction() == CommonConstants.insert) {
						ResourceInfoEntity resourceInfoEntity = VOUtils.po2vo(finalResource, ResourceInfoEntity.class);
						addResourceInfos.add(resourceInfoEntity);
					}else if(finalResource.getAction() == CommonConstants.update) {
						ResourceInfoEntity resourceInfoEntity = VOUtils.po2vo(finalResource, ResourceInfoEntity.class);
						updateResourceInfos.add(resourceInfoEntity);
					}
				}
			}
			
			if(finalDicts != null && finalDicts.size() > 0) {
				List<MovieDictEntity> po2vo = VOUtils.po2vo(finalDicts, MovieDictEntity.class);
				addDicts.addAll(po2vo);
			}
			
			//构建fetch记录
			List<MovieFetchRecordEntity> addFetchRecords = FetchUtils.buildFetchRecordList(addMovies, updateMovies, addResourceInfos, updateResourceInfos, addDicts);
			
			int insertMovieSize = 0;
			int updateMovieSize = 0;
			int insertDictSize = 0;
			int insertResourceSize = 0;
			int updateResourceSize = 0;
			int insertFileIdxsSize = 0;
			int insertFetchRecordSize = 0;
			if(addMovies.size() > 0) {
				insertMovieSize = movieInfoMapper.insertList(addMovies);
			}
			if(updateMovies.size() > 0) {
				updateMovieSize = movieInfoMapper.updateListByPrn(updateMovies);
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
			if(addDicts.size() > 0) {
				insertDictSize = movieDictMapper.insertList(addDicts);
			}
			if(addFetchRecords != null && addFetchRecords.size() > 0) {
				insertFetchRecordSize = movieFetchRecordMapper.insertList(addFetchRecords);
			}
			
			log.info("===================================================================================");
			log.info("新增movieInfo条数："+insertMovieSize);
			log.info("修改movieInfo条数："+updateMovieSize);
			log.info("新增resourceInfo条数："+insertResourceSize);
			log.info("修改resourceInfo条数："+updateResourceSize);
			log.info("movie file index："+insertFileIdxsSize);
			log.info("新增Dict条数："+insertDictSize);
			log.info("新增fetchRecord条数："+insertFetchRecordSize);
			log.info("bachAdd completion");
		
		} catch (Exception e) {
			log.error("分解入库时出现错误", e);
			deleteFiles(finalMovies, finalResources);
			throw e;
		}
	}
	
	private void deleteFiles(List<MovieInfoVO> fetchMovies, List<ResourceInfoVO> fetchResources) {
		List<String> uris = new ArrayList<>();
		
		for(MovieInfoVO fetchMovie : fetchMovies) {
			String iconUri = fetchMovie.getIconUri();
			List<String> posterUriList = fetchMovie.getPosterUriList();
			List<String> photoUriList = fetchMovie.getPhotoUriList();
			
			if(StringUtils.isNotBlank(iconUri)) {
				uris.add(iconUri);
			}
			if(posterUriList != null && posterUriList.size() > 0) {
				uris.addAll(posterUriList);
			}
			
			if(photoUriList != null && photoUriList.size() > 0) {
				uris.addAll(photoUriList);
			}
		}
		
		
		for(ResourceInfoVO fetchResource : fetchResources) {
			List<String> shotUriList = fetchResource.getShotUriList();
			uris.addAll(shotUriList);
			if(Pattern.compile(RegexConstant.torrent).matcher(fetchResource.getDownloadLink()).find()) {
				uris.add(fetchResource.getDownloadLink());
			}
		}
		
		try {
			BucketUtils.deletes(uris);
		} catch (Exception e) {
			log.error("删除文件时出现错误", e);
		}
	}
	
	@Override
	public void after() {
		BucketUtils.closeBucket();
	}

	@Override
	public void before() {
		BucketUtils.openBucket();
	}
	
	

}

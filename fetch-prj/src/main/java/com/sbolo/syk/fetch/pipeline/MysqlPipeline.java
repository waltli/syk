package com.sbolo.syk.fetch.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.MovieLabelEntity;
import com.sbolo.syk.fetch.entity.MovieLocationEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
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
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

@Component
public class MysqlPipeline implements Pipeline {
	
	private static final Logger log = LoggerFactory.getLogger(MysqlPipeline.class);
	
	@Autowired
	private MovieInfoMapper movieInfoMapper;
	@Autowired
	private MovieLabelMapper MovieLabelMapper;
	@Autowired
	private MovieLocationMapper movieLocationMapper;
	@Autowired
	private ResourceInfoMapper resourceInfoMapper;
	
	@Override
	@Transactional
	public void process(Map<String, Object> fields) throws Exception {
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
				int insertMovieSize = 0;
				int updateMovieSize = 0;
				int insertLabelSize = 0;
				int insertLocationSize = 0;
				int insertResourceSize = 0;
				int updateResourceSize = 0;
				if(addMovies.size() > 0) {
					insertMovieSize = movieInfoMapper.insertList(addMovies);
				}
				if(updateMovies.size() > 0) {
					updateMovieSize = movieInfoMapper.updateListByPrn(updateMovies);
				}
				if(addLabels.size() > 0) {
					insertLabelSize = MovieLabelMapper.insertList(addLabels);
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
				
				log.info("新增movieInfo条数："+insertMovieSize);
				log.info("修改movieInfo条数："+updateMovieSize);
				log.info("labels条数："+insertLabelSize);
				log.info("locations条数："+insertLocationSize);
				log.info("新增resourceInfo条数："+insertResourceSize);
				log.info("修改resourceInfo条数："+updateResourceSize);
				log.info("bachAdd completion");
			} catch (Exception e) {
				FetchUtils.deleteMovieFile(fetchMovie);
				for(ResourceInfoVO fetchResource : fetchResources) {
					FetchUtils.deleteResourceFile(fetchResource);
				}
				throw e;
			}
		}
	}
	
	@Override
	public void after() {
	}
	
	

}

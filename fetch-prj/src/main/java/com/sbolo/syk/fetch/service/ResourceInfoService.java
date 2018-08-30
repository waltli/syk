package com.sbolo.syk.fetch.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.mapper.ResourceInfoMapper;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

@Service
public class ResourceInfoService {
	private static final Logger log = LoggerFactory.getLogger(ResourceInfoService.class);
	@Resource
	private ResourceInfoMapper resourceInfoMapper;
	
	@Resource
	private MovieInfoMapper movieInfoMapper;
	
	@Resource
	private MovieInfoService movieInfoService;
	
	public ResourceInfoEntity getOptimalResource(String moviePrn) {
		ResourceInfoEntity dbOptimalResource = resourceInfoMapper.selectOptimalResource(moviePrn);
		return dbOptimalResource;
	}
	
	public void updateStatusByMoviePrn(String moviePrn, int resourceStatus){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("moviePrn", moviePrn);
		params.put("resourceStatus", resourceStatus);
		resourceInfoMapper.signStatusByMoviePrn(params);
	}
	
	public void updateStatus(String resourcePrn, int resourceStatus){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourcePrn", resourcePrn);
		params.put("resourceStatus", resourceStatus);
		resourceInfoMapper.signStatusByPrn(params);
	}
	
	public List<ResourceInfoEntity> getListByMoviePrnOrderNoStatus(String moviePrn, Integer category){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("moviePrn", moviePrn);
		Boolean istv = false;
		if(category == MovieCategoryEnum.tv.getCode()){
			istv = true;
		}
		params.put("istv", istv);
		
		return resourceInfoMapper.selectByMoviePrnOrderNostatus(params);
	}
	
	@Transactional
	public void signDeleteable(String moviePrn, String resourcePrn){
		this.updateStatus(resourcePrn, MovieStatusEnum.deletable.getCode());
		Map<String, Object> params = new HashMap<>();
		params.put("moviePrn", moviePrn);
		params.put("resourceStatus", MovieStatusEnum.available.getCode());
		List<ResourceInfoEntity> resources = resourceInfoMapper.selectAllByMoviePrnAndStatus(params);
		if(resources == null || resources.size() == 0){
			Map<String, Object> params2 = new HashMap<>();
			params2.put("moviePrn", moviePrn);
			params2.put("movieStatus", MovieStatusEnum.deletable.getCode());
			movieInfoMapper.signStatusByPrn(params2);
		}
	}
	
	@Transactional
	public void signAvailable(String moviePrn, String resourcePrn){
		this.updateStatus(resourcePrn, MovieStatusEnum.available.getCode());
		MovieInfoEntity movie = movieInfoMapper.selectByPrn(moviePrn);
		if(movie.getSt() == MovieStatusEnum.deletable.getCode()){
			Map<String, Object> params2 = new HashMap<>();
			params2.put("moviePrn", moviePrn);
			params2.put("movieStatus", MovieStatusEnum.available.getCode());
			movieInfoMapper.signStatusByPrn(params2);
		}
	}
	
	@Transactional
	public void manualAddResources(String moviePrn, List<ResourceInfoVO> resourcesVO) throws Exception{
		
		MovieInfoEntity movieAround = movieInfoMapper.selectAssociationByMoviePrn(moviePrn);
		ResourceInfoEntity optimalResource = movieAround.getOptimalResource();
		
		Date now = new Date();
		Integer optimalIdx = 0;
		Integer maxDefinition = -1;
		Integer maxEpisodeStart = -1;
		Integer maxEpisodeEnd = -1;
		List<ResourceInfoEntity> resources = new ArrayList<>();
		for(int i=0; i<resourcesVO.size(); i++){
			ResourceInfoVO resourceVO = resourcesVO.get(i);
			ResourceInfoEntity resource = VOUtils.po2vo(resourceVO, ResourceInfoEntity.class);
			String resourcePrn = StringUtil.getId(CommonConstants.resource_s);
			resource.setPrn(resourcePrn);
			resource.setCreateTime(now);
			resource.setSt(MovieStatusEnum.available.getCode());
			
			if(movieAround.getCategory() == MovieCategoryEnum.tv.getCode()){
				if(resource.getEpisodeStart() != null && resource.getEpisodeStart() > maxEpisodeStart){
					maxEpisodeStart = resource.getEpisodeStart();
				}
				if(resource.getEpisodeEnd() != null && resource.getEpisodeEnd() > maxEpisodeEnd){
					maxEpisodeEnd = resource.getEpisodeEnd();
				}
			}
			
			//上传torrent文件
			String torrentName = FetchUtils.getTorrentName(resourceVO);
			String torrentTempUri = resourceVO.getDownloadLinkTemp();
			String torrentUri = FetchUtils.uploadTorrentFromUri(torrentTempUri, torrentName);
			resource.setDownloadLink(torrentUri);
			
			//上传shots图片
			String shotTempUriStr = resourceVO.getShotTempUriStr();
			String shotUriJson = FetchUtils.uploadShotAndGetUriJsonFromTempUris(shotTempUriStr);
			resource.setShotUriJson(shotUriJson);
			
			//资源清晰度得分
			Integer definitionScore = FetchUtils.translateDefinitionIntoScore(resourceVO.getQuality(), resourceVO.getResolution());
			resource.setDefinition(definitionScore);
			if(definitionScore > maxDefinition){
				maxDefinition = definitionScore;
				optimalIdx = i;
			}
			resources.add(resource);
		}
		
		//设置最佳资源信息
		ResourceInfoVO newOptimalResourceVO = resourcesVO.get(optimalIdx);
		MovieInfoEntity toUpMovie = movieInfoService.freshOptimalResource(movieAround.getCategory(), newOptimalResourceVO, optimalResource);
		
		if(toUpMovie != null){
			movieInfoMapper.updateByPrn(toUpMovie);
		}
		if(resources.size() != 0){
			resourceInfoMapper.insertList(resources);
		}
	}
	
}

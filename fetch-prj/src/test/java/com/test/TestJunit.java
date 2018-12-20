package com.test;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.fetchApplication;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.MovieDictEnum;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.constants.MovieTagEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.ConfigUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.ThreeDESUtils;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.fetch.entity.MovieDictEntity;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.MovieLabelEntity;
import com.sbolo.syk.fetch.entity.MovieLocationEntity;
import com.sbolo.syk.fetch.mapper.MovieDictMapper;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.mapper.MovieLabelMapper;
import com.sbolo.syk.fetch.mapper.MovieLocationMapper;
import com.sbolo.syk.fetch.service.MovieDictService;
import com.sbolo.syk.fetch.tool.DoubanUtils;
import com.sbolo.syk.fetch.vo.MovieDictVO;
import com.sbolo.syk.fetch.vo.MovieInfoVO;

@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = fetchApplication.class)
public class TestJunit {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private MovieLabelMapper movieLabelMapper;
	
	@Autowired
	private MovieLocationMapper movieLocationMapper;
	
	@Autowired
	private MovieDictMapper movieDictMapper;
	
	@Autowired
	private MovieInfoMapper movieInfoMapper;
	
	@Autowired
	private MovieDictService movieDictService;
	
	@After
	public void after() throws Exception{
//		mockMvc.perform(get("/v2/api-docs?group=接口文档").accept(MediaType.APPLICATION_JSON))
//        .andDo(SwaggerResultHandler.outputDirectory(outputDir).build())
//        .andExpect(status().isOk())
//        .andReturn();
	}
	
	@Test
	public void generMapper() throws Exception{
		List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = 
                      cp.parseConfiguration(this.getClass().getResourceAsStream("/generatorConfig.xml"));
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
        for (String warning : warnings) {
            System.out.println(warning);
        }
	}
	
	@Test
	public void encode3des() throws Exception{
		String encode3Des = ThreeDESUtils.encode3Des("moming30613");
		System.out.println(encode3Des);
	}
	
	@Test
	public void tag() throws Exception{
		List<MovieInfoEntity> selectByCategoryAndNullTag = movieInfoMapper.selectByCategoryAndNullTag(MovieCategoryEnum.tv.getCode());
		
		List<MovieInfoEntity> toupList = new ArrayList<>();
		
		for(MovieInfoEntity entity : selectByCategoryAndNullTag) {
			MovieInfoEntity newEntity = new MovieInfoEntity();
			newEntity.setPrn(entity.getPrn());
			String tagLocation = DoubanUtils.getTagLocation(MovieCategoryEnum.tv.getCode(), entity.getLocations());
			String tag = MovieTagEnum.getTagByLocation(tagLocation);
			newEntity.setTag(tag);
			toupList.add(newEntity);
		}
		
		movieInfoMapper.updateListByPrn(toupList);
		
	}
	
	@Test
	public void Dict() throws Exception{
		List<MovieInfoEntity> selectAll = movieInfoMapper.selectAll();
		
		List<MovieInfoVO> po2vos = VOUtils.po2vo(selectAll, MovieInfoVO.class);
		
		List<MovieDictVO> distDicts = this.distDict(po2vos, new Date());
		
		List<MovieDictEntity> dictAll = VOUtils.po2vo(distDicts, MovieDictEntity.class);
		
		movieDictMapper.insertList(dictAll);
	}
	
	private List<MovieDictVO> distDict(List<MovieInfoVO> fetchMovies, Date thisTime) throws Exception{
		if(fetchMovies == null || fetchMovies.size() == 0) {
			return null;
		}
		
		//放在Set里面去重
		Set<String> setLabels = new HashSet<>();
		Set<String> setLocations = new HashSet<>();
		Set<String> setTags = new HashSet<>();
		for(MovieInfoVO fetchMovie : fetchMovies) {
			String labels = fetchMovie.getLabels();
			String locations = fetchMovie.getLocations();
			String tag = fetchMovie.getTag();
			
			if(StringUtils.isNotBlank(labels)) {
				List<String> oneLabelList = Arrays.asList(labels.split(RegexConstant.slashSep));
				for(String oneLabel : oneLabelList) {
					//添加到Set里面去重
					setLabels.add(oneLabel);
				}
			}
			
			if(StringUtils.isNotBlank(locations)) {
				List<String> oneLocationList = Arrays.asList(locations.split(RegexConstant.slashSep));
				for(String oneLocation : oneLocationList) {
					//添加到Set里面去重
					setLocations.add(oneLocation);
				}
			}
			
			if(StringUtils.isNotBlank(tag)) {
				setTags.add(tag);
			}
		}
		
		if(setLabels.size() == 0 || setLocations.size() == 0 || setTags.size() == 0) {
			return null;
		}
		
		List<MovieDictVO> fetchDicts = new ArrayList<>();
		List<MovieDictEntity> all = movieDictService.getAll();
		if(all != null && all.size() > 0) {
			//放在MAP中在后面进行过滤是否存在
			Map<String, Integer> dbMap = new HashMap<>();
			for(MovieDictEntity dictEntity : all) {
				String val = dictEntity.getVal();
				String parentCode = dictEntity.getParentCode();
				String key = this.getDictKey(parentCode, val);
				dbMap.put(key, 1);
			}
			
			if(setLabels.size() > 0) {
				String parentCode = MovieDictEnum.LABEL.getCode();
				String rootKey = this.getDictKey(MovieDictEnum.ROOT.getCode(), MovieDictEnum.LABEL.getDesc());
				if(dbMap.get(rootKey) == null) {
					MovieDictVO labelRoot = new MovieDictVO(MovieDictEnum.LABEL.getCode(), MovieDictEnum.ROOT.getCode(), MovieDictEnum.LABEL.getDesc(), MovieStatusEnum.available.getCode(), 1, thisTime);
					fetchDicts.add(labelRoot);
				}
				
				for(String fetchLabel : setLabels) {
					String key = this.getDictKey(parentCode, fetchLabel);
					if(dbMap.get(key) != null) {
						continue;
					}
					MovieDictVO vo = new MovieDictVO(StringUtil.getId(CommonConstants.label_s), MovieDictEnum.LABEL.getCode(), fetchLabel, MovieStatusEnum.available.getCode(), 2, thisTime);
					fetchDicts.add(vo);
				}
			}
			
			if(setLocations.size() > 0) {
				String parentCode = MovieDictEnum.LOCATION.getCode();
				String rootKey = this.getDictKey(MovieDictEnum.ROOT.getCode(), MovieDictEnum.LOCATION.getDesc());
				if(dbMap.get(rootKey) == null) {
					MovieDictVO locationRoot = new MovieDictVO(MovieDictEnum.LOCATION.getCode(), MovieDictEnum.ROOT.getCode(), MovieDictEnum.LOCATION.getDesc(), MovieStatusEnum.available.getCode(), 1, thisTime);
					fetchDicts.add(locationRoot);
				}
				for(String fetchLocation : setLocations) {
					String key = this.getDictKey(parentCode, fetchLocation);
					if(dbMap.get(key) != null) {
						continue;
					}
					MovieDictVO vo = new MovieDictVO(StringUtil.getId(CommonConstants.location_s), MovieDictEnum.LOCATION.getCode(), fetchLocation, MovieStatusEnum.available.getCode(), 2, thisTime);
					fetchDicts.add(vo);
				}
			}
			
			if(setTags.size() > 0) {
				String parentCode = MovieDictEnum.TAG.getCode();
				String rootKey = this.getDictKey(MovieDictEnum.ROOT.getCode(), MovieDictEnum.TAG.getDesc());
				if(dbMap.get(rootKey) == null) {
					MovieDictVO tagRoot = new MovieDictVO(MovieDictEnum.TAG.getCode(), MovieDictEnum.ROOT.getCode(), MovieDictEnum.TAG.getDesc(), MovieStatusEnum.available.getCode(), 1, thisTime);
					fetchDicts.add(tagRoot);
				}
				for(String fetchTag : setTags) {
					String key = this.getDictKey(parentCode, fetchTag);
					if(dbMap.get(key) != null) {
						continue;
					}
					MovieDictVO vo = new MovieDictVO(StringUtil.getId(CommonConstants.tag_s), MovieDictEnum.TAG.getCode(), fetchTag, MovieStatusEnum.available.getCode(), 2, thisTime);
					fetchDicts.add(vo);
				}
			}
			
		}else {
			MovieDictVO labelRoot = new MovieDictVO(MovieDictEnum.LABEL.getCode(), MovieDictEnum.ROOT.getCode(), MovieDictEnum.LABEL.getDesc(), MovieStatusEnum.available.getCode(), 1, thisTime);
			MovieDictVO locationRoot = new MovieDictVO(MovieDictEnum.LOCATION.getCode(), MovieDictEnum.ROOT.getCode(), MovieDictEnum.LOCATION.getDesc(), MovieStatusEnum.available.getCode(), 1, thisTime);
			MovieDictVO tagRoot = new MovieDictVO(MovieDictEnum.TAG.getCode(), MovieDictEnum.ROOT.getCode(), MovieDictEnum.TAG.getDesc(), MovieStatusEnum.available.getCode(), 1, thisTime);
			fetchDicts.add(labelRoot);
			fetchDicts.add(locationRoot);
			fetchDicts.add(tagRoot);
			
			for(String fetchLabel : setLabels) {
				MovieDictVO vo = new MovieDictVO(StringUtil.getId(CommonConstants.label_s), MovieDictEnum.LABEL.getCode(), fetchLabel, MovieStatusEnum.available.getCode(), 2, thisTime);
				fetchDicts.add(vo);
			}
			
			for(String fetchLocation : setLocations) {
				MovieDictVO vo = new MovieDictVO(StringUtil.getId(CommonConstants.location_s), MovieDictEnum.LOCATION.getCode(), fetchLocation, MovieStatusEnum.available.getCode(), 2, thisTime);
				fetchDicts.add(vo);
			}
			
			for(String fetchTag : setTags) {
				MovieDictVO vo = new MovieDictVO(StringUtil.getId(CommonConstants.tag_s), MovieDictEnum.TAG.getCode(), fetchTag, MovieStatusEnum.available.getCode(), 2, thisTime);
				fetchDicts.add(vo);
			}
		}
		return fetchDicts;
		
	}
	
	private String getDictKey(String parentCode, String val) {
		return parentCode + "-" + val;
	}
}

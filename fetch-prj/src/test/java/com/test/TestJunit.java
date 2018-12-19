package com.test;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

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
import com.sbolo.syk.fetch.tool.DoubanUtils;
import com.sbolo.syk.fetch.vo.MovieDictVO;

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
	public void Dict() throws Exception{
		List<MovieLabelEntity> selectAll = movieLabelMapper.selectAll();
		List<MovieLocationEntity> selectAll2 = movieLocationMapper.selectAll();
		
		Set<String> labelAll = new HashSet<>();
		for(MovieLabelEntity labelEntity : selectAll) {
			String labelName = labelEntity.getLabelName();
			labelAll.add(labelName);
		}
		
		Set<String> locationAll = new HashSet<>();
		for(MovieLocationEntity locationEntity : selectAll2) {
			String locationName = locationEntity.getLocationName();
			locationAll.add(locationName);
		}
		
		List<MovieDictEntity> dictAll = new ArrayList<>();
		Date thisTime = new Date();
		MovieDictVO labelRoot = new MovieDictVO(MovieDictEnum.LABEL.getCode(), MovieDictEnum.ROOT.getCode(), MovieDictEnum.LABEL.getDesc(), MovieStatusEnum.available.getCode(), 1, thisTime);
		MovieDictVO locationRoot = new MovieDictVO(MovieDictEnum.LOCATION.getCode(), MovieDictEnum.ROOT.getCode(), MovieDictEnum.LOCATION.getDesc(), MovieStatusEnum.available.getCode(), 1, thisTime);
		MovieDictEntity labelRootEntity = VOUtils.po2vo(labelRoot, MovieDictEntity.class);
		MovieDictEntity locationRootEntity = VOUtils.po2vo(locationRoot, MovieDictEntity.class);
		dictAll.add(labelRootEntity);
		dictAll.add(locationRootEntity);
		
		
		for(String labelName : labelAll) {
			MovieDictVO vo = new MovieDictVO(StringUtil.getId(CommonConstants.label_s), MovieDictEnum.LABEL.getCode(), labelName, MovieStatusEnum.available.getCode(), 2, thisTime);
			MovieDictEntity labelEntity = VOUtils.po2vo(vo, MovieDictEntity.class);
			dictAll.add(labelEntity);
		}
		
		for(String locationName : locationAll) {
			MovieDictVO vo = new MovieDictVO(StringUtil.getId(CommonConstants.location_s), MovieDictEnum.LOCATION.getCode(), locationName, MovieStatusEnum.available.getCode(), 2, thisTime);
			MovieDictEntity locationEntity = VOUtils.po2vo(vo, MovieDictEntity.class);
			dictAll.add(locationEntity);
		}
		
		movieDictMapper.insertList(dictAll);
	}
	
	@Test
	public void tag() throws Exception{
		List<MovieInfoEntity> selectByCategory = movieInfoMapper.selectByCategory(MovieCategoryEnum.tv.getCode());
		
		List<MovieInfoEntity> toupList = new ArrayList<>();
		
		for(MovieInfoEntity entity : selectByCategory) {
			MovieInfoEntity newEntity = new MovieInfoEntity();
			newEntity.setPrn(entity.getPrn());
			String tagDesp = DoubanUtils.getTagDesp(MovieCategoryEnum.tv.getCode(), entity.getLocations());
			int tag = MovieTagEnum.getCodeByLocation(tagDesp);
			newEntity.setTag(tag);
			toupList.add(newEntity);
		}
		
		movieInfoMapper.updateListByPrn(toupList);
		
	}
}

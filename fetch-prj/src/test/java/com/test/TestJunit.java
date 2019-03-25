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
import java.util.Random;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.fetchApplication;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.enums.MovieCategoryEnum;
import com.sbolo.syk.common.enums.MovieDictEnum;
import com.sbolo.syk.common.enums.MovieStatusEnum;
import com.sbolo.syk.common.enums.MovieTagEnum;
import com.sbolo.syk.common.tools.ConfigUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.ThreeDESUtils;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.fetch.entity.MovieDictEntity;
import com.sbolo.syk.fetch.entity.MovieFetchRecordEntity;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.MovieLabelEntity;
import com.sbolo.syk.fetch.entity.MovieLocationEntity;
import com.sbolo.syk.fetch.mapper.MovieDictMapper;
import com.sbolo.syk.fetch.mapper.MovieFetchRecordMapper;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.mapper.MovieLabelMapper;
import com.sbolo.syk.fetch.mapper.MovieLocationMapper;
import com.sbolo.syk.fetch.service.MovieDictService;
import com.sbolo.syk.fetch.tool.DoubanUtils;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.vo.MovieDictVO;
import com.sbolo.syk.fetch.vo.MovieInfoVO;

@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = fetchApplication.class)
public class TestJunit {
	
	private static final Logger log = LoggerFactory.getLogger(TestJunit.class);
	
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
	
	@Autowired
	private MovieFetchRecordMapper movieFetchRecordMapper;
	
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
}

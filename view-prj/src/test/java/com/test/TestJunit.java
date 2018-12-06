package com.test;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.viewApplication;
import com.sbolo.syk.common.tools.ConfigUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.view.entity.MovieInfoEntity;
import com.sbolo.syk.view.mapper.MovieInfoMapper;
import com.sbolo.syk.view.service.MovieInfoService;

@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = viewApplication.class)
public class TestJunit {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private MovieInfoService movieInfoService;
	
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
	public void test() throws Exception{
		String[] labelArry = new String[]{"喜剧", "剧情", "爱情", "惊悚", "犯罪", "悬疑", "动作", "科幻", "冒险", "动画", "战争", "奇幻", "历史", "恐怖", "运动", "武侠", "音乐", "传记", "古装", "灾难", "家庭", "同性", "西部", "儿童", "歌舞", "情色", "纪录片", "真人秀", "舞台艺术"};
	}
	
	
}

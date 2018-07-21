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

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.fetchApplication;
import com.sbolo.syk.common.tools.ConfigUtil;

@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = fetchApplication.class)
public class TestJunit {
	
	@Autowired
    private MockMvc mockMvc;
	
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
	
	@Autowired
	private ThreadPoolTaskExecutor threadPool;
	
	@Test
	public void test() throws Exception{
		for(int i=0; i<10; i++) {
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println("a");
				}
			});
		}
		threadPool.shutdown();
		for(int i=0; i<10; i++) {
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println("b");
				}
			});
		}
	}
	
	
}

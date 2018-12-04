package com.sbolo.syk.fetch.scheduler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.common.vo.MigrateVO;
import com.sbolo.syk.fetch.entity.MovieDictEntity;
import com.sbolo.syk.fetch.entity.MovieFetchRecordEntity;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.enums.OperateTypeEnum;
import com.sbolo.syk.fetch.enums.RelyDataEnum;
import com.sbolo.syk.fetch.mapper.MovieDictMapper;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.mapper.ResourceInfoMapper;
import com.sbolo.syk.fetch.service.MigrateService;
import com.sbolo.syk.fetch.service.MovieFetchRecordService;
import com.sbolo.syk.fetch.spider.Spider;
import com.sbolo.syk.fetch.vo.SykUserVO;

import okhttp3.Response;

@Component
public class MovieScheduler {
	private static final Logger log = LoggerFactory.getLogger(MovieScheduler.class);
	
	@Resource
	private Spider spider;
	
	@Resource
	private MovieFetchRecordService movieFetchRecordService;
	
	@Resource
	private MigrateService migrateService;
	
	@Scheduled(cron="0 0 10,14,18,21 * * ?")
	public void goSpider(){
		try {
			spider.run();
			this.migrate();
		} catch (Exception e) {
			log.error("",e);
		}
	}
	
	private void migrate() throws Exception {
		List<MovieFetchRecordEntity> noMigrated = movieFetchRecordService.getNoMigrated();
		
		MigrateVO todo = migrateService.todo(noMigrated);
		
		String url = ConfigUtil.getPropertyValue("migrate.view.url");
		
		HttpResult<RequestResult> httpResult = HttpUtils.httpPost(url, todo, new HttpSendCallback<RequestResult>() {

			@Override
			public RequestResult<String> onResponse(Response response) throws Exception {
				if(!response.isSuccessful()) {
					return RequestResult.error("code: "+ response.code()+ " message: "+response.message());
				}
				String string = response.body().string();
				return JSON.parseObject(string, RequestResult.class);
			}
		});
		
		RequestResult result = httpResult.getValue();
		if(!result.getStatus()) {
			throw new Exception("数据迁移失败！cause:"+result.getError());
		}
		
		List<String> prnList = new ArrayList<>();
		for(MovieFetchRecordEntity entity : noMigrated) {
			prnList.add(entity.getPrn());
		}
		migrateService.done(prnList);
		
	}
}

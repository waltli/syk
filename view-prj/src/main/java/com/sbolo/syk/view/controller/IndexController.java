package com.sbolo.syk.view.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbolo.syk.common.annotation.Paginator;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.view.entity.MovieInfoEntity;
import com.sbolo.syk.view.entity.ResourceInfoEntity;
import com.sbolo.syk.view.service.MovieInfoService;
import com.sbolo.syk.view.service.ResourceInfoService;
import com.sbolo.syk.view.vo.MovieHotStatVO;
import com.sbolo.syk.view.vo.MovieInfoVO;
import com.sbolo.syk.view.vo.ResourceInfoVO;


@Controller
public class IndexController {
	private static final Logger log = LoggerFactory.getLogger(IndexController.class);
	
	private static final String index = "index.html";
	private static final String detail  = "detail.html";
	private static final String disclaimer = "disclaimer.html";
	private static final String about = "about.html";
	private static final String reference = "reference.html";
	private static final Integer pageSize = 10;
	
	@Autowired
	private MovieInfoService movieInfoService;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPool;
	
	@Autowired
	private ResourceInfoService resourceInfoService;
	
	@RequestMapping("")
	@Paginator
	public String go(Model model,
			@RequestParam(value="page",defaultValue="1", required=false) Integer pageNum,
            @RequestParam(value="l", required=false) String label,
            @RequestParam(value="q", required=false) String keyword) throws Exception{
		
		RequestResult<MovieInfoVO> result = movieInfoService.getAroundList(pageNum, pageSize, label, keyword);
		model.addAttribute("requestResult", result);
		return index;
	}
	
	@RequestMapping("sidebar")
	@ResponseBody
	public RequestResult<Map<String, Object>> sidebar() throws Exception{
		String[] labelArr = new String[]{"喜剧", "剧情", "爱情", "惊悚", "犯罪", "悬疑", "动作", "科幻", "冒险", "动画", "战争", "奇幻", "历史", "恐怖", "运动", "武侠", "音乐", "传记", "古装", "灾难", "家庭", "同性", "西部", "儿童", "歌舞", "情色", "纪录片", "真人秀", "舞台艺术"};
		List<String> labels = Arrays.asList(labelArr);
		Map<String, List<MovieHotStatVO>> currMonthTop = movieInfoService.getCurrMonthTop();
		Map<String, List<MovieHotStatVO>> lastMonthTop = movieInfoService.getLastMonthTop();
		Map<String, Object> m = new HashMap<>();
		if(labels.size() > 0){
			m.put("labels", labels);
		}
		if(currMonthTop != null && currMonthTop.size() > 0){
			m.put("currMonthTop", currMonthTop);
		}
		if(lastMonthTop != null && lastMonthTop.size() > 0){
			m.put("lastMonthTop", lastMonthTop);
		}
		RequestResult<Map<String, Object>> result = new RequestResult<Map<String,Object>>(m);
		return result;
	}
	
	@RequestMapping("hotDownload")
	@ResponseBody
	public RequestResult<String> hotDownload(@RequestParam(value="mi", required=true) String moviePrn){
		movieInfoService.modifyCountDownload(moviePrn);
		RequestResult<String> result = new RequestResult<>("success");
		return result;
	}
	
	@RequestMapping("detail")
	public String detail(Model model,
            @RequestParam(value="mi", required=true) final String moviePrn) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		if(StringUtils.isBlank(moviePrn) || moviePrn.equals("null")) {
			throw new BusinessException("prn不能为空");
		}
		
		MovieInfoEntity movie = movieInfoService.getMovieByPrn(moviePrn);
		if(movie == null) {
			throw new BusinessException("該影片已失效！");
		}
		List<ResourceInfoEntity> resources = resourceInfoService.getListByMoviePrnOrder(moviePrn, movie.getCategory());
		MovieInfoVO movieVO = VOUtils.po2vo(movie, MovieInfoVO.class);
		movieVO.parse();
		String optimalResourcePrn = movieVO.getOptimalResourcePrn();
		List<ResourceInfoVO> reosurcesVO = VOUtils.po2vo(resources, ResourceInfoVO.class);
		ResourceInfoVO.parse(reosurcesVO);
		for(ResourceInfoVO resourceVO:reosurcesVO){
			if(optimalResourcePrn.equals(resourceVO.getPrn())){
				List<String> shotUrlList = resourceVO.getShotUrlList();
				if(shotUrlList != null && shotUrlList.size() > 0){
					movieVO.setShotUrlList(shotUrlList);
				}
				break;
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("movie", movieVO);
		map.put("resources", reosurcesVO);
		RequestResult<Map<String, Object>> result = new RequestResult<>(map);
		model.addAttribute("result", result);
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				movieInfoService.modifyCountClick(moviePrn);
			}
		});
		
		return detail;
	}
	
	@RequestMapping("disclaimer")
	public String disclaimer(){
		return disclaimer;
	}
	
	@RequestMapping("about")
	public String about(){
		return about;
	}
	@RequestMapping("reference")
	public String reference(){
		return reference;
	}
}

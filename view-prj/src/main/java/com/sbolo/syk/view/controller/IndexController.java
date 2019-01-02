package com.sbolo.syk.view.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.TriggerEnum;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.mvc.controller.BaseController;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.view.entity.MovieInfoEntity;
import com.sbolo.syk.view.entity.ResourceInfoEntity;
import com.sbolo.syk.view.service.MovieDictService;
import com.sbolo.syk.view.service.MovieInfoService;
import com.sbolo.syk.view.service.ResourceInfoService;
import com.sbolo.syk.view.vo.MovieHotStatVO;
import com.sbolo.syk.view.vo.MovieInfoVO;
import com.sbolo.syk.view.vo.ResourceInfoVO;


@Controller
public class IndexController extends BaseController {
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
	private ResourceInfoService resourceInfoService;
	
	@Autowired
	private MovieDictService movieDictService;
	
	@RequestMapping("")
	@Paginator
	public String go(Model model,
			@RequestParam(value="page",defaultValue="1", required=false) Integer pageNum,
            @RequestParam(value="l", required=false) String label,
            @RequestParam(value="q", required=false) String keyword,
            @RequestParam(value="c", required=false) String categoryDesp,
            @RequestParam(value="t", required=false) String tag) throws Exception{
		
		RequestResult<MovieInfoVO> result = movieInfoService.getAroundList(pageNum, pageSize, label, keyword, categoryDesp, tag);
		model.addAttribute("requestResult", result);
		return index;
	}
	
	@RequestMapping("nav")
	@ResponseBody
	public RequestResult<Map<String, Object>> nav() throws Exception{
		Map<String, Object> m = new HashMap<>();
		List<String> desps = MovieCategoryEnum.getDesps();
		m.put("categories", desps);
		
		List<String> tags = movieDictService.getTags();
		m.put("tags", tags);
		
		return new RequestResult<Map<String, Object>>(m);
	}
	
	@RequestMapping("sidebar")
	@ResponseBody
	public RequestResult<Map<String, Object>> sidebar() throws Exception{
		List<String> labels = movieDictService.getLabels();
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
	public RequestResult<String> hotDownload(HttpServletRequest request, @RequestParam(value="mi", required=true) String moviePrn){
		String clientIP = this.getClientIP(request);
		movieInfoService.modiHot(moviePrn, TriggerEnum.download.getCode(), clientIP);
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
		movieInfoService.modiHot(moviePrn, TriggerEnum.click.getCode(), clientIP);
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

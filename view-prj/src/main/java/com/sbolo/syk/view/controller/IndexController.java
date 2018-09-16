package com.sbolo.syk.view.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbolo.syk.common.annotation.Paginator;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.view.entity.MovieHotStatEntity;
import com.sbolo.syk.view.entity.MovieInfoEntity;
import com.sbolo.syk.view.entity.ResourceInfoEntity;
import com.sbolo.syk.view.mapper.ResourceInfoMapper;
import com.sbolo.syk.view.service.MovieInfoService;
import com.sbolo.syk.view.service.MovieLabelService;
import com.sbolo.syk.view.service.ResourceInfoService;
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
	private static final String error = "error.html";
	private static final Integer pageSize = 10;
	
	@Autowired
	private MovieInfoService movieInfoService;
	
	@Autowired
	private MovieLabelService movieLabelService;
	
	@Autowired
	private ThreadPoolTaskExecutor threadPool;
	
	@Autowired
	private ResourceInfoService resourceInfoService;
	
	@RequestMapping("")
	@Paginator
	public String go(Model model,
			@RequestParam(value="page",defaultValue="1", required=false) Integer pageNum,
            @RequestParam(value="l", required=false) String label,
            @RequestParam(value="q", required=false) String keyword){
		RequestResult<MovieInfoVO> result = null;
		try {
			result = movieInfoService.getAroundList(pageNum, pageSize, label, keyword);
			model.addAttribute("requestResult", result);
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("", e);
		}
		return index;
	}
	
	@RequestMapping("sidebar")
	@ResponseBody
	public RequestResult<Map<String, Object>> sidebar(){
		RequestResult<Map<String, Object>> result = null;
		try {
			List<String> labels = movieLabelService.getLabels();
			Map<String, List<MovieHotStatEntity>> currMonthTop = movieInfoService.getCurrMonthTop();
			Map<String, List<MovieHotStatEntity>> lastMonthTop = movieInfoService.getLastMonthTop();
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
			result = new RequestResult<Map<String,Object>>(m);
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("",e);
		}
		
		return result;
	}
	
	@RequestMapping("hotDownload")
	@ResponseBody
	public RequestResult<String> hotDownload(@RequestParam(value="mi", required=true) String moviePrn){
		RequestResult<String> result = null;
		try {
			movieInfoService.modifyCountDownload(moviePrn);
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("",e);
		}
		
		return result;
	}
	
	@RequestMapping("detail")
	public String detail(Model model,
            @RequestParam(value="mi", required=true) final String moviePrn){
		RequestResult<Map<String, Object>> result = null;
		try {
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
			result = new RequestResult<>(map);
			model.addAttribute("result", result);
			
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					movieInfoService.modifyCountClick(moviePrn);
				}
			});
			
			return detail;
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("", e);
			model.addAttribute("result", result);
			return error;
		}
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

package com.sbolo.syk.view.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.view.entity.MovieHotStatEntity;
import com.sbolo.syk.view.entity.MovieInfoEntity;
import com.sbolo.syk.view.service.MovieInfoService;
import com.sbolo.syk.view.service.MovieLabelService;
import com.sbolo.syk.view.vo.MovieInfoVO;


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
	private MovieLabelService movieLabelService;
	
	@RequestMapping("/index")
	@Paginator
	public String go(Model model,
			@RequestParam(value="page",defaultValue="1", required=false) Integer pageNum,
            @RequestParam(value="label", required=false) String label,
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
//	
//	@RequestMapping("hotDownload")
//	@ResponseBody
//	public AjaxResult hotDownload(@RequestParam(value="mi", required=true) String movieId){
//		AjaxResult result = new AjaxResult(true);
//		try {
//			movieInfoBizService.modifyCountDownload(movieId);
//		} catch (Exception e) {
//			result.setRequestResult(false);
//			log.error("",e);
//		}
//		
//		return result;
//	}
//	
//	@RequestMapping("detail")
//	public String detail(Model model,
//            @RequestParam(value="mi", required=true) final String movieId){
//		
//		threadPool.execute(new Runnable() {
//			@Override
//			public void run() {
//				movieInfoBizService.modifyCountClick(movieId);
//			}
//		});
//		
//		MovieInfoEntity movie = movieInfoBizService.getMovieInfoByMovieId(movieId);
//		movie.parse();
//		String optimalResourceId = movie.getOptimalResourceId();
//		List<ResourceInfoEntity> resources = resourceInfoBizService.getListByMovieIdOrder(movieId, movie.getCategory());
//		ResourceInfoEntity.parse(resources);
//		for(ResourceInfoEntity resource:resources){
//			if(optimalResourceId.equals(resource.getResourceId())){
//				if(resource.getBusPhotosList() != null){
//					movie.setBusPhotosList(resource.getBusPhotosList());
//				}
//			}
//		}
//		model.addAttribute("movie", movie);
//		model.addAttribute("resources", resources);
//		return detail;
//	}
	
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

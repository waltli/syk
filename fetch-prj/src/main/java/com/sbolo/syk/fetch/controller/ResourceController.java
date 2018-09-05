package com.sbolo.syk.fetch.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.service.MovieInfoService;
import com.sbolo.syk.fetch.service.ResourceInfoService;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;
import com.sbolo.syk.fetch.vo.ResourceInfosVO;

@Controller
@RequestMapping("resource")
public class ResourceController {
	private static final Logger log = LoggerFactory.getLogger(ResourceController.class);
	private static final String list = "resource/list.jsp";
	private static final String modi_page = "resource/modi_resource.jsp";
	private static final String add_page = "resource/add_resources.jsp";
	private static final String add_result = "movie/add_result.jsp";
	
	@Resource
	private MovieInfoService movieInfoService;
	
	@Resource
	private ResourceInfoService resourceInfoService;
	
	@RequestMapping("list")
	public String list(Model model,
			@RequestParam(value="mi", required=true) String moviePrn){
		RequestResult<Map<String, Object>> result = null;
		try {
			Map<String, Object> map = new HashMap<>();
			MovieInfoVO movieVO = movieInfoService.getMovieInfoByPrn(moviePrn);
			List<ResourceInfoVO> resourceVOList = resourceInfoService.getListByMoviePrnOrderNoStatus(moviePrn, movieVO.getCategory());
			map.put("movie", movieVO);
			map.put("resources", resourceVOList);
			result = new RequestResult<>(map);
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("", e);
		}
		model.addAttribute("result", result);
		return list;
	}
	
	@RequestMapping(value="signDelete", method=RequestMethod.POST)
	@ResponseBody
	public RequestResult<String> signDelete(String moviePrn, String resourcePrn){
		RequestResult<String> result = null;
		try {
			resourceInfoService.signDeleteable(moviePrn, resourcePrn);
			result = new RequestResult<>("success");
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("",e);
		}
		return result;
	}
	
	@RequestMapping(value="signAvailable", method=RequestMethod.POST)
	@ResponseBody
	public RequestResult<String> signAvailable(String moviePrn, String resourcePrn){
		RequestResult<String> result = null;
		try {
			resourceInfoService.signAvailable(moviePrn, resourcePrn);
			result = new RequestResult<>("success");
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("",e);
		}
		return result;
	}
	
	@RequestMapping("add-page")
	public String addResourceHere(Model model, HttpServletRequest request, 
			@RequestParam(value="mi", required=true) String moviePrn){
		RequestResult<MovieInfoVO> result = null;
		try {
			MovieInfoVO movieVO = movieInfoService.getMovieInfoByPrn(moviePrn);
			result = new RequestResult<>(movieVO);
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("",e);
		}
		model.addAttribute("result", result);
		return add_page;
	}
	
	@RequestMapping(value="add-work", method=RequestMethod.POST)
	public String addResourceHere(Model model, HttpServletRequest request, String moviePrn, ResourceInfosVO resourceModel){
		RequestResult<String> result = null;
		try {
			List<ResourceInfoVO> resourcesVO = resourceModel.getResources();
			resourceInfoService.manualAddResources(moviePrn, resourcesVO);
			result = new RequestResult<>("success");
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("",e);
		}
		model.addAttribute("result", result);
		return add_result;
	}
	
	@RequestMapping("modi-page")
	public String modiResourceHere(Model model, 
			@RequestParam(value="ri", required=true) String resourcePrn){
		RequestResult<Map<String, Object>> result = null;
		try {
			ResourceInfoVO resourceVO = resourceInfoService.getResourceByPrn(resourcePrn);
			if(resourceVO == null){
				throw new Exception("该条资源不存在，请重试！");
			}
			MovieInfoVO movieVO = movieInfoService.getMovieInfoByPrn(resourceVO.getMoviePrn());
			if(movieVO == null){
				throw new Exception("该条资源对应的Movie不存在！");
			}
			Map<String, Object> map = new HashMap<>();
			map.put("resource", resourceVO);
			map.put("movie", movieVO);
			result = new RequestResult<>(map);
			model.addAttribute("result", result);
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("",e);
		}
		return modi_page;
	}
	
	@RequestMapping(value="modi-work", method={RequestMethod.POST})
	public String modiWork(Model model, ResourceInfoVO newResource, boolean isOptimal){
		RequestResult<String> result = null;
		try {
			resourceInfoService.modiResource(newResource, isOptimal);
			result = new RequestResult<>("sucess");
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("",e);
		}
		model.addAttribute("result", result);
		return add_result;
	}
}

package com.sbolo.syk.fetch.controller;

import java.util.Date;
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
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.mapper.ResourceInfoMapper;
import com.sbolo.syk.fetch.service.MovieInfoService;
import com.sbolo.syk.fetch.service.ResourceInfoService;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;
import com.sbolo.syk.fetch.vo.ResourceInfosVO;

@Controller
@RequestMapping("resource")
public class ResourceController {
	private static final Logger log = LoggerFactory.getLogger(ResourceController.class);
	private static final String list = "resource/list.html";
	private static final String modi_page = "resource/modi_resource.html";
	private static final String add_page = "resource/add_resources.html";
	private static final String add_result = "resource/add_result.html";
	
	@Resource
	private MovieInfoService movieInfoService;
	
	@Resource
	private ResourceInfoService resourceInfoService;
	
	@Resource
	private MovieInfoMapper movieInfoMapper;
	
	@Resource
	private ResourceInfoMapper resourceInfoMapper;
	
	@RequestMapping("list")
	public String list(Model model,
			@RequestParam(value="mi", required=true) String moviePrn) throws Exception{
		Map<String, Object> map = new HashMap<>();
		MovieInfoVO movieVO = movieInfoService.getMovieInfoByPrn(moviePrn);
		List<ResourceInfoVO> resourceVOList = resourceInfoService.getListByMoviePrnOrderNoStatus(moviePrn, movieVO.getCategory());
		map.put("movie", movieVO);
		map.put("resources", resourceVOList);
		RequestResult<Map<String, Object>> result = new RequestResult<>(map);
		model.addAttribute("result", result);
		return list;
	}
	
	@RequestMapping(value="signDelete", method=RequestMethod.POST)
	@ResponseBody
	public RequestResult<String> signDelete(String moviePrn, String resourcePrn){
		resourceInfoService.signDeleteable(moviePrn, resourcePrn);
		RequestResult<String> result = new RequestResult<>("success");
		return result;
	}
	
	@RequestMapping(value="signAvailable", method=RequestMethod.POST)
	@ResponseBody
	public RequestResult<String> signAvailable(String moviePrn, String resourcePrn){
		resourceInfoService.signAvailable(moviePrn, resourcePrn);
		RequestResult<String> result = new RequestResult<>("success");
		return result;
	}
	
	@RequestMapping("add-page")
	public String addResourceHere(Model model, HttpServletRequest request, 
			@RequestParam(value="mi", required=true) String moviePrn) throws Exception{
		MovieInfoVO movieVO = movieInfoService.getMovieInfoByPrn(moviePrn);
		RequestResult<MovieInfoVO> result = new RequestResult<>(movieVO);
		model.addAttribute("result", result);
		return add_page;
	}
	
	@RequestMapping(value="add-work", method=RequestMethod.POST)
	public String addResourceHere(Model model, HttpServletRequest request, String moviePrn, ResourceInfosVO resourceModel) throws Exception{
		List<ResourceInfoVO> resources = resourceModel.getResources();
		MovieInfoEntity movieAround = movieInfoMapper.selectAssociationByMoviePrn(moviePrn);
		try {
			resourceInfoService.addResourcesProcess(movieAround, resources);
			MovieInfoVO toUpMovie = resourceInfoService.setOptimalAndGet(movieAround, resources);
			resourceInfoService.manualAddResources(toUpMovie, resources);
		} catch (Exception e) {
			FetchUtils.deleteFiles(resources);
			throw e;
		}
		RequestResult<String> result = new RequestResult<>("success");
		model.addAttribute("result", result);
		return add_result;
	}
	
	@RequestMapping("modi-page")
	public String modiResourceHere(Model model, 
			@RequestParam(value="ri", required=true) String resourcePrn) throws Exception{
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
		RequestResult<Map<String, Object>> result = new RequestResult<>(map);
		model.addAttribute("result", result);
		return modi_page;
	}
	
	@RequestMapping(value="modi-work", method={RequestMethod.POST})
	public String modiWork(Model model, ResourceInfoVO newResource, boolean isOptimal) throws Exception{
		ResourceInfoEntity dbResource = resourceInfoMapper.selectByPrn(newResource.getPrn());
		if(dbResource == null){
			throw new Exception("该资源信息不存在，修改失败！");
		}
		Date thisTime = new Date();
		ResourceInfoVO changeResource = resourceInfoService.modiResourceProcess(newResource, dbResource, thisTime);
		MovieInfoVO toUpMovie = resourceInfoService.getToUpMovie(isOptimal, changeResource, dbResource.getMoviePrn(), thisTime);
		resourceInfoService.modiResource(changeResource, toUpMovie);
		RequestResult<String> result = new RequestResult<>("sucess");
		model.addAttribute("result", result);
		return add_result;
	}
}

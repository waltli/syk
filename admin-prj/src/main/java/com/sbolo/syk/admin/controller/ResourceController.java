package com.sbolo.syk.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbolo.syk.po.MovieInfoEntity;
import com.sbolo.syk.po.ResourceInfoEntity;
import com.sbolo.syk.service.MovieInfoBizService;
import com.sbolo.syk.service.ResourceInfoBizService;
import com.sbolo.syk.service.ResourceService;
import com.sbolo.syk.ui.AjaxResult;
import com.sbolo.syk.vo.ResourceInfosVO;

@Controller
@RequestMapping("resource")
public class ResourceController {
	private static final Logger log = LoggerFactory.getLogger(ResourceController.class);
	private static final String list = "resource/list.jsp";
	private static final String modi_page = "resource/modi_resource.jsp";
	private static final String add_page = "resource/add_resources.jsp";
	private static final String add_result = "movie/add_result.jsp";
	
	@Resource
	private ResourceInfoBizService resourceInfoBizService;
	
	@Resource
	private MovieInfoBizService movieInfoBizService;
	
	@Resource
	private ResourceService resourceService;
	
	@RequestMapping("list")
	public String list(Model model,
			@RequestParam(value="mi", required=true) String movieId){
		MovieInfoEntity movie = movieInfoBizService.getMovieInfoByMovieId(movieId);
		List<ResourceInfoEntity> resources = resourceInfoBizService.getListByMovieIdOrderNoStatus(movieId, movie.getCategory());
		ResourceInfoEntity.parse(resources);
		model.addAttribute("movie", movie);
		model.addAttribute("resources", resources);
		
		return list;
	}
	
	@RequestMapping(value="signDelete", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult signDelete(String movieId, String resourceId){
		AjaxResult result = new AjaxResult(true);
		try {
			resourceService.signDeleteable(movieId, resourceId);
		} catch (Exception e) {
			result.setRequestResult(false);
			result.setError(e.getMessage());
			log.error("",e);
		}
		return result;
	}
	
	@RequestMapping(value="signAvailable", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult signAvailable(String movieId, String resourceId){
		AjaxResult result = new AjaxResult(true);
		try {
			resourceService.signAvailable(movieId, resourceId);
		} catch (Exception e) {
			result.setRequestResult(false);
			result.setError(e.getMessage());
			log.error("",e);
		}
		return result;
	}
	
	@RequestMapping("add-page")
	public String addResourceHere(Model model, HttpServletRequest request, 
			@RequestParam(value="mi", required=true) String movieId){
		MovieInfoEntity movie = movieInfoBizService.getMovieInfoByMovieId(movieId);
		movie.parse();
		model.addAttribute("movie", movie);
		return add_page;
	}
	
	@RequestMapping(value="add-work", method=RequestMethod.POST)
	public String addResourceHere(Model model, HttpServletRequest request, String movieId, ResourceInfosVO resourceModel){
		List<ResourceInfoEntity> resources = resourceModel.getResources();
		String oldRootPath = request.getSession().getServletContext().getRealPath("");
		try {
			resourceService.manualAddResources(movieId, resources, oldRootPath);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			log.error("",e);
		}
		return add_result;
	}
	
	@RequestMapping("modi-page")
	public String modiResourceHere(Model model, 
			@RequestParam(value="ri", required=true) String resourceId){
		try {
			ResourceInfoEntity resource = resourceService.getResourceByResourceId(resourceId);
			if(resource == null){
				throw new Exception("该条资源不存在，请重试！");
			}
			resource.parse();
			MovieInfoEntity movie = movieInfoBizService.getMovieInfoByMovieId(resource.getMovieId());
			if(movie == null){
				throw new Exception("该条资源对应的Movie不存在！");
			}
			movie.parse();
			model.addAttribute("resource", resource);
			model.addAttribute("movie", movie);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			log.error("",e);
		}
		return modi_page;
	}
	
	@RequestMapping(value="modi-work", method={RequestMethod.POST})
	public String modiWork(Model model, HttpServletRequest request, ResourceInfoEntity modiResource, boolean isOptimal){
		String tempRootPath = request.getSession().getServletContext().getRealPath("");
		try {
			resourceService.modiResource(modiResource, tempRootPath, isOptimal);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			log.error("",e);
		}
		return add_result;
	}
}

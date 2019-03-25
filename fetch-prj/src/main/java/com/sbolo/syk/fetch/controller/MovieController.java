package com.sbolo.syk.fetch.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbolo.syk.common.annotation.Paginator;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.tools.ConfigUtils;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.ui.ResultApi;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.service.MovieInfoService;
import com.sbolo.syk.fetch.tool.DoubanUtils;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;
import com.sbolo.syk.fetch.vo.ResourceInfosVO;

@Controller
@RequestMapping("movie")
public class MovieController {
	private static final Logger log = LoggerFactory.getLogger(MovieController.class);
	
	private static final String list = "movie/list.html";
	private static final String search_from_douban = "movie/search_from_douban.html";
	private static final String add_page = "movie/add_page.html";
	private static final String modi_movie_page = "movie/modi_movie_page.html";
	private static final String add_result = "movie/add_result.html";
	private static final String existed = "existed.html";
	private static final Integer pageSize = 10;
	
	@Resource
	private MovieInfoService movieInfoService;
	
	@Resource
	private ThreadPoolTaskExecutor threadPool;
	
	@RequestMapping("list")
	@Paginator
	public String go(Model model, HttpServletRequest request, 
			@RequestParam(value="page",defaultValue="1", required=false) Integer pageNum,
            @RequestParam(value="q", required=false) String keyword) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		ResultApi<MovieInfoVO> result = movieInfoService.getAroundList(pageNum, pageSize, null, keyword);
		model.addAttribute("result", result);
		return list;
	}
	
	@RequestMapping("fetch-list")
	@ResponseBody
	public ResultApi<MovieInfoVO> doubanResult(@RequestParam(value="q", required=true) String query) throws Exception{
		List<MovieInfoVO> fetchMovies = DoubanUtils.fetchListFromDouban(query);
		ResultApi<MovieInfoVO> result = new ResultApi<>(fetchMovies);
		return result;
	}
	
	@RequestMapping("fetch-detail")
	@ResponseBody
	public ResultApi<MovieInfoVO> doubanDetail(@RequestParam(value="doubanId", required=true) String doubanId) throws Exception{
		String doubanUrl = StringUtil.jointDoubanUrl(doubanId);
		MovieInfoVO fetchMovie = DoubanUtils.fetchMovieFromDouban(doubanUrl, new Date());
		ResultApi<MovieInfoVO> result = new ResultApi<>(fetchMovie);
		return result;
		
	}
	
	@RequestMapping(value="signDelete", method=RequestMethod.POST)
	@ResponseBody
	public ResultApi<String> signDelete(String moviePrn){
		movieInfoService.signDeleteable(moviePrn);
		ResultApi<String> result = new ResultApi<>("success");
		return result;
	}
	
	@RequestMapping(value="signAvailable", method=RequestMethod.POST)
	@ResponseBody
	public ResultApi<String> signAvailable(String moviePrn){
		movieInfoService.signAvailable(moviePrn);
		ResultApi<String> result = new ResultApi<>("success");
		return result;
	}
	
	@RequestMapping("add-page")
	public String addHere(Model model,
			@RequestParam(value="doubanId", required=false) String doubanId){
		if(StringUtils.isNotBlank(doubanId)){
			MovieInfoEntity movie = movieInfoService.getMovieInfoByDoubanId(doubanId);
			if(movie != null){
				return "redirect:existed?mi="+movie.getPrn();
			}
		}
		if(StringUtils.isNotBlank(doubanId)){
			model.addAttribute("doubanId", doubanId);
		}
		return add_page;
	}
	
	@RequestMapping(value="add-work", method={RequestMethod.POST})
	public String addWork(Model model, HttpServletRequest request, MovieInfoVO movie, ResourceInfosVO resourceModel) throws Exception {
		Date releaseTime = DateUtil.str2Date(movie.getReleaseTimeStr());
		MovieInfoEntity existMovie = movieInfoService.getMovieInfoByPureNameAndReleaseTime(movie.getPureName(), releaseTime);
		if(existMovie != null){
			return "redirect:existed?mi="+existMovie.getPrn();
		}
		List<ResourceInfoVO> resources = resourceModel.getResources();
		try {
			movieInfoService.manualProcess(movie, resources);
			movieInfoService.manualAddAround(movie, resources);
		} catch (Exception e) {
			FetchUtils.deleteFiles(movie, resources);
			throw e;
		}
		ResultApi<MovieInfoVO> result = new ResultApi<>(movie);
		model.addAttribute("result", result);
		return add_result;
	}
	
	@RequestMapping("exists")
	@ResponseBody
	public ResultApi<Boolean> exists(String pureName){
		ResultApi<Boolean> result = null;
		MovieInfoEntity movie = movieInfoService.getMovieInfoByPureName(pureName);
		if(movie == null){
			result = new ResultApi<>(false);
		}else {
			result = new ResultApi<>(true);
		}
		return result;
	}
	

	@RequestMapping("existed")
	public String existed(Model model,
			@RequestParam(value="mi", required=true) String moviePrn) throws Exception{
		if(StringUtils.isBlank(moviePrn)) {
			throw new BusinessException("moviePrn为空！");
		}
		MovieInfoVO movieVO = movieInfoService.getMovieInfoByPrn(moviePrn);
		ResultApi<MovieInfoVO> result = new ResultApi<>(movieVO);
		model.addAttribute("result", result);
		return existed;
	}
	
	@RequestMapping("modi-movie-page")
	public String updateHere(Model model,
			@RequestParam(value="mi", required=true) String moviePrn) throws Exception{
		MovieInfoVO movieVO = movieInfoService.getMovieInfoByPrn(moviePrn);
		if(movieVO == null){
			throw new Exception("未查询到prn为"+moviePrn+"的影片信息！");
		}
		ResultApi<MovieInfoVO> result = new ResultApi<>(movieVO);
		model.addAttribute("result", result);
		
		return modi_movie_page;
	}
	
	@RequestMapping(value="modi-movie-work", method={RequestMethod.POST})
	public String updateWork(Model model, HttpServletRequest request, MovieInfoVO movie) throws Exception{
		try {
			MovieInfoVO changeMovie = movieInfoService.modiMovieProcess(movie);
			movieInfoService.modiMovie(changeMovie);
		} catch (Exception e) {
			FetchUtils.deleteFiles(movie, null);
			throw e;
		}
		ResultApi<MovieInfoVO> result = new ResultApi<>(movie);
		model.addAttribute("result", result);
		return add_result;
	}
	
	
	@RequestMapping("search-from-douban")
	public String addFromDouban(){
		return search_from_douban;
	}
	
	@RequestMapping(value="set-optimal", method=RequestMethod.POST)
	@ResponseBody
	public ResultApi<String> setOptimal(String moviePrn, String optimalResourcePrn){
		MovieInfoEntity toUpMovie = new MovieInfoEntity();
		toUpMovie.setOptimalResourcePrn(optimalResourcePrn);
		toUpMovie.setPrn(moviePrn);
		movieInfoService.updateByPrn(toUpMovie);
		ResultApi<String> result = new ResultApi<>("success");
		return result;
	}
	
	@RequestMapping("download-icon")
	@ResponseBody
	public ResultApi<Map<String, Object>> downloadIcon(HttpServletRequest request, String url) throws Exception{
		String subDir = FetchUtils.saveTempIcon(url);
		Map<String, Object> map = new HashMap<>();
		map.put("subDir", subDir);
		String root = ConfigUtils.getPropertyValue("fs.temp.mapping");
		map.put("uri", root+subDir);
		ResultApi<Map<String, Object>> result = new ResultApi<>(map);
		return result;
	}
	
	@RequestMapping("download-photo")
	@ResponseBody
	public ResultApi<Map<String, Object>> downloadPhoto(HttpServletRequest request, String urlStr) throws Exception{
		String[] urlArr = urlStr.split(",");
		List<String> subDirList = new ArrayList<>();
		List<String> tempUriList = new ArrayList<>();
		for(String url : urlArr) {
			String subDir = FetchUtils.saveTempPhoto(url);
			String root = ConfigUtils.getPropertyValue("fs.temp.mapping");
			subDirList.add(subDir);
			tempUriList.add(root+subDir);
		}
		
		if(tempUriList.size() > 0){
			Map<String, Object> map = new HashMap<>();
			map.put("subDirList", subDirList);
			map.put("tempUriList", tempUriList);
			return new ResultApi<Map<String, Object>>(map);
		}else {
			throw new BusinessException("下载photo计数为零");
		}
	}
	
	
	@RequestMapping("download-posters")
	@ResponseBody
	public ResultApi<Map<String, Object>> downloadPosters(HttpServletRequest request, final String pageUrl, final String iconName) throws Exception{
		List<String> posterUrlList = DoubanUtils.getPosterUrlList(pageUrl, iconName);
		
		if(posterUrlList == null || posterUrlList.size() <= 0){
			throw new BusinessException("在"+pageUrl+"中没有获取到poster");
		}
		
		List<String> posterTempUriList = new ArrayList<>();
		List<String> subDirList = new ArrayList<>();
		String root = ConfigUtils.getPropertyValue("fs.temp.mapping");
		for(String posterUrl : posterUrlList) {
			String subDir = FetchUtils.saveTempPoster(posterUrl);
			subDirList.add(subDir);
			posterTempUriList.add(root+subDir);
		}
		
		if(posterTempUriList.size() > 0){
			Map<String, Object> map = new HashMap<>();
			map.put("subDirList", subDirList);
			map.put("posterTempUriList", posterTempUriList);
			return new ResultApi<>(map);
		}else {
			throw new BusinessException("下载poster计数为零");
		}
	}
}

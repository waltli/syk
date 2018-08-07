package com.sbolo.syk.admin.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import okhttp3.Response;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.admin.entity.MovieInfoEntity;
import com.sbolo.syk.admin.service.MovieInfoService;
import com.sbolo.syk.admin.vo.MovieInfoVO;
import com.sbolo.syk.common.annotation.Paginator;
import com.sbolo.syk.common.ui.RequestResult;

@Controller
@RequestMapping("movie")
public class MovieController {
	private static final Logger log = LoggerFactory.getLogger(MovieController.class);
	
	private static final String list = "movie/list.jsp";
	private static final String search_from_douban = "movie/search_from_douban.jsp";
	private static final String add_page = "movie/add_page.jsp";
	private static final String modi_movie_page = "movie/modi_movie_page.jsp";
	private static final String add_result = "movie/add_result.jsp";
	private static final String existed = "existed.jsp";
	private static final Integer pageSize = 10;
	
	@Resource
	private MovieInfoService movieInfoService;
	
	@Resource
	private ResourceInfoBizService resourceInfoBizService;
	
	@Resource
	private ThreadPoolTaskExecutor threadPool;
	
	@Resource
	private LabelMappingBizService labelMappingBizService;
	
	@Resource
	private MovieService movieService;
	
	@RequestMapping("list")
	@Paginator
	public String go(Model model, HttpServletRequest request, 
			@RequestParam(value="page",defaultValue="1", required=false) Integer pageNum,
            @RequestParam(value="q", required=false) String keyword){
		RequestResult<MovieInfoVO> result = null;
		try {
			result = movieInfoService.getAroundList(pageNum, pageSize, null, keyword);
			model.addAttribute("requestResult", result);
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("", e);
		}
		return list;
	}
	
	@RequestMapping("fetch-list")
	@ResponseBody
	public RequestResult<MovieInfoEntity> doubanResult(@RequestParam(value="q", required=true) String query){
		RequestResult<MovieInfoEntity> result = null;
		try {
			List<MovieInfoEntity> fetchMovies = movieInfoService.fetchFromDouban(query);
			result = new RequestResult<>(fetchMovies);
		} catch (Exception e) {
			result = RequestResult.error(e);
			log.error("",e);
		}
		return result;
	}
	
	@RequestMapping("fetch-detail")
	@ResponseBody
	public AjaxResult doubanDetail(@RequestParam(value="doubanId", required=true) String doubanId){
		AjaxResult result = new AjaxResult(true);
		try {
			MovieInfoEntity fetchMovie = movieService.fetchDetailFromDouban(doubanId);
			result.put("movie", fetchMovie);
		} catch (Exception e) {
			result.setRequestResult(false);
			result.setError(e.getMessage());
			log.error("",e);
		}
		return result;
		
	}
	
	@RequestMapping(value="signDelete", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult signDelete(String movieId){
		AjaxResult result = new AjaxResult(true);
		try {
			movieService.signDeleteable(movieId);
		} catch (Exception e) {
			result.setRequestResult(false);
			result.setError(e.getMessage());
			log.error("",e);
		}
		return result;
	}
	
	@RequestMapping(value="signAvailable", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult signAvailable(String movieId){
		AjaxResult result = new AjaxResult(true);
		try {
			movieService.signAvailable(movieId);
		} catch (Exception e) {
			result.setRequestResult(false);
			result.setError(e.getMessage());
			log.error("",e);
		}
		return result;
	}
	
	@RequestMapping("add-page")
	public String addHere(Model model,
			@RequestParam(value="doubanId", required=false) String doubanId){
		if(StringUtils.isNotBlank(doubanId)){
			MovieInfoEntity movie = movieInfoBizService.getMovieInfoByDoubanId(doubanId);
			if(movie != null){
				return "redirect:existed?mi="+movie.getMovieId();
			}
		}
		if(StringUtils.isNotBlank(doubanId)){
			model.addAttribute("doubanId", doubanId);
		}
		return add_page;
	}
	
	@RequestMapping(value="add-work", method={RequestMethod.POST})
	public String addWork(Model model, HttpServletRequest request, MovieInfoEntity movie, ResourceInfosVO resourceModel) {
		Date releaseTime = Utils.str2DateByFormat(movie.getReleaseTimeStr());
		MovieInfoEntity existMovie = movieInfoBizService.getMovieInfoByPureNameAndReleaseTime(movie.getPureName(), releaseTime);
		if(existMovie != null){
			return "redirect:existed?mi="+movie.getMovieId();
		}
		
		List<ResourceInfoEntity> resources = resourceModel.getResources();
		String rootPath = request.getSession().getServletContext().getRealPath("");
		try {
			movieService.manualAddAround(movie, resources, rootPath);
			model.addAttribute("requestResult", true);
		} catch (ParseException e) {
			model.addAttribute("requestResult", false);
			model.addAttribute("error", e.getMessage());
			log.error("",e);
		}
		return add_result;
	}
	
	@RequestMapping("exists")
	@ResponseBody
	public AjaxResult exists(String pureName){
		AjaxResult result = new AjaxResult(true);
		try {
			MovieInfoEntity movie = movieInfoBizService.getMovieInfoByPureName(pureName);
			if(movie == null){
				result.put("exists", false);
			}else {
				result.put("exists", true);
			}
		} catch (Exception e) {
			result.setRequestResult(false);
			result.setError(e.getMessage());
			log.error("", e);
		}
		return result;
	}
	

	@RequestMapping("existed")
	public String existed(Model model,
			@RequestParam(value="mi", required=true) String movieId){
		MovieInfoEntity movie = movieInfoBizService.getMovieInfoByMovieId(movieId);
		model.addAttribute("movie", movie);
		return existed;
	}
	
	@RequestMapping("modi-movie-page")
	public String updateHere(Model model,
			@RequestParam(value="mi", required=true) String movieId){
		
		try {
			MovieInfoEntity localMovie = movieInfoBizService.getMovieInfoByMovieId(movieId);
			if(localMovie == null){
				throw new Exception("未查询到id为"+movieId+"的影片信息！");
			}
			localMovie.parse();
			model.addAttribute("movie", localMovie);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			log.error("",e);
		}
		
		return modi_movie_page;
	}
	
	@RequestMapping(value="modi-movie-work", method={RequestMethod.POST})
	public String updateWork(Model model, HttpServletRequest request, MovieInfoEntity movie){
		try {
			String tempRootPath = request.getSession().getServletContext().getRealPath("");
			movieService.modiMovieInfoManual(movie, tempRootPath);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			log.error("",e);
		}
		return add_result;
	}
	
	
	@RequestMapping("search-from-douban")
	public String addFromDouban(){
		return search_from_douban;
	}
	
	@RequestMapping(value="set-optimal", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult setOptimal(String movieId, String optimalResourceId){
		AjaxResult result = new AjaxResult(true);
		try {
			MovieInfoEntity toUpMovie = new MovieInfoEntity();
			toUpMovie.setOptimalResourceId(optimalResourceId);
			toUpMovie.setMovieId(movieId);
			movieInfoBizService.modiMovieInfo(toUpMovie);
		} catch (Exception e) {
			result.setRequestResult(false);
			result.setError(e.getMessage());
			log.error("", e);
		}
		return result;
	}
	
	@RequestMapping("download-icon")
	@ResponseBody
	public AjaxResult downloadPic(HttpServletRequest request, String url){
		AjaxResult result = new AjaxResult(true);
		try {
			int idx = url.lastIndexOf("/")+1;
			String iconName = url.substring(idx);
			String ownPath = "temp/icon";
			String tempPath = request.getSession().getServletContext().getRealPath(ownPath);
			HttpUtil.downloadPicResize(url, tempPath, iconName, CommonConstants.icon_width, CommonConstants.icon_height);
			result.put("uri", ownPath+"/"+iconName);
		} catch (Exception e) {
			result.setRequestResult(false);
			result.setError(e.getMessage());
			log.error("下载出错："+url, e);
		}
		return result;
	}
	
	
	@RequestMapping("download-posters")
	@ResponseBody
	public AjaxResult downloadPosters(HttpServletRequest request, final String pageUrl, final String iconName){
		AjaxResult result = new AjaxResult(true);
		final List<String> picUrls = new ArrayList<String>();
		try {
			HttpUtil.httpGet(pageUrl, new HttpSendCallbackPure() {
				
				@Override
				public void onResponse(Response response) throws Exception {
					if(!response.isSuccessful()){
						throw new MovieInfoFetchException("Get movie poster failed from douban, code:"+response.code()+", url:"+pageUrl);
					}
					Document doc = Jsoup.parse(new String(response.body().bytes(), "utf-8"));
					Elements lis = doc.select("#content > div > div.article > ul > li[data-id]");
					for(int i=lis.size()-1; i >= 0; i--){
						Element li = lis.get(i);
						Element img = li.select(".cover > a > img").first();
						if(img == null){
							continue;
						}
						//此处打开的是预览图，预览图较小，需要高清图，但高清图在下一层链接里，因知道高清图和预览图只有一个字段的区别，顾直接替换
						String imgUrl = img.attr("src").replaceAll("thumb", "photo");
						int idx = imgUrl.lastIndexOf("/")+1;
						
						String posterName = imgUrl.substring(idx);
						if(iconName.equals(posterName)){
							continue;
						}
						picUrls.add(imgUrl);
						if(picUrls.size() >= 4){
							break;
						}
					}
				}
			});
			
			if(picUrls.size() <= 0){
				result.setError("在"+pageUrl+"中没有获取到poster");
				return result;
			}
			
			final CountDownLatch downLatch = new CountDownLatch(picUrls.size());
			final String ownPath = "temp/poster";
			final String tempPath = request.getSession().getServletContext().getRealPath(ownPath);
			final Vector<String> completeUri = new Vector<String>();
			for(final String picUrl : picUrls){
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							int idx = picUrl.lastIndexOf("/")+1;
							String posterName = picUrl.substring(idx);
							HttpUtil.downloadPicResize(picUrl, tempPath, posterName, CommonConstants.photo_width, CommonConstants.photo_height);
							completeUri.add(ownPath+"/"+posterName);
						} catch (Exception e) {
							log.error("download poster wrong, url: {}", picUrl, e);
						}finally {
							downLatch.countDown();
						}
					}
				});
			}
			try {
				downLatch.await();
			} catch (InterruptedException e) {
				log.error("downLatch.await()",e);
			}
			if(completeUri.size() > 0){
				result.put("uris", completeUri);
			}
		} catch (Exception e) {
			result.setRequestResult(false);
			result.setError(e.getMessage());
			log.error("下载poster出错："+pageUrl, e);
		}
		return result;
	}
	
	
}

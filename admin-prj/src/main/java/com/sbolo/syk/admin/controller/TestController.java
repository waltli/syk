package com.sbolo.syk.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import okhttp3.Response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.util.IOUtils;
import com.sbolo.syk.annotation.Paginator;
import com.sbolo.syk.constants.MovieCategoryEnum;
import com.sbolo.syk.constants.RegexConstant;
import com.sbolo.syk.dao.LocationMappingEntityMapper;
import com.sbolo.syk.dao.MovieInfoEntityMapper;
import com.sbolo.syk.http.HttpUtil;
import com.sbolo.syk.http.callback.HttpSendCallbackPure;
import com.sbolo.syk.po.LocationMappingEntity;
import com.sbolo.syk.po.MovieInfoEntity;
import com.sbolo.syk.service.MovieInfoBizService;
import com.sbolo.syk.tools.Utils;

@Controller
public class TestController {
	
	@Resource
	private MovieInfoBizService movieInfoBizService;
	
	@Resource
	private MovieInfoEntityMapper movieInfoEntityMapper;
	
	@Resource
	private LocationMappingEntityMapper locationMappingEntityMapper;
	
	public static void main(String[] args) throws Exception {
		String d = "http://i2.kiimg.com/4551/163859f9aed53fba.jpg";
		HttpUtil.httpGet(d, new HttpSendCallbackPure() {
			
			@Override
			public void onResponse(Response response) throws Exception {
				
				InputStream sbs = response.body().byteStream();
				Image srcImg = ImageIO.read(sbs);
				int w = srcImg.getWidth(null);  
		        int h = srcImg.getHeight(null); 
		        BufferedImage buffImg = null;  
		        buffImg = new BufferedImage(0, 272, BufferedImage.TYPE_INT_RGB);  
		        buffImg.getGraphics().drawImage(  
		                srcImg.getScaledInstance(0, 272, Image.SCALE_SMOOTH), 0,  
		                0, null);  
		        ImageIO.write(buffImg, "jpg", new File("d:/TEXTDD/JI.jpg"));
			}
		});
		
		
        
	}
	
	@RequestMapping("test")
	@Paginator
	public String test(Model model, HttpServletRequest request, String name, String pass){
		model.addAttribute("a", 1);
		model.addAttribute("b", 2);
		model.addAttribute("requestResult", "wefwef");
		return "test.jsp";
	}
	
	@RequestMapping("test2")
	public String go(){
//		List<MovieInfoEntity> fji = movieInfoEntityMapper.selectAll();
//		List<LocationMappingEntity> fjie = new ArrayList<LocationMappingEntity>();
//		for(MovieInfoEntity movie : fji){
//			String locations = movie.getLocations();
//			String[] locationArr = locations.split(RegexConstant.slashSep);
//			for(String lo : locationArr){
//				lo = lo.trim();
//				String movieId = movie.getMovieId();
//				LocationMappingEntity locationMapping = new LocationMappingEntity();
//				locationMapping.setLocation(lo);
//				String mappingId = Utils.getUUID();
//				locationMapping.setLocationId(mappingId);
//				locationMapping.setMovieId(movieId);
//				locationMapping.setPureName(movie.getPureName());
//				locationMapping.setReleaseTime(movie.getReleaseTime());
//				locationMapping.setCreateTime(movie.getCreateTime());
//				fjie.add(locationMapping);
//			}
//		}
//		locationMappingEntityMapper.batchInsert(fjie);
		return "terminal.jsp";
	}
	
	@RequestMapping("test3")
	public String go1(){
//		List<MovieInfoEntity> fji = movieInfoEntityMapper.selectAll();
//		List<MovieInfoEntity> jiji = new ArrayList<MovieInfoEntity>();
//		for(MovieInfoEntity movie : fji){
//			MovieInfoEntity newm = new MovieInfoEntity();
//			newm.setMovieId(movie.getMovieId());
//			newm.setResourceWriteTime(movie.getUpdateTime());
//			jiji.add(newm);
//		}
//		movieInfoEntityMapper.batchUpdateByMovieIdSelective(jiji);
		return "terminal.jsp";
	}
	
	@RequestMapping("test4")
	public String go2() throws Exception{
//		List<MovieInfoEntity> fji = movieInfoEntityMapper.selectAll();
//		List<MovieInfoEntity> jiji = new ArrayList<MovieInfoEntity>();
//		for(MovieInfoEntity movie : fji){
//			final MovieInfoEntity newm = new MovieInfoEntity();
//			String movieId = movie.getMovieId();
//			newm.setMovieId(movieId);
//			final String doubanUrl = "https://movie.douban.com/subject/"+movie.getDoubanId()+"/";
//			HttpUtil.httpGet(doubanUrl, new HttpSendCallbackPure() {
//				
//				@Override
//				public void onResponse(Response response) throws Exception {
//					if(!response.isSuccessful()){
//						System.out.println("请求失败：code："+response.code()+"url:"+doubanUrl);
//						return;
//					}
//					
//					Document doc = Jsoup.parse(new String(response.body().bytes(), "utf-8"));
//					String year = doc.select("#content > h1 > span.year").first().text();
//					year = Utils.getTimeStr(year);
//					newm.setYear(year);
//				}
//			});
//			jiji.add(newm);
//		}
//		movieInfoEntityMapper.batchUpdateByMovieIdSelective(jiji);
		return "terminal.jsp";
	}
}

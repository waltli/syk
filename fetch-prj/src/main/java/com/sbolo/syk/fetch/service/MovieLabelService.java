package com.sbolo.syk.fetch.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sbolo.syk.fetch.entity.MovieLabelEntity;
import com.sbolo.syk.fetch.mapper.MovieLabelMapper;

@Service
public class MovieLabelService {
	@Resource
	private MovieLabelMapper movieLabelMapper;
	
	public List<MovieLabelEntity> getListByMoviePrn(String moviePrn){
		return movieLabelMapper.selectListByMoviePrn(moviePrn);
	}
	
	
}

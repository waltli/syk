package com.sbolo.syk.view.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sbolo.syk.view.entity.MovieLocationEntity;
import com.sbolo.syk.view.mapper.MovieLocationMapper;

@Service
public class MovieLocationService {
	@Resource
	private MovieLocationMapper movieLocationMapper;
	
	public List<MovieLocationEntity> getListByMoviePrn(String moviePrn){
		return movieLocationMapper.selectListByMoviePrn(moviePrn);
	}
	
	
}

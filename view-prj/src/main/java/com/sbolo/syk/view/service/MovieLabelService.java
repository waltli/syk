package com.sbolo.syk.view.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sbolo.syk.view.entity.MovieLabelEntity;
import com.sbolo.syk.view.mapper.MovieLabelMapper;

@Service
public class MovieLabelService {
	@Resource
	private MovieLabelMapper movieLabelMapper;
	
	public List<MovieLabelEntity> getListByMoviePrn(String moviePrn){
		return movieLabelMapper.selectListByMoviePrn(moviePrn);
	}
	
	public List<String> getLabels(){
		List<String> labels = movieLabelMapper.selectLabelsGroupLabel();
		Collections.sort(labels, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				int o1Len = o1.length();
				int o2Len = o2.length();
				if(o1Len > o2Len){
	                return 1;
	            }else if (o1Len < o2Len){
	                return -1;
	            }else {
	                return 0;
	            }
			}
		});
		return labels;
	}
	
	
}

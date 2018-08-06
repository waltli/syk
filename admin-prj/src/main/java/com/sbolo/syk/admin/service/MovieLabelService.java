package com.sbolo.syk.admin.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sbolo.syk.admin.entity.MovieLabelEntity;
import com.sbolo.syk.admin.mapper.MovieLabelMapper;

@Service
public class MovieLabelService {
	@Resource
	private MovieLabelMapper movieLabelMapper;
	
}

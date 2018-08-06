package com.sbolo.syk.admin.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sbolo.syk.admin.entity.MovieLocationEntity;
import com.sbolo.syk.admin.mapper.MovieLocationMapper;

@Service
public class MovieLocationService {
	@Resource
	private MovieLocationMapper movieLocationMapper;
	
}

package com.sbolo.syk.view.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbolo.syk.view.mapper.SykMessageLikeMapper;

@Service
public class SykMessageLikeService {
	private static final Logger log = LoggerFactory.getLogger(SykMessageLikeService.class);
	
	@Autowired
	private SykMessageLikeMapper sykMessageLikeMapper;
}

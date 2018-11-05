package com.sbolo.syk.fetch.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sbolo.syk.common.tools.ThreeDESUtils;
import com.sbolo.syk.fetch.entity.SykUserEntity;
import com.sbolo.syk.fetch.mapper.SykUserMapper;

@Service
public class SykUserService {
	private static final Logger log = LoggerFactory.getLogger(SykUserService.class);
	@Resource
	private SykUserMapper sykUserMapper;
	
	public SykUserEntity getOneByUsernamePassword(String username, String password) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("username", username);
		params.put("password", ThreeDESUtils.encode3Des(password));
		SykUserEntity user = sykUserMapper.selectOneByUsernamePassword(params);
		return user;
	}
}

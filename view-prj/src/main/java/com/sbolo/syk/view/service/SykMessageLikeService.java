package com.sbolo.syk.view.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.UIDGen;
import com.sbolo.syk.view.entity.SykMessageLikeEntity;
import com.sbolo.syk.view.mapper.SykMessageLikeMapper;

@Service
public class SykMessageLikeService {
	private static final Logger log = LoggerFactory.getLogger(SykMessageLikeService.class);
	
	@Autowired
	private SykMessageLikeMapper sykMessageLikeMapper;
	
	
	public int add(String msgPrn, String gaverPrn, String gaverIp) {
		SykMessageLikeEntity likeEntity = new SykMessageLikeEntity();
		likeEntity.setCreateTime(new Date());
		likeEntity.setPrn(UIDGen.getUID()+"");
		likeEntity.setGaverIp(gaverIp);
		likeEntity.setGaverPrn(gaverPrn);
		likeEntity.setMsgPrn(msgPrn);
		return sykMessageLikeMapper.insert(likeEntity);
	}
	
	public int remove(String likePrn) {
		SykMessageLikeEntity record = new SykMessageLikeEntity();
		record.setPrn(likePrn);
		return sykMessageLikeMapper.delete(record);
	}
	
	public SykMessageLikeEntity getByMsgPrnAndGaverPrn(String msgPrn, String gaverPrn) {
		if(StringUtils.isBlank(msgPrn)) {
			throw new BusinessException("消息prn不能为空！");
		}
		
		if(StringUtils.isBlank(gaverPrn)) {
			throw new BusinessException("用户prn不能为空！");
		}
		
		SykMessageLikeEntity record = new SykMessageLikeEntity();
		record.setGaverPrn(gaverPrn);
		record.setMsgPrn(msgPrn);
		SykMessageLikeEntity one = sykMessageLikeMapper.selectOne(record);
		return one;
	}
	
	public int removeByMsgPrns(List<String> msgPrns) {
		return sykMessageLikeMapper.deleteByMsgPrns(msgPrns);
	}
}

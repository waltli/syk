package com.sbolo.syk.view.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.view.entity.SykMessageEntity;
import com.sbolo.syk.view.mapper.SykMessageMapper;
import com.sbolo.syk.view.vo.SykMessageVO;

@Service
public class SykMessageService {
	private static final Logger log = LoggerFactory.getLogger(SykMessageService.class);
	
	@Autowired
	private SykMessageMapper sykMessageMapper;
	
	public void addOne(SykMessageVO vo, String userPrn, String ip, String userAgent) {
		this.check(vo);
		vo.setAuthorPrn(userPrn);
		vo.setCreateTime(new Date());
		vo.setAuthorIp(ip);
		vo.setLikeCount(0);
		if(vo.getMsgLevel() == null) {
			vo.setMsgLevel(1);
		}
		vo.setPrn(StringUtil.getId(CommonConstants.message_s));
		vo.setUserAgent(userAgent);
		SykMessageEntity entity = VOUtils.po2vo(vo, SykMessageEntity.class);
		sykMessageMapper.insert(entity);
	}
	
	private void check(SykMessageVO vo) {
		if(StringUtils.isBlank(vo.getMsgContent())) {
			throw new BusinessException("获取消息内容失败！");
		}
		if(StringUtils.isBlank(vo.getPkey())) {
			throw new BusinessException("发帖鉴别获取失败！");
		}
	}
}

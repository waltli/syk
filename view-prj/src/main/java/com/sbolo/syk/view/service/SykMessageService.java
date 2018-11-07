package com.sbolo.syk.view.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.view.entity.MovieInfoEntity;
import com.sbolo.syk.view.entity.ResourceInfoEntity;
import com.sbolo.syk.view.entity.SykMessageEntity;
import com.sbolo.syk.view.entity.SykMessageLikeEntity;
import com.sbolo.syk.view.entity.SykUserEntity;
import com.sbolo.syk.view.enums.OrderMarkerEnum;
import com.sbolo.syk.view.mapper.SykMessageLikeMapper;
import com.sbolo.syk.view.mapper.SykMessageMapper;
import com.sbolo.syk.view.vo.MovieInfoVO;
import com.sbolo.syk.view.vo.ResourceInfoVO;
import com.sbolo.syk.view.vo.SykMessageVO;
import com.sbolo.syk.view.vo.SykUserVO;
import com.sbolo.syk.view.vo.TestVO;

@Service
public class SykMessageService {
	private static final Logger log = LoggerFactory.getLogger(SykMessageService.class);
	
	@Autowired
	private SykMessageMapper sykMessageMapper;
	
	@Autowired
	private SykMessageLikeService sykMessageLikeService;
	
	public TestVO getListByPage(String pkey, SykUserVO token, int pageNum, int pageSize, String orderMarker) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		OrderMarkerEnum orderMarkerEnum = OrderMarkerEnum.getEnumByCode(orderMarker);
		String orderBy = orderMarkerEnum.getSort()+" "+orderMarkerEnum.getDirect();
		PageHelper.startPage(pageNum, pageSize, orderBy);
		List<String> rootPrnList = sykMessageMapper.selectRootPrns(pkey);
		
		if(rootPrnList == null || rootPrnList.size() == 0) {
			throw new BusinessException("获取消息失败！");
		}
		
		PageHelper.orderBy("t."+orderBy);
		List<SykMessageEntity> msgEntityList = sykMessageMapper.batchSelectAssociationByRootPrns(rootPrnList);
		
		PageHelper.startPage(0, 5, "t.like_count DESC");
		List<SykMessageEntity> hotMsgEntityList = sykMessageMapper.selectAssociationByHot();
		
		Set<String> authorSet = new HashSet<>();
		if(hotMsgEntityList != null && hotMsgEntityList.size() > 0) {
			for(SykMessageEntity entity : hotMsgEntityList) {
				authorSet.add(entity.getAuthorPrn());
			}
		}
		if(msgEntityList != null && msgEntityList.size() > 0) {
			for(SykMessageEntity entity : msgEntityList) {
				authorSet.add(entity.getAuthorPrn());
			}
		}
		
		List<SykMessageVO> messages = new ArrayList<>();
		for(SykMessageEntity msgEntity : msgEntityList) {
			SykUserEntity authorEntity = msgEntity.getAuthor();
			msgEntity.setAuthor(null);
			SykMessageVO message = VOUtils.po2vo(msgEntity, SykMessageVO.class);
			message.parse();
			if(authorEntity != null) {
				SykUserVO author = VOUtils.po2vo(authorEntity, SykUserVO.class);
				message.setAuthor(author);
			}
			messages.add(message);
		}
		Collections.sort(messages);
		
		List<SykMessageVO> hotMessages = new ArrayList<>();
		for(SykMessageEntity hotMsgEntity : hotMsgEntityList) {
			SykUserEntity hotAuthorEntity = hotMsgEntity.getAuthor();
			hotMsgEntity.setAuthor(null);
			SykMessageVO hotMessage = VOUtils.po2vo(hotMsgEntity, SykMessageVO.class);
			hotMessage.parse();
			if(hotAuthorEntity != null) {
				SykUserVO hotAuthor = VOUtils.po2vo(hotAuthorEntity, SykUserVO.class);
				hotMessage.setAuthor(hotAuthor);
			}
			hotMessages.add(hotMessage);
		}
		
		Map<String, Object> userTips = new HashMap<>();
		if(authorSet.size() > 0) {
			List<Map<String,Object>> countMessage = sykMessageMapper.countMessageByAuthor(authorSet);
			if(countMessage != null && countMessage.size() > 0) {
				for(Map<String,Object> m : countMessage) {
					userTips.put(m.get("author_prn").toString(), m.get("msg_count"));
				}
			}
		}
		return new TestVO(pkey, token, hotMessages, messages, userTips);
		
		
	}
	
	public void addOne(SykMessageVO vo, String userPrn, String ip, String userAgent) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		this.check(vo);
		String prn = StringUtil.getId(CommonConstants.message_s);
		vo.setPrn(prn);
		vo.setAuthorPrn(userPrn);
		vo.setCreateTime(new Date());
		vo.setAuthorIp(ip);
		vo.setLikeCount(0);
		if(vo.getMsgLevel() == null) {
			vo.setMsgLevel(1);
		}
		if(StringUtils.isBlank(vo.getParentPrn())) {
			vo.setParentPrn(prn);
		}
		if(StringUtils.isBlank(vo.getParentPrns())) {
			vo.setParentPrns(","+prn+",");
		}
		if(StringUtils.isBlank(vo.getRootPrn())) {
			vo.setRootPrn(prn);
		}
		vo.setUserAgent(userAgent);
		SykMessageEntity entity = VOUtils.po2vo(vo, SykMessageEntity.class);
		sykMessageMapper.insert(entity);
	}
	
	public SykMessageEntity getOne(String msgPrn) {
		SykMessageEntity entity = sykMessageMapper.selectByPrn(msgPrn);
		return entity;
	}
	
	@Transactional
	public boolean giveLike(String msgPrn, String gaverPrn, String gaverIp) {
		int upc = sykMessageMapper.addLike(msgPrn);
		if(upc != 1) {
			throw new BusinessException("修改条数："+ upc + " 不符合预期。");
		}
		int addc = sykMessageLikeService.add(msgPrn, gaverPrn, gaverIp);
		if(addc != 1) {
			throw new BusinessException("新增条数："+ upc + " 不符合预期。");
		}
		return true;
	}
	
	
	
	@Transactional
	public boolean backLike(String msgPrn, String likePrn) {
		int upc = sykMessageMapper.subLike(msgPrn);
		if(upc != 1) {
			throw new BusinessException("修改数："+ upc + " 不符合预期 1 。");
		}
		int addc = sykMessageLikeService.remove(likePrn);
		if(addc != 1) {
			throw new BusinessException("删除数："+ addc + " 不符合预期 1 。");
		}
		return true;
	}
	
	@Transactional
	public boolean remove(List<String> msgPrnl) {
		int size = msgPrnl.size();
		int msgdc = sykMessageMapper.deleteByPrns(msgPrnl);
		if(msgdc != size) {
			throw new BusinessException("删除消息数："+ msgdc + " 不符合预期 "+size+" 。");
		}
		int likedc = sykMessageLikeService.removeByMsgPrns(msgPrnl);
		
		if(likedc != size) {
			throw new BusinessException("删除赞数："+ likedc + " 不符合预期 "+size+" 。");
		}
		return true;
	}
	
	public List<String> getByParentPrns(String msgPrn){
		String msgPrnEvo = ","+msgPrn+",";
		return sykMessageMapper.selectByParentPrns(msgPrnEvo);
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

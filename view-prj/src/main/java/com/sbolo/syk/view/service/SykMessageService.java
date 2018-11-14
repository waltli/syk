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
	
	public TestVO getListByPage(String pkey, SykUserVO token, String orderMarker) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		//获取rootPrns
		OrderMarkerEnum orderMarkerEnum = OrderMarkerEnum.getEnumByCode(orderMarker);
		String orderBy = orderMarkerEnum.getSort()+" "+orderMarkerEnum.getDirect();
		PageHelper.orderBy(orderBy);
		List<String> rootPrnList = sykMessageMapper.selectRootPrns(pkey);
		if(rootPrnList == null || rootPrnList.size() == 0) {
			return new TestVO(pkey, token, new ArrayList<>(), new ArrayList<>(), new HashMap<>());
		}
		
		//获取消息列表和最热消息列表
		PageHelper.orderBy("t."+orderBy);
		List<SykMessageEntity> msgEntityList = sykMessageMapper.batchSelectAssociationByRootPrns(rootPrnList);
		PageHelper.startPage(0, 5, "t.like_count DESC");
		List<SykMessageEntity> hotMsgEntityList = sykMessageMapper.selectAssociationByHot(1, pkey);
		//转换为VO
		List<SykMessageVO> messages = this.buildMessageVO(msgEntityList);
		Collections.sort(messages);
		List<SykMessageVO> hotMessages = this.buildMessageVO(hotMsgEntityList);
		
		//获取userTips
		Map<String, Object> userTips = this.getUserTips(pkey, msgEntityList, hotMsgEntityList);
		
		return new TestVO(pkey, token, hotMessages, messages, userTips);
	}
	
	private Map<String, Object> getUserTips(String pkey, List<SykMessageEntity> msgEntityList, List<SykMessageEntity> hotMsgEntityList){
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
		
		Map<String, Object> userTips = new HashMap<>();
		if(authorSet.size() > 0) {
			List<Map<String,Object>> countMessage = sykMessageMapper.countMessageByAuthor(pkey, authorSet);
			if(countMessage != null && countMessage.size() > 0) {
				for(Map<String,Object> m : countMessage) {
					userTips.put(m.get("author_prn").toString(), m.get("msg_count"));
				}
			}
		}
		return userTips;
	}
	
	private List<SykMessageVO> buildMessageVO(List<SykMessageEntity> msgEntityList) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		if(msgEntityList == null) {
			return null;
		}
		List<SykMessageVO> messages = new ArrayList<>();
		for(SykMessageEntity msgEntity : msgEntityList) {
			SykUserEntity authorEntity = msgEntity.getAuthor();
			if(authorEntity == null) {
				log.warn("消息："+msgEntity.getPrn()+" 没有找到相关作者！跳过。");
				continue;
			}
			msgEntity.setAuthor(null);
			SykMessageVO message = VOUtils.po2vo(msgEntity, SykMessageVO.class);
			message.parse();
			if(authorEntity != null) {
				SykUserVO author = VOUtils.po2vo(authorEntity, SykUserVO.class);
				message.setAuthor(author);
			}
			messages.add(message);
		}
		return messages;
	}
	
	public void addOne(SykMessageVO vo) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		this.check(vo);
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
		sykMessageLikeService.removeByMsgPrns(msgPrnl);
		
		return true;
	}
	
	public List<String> getByPrnLine(String msgPrn){
		String msgPrnEvo = ","+msgPrn+",";
		return sykMessageMapper.selectByPrnLine(msgPrnEvo);
	}
	
	public SykMessageEntity getByPrn(String msgPrn) {
		return sykMessageMapper.selectByPrn(msgPrn);
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

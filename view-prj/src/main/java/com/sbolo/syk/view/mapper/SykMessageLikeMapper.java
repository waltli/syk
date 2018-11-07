package com.sbolo.syk.view.mapper;

import java.util.List;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.view.entity.SykMessageLikeEntity;

import tk.mybatis.mapper.common.Mapper;

public interface SykMessageLikeMapper extends Mapper<SykMessageLikeEntity>, BatchWriteMapper<SykMessageLikeEntity> {
	
	int deleteByMsgPrns(List<String> msgPrns);
}

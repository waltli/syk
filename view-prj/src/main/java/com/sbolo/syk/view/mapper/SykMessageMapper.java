package com.sbolo.syk.view.mapper;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.view.entity.SykMessageEntity;

import tk.mybatis.mapper.common.Mapper;

public interface SykMessageMapper extends Mapper<SykMessageEntity>, BatchWriteMapper<SykMessageEntity> {
	
}

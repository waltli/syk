package com.sbolo.syk.view.mapper;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.view.entity.SykDictEntity;

import tk.mybatis.mapper.common.Mapper;

public interface SykDictMapper extends Mapper<SykDictEntity>, BatchWriteMapper<SykDictEntity> {
	
}

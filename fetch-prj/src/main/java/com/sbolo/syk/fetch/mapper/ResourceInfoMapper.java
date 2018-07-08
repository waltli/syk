package com.sbolo.syk.fetch.mapper;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.fetch.basemapper.BatchWriteMapper;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;

import tk.mybatis.mapper.common.Mapper;

public interface ResourceInfoMapper extends Mapper<ResourceInfoEntity> {
	
	@ResultMap("BaseResultMap")
	@Select("select * from resource_info where prn = 'r168121444744'")
	ResourceInfoEntity selectOptimalResource(String moviePrn);
}

package com.sbolo.syk.fetch.mapper;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;

import tk.mybatis.mapper.common.Mapper;

public interface ResourceInfoMapper extends Mapper<ResourceInfoEntity>, BatchWriteMapper<ResourceInfoEntity> {
	
	@ResultMap("BaseResultMap")
	@Select("select t1.* from movie_info t, resource_info t1 where t.optimal_resource_prn = t1.prn and t.prn = #{moviePrn}")
	ResourceInfoEntity selectOptimalResource(String moviePrn);
}

package com.sbolo.syk.view.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.view.entity.ResourceInfoEntity;

import tk.mybatis.mapper.common.Mapper;

public interface ResourceInfoMapper extends Mapper<ResourceInfoEntity>, BatchWriteMapper<ResourceInfoEntity> {
	
	@ResultMap("BaseResultMap")
	@Select("select t1.* from movie_info t, resource_info t1 where t.optimal_resource_prn = t1.prn and t.prn = #{moviePrn}")
	ResourceInfoEntity selectOptimalResource(String moviePrn);
	
	List<ResourceInfoEntity> selectByMoviePrnOrder(Map<String, Object> param);
}

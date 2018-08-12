package com.sbolo.syk.admin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.admin.entity.ResourceInfoEntity;

import tk.mybatis.mapper.common.Mapper;

public interface ResourceInfoMapper extends Mapper<ResourceInfoEntity>, BatchWriteMapper<ResourceInfoEntity> {
	
	@ResultMap("BaseResultMap")
	@Select("select t1.* from movie_info t, resource_info t1 where t.optimal_resource_prn = t1.prn and t.prn = #{moviePrn}")
	ResourceInfoEntity selectOptimalResource(String moviePrn);
	
	@Update("update resource_info t set t.st=#{resourceStatus} where t.movie_prn=#{moviePrn}")
	int signStatusByMoviePrn(Map<String, Object> param);
	
	List<ResourceInfoEntity> selectByMoviePrnOrder(Map<String, Object> param);
}

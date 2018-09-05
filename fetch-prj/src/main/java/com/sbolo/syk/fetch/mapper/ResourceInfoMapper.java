package com.sbolo.syk.fetch.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;

import tk.mybatis.mapper.common.Mapper;

public interface ResourceInfoMapper extends Mapper<ResourceInfoEntity>, BatchWriteMapper<ResourceInfoEntity> {
	
	@ResultMap("BaseResultMap")
	@Select("select t1.* from movie_info t, resource_info t1 where t.optimal_resource_prn = t1.prn and t.prn = #{moviePrn}")
	ResourceInfoEntity selectOptimalResource(String moviePrn);
	
	@Update("update resource_info t set t.st=#{resourceStatus} where t.movie_prn=#{moviePrn}")
	int signStatusByMoviePrn(Map<String, Object> params);
	
	@Update("update resource_info t set t.st=#{resourceStatus} where t.prn=#{resourcePrn}")
	int signStatusByPrn(Map<String, Object> params);
	
	@ResultMap("BaseResultMap")
	@Select("SELECT * FROM resource_info WHERE movie_prn = #{moviePrn} and st = #{resourceStatus}")
	List<ResourceInfoEntity> selectAllByMoviePrnAndStatus(Map<String, Object> params);
	
	@Select("select * from resource_info where prn=#{resourcePrn}")
	ResourceInfoEntity selectByPrn(String resourcePrn);
	
	List<ResourceInfoEntity> selectByMoviePrnOrder(Map<String, Object> param);
	
	List<ResourceInfoEntity> selectByMoviePrnOrderNostatus(Map<String, Object> params);
}

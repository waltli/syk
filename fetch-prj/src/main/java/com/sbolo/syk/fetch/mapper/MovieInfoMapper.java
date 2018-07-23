package com.sbolo.syk.fetch.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieInfoMapper extends Mapper<MovieInfoEntity>, BatchWriteMapper<MovieInfoEntity> {

	@ResultMap("BaseResultMap")
	@Select("select t.* from movie_info t " + 
			"where t.pure_name = #{pureName} and t.release_time >= #{year} " + 
			"limit 1")
	public MovieInfoEntity selectOneByPureNameAndYear(@Param(value = "pureName") String pureName, @Param(value = "year") Date year);
	
	
	
	
}

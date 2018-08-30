package com.sbolo.syk.fetch.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieInfoMapper extends Mapper<MovieInfoEntity>, BatchWriteMapper<MovieInfoEntity> {

	@ResultMap("BaseResultMap")
	@Select("select t.* from movie_info t " + 
			"where t.pure_name = #{pureName} and t.release_time >= #{year} " + 
			"limit 1")
	public MovieInfoEntity selectOneByPureNameAndYear(@Param(value = "pureName") String pureName, @Param(value = "year") Date year);
	
	List<MovieInfoEntity> selectByAssociation(Map<String, Object> params);
	
	List<MovieInfoEntity> selectByAssociationWithLabel(Map<String, Object> params);
	
	MovieInfoEntity selectAssociationByMoviePrn(String moviePrn);
	
	@Update("update movie_info t set t.st=#{movieStatus} where t.prn=#{moviePrn}")
	int signStatusByPrn(Map<String, Object> params);
	
	@Select("select * from movie_info where douban_id = #{doubanId}")
	MovieInfoEntity selectByDoubanId(String doubanId);
	
	@Select("select * from movie_info where pure_name = #{pureName} and release_time = #{releaseTime} limit 1")
	MovieInfoEntity selectByPureNameAndReleaseTime(Map<String, Object> params);
	
	@Select("select * from movie_info where pure_name = #{pureName} limit 1")
	MovieInfoEntity selectByPureName(String pureName);
	
	@Select("select * from movie_info where prn=#{moviePrn}")
	MovieInfoEntity selectByPrn(String moviePrn);
	
	MovieInfoEntity selectByPureNameAndPrecision(Map<String, Object> params);
	
	
}

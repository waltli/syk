package com.sbolo.syk.admin.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.PageInfo;
import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.admin.entity.MovieInfoEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieInfoMapper extends Mapper<MovieInfoEntity>, BatchWriteMapper<MovieInfoEntity> {

	List<MovieInfoEntity> selectByAssociation(Map<String, Object> params);
	
	List<MovieInfoEntity> selectByAssociationWithLabel(Map<String, Object> params);
	
	@Update("update movie_info t set t.st=#{movieStatus} where t.prn=#{moviePrn}")
	int signStatusByPrn(Map<String, Object> params);
	
	@Select("select * from movie_info where douban_id = #{doubanId}")
	MovieInfoEntity selectByDoubanId(String doubanId);
	
	@Select("select * from movie_info where pure_name = #{pureName} and release_time = #{releaseTime} limit 1")
	MovieInfoEntity selectByPureNameAndReleaseTime(Map<String, Object> params);
}

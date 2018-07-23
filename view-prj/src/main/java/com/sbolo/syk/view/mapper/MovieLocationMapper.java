package com.sbolo.syk.view.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.view.entity.MovieLocationEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieLocationMapper extends Mapper<MovieLocationEntity>, BatchWriteMapper<MovieLocationEntity> {
	
	@Select("select t.* from movie_location t where t.movie_prn = #{moviePrn}")
	List<MovieLocationEntity> selectListByMoviePrn(String moviePrn);
}

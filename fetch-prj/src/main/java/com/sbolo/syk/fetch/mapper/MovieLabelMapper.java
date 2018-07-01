package com.sbolo.syk.fetch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.fetch.basemapper.BatchWriteMapper;
import com.sbolo.syk.fetch.entity.MovieLabelEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieLabelMapper extends Mapper<MovieLabelEntity>, BatchWriteMapper<MovieLabelEntity> {
	
	@Select("select t.* from movie_label t where t.movie_prn=#{moviePrn}")
	List<MovieLabelEntity> selectListByMoviePrn(String moviePrn);
}

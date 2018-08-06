package com.sbolo.syk.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.admin.entity.MovieLabelEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieLabelMapper extends Mapper<MovieLabelEntity>, BatchWriteMapper<MovieLabelEntity> {
	
	@Select("select t.* from movie_label t where t.movie_prn=#{moviePrn}")
	List<MovieLabelEntity> selectListByMoviePrn(String moviePrn);
	
	@Select("select t.label_name from movie_label t GROUP BY t.label_name")
	List<String> selectLabelsGroupLabel();
}

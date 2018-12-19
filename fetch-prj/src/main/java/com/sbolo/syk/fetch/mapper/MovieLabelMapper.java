package com.sbolo.syk.fetch.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.fetch.entity.MovieDictEntity;
import com.sbolo.syk.fetch.entity.MovieLabelEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieLabelMapper extends Mapper<MovieLabelEntity>, BatchWriteMapper<MovieLabelEntity> {
	
}

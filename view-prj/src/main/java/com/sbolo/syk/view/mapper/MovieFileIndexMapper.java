package com.sbolo.syk.view.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.view.entity.MovieFileIndexEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieFileIndexMapper extends Mapper<MovieFileIndexEntity>, BatchWriteMapper<MovieFileIndexEntity> {
	List<MovieFileIndexEntity> selectBatchBySourceUrl(@Param("set")Set<String> urls);
}

package com.sbolo.syk.fetch.mapper;

import java.util.List;

import com.sbolo.syk.fetch.basemapper.BatchWriteMapper;
import com.sbolo.syk.fetch.entity.MovieFileIndexEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieFileIndexMapper extends Mapper<MovieFileIndexEntity>, BatchWriteMapper<MovieFileIndexEntity> {
	List<MovieFileIndexEntity> selectBatchBySourceUrl(List<String> urls);
}

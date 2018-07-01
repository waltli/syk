package com.sbolo.syk.fetch.basemapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.UpdateProvider;

import com.sbolo.syk.fetch.basemapper.provider.BatchWriteProvider;

import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface BatchWriteMapper<T> {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@InsertProvider(type = BatchWriteProvider.class, method = "dynamicSQL")
    int insertList(List<T> recordList);
	
	@UpdateProvider(type = BatchWriteProvider.class, method = "dynamicSQL")
	int updateListByPrn(List<T> recordList);
	
}

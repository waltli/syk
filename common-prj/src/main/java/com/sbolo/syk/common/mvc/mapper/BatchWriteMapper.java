package com.sbolo.syk.common.mvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.UpdateProvider;

import com.sbolo.syk.common.mvc.mapper.provider.BatchWriteProvider;

import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface BatchWriteMapper<T> {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@InsertProvider(type = BatchWriteProvider.class, method = "dynamicSQL")
    int insertList(List<T> recordList);
	
	@UpdateProvider(type = BatchWriteProvider.class, method = "dynamicSQL")
	int updateListByPrn(List<T> recordList);
	
}

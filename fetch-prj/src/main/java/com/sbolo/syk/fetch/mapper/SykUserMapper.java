package com.sbolo.syk.fetch.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.entity.SykUserEntity;

import tk.mybatis.mapper.common.Mapper;

public interface SykUserMapper extends Mapper<SykUserEntity>, BatchWriteMapper<SykUserEntity> {
	
	@ResultMap("BaseResultMap")
	@Select("select * from syk_user t where t.username = #{username} and t.password = #{password}")
	SykUserEntity selectOneByUsernamePassword(Map<String, Object> params);
}

package com.sbolo.syk.fetch.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.entity.SykUsersEntity;

import tk.mybatis.mapper.common.Mapper;

public interface SykUsersMapper extends Mapper<SykUsersEntity>, BatchWriteMapper<SykUsersEntity> {
	
	@ResultMap("BaseResultMap")
	@Select("select * from syk_users t where t.username = #{username} and t.password = #{password}")
	SykUsersEntity selectOneByUsernamePassword(Map<String, Object> params);
}

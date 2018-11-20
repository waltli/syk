package com.sbolo.syk.view.mapper;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.view.entity.SykUserEntity;

import tk.mybatis.mapper.common.Mapper;

public interface SykUserMapper extends Mapper<SykUserEntity>, BatchWriteMapper<SykUserEntity> {
	
	@ResultMap("BaseResultMap")
	@Select("select t.* from syk_user t where t.prn = #{prn}")
	SykUserEntity selectUser(String userPrn);
}

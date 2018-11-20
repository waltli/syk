package com.sbolo.syk.view.mapper;

import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.view.entity.SykOpenUserEntity;

import tk.mybatis.mapper.common.Mapper;

public interface SykOpenUserMapper extends Mapper<SykOpenUserEntity>, BatchWriteMapper<SykOpenUserEntity> {

	@Select("select t.user_prn from syk_open_user t where t.open_id = #{openid}")
	String selectUserPrn(String openid);
	
}

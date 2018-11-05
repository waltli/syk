package com.sbolo.syk.view.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.view.entity.SykMessageEntity;

import tk.mybatis.mapper.common.Mapper;

public interface SykMessageMapper extends Mapper<SykMessageEntity>, BatchWriteMapper<SykMessageEntity> {
	
	@Select("select t.prn from syk_message t where t.msg_level = 1 and t.pkey = #{pkey}")
	List<String> selectRootPrns(String pkey);
	
	List<SykMessageEntity> batchSelectAssociationByRootPrns(List<String> rootPrnList);
	
	List<SykMessageEntity> selectAssociationByHot();
	
	List<Map<String, Object>> countMessageByAuthor(@Param("set") Set<String> authorSet);
}

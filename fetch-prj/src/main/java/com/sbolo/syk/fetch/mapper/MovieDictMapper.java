package com.sbolo.syk.fetch.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.fetch.entity.MovieDictEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieDictMapper extends Mapper<MovieDictEntity>, BatchWriteMapper<MovieDictEntity> {
	
	@Select("select t.val from movie_dict t where t.parent_code = #{parentCode}")
	List<String> selectByParentCode(String parentCode);
	
	@Select("select t.val from movie_dict t where t.code = #{code}")
	String selectByCode(String code);
	
	@Select("select t.val from movie_dict t where t.root_code = #{rootCode}")
	List<String> selectByRootCode(String rootCode);
	
	List<MovieDictEntity> selectByRootCodes(List<String> rootCodes);
	
	List<MovieDictEntity> selectByCodes(List<String> codes);
	
	List<String> selectByVals(@Param("parentCode") String parentCode, @Param("set") Set<String> vals);
	
}

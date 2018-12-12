package com.sbolo.syk.view.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.view.entity.MovieDictEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieDictMapper extends Mapper<MovieDictEntity>, BatchWriteMapper<MovieDictEntity> {
	
	@Select("select t.val from movie_dict t where t.parent_code = #{parentCode}")
	List<String> selectByParentCode(String parentCode);
	
}

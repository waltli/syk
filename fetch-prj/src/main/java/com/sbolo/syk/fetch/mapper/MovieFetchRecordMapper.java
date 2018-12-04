package com.sbolo.syk.fetch.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.fetch.entity.MovieFetchRecordEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieFetchRecordMapper extends Mapper<MovieFetchRecordEntity>, BatchWriteMapper<MovieFetchRecordEntity> {
	@ResultMap("BaseResultMap")
	@Select("select * from movie_fetch_record t where t.has_migrated = #{hasMigrated}")
	List<MovieFetchRecordEntity> selectByHasMigrated(boolean hasMigrated);
}

package com.sbolo.syk.admin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.admin.entity.MovieHotStatEntity;

public interface MovieHotStatMapper {
	
	@ResultMap("BaseResultMap")
	@Select("SELECT a.prn, a.pure_name, a.douban_score, b.hot_count " + 
			"from " + 
			"(SELECT t.movie_prn, COUNT(1) AS hot_count " + 
			"FROM movie_hot_stat t " + 
			"WHERE " + 
			"t.create_time > #{timeStart} " + 
			"AND t.create_time < #{timeEnd} " + 
			"GROUP BY t.movie_prn " +
			"LIMIT #{limitNum}) b " + 
			"left join movie_info a " + 
			"on a.prn = b.movie_prn " + 
			"ORDER BY hot_count DESC")
	List<MovieHotStatEntity> selectHotByTime(Map<String, Object> params);
	
	int insertSelective(MovieHotStatEntity record);
}

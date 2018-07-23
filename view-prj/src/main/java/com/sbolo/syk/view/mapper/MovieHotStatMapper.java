package com.sbolo.syk.view.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.sbolo.syk.view.entity.MovieHotStatEntity;

public interface MovieHotStatMapper {
	
	@ResultMap("BaseResultMap")
	@Select("SELECT a.movie_prn, a.pure_name, a.douban_score, b.hot_count " + 
			"from movie_hot_stat a left join " + 
			"(SELECT t.movie_prn, COUNT(1) AS hot_count " + 
			"FROM movie_hot_stat t " + 
			"WHERE " + 
			"t.create_time > #{timeStart} " + 
			"AND t.create_time < #{timeEnd} " + 
			"GROUP BY t.movie_prn " +
			"LIMIT #{limitNum}) b " + 
			"on a.movie_prn = b.movie_prn " + 
			"ORDER BY hot_count DESC")
	List<MovieHotStatEntity> selectHotByTime(Map<String, Object> params);
}

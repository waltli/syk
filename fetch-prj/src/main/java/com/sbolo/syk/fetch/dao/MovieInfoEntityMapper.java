package com.sbolo.syk.fetch.dao;

import java.util.List;
import java.util.Map;

import com.sbolo.syk.fetch.po.MovieInfoEntity;
import com.sbolo.syk.fetch.po.MovieResourceEntity;

public interface MovieInfoEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(MovieInfoEntity record);

    MovieInfoEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(MovieInfoEntity record);
    
    MovieInfoEntity selectByPureNameAndReleaseTime(Map<String, Object> params);
    
    MovieInfoEntity selectByPureName(String pureName);
    
    MovieInfoEntity selectByPureNameAndPrecision(Map<String, Object> params);
    
    MovieInfoEntity selectByMovieId(String movieId);
    
    MovieInfoEntity selectByDoubanId(String doubanId);
    
    void batchInsert(List<MovieInfoEntity> adMovies);
    
    void batchUpdateByMovieIdSelective(List<MovieInfoEntity> upMovies);
    
    void updateByMovieIdSelective(MovieInfoEntity upMovie);
    
    List<MovieInfoEntity> selectAllNoCondition();
    
    void batchUpdateOptimalByMovieId(List<MovieInfoEntity> mies);
    
    int updateCountClick(String movieId);
    
    int updateCountDownload(String movieId);
    
    List<MovieInfoEntity> selectAll();
    
    void signStatusByMovieId(Map<String, Object> params);
    
    List<MovieInfoEntity> selectListByCondition(Map<String, Object> params);
    
    int selectListByConditionCount(Map<String, Object> params);
}
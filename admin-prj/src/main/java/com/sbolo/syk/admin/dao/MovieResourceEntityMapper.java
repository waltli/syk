package com.sbolo.syk.admin.dao;

import java.util.List;
import java.util.Map;

import com.sbolo.syk.admin.po.MovieResourceEntity;

public interface MovieResourceEntityMapper {
    List<MovieResourceEntity> selectListByCondition(Map<String, Object> params);
    
    List<MovieResourceEntity> selectListByConditionWithLabel(Map<String, Object> params);
    
    int selectListByConditionCount(Map<String, Object> params);
    
    int selectListByConditionWithLabelCount(Map<String, Object> params);
    
    MovieResourceEntity selectByMovieId(String movieId);
    
    MovieResourceEntity selectByPureNameAndLimitReleaseTime(Map<String, Object> params);
}
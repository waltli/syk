package com.sbolo.syk.fetch.dao;

import java.util.List;
import java.util.Map;

import com.sbolo.syk.fetch.po.HotStatisticsEntity;

public interface HotStatisticsEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(HotStatisticsEntity record);

    HotStatisticsEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HotStatisticsEntity record);

    int updateByPrimaryKey(HotStatisticsEntity record);
    
    List<HotStatisticsEntity> selectHotByTime(Map<String, Object> params);
}
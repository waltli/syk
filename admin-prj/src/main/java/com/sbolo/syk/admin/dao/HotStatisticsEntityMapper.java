package com.sbolo.syk.admin.dao;

import java.util.List;
import java.util.Map;

import com.sbolo.syk.admin.po.HotStatisticsEntity;

public interface HotStatisticsEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(HotStatisticsEntity record);

    HotStatisticsEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HotStatisticsEntity record);

    int updateByPrimaryKey(HotStatisticsEntity record);
    
    List<HotStatisticsEntity> selectHotByTime(Map<String, Object> params);
}
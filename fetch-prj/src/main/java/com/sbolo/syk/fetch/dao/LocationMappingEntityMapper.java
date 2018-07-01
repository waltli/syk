package com.sbolo.syk.fetch.dao;

import java.util.List;

import com.sbolo.syk.fetch.po.LocationMappingEntity;

public interface LocationMappingEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LocationMappingEntity record);

    int insertSelective(LocationMappingEntity record);

    LocationMappingEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LocationMappingEntity record);

    int updateByPrimaryKey(LocationMappingEntity record);
    
    void batchInsert(List<LocationMappingEntity> locations);
}
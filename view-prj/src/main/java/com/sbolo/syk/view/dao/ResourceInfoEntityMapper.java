package com.sbolo.syk.view.dao;

import java.util.List;
import java.util.Map;

import com.sbolo.syk.view.po.ResourceInfoEntity;

public interface ResourceInfoEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ResourceInfoEntity record);

    int insertSelective(ResourceInfoEntity record);

    ResourceInfoEntity selectByResourceId(String resourceId);

    int updateByResourceIdSelective(ResourceInfoEntity record);

    ResourceInfoEntity selectHighestResource(Map<String, Object> param);

    void batchInsert(List<ResourceInfoEntity> adResources);
    
    void batchUpdateByResourceIdSelective(List<ResourceInfoEntity> upResources);
    
    List<ResourceInfoEntity> selectAllNoCondition();
    
    List<ResourceInfoEntity> selectByMovieIdOrder(Map<String, Object> params);
    
    List<ResourceInfoEntity> selectByMovieIdOrderNostatus(Map<String, Object> params);
    
    List<ResourceInfoEntity> selectAllByMovieId(String movieId);
    
    void signStatusByResourceId(Map<String, Object> params);
    
    void signStatusByMovieId(Map<String, Object> params);
    
    List<ResourceInfoEntity> selectAllByMovieIdAndStatus(Map<String, Object> params);
}
package com.sbolo.syk.admin.dao;

import java.util.List;

import com.sbolo.syk.admin.po.LabelMappingEntity;

public interface LabelMappingEntityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LabelMappingEntity record);

    int insertSelective(LabelMappingEntity record);

    int updateByPrimaryKeySelective(LabelMappingEntity record);

    int updateByPrimaryKey(LabelMappingEntity record);
    
    void batchInsert(List<LabelMappingEntity> labels);
    
    List<String> selectLabelsGroupLabel();
}
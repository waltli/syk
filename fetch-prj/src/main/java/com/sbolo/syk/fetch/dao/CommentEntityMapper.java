package com.sbolo.syk.fetch.dao;

import com.sbolo.syk.fetch.po.CommentEntity;

public interface CommentEntityMapper {
    int deleteByPrimaryKey(Integer id); 

    int insert(CommentEntity record);

    int insertSelective(CommentEntity record);

    CommentEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommentEntity record);

    int updateByPrimaryKey(CommentEntity record);
}
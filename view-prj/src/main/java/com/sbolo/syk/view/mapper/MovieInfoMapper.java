package com.sbolo.syk.view.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.pagehelper.PageInfo;
import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.view.entity.MovieInfoEntity;

import tk.mybatis.mapper.common.Mapper;

public interface MovieInfoMapper extends Mapper<MovieInfoEntity>, BatchWriteMapper<MovieInfoEntity> {

	List<MovieInfoEntity> selectByAssociation(Map<String, Object> params);
	
	List<MovieInfoEntity> selectByAssociationWithLabel(Map<String, Object> params);
	
	@Update("update movie_info set count_click = count_click+1 where prn=#{prn}")
	int updateCountClick(String prn);
	
	@Update("update movie_info set count_comment = count_comment+1 where prn=#{prn}")
	int updateCountComment(String prn);
	
	@Update("update movie_info set count_comment = count_comment-#{size} where prn=#{prn}")
	int updateCountCommentSub(@Param("prn") String prn, @Param("size") int size);
	
	@Update("update movie_info set count_download = count_download+1 where prn=#{prn}")
	int updateCountDownload(String prn);
	
	@ResultMap("BaseResultMap")
	@Select("select t.* from movie_info t where t.prn=#{prn}")
	MovieInfoEntity selectByPrn(String prn);
}

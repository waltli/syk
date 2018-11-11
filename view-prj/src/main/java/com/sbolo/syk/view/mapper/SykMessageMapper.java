package com.sbolo.syk.view.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sbolo.syk.common.mvc.mapper.BatchWriteMapper;
import com.sbolo.syk.view.entity.SykMessageEntity;

import tk.mybatis.mapper.common.Mapper;

public interface SykMessageMapper extends Mapper<SykMessageEntity>, BatchWriteMapper<SykMessageEntity> {
	
	@Select("select t.prn from syk_message t where t.msg_level = 1 and t.pkey = #{pkey}")
	List<String> selectRootPrns(String pkey);
	
	List<SykMessageEntity> batchSelectAssociationByRootPrns(List<String> rootPrnList);
	
	List<SykMessageEntity> selectAssociationByHot(@Param("likeCount") Integer likeCount, @Param("pkey") String pkey);
	
	List<Map<String, Object>> countMessageByAuthor(@Param("pkey") String pkey, @Param("set") Set<String> authorSet);
	
	@Update("update syk_message t set t.like_count = t.like_count+1 where prn = #{msgPrn}")
	int addLike(String msgPrn);
	
	@Update("update syk_message t set t.like_count = t.like_count-1 where prn = #{msgPrn}")
	int subLike(String msgPrn);
	
	int deleteByPrns(List<String> prns);
	
	@Select("select prn from syk_message where LOCATE(#{msgPrnEvo}, prn_line)>0")
	List<String> selectByPrnLine(String msgPrnEvo);
}

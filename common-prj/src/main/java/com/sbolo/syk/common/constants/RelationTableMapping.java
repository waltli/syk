package com.sbolo.syk.common.constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置表之间关联关系类
 * @author zhz
 *
 */
public class RelationTableMapping {
	
	/**
	 * 标准表名
	 */
	public static final String STANDARD_TABLE_NAME = "bzjx_eqp_maint_rec_b";
	/**
	 * 关联标准表使用的列名
	 */
	public static final String STANDARD_RELATION_KEY_NAME = "std_prn";
	
	/**
	 * 产品表名
	 */
	public static final String PRODUCT_TABLE_NAME = "bzjx_prod_b";
	/**
	 * 关联产品表使用的列名
	 */
	public static final String PRODUCT_RELATION_KEY_NAME = "prod_prn";
	
	/**
	 * 项目表名
	 */
	public static final String PROJECT_TABLE_NAME = "bzjx_prj_b";
	/**
	 * 关联项目表使用的列名
	 */
	public static final String PROJECT_RELATION_KEY_NAME = "prj_prn";
	
	/**
	 * 存放关联对应表的表名集合在Map中的键名
	 */
	public static final String RELATION_LIST_KEY_NAME = "relationList";
	/**
	 * 存放关联对应表的列名在Map中的键名
	 */
	public static final String RELATION_KEY_NAME = "relationKeyName";
	
	/**
	 * 返回已配置的关联关系
	 * @return
	 */
	public static Map<String,Map<String,Object>> getRelationTable(){

		Map<String,Map<String,Object>> relationTableMap = new LinkedHashMap<>();
		
		//标准关联表信息集合
		List<String> standardRelationTableList = new ArrayList<>();
		standardRelationTableList.add("bzjx_prj_std_f");
		standardRelationTableList.add("bzjx_prod_std_f");
		standardRelationTableList.add("bzjx_std_file_b");
		//产品关联表信息集合
		List<String> productRelationTableList = new ArrayList<>();
		//项目关联表信息结合
		List<String> projectRelationTableList = new ArrayList<>();
		
		//标准
		Map<String,Object> standardMap = new LinkedHashMap<>();
		standardMap.put(RELATION_LIST_KEY_NAME, standardRelationTableList);
		standardMap.put(RELATION_KEY_NAME, STANDARD_RELATION_KEY_NAME);
		
		//产品
		Map<String,Object> productMap = new LinkedHashMap<>();
		productMap.put(RELATION_LIST_KEY_NAME, productRelationTableList);
		productMap.put(RELATION_KEY_NAME, PRODUCT_RELATION_KEY_NAME);
		
		//项目
		Map<String,Object> projectMap = new LinkedHashMap<>();
		projectMap.put(RELATION_LIST_KEY_NAME, projectRelationTableList);
		projectMap.put(RELATION_KEY_NAME, PROJECT_RELATION_KEY_NAME);
		
		//封装
		relationTableMap.put(STANDARD_TABLE_NAME, standardMap);
		relationTableMap.put(PRODUCT_TABLE_NAME, productMap);
		relationTableMap.put(PROJECT_TABLE_NAME, projectMap);
		
		return relationTableMap;
	}
}

package com.sbolo.syk.common.mvc.dao;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sbolo.syk.common.annotation.GroupSearch;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.ConnTypeEnum;
import com.sbolo.syk.common.constants.JudgeTypeEnum;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.tools.VOUtils;

/**
 * 针对系统需求对Jfinal Db+record的一些方法操作类
 * @author zhz
 *
 */
public class CustomDbRecordUtil {
	
	public static final String DIC_PARENT_CODE = "parent_code";
	

	/**
	 * 拼接更新Sql
	 * @param tableName 表名
	 * @param pKeys	条件列名
	 * @param ids	条件对应的值
	 * @param record record对象
	 * @param sql 返回的sql对象
	 * @param paras 返回的参数值对象
	 * @param isUpAll 是否更新所有字段 false 不更新值为null的列
	 * @param isUpVer 是否版本控制
	 */
	public static void updateRecordToSql(String tableName, String[] pKeys, Object[] ids, Record record, StringBuilder sql, List<Object> paras,Boolean isUpAll,Boolean isUpVer){
		tableName = tableName.trim();
		
		DbKit.getConfig().getDialect().trimPrimaryKeys(pKeys);
		
		//拼接sql
		sql.append("update ").append(tableName).append(" set ");
		for (Entry<String, Object> e: record.getColumns().entrySet()) {
			String colName = e.getKey();
			//判断列名是否等于条件列名 否则不予添加 （乐观锁列名除外）
			if(isUpVer){
				if (!DbKit.getConfig().getDialect().isPrimaryKey(colName, pKeys) || CommonConstants.UP_VERSION_COLUMN_NAME.equals(colName)) {
					if(isUpAll == false && e.getValue() == null){
						continue;
					}
					if (paras.size() > 0) {
						sql.append(", ");
					}
					sql.append(colName).append(" = ? ");
					paras.add(e.getValue());
				}
			}else{
				if (!DbKit.getConfig().getDialect().isPrimaryKey(colName, pKeys)) {
					if(isUpAll == false && e.getValue() == null){
						continue;
					}
					if (paras.size() > 0) {
						sql.append(", ");
					}
					sql.append(colName).append(" = ? ");
					paras.add(e.getValue());
				}
			}
			
		}
		//拼接条件
		sql.append(" where ");
		for (int i=0; i<pKeys.length; i++) {
			if (i > 0) {
				sql.append(" and ");
			}
			sql.append(pKeys[i]).append(" = ?");
			paras.add(ids[i]);
		}
	}
	/**
	 * 修改数据(乐观锁)
	 * @param primaryKey 条件列名(,号拆分,默认为prn和版本号列名)
	 * @param tableName 表名
	 * @param record 操作的record对象
	 * @return
	 */
	public static boolean updateByVersion(String primaryKey,String tableName,Record record,Boolean isUpAll){
		//String primaryKey = "id,"+CommonConstants.UP_VERSION_COLUMN_NAME;
		if(record == null){
			throw new BusinessException("操作数据对象为空。");
		}else{
			record.remove(CommonConstants.ID_NAME);
		}
		//如果主键为空 默认为id列和版本号列
		if(StringUtils.isNotBlank(primaryKey)){
			primaryKey = CommonConstants.PRN+","+CommonConstants.UP_VERSION_COLUMN_NAME;
		}
		//primaryKey+=","+CommonConstants.ID_NAME;
		String[] pKeys = primaryKey.split(",");
		Object[] condition = new Object[pKeys.length];
		List<Object> paras = new ArrayList<Object>();
		for (int i=0; i<pKeys.length; i++) {
			condition[i] = record.get(pKeys[i].trim());	// .trim() is important!
			if (condition[i] == null)
				throw new BusinessException("更新数据失败, " + pKeys[i] + " 不能为空.");
		}
		StringBuilder sql = new StringBuilder();
		//更新数据版本
		record.set(CommonConstants.UP_VERSION_COLUMN_NAME, ((Integer)record.get(CommonConstants.UP_VERSION_COLUMN_NAME))+1);
		//拼接Sql
		updateRecordToSql(tableName,pKeys,condition,record,sql,paras,isUpAll,true);
		return Db.update(sql.toString(), paras.toArray()) >= 1;
	}
	
	/**
	 * 修改数据
	 * @param primaryKey 条件列名(,号拆分,默认为prn)
	 * @param tableName 表名
	 * @param record 操作的record对象
	 * @return
	 */
	public static boolean update(String primaryKey,String tableName,Record record,Boolean isUpAll){
		//String primaryKey = "id,"+CommonConstants.UP_VERSION_COLUMN_NAME;
		if(record == null){
			throw new BusinessException("操作数据对象为空。");
		}else{
			record.remove(CommonConstants.ID_NAME);
		}
		//如果主键为空 默认为id列和版本号列
		if(StringUtils.isNotBlank(primaryKey)){
			primaryKey = CommonConstants.PRN;
		}
		//primaryKey+=","+CommonConstants.ID_NAME;
		String[] pKeys = primaryKey.split(",");
		Object[] condition = new Object[pKeys.length];
		List<Object> paras = new ArrayList<Object>();
		for (int i=0; i<pKeys.length; i++) {
			condition[i] = record.get(pKeys[i].trim());	// .trim() is important!
			if (condition[i] == null)
				throw new BusinessException("更新数据失败, " + pKeys[i] + " 不能为空.");
		}
		StringBuilder sql = new StringBuilder();
		//更新数据版本
		//record.set(CommonConstants.UP_VERSION_COLUMN_NAME, ((Integer)record.get(CommonConstants.UP_VERSION_COLUMN_NAME))+1);
		//拼接Sql
		updateRecordToSql(tableName,pKeys,condition,record,sql,paras,isUpAll,false);
		return Db.update(sql.toString(), paras.toArray()) >= 1;
	}
	
	/**
	 * 将实体集合数据 转换成Record对象集合
	 * @param list
	 * @return
	 */
	public static List<Record> getListRecord(List<?> list){
		if(list == null || list.size() == 0){
			throw new BusinessException("数据不能为空。");
		}
		List<Record> recordList = new ArrayList<>();
		for(int i=0;i<list.size();i++){
			recordList.add(getRecord(list.get(i)));
		}
		return recordList;
	}
	/**
     * 实体对象转成Record对象
     * @param obj 实体对象
     * @return
     */
    public static Record getRecord(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return new Record();
        }
        Class<? extends Object> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Record().setColumns(map);
    }
    /**
     * 实体对象转成Record对象(如果为空则不赋值)
     * @param obj 实体对象
     * @return
     */
    public static Record getRecordNotNull(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return new Record();
        }
        Class<? extends Object> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if(field.get(obj) != null){
                	map.put(field.getName(), field.get(obj));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Record().setColumns(map);
    }
    
    /**
     * Record 转 任意实例类
     * @param obj 实体对象
     * @return
     */
	public static <T> T converModel(Class<T> clazz,Record record){
			
			try {
				if(record == null){
					return null;
				}
				ConvertUtils.register(new Converter(){
			        @SuppressWarnings({ "rawtypes", "unchecked" })    
			        @Override    
			        public Object convert(Class arg0, Object arg1){     
			            if(arg1 == null)    
			            {    
			                return null;    
			            }    
			            if(!(arg1 instanceof String))    
			            {    
			                return arg1;    
			            }    
			            String str = (String)arg1;    
			            if(str.trim().equals(""))    
			            {    
			                return null;    
			            }    
			                 
			            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
			                 
			            try{    
			                return sd.parse(str);    
			            }    
			            catch(ParseException e)    
			            {    
			                throw new RuntimeException(e);    
			            }
			        }
			    }, java.util.Date.class); 
				return VOUtils.po2vo(record.getColumns(), clazz);
			} catch (Exception e) { 
				 e.printStackTrace();
			} 
			
		 return null;
		}
	 /**
     * Record集合 转 任意实例类集合
     * @return
     */
	public static <T> List<T> converModel(Class<T> clazz,List<Record> recordList){
		List<T> list = new ArrayList<>();
		try {
			if(recordList != null && recordList.size() > 0){
				for(Record record:recordList){
					list.add(converModel(clazz,record));
				}
			}
		} catch (Exception e) { 
			 e.printStackTrace();
		} 
			
		return list;
	}
	
	/**
	 * 拼接查询的列名
	 * @param selectColumns
	 * @return
	 */
	public static String appendSqlByCustomSelect(String selectColumns){
		if(StringUtils.isBlank(selectColumns)){
			return "";
		}
		String[] temp = selectColumns.split(",");
		StringBuffer sql = new StringBuffer();
		for(String tempStr:temp){
			sql.append(CommonConstants.USE_FILTER_CHAR).append(tempStr).append(CommonConstants.USE_FILTER_CHAR).append(",");
		}
		if(sql.length()==0){
			return "";
		}
		return sql.substring(0, sql.length()-1);
	}
	/**
	 * 拼接查询的条件增加占位符
	 * @param conditions 条件列拼接字符串
	 * @param judge 条件拼接方式 and or
	 * @return
	 */
	public static String appendSqlByCustomCondition(String conditions,String judge){
		if(StringUtils.isBlank(conditions)){
			return "";
		}
		String[] temp = conditions.split(",");
		StringBuffer sql = new StringBuffer();
		for(Integer i=0;i<temp.length;i++){
			String tempStr = temp[i];
			sql.append(CommonConstants.USE_FILTER_CHAR).append(tempStr).append(CommonConstants.USE_FILTER_CHAR).append("= ? ");
			if(i!=temp.length-1){
				sql.append(" "+judge+" ");
			}
		}
		if(sql.length()==0){
			return "";
		}
		return sql.substring(0, sql.length()-1);
	}
	
	/**
	 * 拼接IN 或者  NOT IN 的问号占位符
	 * @param params
	 * @return
	 */
	public static String getInSql(String params){
		
		if(StringUtils.isBlank(params)){
			return "";
		}
		String[] paramsArray = params.split(",");
		String inSql = "";
		for(int i=0;i<paramsArray.length;i++){
			inSql+="?";
			if(i!=paramsArray.length-1){
				inSql+=",";
			}
		}
		
		return inSql;
	}
	
	/**
	 * 返回 WHERE IN 或者 NOT IN 对应的参数集合
	 * @param params
	 * @return
	 */
	public static List<Object> getInParams(String params){
		 String[] paramsArray = params.split(",");
	     List<Object> paramsList = new ArrayList<Object>(Arrays.asList(paramsArray));
	     return paramsList;
	}
}

package com.sbolo.syk.common.mvc.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RelationTableMapping;
import com.sbolo.syk.common.constants.StatusEnum;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.mvc.dao.BaseDao;
import com.sbolo.syk.common.mvc.dao.CustomDbRecordUtil;

/**
 * 公用Dao实现
 * 注：子类继承使用某些方法需要重新赋值tableName为对应的表名
 * @author zhz
 *
 */
@Repository
public class BaseDaoImpl implements BaseDao{
	/**
	 * 对应表名
	 */
	public String tableName = "";
	
	@Override
	public Boolean save(Record record) {
		if(record != null && record.getColumns() != null){
			if(record.getColumns().containsKey(CommonConstants.ST_NAME)&&record.getColumns().get(CommonConstants.ST_NAME) == null){
				record.set(CommonConstants.ST_NAME, StatusEnum.CS.getCode());
			}
		}
		if(record != null && record.getLong("id") == null){
			record.remove("id");
		}
		return Db.save(tableName, record);
	}
	
	@Override
	public Boolean save(String tableName,Record record) {
		if(record != null && record.getColumns() != null){
			if(record.getColumns().containsKey(CommonConstants.ST_NAME)&&record.getColumns().get(CommonConstants.ST_NAME) == null){
				record.set(CommonConstants.ST_NAME, StatusEnum.CS.getCode());
			}
		}
		if(record != null && record.getLong("id") == null){
			record.remove("id");
		}
		return Db.save(tableName, record);
	}

	@Override
	public Boolean update(Record record) {
		return Db.update(tableName, record);
	}
	@Override
	public Boolean update(Record record,Boolean isUpAll) {
		return CustomDbRecordUtil.update(CommonConstants.PRN, tableName, record, isUpAll);
	}
	
	@Override
	public Boolean update(String tableName,Record record) {
		return Db.update(tableName, record);
	}
	@Override
	public Boolean update(String tableName,Record record,Boolean isUpAll) {
		return CustomDbRecordUtil.update(CommonConstants.PRN, tableName, record, isUpAll);
	}

	@Override
	public Boolean deleteById(Long id) {
		return Db.deleteById(tableName, id);
	}
	
	@Override
	public Boolean deleteById(String tableName,Long id) {
		return Db.deleteById(tableName, id);
	}

	@Override
	public Page<Record> getPageList(Integer page, Integer pageSize) {
		return Db.paginate(page,pageSize,"select *","from "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR);
	}
	
	@Override
	public Page<Record> getPageList(String tableName,Integer page, Integer pageSize) {
		return Db.paginate(page,pageSize,"select *","from "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR);
	}

	@Override
	public Record selectById(Long id) {
		return Db.findById(tableName, id);
	}
	
	@Override
	public Record selectById(String tableName,Long id) {
		return Db.findById(tableName, id);
	}

	@Override
	public Boolean addColumn(String columnName, String type, Integer length) {
		return Db.update("alter table "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" add "+CommonConstants.USE_FILTER_CHAR+columnName +CommonConstants.USE_FILTER_CHAR+" "+ type+"("+length+")") > 0;
	}
	
	@Override
	public Boolean addColumn(String tableName,String columnName, String type, Integer length) {
		return Db.update("alter table "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" add "+CommonConstants.USE_FILTER_CHAR+columnName +CommonConstants.USE_FILTER_CHAR+" "+ type+"("+length+")") > 0;
	}

	@Override
	public Boolean updateColumn(String columnName, String type, Integer length) {
		return Db.update("alter table "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" MODIFY "+CommonConstants.USE_FILTER_CHAR+columnName +CommonConstants.USE_FILTER_CHAR+" "+ type+"("+length+")") > 0;
	}
	
	@Override
	public Boolean updateColumn(String tableName,String columnName, String type, Integer length) {
		return Db.update("alter table "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" MODIFY "+CommonConstants.USE_FILTER_CHAR+columnName +CommonConstants.USE_FILTER_CHAR+" "+ type+"("+length+")") > 0;
	}

	@Override
	public Boolean updateByVersion(Record record,Boolean isUpAll) {
		String primaryKey = CommonConstants.PRN+","+CommonConstants.UP_VERSION_COLUMN_NAME;
		return CustomDbRecordUtil.updateByVersion(primaryKey, tableName, record,isUpAll);
	}

	@Override
	public Boolean updateByVersion(String tableName, Record record,Boolean isUpAll) {
		String primaryKey = CommonConstants.PRN+","+CommonConstants.UP_VERSION_COLUMN_NAME;
		return CustomDbRecordUtil.updateByVersion(primaryKey, tableName, record,isUpAll);
	}

	@Override
	public Boolean updateByVersionAndPrimaryKey(String primaryKey, Record record,Boolean isUpAll) {
		if(StringUtils.isNotBlank(primaryKey)){
			throw new BusinessException("条件列名不能为空");
		}
		if(!primaryKey.endsWith(",")){
			primaryKey+=",";
		}
		primaryKey+=CommonConstants.UP_VERSION_COLUMN_NAME;
		return CustomDbRecordUtil.updateByVersion(primaryKey, tableName, record,isUpAll);
	}

	@Override
	public Boolean updateByVersionAndPrimaryKey(String tableName, String primaryKey, Record record,Boolean isUpAll) {
		if(StringUtils.isNotBlank(primaryKey)){
			throw new BusinessException("条件列名不能为空");
		}
		if(!primaryKey.endsWith(",")){
			primaryKey+=",";
		}
		primaryKey+=CommonConstants.UP_VERSION_COLUMN_NAME;
		return CustomDbRecordUtil.updateByVersion(primaryKey, tableName, record,isUpAll);
	}

	@Override
	public Boolean updateByPrimaryKey(String primaryKey, Record record) {
		if(StringUtils.isBlank(primaryKey)){
			throw new BusinessException("条件列名不能为空");
		}
		return Db.update(tableName,primaryKey, record);
	}

	@Override
	public Boolean updateByPrimaryKey(String tableName, String primaryKey, Record record) {
		if(StringUtils.isBlank(primaryKey)){
			throw new BusinessException("条件列名不能为空");
		}
		return Db.update(tableName,primaryKey, record);
	}

	@Override
	public Boolean saveAndUpVer(Record record) {
		if(record == null){
			throw new BusinessException("操作数据对象不能为空。");
		}
		if(record != null && record.getColumns() != null){
			if(record.getColumns().containsKey(CommonConstants.ST_NAME)&&record.getColumns().get(CommonConstants.ST_NAME) == null){
				record.set(CommonConstants.ST_NAME, StatusEnum.CS.getCode());
			}
		}
		if(record.getColumns().get(CommonConstants.UP_VERSION_COLUMN_NAME) == null){
			record.set(CommonConstants.UP_VERSION_COLUMN_NAME, 1);
		}
		if(record != null && record.getLong("id") == null){
			record.remove("id");
		}
		return Db.save(tableName, record);
	}
	
	
	public Boolean saveAndUpVer(String tableName,String uniqueColumnName,Record record) {
		if(record == null){
			throw new BusinessException("操作数据对象不能为空。");
		}
		if(record != null && record.getColumns() != null){
			if(record.getColumns().containsKey(CommonConstants.ST_NAME)&&record.getColumns().get(CommonConstants.ST_NAME) == null){
				record.set(CommonConstants.ST_NAME, StatusEnum.CS.getCode());
			}
		}
		if(record.getColumns().get(CommonConstants.UP_VERSION_COLUMN_NAME) == null){
			record.set(CommonConstants.UP_VERSION_COLUMN_NAME, 1);
		}
		if(record != null && record.getLong("id") == null){
			record.remove("id");
		}
		
		Record oldRecord = Db.findFirst("select * from "+tableName +" where "+uniqueColumnName+" = ? and "+CommonConstants.UP_VERSION_COLUMN_NAME+" = (select max(b."+CommonConstants.UP_VERSION_COLUMN_NAME+") from "+tableName+" b where b."+uniqueColumnName+"=?)",record.get(uniqueColumnName),record.get(uniqueColumnName));
		if(oldRecord == null || oldRecord.getColumns().get(CommonConstants.UP_VERSION_COLUMN_NAME) == null || oldRecord.getInt(CommonConstants.UP_VERSION_COLUMN_NAME).intValue() == record.getInt(CommonConstants.UP_VERSION_COLUMN_NAME).intValue()){
			record.set(CommonConstants.UP_VERSION_COLUMN_NAME, oldRecord.getInt(CommonConstants.UP_VERSION_COLUMN_NAME) + 1);
			return Db.save(tableName, record);
		}else{
			return false;
		}
		
	}

	@Override
	public Boolean saveAndUpVer(String tableName, Record record) {
		if(record == null){
			throw new BusinessException("操作数据对象不能为空。");
		}
		if(record != null && record.getColumns() != null){
			if(record.getColumns().containsKey(CommonConstants.ST_NAME)&&record.getColumns().get(CommonConstants.ST_NAME) == null){
				record.set(CommonConstants.ST_NAME, StatusEnum.CS.getCode());
			}
		}
		if(record != null && record.getLong("id") == null){
			record.remove("id");
		}
		if(record.getColumns().get(CommonConstants.UP_VERSION_COLUMN_NAME) == null){
			record.set(CommonConstants.UP_VERSION_COLUMN_NAME, 1);
		}
		return Db.save(tableName, record);
	}

	@Override
	public Boolean deleteByPrn(String prn) {
		return deleteCheckRelation(tableName,prn);
	}
	
	@Override
	public Boolean deleteByPrn(String tableName, String prn) {
		return deleteCheckRelation(tableName,prn);
	}
	
	/**
	 * 删除并验证是否有相关联的表数据
	 * @param tableName 表名
	 * @param prn 全局唯一标示
	 * @return
	 */
	public boolean deleteCheckRelation(String tableName,String prn){
		Boolean isDel = Db.tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				Map<String,Map<String,Object>> relationMap = RelationTableMapping.getRelationTable();
				for (Map.Entry<String, Map<String,Object>> entry : relationMap.entrySet()) { 
					  if(entry.getKey().equals(tableName)){
						  deleteByRelation(entry.getValue(),prn);
						  return Db.deleteById(tableName, CommonConstants.PRN, prn);
					  }
				}
				return Db.deleteById(tableName, CommonConstants.PRN, prn);
			}
		});
		return isDel;
	}
	
	/**
	 * 删除关联表的相关数据
	 * @param relationMap 配置的关联表数据信息
	 * @param prn 关联的全局唯一标识
	 * @return
	 */
	public Boolean deleteByRelation(Map<String,Object> relationMap,String prn){
		
		@SuppressWarnings("unchecked")
		List<String> relationList = (List<String>)relationMap.get(RelationTableMapping.RELATION_LIST_KEY_NAME);
		
		String relationPrnName = relationMap.get(RelationTableMapping.RELATION_KEY_NAME).toString();
		
		for(String temp : relationList){
			Db.deleteById(temp, relationPrnName, prn);
		}
		return true;
	}

	@Override
	public Record selectByPrn(String prn) {
		//return selectByPrnAndSt(prn);
		return Db.findById(tableName, CommonConstants.PRN, prn);
	}

	@Override
	public Record selectByIdPrn(String tableName, String prn) {
		//return selectByPrnAndSt(tableName,prn);
		return Db.findById(tableName, CommonConstants.PRN, prn);
	}
	
	@Override
	public Record selectByPrnAndSt(String prn) {
		String sql = "select * from "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" where "+CommonConstants.USE_FILTER_CHAR+CommonConstants.PRN+CommonConstants.USE_FILTER_CHAR+" = ? AND "+CommonConstants.USE_FILTER_CHAR+CommonConstants.ST_NAME+CommonConstants.USE_FILTER_CHAR+" !=?";
		return Db.findFirst(sql,prn,StatusEnum.SC.getCode());
	}

	@Override
	public Record selectByPrnAndSt(String tableName, String prn) {
		String sql = "select * from "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" where "+CommonConstants.USE_FILTER_CHAR+CommonConstants.PRN+CommonConstants.USE_FILTER_CHAR+" = ? AND "+CommonConstants.USE_FILTER_CHAR+CommonConstants.ST_NAME+CommonConstants.USE_FILTER_CHAR+" !=?";
		return Db.findFirst(sql,prn,StatusEnum.SC.getCode());
	}

	@Override
	public Boolean saveList(List<Record> recordList) {
		if(recordList == null || recordList.size() == 0){
			throw new BusinessException("批量添加的数据不能为空。");
		}
		for(Record record:recordList){
			if(record != null && record.getLong("id") == null){
				record.remove("id");
			}
		}
		boolean isSucuess = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				return Db.batchSave(tableName, recordList, recordList.size()).length  == recordList.size();
			}
		});
		return isSucuess;
	}

	@Override
	public Boolean saveList(String tableName, List<Record> recordList) {
		if(recordList == null || recordList.size() == 0){
			throw new BusinessException("批量添加的数据不能为空。");
		}
		for(Record record:recordList){
			if(record != null && record.getLong("id") == null){
				record.remove("id");
			}
		}
		boolean isSucuess = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				return Db.batchSave(tableName, recordList, recordList.size()).length  == recordList.size();
			}
		});
		return isSucuess;
	}
	
	@Override
	public Boolean updateList(List<Record> recordList) {
		if(recordList == null || recordList.size() == 0){
			throw new BusinessException("批量添加的数据不能为空。");
		}
		boolean isSucuess = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				return Db.batchUpdate(tableName, recordList, recordList.size()).length  == recordList.size();
			}
		});
		return isSucuess;
	}

	@Override
	public Boolean updateList(String tableName, List<Record> recordList) {
		if(recordList == null || recordList.size() == 0){
			throw new BusinessException("批量添加的数据不能为空。");
		}
		boolean isSucuess = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				return Db.batchSave(tableName, recordList, recordList.size()).length  == recordList.size();
			}
		});
		return isSucuess;
	}

	@Override
	public Boolean deleteByCustom(String customColumns, Object... objects) {
		return Db.deleteById(tableName, customColumns, objects);
	}

	@Override
	public Boolean deleteByCustom(String tableName, String customColumns, Object... objects) {
		return Db.deleteById(tableName, customColumns, objects);
	}

	@Override
	public Record selectByCustome(String customColumnName, List<Object> paramsValues) {
		return Db.findById(tableName, customColumnName, paramsValues.toArray());
	}

	@Override
	public Record selectByCustom(String tableName, String customColumnName, Object... object) {
		return Db.findById(tableName, customColumnName, object);
	}

	@Override
	public List<Record> selectListByCustom(String tableName, String customColumnName, Object... object) {
		if(StringUtils.isBlank(tableName) || StringUtils.isBlank(customColumnName) ||object==null){
			throw new BusinessException("表名和自定义条件列名以及对应值均不能为空。");
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select * from "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" where ");
		String[] columnNames = customColumnName.split(",");
		for(int i=0;i<columnNames.length;i++){
			sb.append(CommonConstants.USE_FILTER_CHAR+columnNames[i]+CommonConstants.USE_FILTER_CHAR+" = ? ");
			if(i != 0 && i!=columnNames.length-1){
				sb.append(" and ");
			}
		}
		return Db.find(sb.toString(),object);
	}

	@Override
	public List<Record> selectListByCustom(String customColumnName, List<Object> paramsValues) {
		if(StringUtils.isBlank(tableName) || StringUtils.isBlank(customColumnName) ||paramsValues==null){
			throw new BusinessException("表名和自定义条件列名以及对应值均不能为空。");
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select * from "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" where ");
		String[] columnNames = customColumnName.split(",");
		for(int i=0;i<columnNames.length;i++){
			sb.append(CommonConstants.USE_FILTER_CHAR+columnNames[i]+CommonConstants.USE_FILTER_CHAR+" = ? ");
			if(i != 0 && i!=columnNames.length-1){
				sb.append(" and ");
			}
		}
		return Db.find(sb.toString(),paramsValues.toArray());
	}

	@Override
	public Boolean deleteByPrnAndSt(String tableName, String prn) {
		String sql = " update "+CommonConstants.USE_FILTER_CHAR +tableName+CommonConstants.USE_FILTER_CHAR+" set "+CommonConstants.USE_FILTER_CHAR+CommonConstants.ST_NAME+CommonConstants.USE_FILTER_CHAR+" = ? where "+CommonConstants.PRN+" = ?";
		return Db.update(sql,StatusEnum.SC.getCode(),prn) > 0;
	}

	@Override
	public Boolean deleteByPrnAndSt(String prn) {
		String sql = " update "+CommonConstants.USE_FILTER_CHAR +tableName+CommonConstants.USE_FILTER_CHAR+" set "+CommonConstants.USE_FILTER_CHAR+CommonConstants.ST_NAME+CommonConstants.USE_FILTER_CHAR+" = ? where "+CommonConstants.PRN+" = ?";
		return Db.update(sql,StatusEnum.SC.getCode(),prn) > 0;
	}

	@Override
	public Page<Record> getPageListBySt(Integer page, Integer pageSize) {
		return Db.paginate(page,pageSize,"select *","from "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" WHERE "+CommonConstants.USE_FILTER_CHAR+CommonConstants.ST_NAME+CommonConstants.USE_FILTER_CHAR+" != ?",StatusEnum.SC.getCode());
	}

	@Override
	public Page<Record> getPageListBySt(String tableName, Integer page, Integer pageSize) {
		return Db.paginate(page,pageSize,"select *","from "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" WHERE "+CommonConstants.USE_FILTER_CHAR+CommonConstants.ST_NAME+CommonConstants.USE_FILTER_CHAR+" != ?",StatusEnum.SC.getCode());
	}

	@Override
	public Page<Record> getPageListByCustom(String tableName, String customSelectKey, String customCondition,
			List<Object> params, Integer page, Integer pageSize,String judge) {
		String selectSql = CustomDbRecordUtil.appendSqlByCustomSelect(customSelectKey);
		String whereSql = CustomDbRecordUtil.appendSqlByCustomCondition(customCondition, judge);
		return Db.paginate(page,pageSize,"select "+selectSql,"from "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" WHERE "+whereSql,params.toArray());
	}
	@Override
	public Page<Record> getPageListByCustom(String customSelectKey, String customCondition,
			List<Object> params, Integer page, Integer pageSize,String judge) {
		String selectSql = CustomDbRecordUtil.appendSqlByCustomSelect(customSelectKey);
		String whereSql = CustomDbRecordUtil.appendSqlByCustomCondition(customCondition, judge);
		return Db.paginate(page,pageSize,"select "+selectSql,"from "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" WHERE "+whereSql,params.toArray());
	}

	@Override
	public Integer deleteByPrns(String tableName, String prns) {
		
		String inSql = CustomDbRecordUtil.getInSql(prns);
		return Db.delete("delete from "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" WHERE "+CommonConstants.PRN +" in ("+inSql+")",CustomDbRecordUtil.getInParams(prns).toArray());
	}

	@Override
	public Integer deleteByPrns(String prns) {
		String inSql = CustomDbRecordUtil.getInSql(prns);
		return Db.delete("delete from "+CommonConstants.USE_FILTER_CHAR+tableName+CommonConstants.USE_FILTER_CHAR+" WHERE "+CommonConstants.PRN +" in ("+inSql+")",CustomDbRecordUtil.getInParams(prns).toArray());
	}

	@Override
	public Integer deleteByPrnsSt(String tableName, String prns) {
		String inSql = CustomDbRecordUtil.getInSql(prns);
		List<Object> list = CustomDbRecordUtil.getInParams(prns);
		list.add(0,StatusEnum.SC.getCode());
		return Db.delete("update "+tableName+" SET "+CommonConstants.ST_NAME+" = ? WHERE "+CommonConstants.PRN +" in ("+inSql+")",list.toArray());
	}

	@Override
	public Integer deleteByPrnsSt(String prns) {
		String inSql = CustomDbRecordUtil.getInSql(prns);
		List<Object> list = CustomDbRecordUtil.getInParams(prns);
		list.add(0,StatusEnum.SC.getCode());
		return Db.delete("update "+tableName+" SET "+CommonConstants.ST_NAME+" = ? WHERE "+CommonConstants.PRN +" in ("+inSql+")",list.toArray());
	}

	@Override
	public Integer updateListByVersion(String tableName, List<Record> recordList,Boolean isUpAll) {
		String primaryKey = CommonConstants.PRN+","+CommonConstants.UP_VERSION_COLUMN_NAME;
		Integer count = 0;
		for(Record record:recordList){
			Boolean isUpdate = CustomDbRecordUtil.updateByVersion(primaryKey, tableName, record,isUpAll);
			if(isUpdate){
				count+=1;
			}
		}
		return count;
	}
}

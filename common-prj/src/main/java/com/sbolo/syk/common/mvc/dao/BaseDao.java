package com.sbolo.syk.common.mvc.dao;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 公用Dao层方法
 * 注：默认主键列名为id,如果主键列名非id或多个主键则需自己新增方法
 * @author zhz
 *
 */
public interface BaseDao {
	
	/**
	 * 新增一条数据
	 * @param record
	 * @return
	 */
	Boolean save(Record record);
	/**
	 * 新增一条数据 带乐观锁
	 * @param record
	 * @return
	 */
	Boolean saveAndUpVer(Record record);
	/**
	 * 新增多条数据
	 * @param record
	 * @return
	 */
	Boolean saveList(List<Record> record);
	/**
	 * 新增多条数据对应tableName表
	 * @param record
	 * @return
	 */
	Boolean saveList(String tableName,List<Record> record);
	/**
	 * 新增一条对应tableName表数据 带乐观锁
	 * @param tableName 表名
	 * @param record
	 * @return
	 */
	Boolean saveAndUpVer(String tableName,Record record);
	
	/**
	 * 新增一条数据并带版本控制(对比表中某一相同列的最大版本号是否一致)
	 * @param tableName 表名
	 * @param uniqueColumnName 判断版本控制数据的标识列
	 * @param record 保存的数据
	 * @return
	 */
	Boolean saveAndUpVer(String tableName,String uniqueColumnName,Record record);
	/**
	 * 新增一条对应tableName表数据
	 * @param tableName 表名
	 * @param record
	 * @return
	 */
	Boolean save(String tableName,Record record);
	/**
	 * 修改一条数据
	 * @param record
	 * @return
	 */
	Boolean update(Record record);
	/**
	 * 修改一条数据
	 * @param record
	 * @param isUpAll 是否更新为空的字段
	 * @return
	 */
	Boolean update(Record record,Boolean isUpAll);
	/**
	 * 修改一条对应tableName表数据
	 * @param tableName 表名
	 * @param record
	 * @return
	 */
	Boolean update(String tableName,Record record);
	/**
	 * 修改一条数据
	 * @param isUpAll 是否更新为空的字段
	 * @param record
	 * @return
	 */
	Boolean update(String tableName,Record record,Boolean isUpAll);
	/**
	 * 修改多条数据
	 * @param record
	 * @return
	 */
	Boolean updateList(List<Record> recordList);
	/**
	 * 修改多条对应tableName表数据
	 * @param tableName 表名
	 * @param record
	 * @return
	 */
	Boolean updateList(String tableName,List<Record> recordList);
	/**
	 * 根据条件列名修改一条数据
	 * 备注：	1.逗号拼接(例如:id,user_id)
	 * 		2条件列名 会拼接到where 后面 值为record对象里面对应键的值
	 * @param primaryKey 条件列名
	 * @param record
	 * @return
	 */
	Boolean updateByPrimaryKey(String primaryKey,Record record);
	/**
	 * 根据条件列名修改一条对应tableName表数据
	 * 备注：	1.逗号拼接(例如:id,user_id)
	 * 		2条件列名 会拼接到where 后面 值为record对象里面对应键的值
	 * @param tableName 表名
	 * @param primaryKey 条件列名
	 * @param record
	 * @return
	 */
	Boolean updateByPrimaryKey(String tableName,String primaryKey,Record record);
	/**
	 * 修改(乐观锁) 
	 * 备注：根据Prn标识来修改数据
	 * @param record
	 * @param isUpAll 是否更新所有字段 false 只更新值不为空的列。
	 * @return
	 */
	Boolean updateByVersion(Record record,Boolean isUpAll);
	/**
	 * 修改(乐观锁)
	 * 备注：根据Prn标识来修改数据
	 * @param tableName 表名
	 * @param record
	 * @param isUpAll 是否更新所有字段 false 只更新值不为空的列。
	 * @return
	 */
	Boolean updateByVersion(String tableName,Record record,Boolean isUpAll);
	/**
	 * 修改(乐观锁)带条件列名
	 * 备注：	1.逗号拼接(例如:id,user_id)
	 * 		2条件列名 会拼接到where 后面 值为record对象里面对应键的值
	 * @param primaryKey 条件列名
	 * @param record
	 * @param isUpAll 是否更新所有字段 false 只更新值不为空的列。 
	 * @return
	 */
	Boolean updateByVersionAndPrimaryKey(String primaryKey,Record record,Boolean isUpAll);
	/**
	 * 修改(乐观锁)带条件列名
	 * 备注：	1.逗号拼接(例如:id,user_id)
	 * 		2条件列名 会拼接到where 后面 值为record对象里面对应键的值
	 * @param tableName 表名
	 * @param primaryKey 条件列名
	 * @param record
	 * @param isUpAll 是否更新所有字段 false 只更新值不为空的列。
	 * @return
	 */
	Boolean updateByVersionAndPrimaryKey(String tableName,String primaryKey,Record record,Boolean isUpAll);
	/**
	 * 根据ID删除数据
	 * @param id
	 * @return
	 */
	Boolean deleteById(Long id);
	/**
	 * 根据Prn删除数据
	 * @param id
	 * @return
	 */
	Boolean deleteByPrn(String prn);
	/**
	 * 根据ID删除对应tableName表数据
	 * @param tableName 表名
	 * @param id
	 * @return
	 */
	Boolean deleteById(String tableName,Long id);
	/**
	 * 根据Prn删除对应tableName表数据
	 * @param tableName 表名
	 * @param id
	 * @return
	 */
	Boolean deleteByPrn(String tableName,String prn);
	/**
	 * 根据Prns批量删除对应tableName表数据
	 * @param tableName 表名
	 * @param prns
	 * @return
	 */
	Integer deleteByPrns(String tableName,String prns);
	/**
	 * 根据Prns批量删除对应tableName表数据
	 * @param prns
	 * @return
	 */
	Integer deleteByPrns(String prns);
	/**
	 * 根据Prns批量删除对应tableName表数据(逻辑删除)
	 * @param tableName 表名
	 * @param prns
	 * @return
	 */
	Integer deleteByPrnsSt(String tableName,String prns);
	/**
	 * 根据Prns批量删除对应tableName表数据(逻辑删除)
	 * @param prns
	 * @return
	 */
	Integer deleteByPrnsSt(String prns);
	/**
	 * 根据自定义列名删除数据
	 * @param tableName 表名
	 * @param id
	 * @return
	 */
	Boolean deleteByCustom(String customColumns,Object... objects);
	/**
	 * 根据自定义列名删除对应tableName表数据
	 * @param tableName 表名
	 * @param id
	 * @return
	 */
	Boolean deleteByCustom(String tableName,String customColumns,Object... objects);
	/**
	 * 根据PRN逻辑删除
	 * @param tableName 表名
	 * @param prn 唯一标识
	 * @return
	 */
	Boolean deleteByPrnAndSt(String tableName,String prn);
	/**
	 * 根据PRN逻辑删除
	 * @param prn 唯一标识
	 * @return
	 */
	Boolean deleteByPrnAndSt(String prn);
	/**
	 * 分页查询数据
	 * @param page 页数
	 * @param pageSize 每页显示条数
	 * @return
	 */
	Page<Record> getPageList(Integer page,Integer pageSize);
	/**
	 * 分页查询对应tableName表数据
	 * @param tableName 表名
	 * @param page 页数
	 * @param pageSize 每页显示条数
	 * @return
	 */
	Page<Record> getPageList(String tableName,Integer page,Integer pageSize);
	/**
	 * 根据ID查询数据
	 * @param id
	 * @return
	 */
	Record selectById(Long id);
	/**
	 * 根据ID查询对应tableName表数据
	 * @param tableName 表名
	 * @param id
	 * @return
	 */
	Record selectById(String tableName,Long id);
	/**
	 * 根据PRN查询数据
	 * @param id
	 * @return
	 */
	Record selectByPrn(String prn);
	/**
	 * 根据prn查询对应tableName表数据
	 * @param tableName 表名
	 * @param id
	 * @return
	 */
	Record selectByIdPrn(String tableName,String prn);
	/**
	 * 根据自定义列查询数据(只有一条数据 )
	 * @param customColumnName 自定义列名 ,号拼接 条件之间关系为并且
	 * @param object 对应列名的值
	 * @return
	 */
	Record selectByCustome(String customColumnName,List<Object> paramValues);
	/**
	 * 根据自定义列查询对应tableName表数据(只有一条数据)
	 * @param tableName 表名
	 * @param customColumnName 自定义列名 ,号拼接 条件之间关系为并且
	 * @param object 对应列名的值
	 * @return
	 */
	Record selectByCustom(String tableName,String customColumnName,Object... object);
	/**
	 * 根据自定义列查询对应tableName表数据(返回多条数据)
	 * @param tableName 表名
	 * @param customColumnName 自定义列名 ,号拼接 条件之间关系为并且
	 * @param object 对应列名的值
	 * @return
	 */
	List<Record> selectListByCustom(String tableName,String customColumnName,Object... object);
	/**
	 * 根据自定义列查询数据(返回多条数据)
	 * @param customColumnName 自定义列名 ,号拼接 条件之间关系为并且
	 * @param object 对应列名的值
	 * @return
	 */
	List<Record> selectListByCustom(String customColumnName,List<Object> paramValues);
	/**
	 * 添加数据库列
	 * @param columnName 列名
	 * @param type	类型
	 * @param length 长度
	 * @return
	 */
	Boolean addColumn(String columnName,String type,Integer length);
	/**
	 * 添加对应tableName数据库列
	 * @param tableName 表名
	 * @param columnName 列名
	 * @param type	类型
	 * @param length 长度
	 * @return
	 */
	Boolean addColumn(String tableName,String columnName,String type,Integer length);
	/**
	 * 修改数据库列
	 * @param columnName 列名
	 * @param type 类型
	 * @param length 长度
	 * @return
	 */
	Boolean updateColumn(String columnName,String type,Integer length);
	/**
	 * 修改对应tableName表列
	 * @param tableName 表名
	 * @param columnName 列名
	 * @param type 类型
	 * @param length 长度
	 * @return
	 */
	Boolean updateColumn(String tableName,String columnName,String type,Integer length);
	/**
	 * 查询未被逻辑删除的数据
	 * @param prn 唯一标识
	 * @return
	 */
	Record selectByPrnAndSt(String prn);
	/**
	 * 查询未被逻辑删除的数据
	 * @param tableName 表名
	 * @param prn 唯一标识
	 * @return
	 */
	Record selectByPrnAndSt(String tableName,String prn);
	/**
	 * 分页查询未被逻辑删除的数据
	 * @param page 当前页数
	 * @param pageSize 每页显示条数
	 * @return
	 */
	Page<Record> getPageListBySt(Integer page, Integer pageSize);
	/**
	 * 分页查询未被逻辑删除的数据
	 * @param tableName 表名
	 * @param page 当前页数
	 * @param pageSize 每页显示条数
	 * @return
	 */
	Page<Record> getPageListBySt(String tableName,Integer page, Integer pageSize);
	/**
	 * 自定义列名自定义条件分页查询相关数据
	 * @param tableName 表名
	 * @param customSelectKey 自定义列名 ,号分割
	 * @param customCondition 自定义条件,号分割
	 * @param params 条件对应的值。（下标需要对应customCondition）
	 * @param page 当前页数
	 * @param pageSize 每页显示条数
	 * @param 判断方式 and or
	 * @return
	 */
	Page<Record> getPageListByCustom(String tableName,String customSelectKey,String customCondition,List<Object> params,Integer page,Integer pageSize,String judge);
	/**
	 * 自定义列名自定义条件分页查询相关数据
	 * @param customSelectKey 自定义列名 ,号分割
	 * @param customCondition 自定义条件,号分割
	 * @param params 条件对应的值。（下标需要对应customCondition）
	 * @param page 当前页数
	 * @param pageSize 每页显示条数
	 * @param 判断方式 and or
	 * @return
	 */
	Page<Record> getPageListByCustom(String customSelectKey,String customCondition,List<Object> params,Integer page,Integer pageSize,String judge);
	
	/**
	 * 根据Prn批量修改数据带版本号
	 * @param tableName 表名
	 * @param recordList 数据集合
	 * @param isUpAll 是否更新为空的字段
	 * @return
	 */
	Integer updateListByVersion(String tableName,List<Record> recordList,Boolean isUpAll);
}

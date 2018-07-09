package com.sbolo.syk.fetch.basemapper.provider;

import java.util.Set;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class BatchWriteProvider extends MapperTemplate {

	public BatchWriteProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}
	
	public String insertList(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, true, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnList) {
            if (!column.isId() && column.isInsertable()) {
                sql.append(column.getColumnHolder("record") + ",");
            }
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
	}
	
	public String updateListByPrn(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("<foreach  collection=\"list\" item=\"item\" index=\"index\" separator=\";\" >");
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append("<set>");
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnList) {
            if (!column.isId() && !column.getEntityField().getName().equals("prn") && column.isUpdatable()) {
            	String equalsHolder = SqlHelper.getIfNotNull("item", column, column.getColumnEqualsHolder("item") +",", true);
                sql.append(equalsHolder);
            }
        }
        sql.append("</set>");
        sql.append("<where>");
        sql.append("prn = #{item.prn}");
        sql.append("</where>");
        sql.append("</foreach>");
        return sql.toString();
	}

}

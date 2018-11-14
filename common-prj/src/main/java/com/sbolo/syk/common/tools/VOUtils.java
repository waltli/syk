package com.sbolo.syk.common.tools;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public class VOUtils {

	private static final Logger LOG = LoggerFactory.getLogger(VOUtils.class);

	public static <V> V po2vo(Object po, Class<V> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		if (ObjectUtils.isEmpty(po)) {
			return null;
		}
		V vo = clazz.newInstance();
		BeanUtils.copyProperties(vo, po);
		return vo;
	}

	public static <V> V po2voByType(Object po, Class<V> clazz) {
		V vo = null;
		try {
			if (ObjectUtils.isEmpty(po)) {
				return vo;
			}
			vo = clazz.newInstance();
			BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
			beanUtilsBean.getConvertUtils().register(new org.apache.commons.beanutils.converters.BigDecimalConverter(null), BigDecimal.class);
			beanUtilsBean.getConvertUtils().register(new org.apache.commons.beanutils.converters.DateConverter(null),java.util.Date.class);
			beanUtilsBean.getConvertUtils().register(new org.apache.commons.beanutils.converters.SqlTimestampConverter(null), java.sql.Timestamp.class);
			beanUtilsBean.getConvertUtils().register(new org.apache.commons.beanutils.converters.SqlDateConverter(null),java.sql.Date.class);
			beanUtilsBean.getConvertUtils().register(new org.apache.commons.beanutils.converters.SqlTimeConverter(null),java.sql.Time.class);
			beanUtilsBean.copyProperties(vo, po);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return vo;
	}

	public static <P, V> List<V> po2vo(List<P> content, Class<V> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		List<V> newContent = new ArrayList<V>();
		if (content != null && content.size() > 0) {
			for (P po : content) {
				V vo = po2vo(po, clazz);
				newContent.add(vo);
			}
		}
		return newContent;
	}

	// public static <P, V> Page<V> po2vo(final Page<P> poPage, final Class<V>
	// clazz) {
	// List<P> poList = poPage.getResult();
	// List<V> voList = po2vo(poList, clazz);
	// Page<V> voPage = new Page<>();
	// voPage.addAll(voList);
	// voPage.setCountColumn(poPage.getCountColumn());
	// voPage.setEndRow(poPage.getEndRow());
	// voPage.setOrderBy(poPage.getOrderBy());
	// voPage.setPageNum(poPage.getPageNum());
	// voPage.setPages(poPage.getPages());
	// voPage.setPageSize(poPage.getPageSize());
	// voPage.setPageSizeZero(poPage.getPageSizeZero());
	// voPage.setReasonable(poPage.getReasonable());
	// voPage.setStartRow(poPage.getStartRow());
	// voPage.setTotal(poPage.getTotal());
	// return voPage;
	// }

}

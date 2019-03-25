package com.sbolo.syk.common.ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.tools.ConfigUtils;

/**
 * 
 * @author lihaopeng
 * @date 2017年9月11日
 * @version 1.0.0
 * @description desc
 */
@SuppressWarnings({ "serial" })
public class ResultApi<T> implements Serializable {

	private T obj; // 第一条记录
	private List<T> list; // 记录列表
	private int listSize;
	private long allRow; // 总记录数
	private int totalPage; // 总页数
	private int currentPage; // 当前页
	private int pageSize; // 每页记录数

	private Boolean isFirstPage; // 是否为第一页
	private Boolean isLastPage; // 是否为最后一页
	private Boolean hasPreviousPage; // 是否有前一页
	private Boolean hasNextPage; // 是否有下一页

	private String order;
	private String sort;
	
	private int beginPage;
	private int pageLength;

	private Boolean status = true;
	private int code;
	private String[] error;
	private Throwable throwable;

	public ResultApi() {
		this.code = 200;
	}

	public ResultApi(T obj) {
		this.obj = obj;
		this.code = 200;
	}
	
	public ResultApi(List<T> list) {
		this.list = list;
		if (list != null && list.size() > 0) {
			this.listSize = list.size();
			this.obj = list.get(0);
		}
		this.code = 200;
	}

	public ResultApi(List<T> list, long allRow, int currentPage, int pageSize) {
		this(list);
		this.allRow = allRow;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.code = 200;
		init();
	}
	
	public ResultApi(List<T> list, long allRow, int currentPage, int pageSize, String order, String sort) {
		this(list, allRow, currentPage, pageSize);
		if(StringUtils.isNotBlank(order)){
			this.order = order;
		}
		if(StringUtils.isNotBlank(sort)){
			this.sort = sort;
		}
	}
	
	public static <T> ResultApi<T> error(String str) {
		String[] strs = new String[]{str};
		return error(strs);
	}
	
	public static <T> ResultApi<T> error(String[] strs) {
		ResultApi<T> requestResult = new ResultApi<T>();
		requestResult.setStatus(false);
		requestResult.setError(strs);
		return requestResult;
	}

	public static <T> ResultApi<T> error(Throwable t) {
		ResultApi<T> requestResult = new ResultApi<T>();
		requestResult.setStatus(false);
		requestResult.setThrowable(t);
		requestResult.setError(t.getMessage());
		return requestResult;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public long getAllRow() {
		return allRow;
	}

	public void setAllRow(long allRow) {
		this.allRow = allRow;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String[] getError() {
		return error;
	}

	public void setError(String[] error) {
		this.error = error;
	}
	
	public void setError(String error) {
		this.error = new String[]{error};
	}

	public T getObj() {
		return obj;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @param obj
	 *            the obj to set
	 */
	public void setObj(T obj) {
		this.obj = obj;
	}

	/**
	 * @return the isFirstPage
	 */
	public Boolean getIsFirstPage() {
		return isFirstPage;
	}

	/**
	 * @param isFirstPage
	 *            the isFirstPage to set
	 */
	public void setIsFirstPage(Boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	/**
	 * @return the isLastPage
	 */
	public Boolean getIsLastPage() {
		return isLastPage;
	}

	/**
	 * @param isLastPage
	 *            the isLastPage to set
	 */
	public void setIsLastPage(Boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	/**
	 * @return the hasPreviousPage
	 */
	public Boolean getHasPreviousPage() {
		return hasPreviousPage;
	}

	/**
	 * @param hasPreviousPage
	 *            the hasPreviousPage to set
	 */
	public void setHasPreviousPage(Boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	/**
	 * @return the hasNextPage
	 */
	public Boolean getHasNextPage() {
		return hasNextPage;
	}

	/**
	 * @param hasNextPage
	 *            the hasNextPage to set
	 */
	public void setHasNextPage(Boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public void init() {
		// 计算当前页
		countCurrentPage();
		// 计算总页数
		countTotalPage();
		this.isFirstPage = isFirstPage();
		this.isLastPage = isLastPage();
		this.hasPreviousPage = isHasPreviousPage();
		this.hasNextPage = isHasNextPage();
		// p:9 每次显示9 个页码
		// t:20 总数
		// c:2当前页
		// b:1 开始页

		// c:18 12,13,14,15,16,17,18,19,20
		// b:20-9

		// c:5
		// b:0

		// c:6
		// b:1

		// c:16
		// b: 11,12,13
		if (this.currentPage - 4 <= 0) {
			this.beginPage = 1;
		} else if (this.currentPage + 4 >= this.totalPage) {
			this.beginPage = this.totalPage - 8;// 17 20-8=12
												// 开始页12，那么12,13，14,15,16,17,18,19,20
			if (beginPage <= 0) {
				this.beginPage = 1;
			}
		} else {
			// 18-5 13 13,14,15,16,17,18
			this.beginPage = currentPage - 4;
		}
		this.pageLength = this.totalPage - this.beginPage; // 20-12=8
		// pagingResult.beginPage+pagingResult.pageLength end pageLength --->end
		if (this.pageLength >= 8) {
			this.pageLength = 8;
		} else if (this.pageLength < 0) {
			this.pageLength = 0;
		}
	}

	public boolean isFirstPage() {
		return currentPage == 1; // 如是当前页是第1页
	}

	public boolean isLastPage() {
		return currentPage == totalPage; // 如果当前页是最后一页
	}

	public boolean isHasPreviousPage() {
		return currentPage != 1; // 只要当前页不是第1页
	}

	public boolean isHasNextPage() {
		return currentPage != totalPage; // 只要当前页不是最后1页
	}

	public void countTotalPage() {
		totalPage = (int) (allRow % pageSize == 0 ? allRow / pageSize : allRow / pageSize + 1);
	}

	public static int countOffset(final int currentPage, final int pageSize) {
		final int offset = (pageSize * (currentPage - 1));
		return offset;
	}

	public void countCurrentPage() {
		currentPage = (currentPage == 0 ? 1 : currentPage);
	}

	public int getBeginPage() {
		return beginPage;
	}

	public void setBeginPage(int beginPage) {
		this.beginPage = beginPage;
	}

	public int getPageLength() {
		return pageLength;
	}

	public void setPageLength(int pageLength) {
		this.pageLength = pageLength;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	/**
	 * 生成分页查询基本参数maps
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public static Map<String, Object> getParamsMap(int currentPage, int pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startIndex", countOffset(currentPage, pageSize));
		params.put("limitNum", pageSize);
		return params;
	}

}

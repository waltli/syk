package com.sbolo.syk.common.tools;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import com.sbolo.syk.common.exception.BusinessException;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;

/**
 * 对easypoi 一些简单的封装操作类 
 * 官方文档：http://easypoi.mydoc.io/
 * @author zhz
 *
 */
public class ExcelUtil {
	/**
	 * 导出Excel(如果fileName为空则会生成一个流水号命名,文件格式为.xlsx)
	 * @param list 导出的集合数据
	 * @param title Excel标题名称
	 * @param sheetName excel页的名称
	 * @param pojoClass 对应的实体类Class
	 * @param fileName 文件名称
	 * @param response HTTP输出流
	 */
	public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,String fileName, HttpServletResponse response){
        if(response == null){
        	throw new BusinessException("输出流对象不能为空。");
        }
        if(pojoClass == null){
        	throw new BusinessException("导出Excel的实体对象类不能为空。");
        }
        if(list == null){
        	list = new ArrayList<>();
        }
		if(StringUtils.isBlank(fileName)){
        	fileName = UIDGenerator.getUID()+ ".xlsx";
        }else{
        	fileName+=".xlsx";
        }
		defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    }
	/**
	 * 生成Excel
	 * @param list 集合数据
	 * @param pojoClass 对应的实体类Class
	 * @param fileName 文件名称
	 * @param response HTTP输出流
	 * @param exportParams 导出配置的参数
	 */
	private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) {
		if(response == null){
        	throw new BusinessException("输出流对象不能为空。");
        }
        if(pojoClass == null){
        	throw new BusinessException("导出Excel的实体对象类不能为空。");
        }
		Workbook workbook = ExcelExportUtil.exportExcel(exportParams,pojoClass,list);
        if (workbook != null);
        downLoadExcel(fileName, response, workbook);
    }
	/**
	 * 通过Response流下载Excel文件
	 * @param fileName 文件名称
	 * @param response 请求流
	 * @param workbook Excel对象
	 */
	private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
		if(StringUtils.isBlank(fileName)){
			throw new BusinessException("文件名称不能为空。");
		}
		if(response == null){
        	throw new BusinessException("输出流对象不能为空。");
        }
        if(workbook == null){
        	throw new BusinessException("Excel对象不能为空。");
        }
		try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
        	e.printStackTrace();
            throw new BusinessException("导出EXCEL失败。");
        }
    }
}

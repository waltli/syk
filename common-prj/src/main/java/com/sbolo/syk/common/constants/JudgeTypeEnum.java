package com.sbolo.syk.common.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sbolo.syk.common.tools.StringUtil;

public enum JudgeTypeEnum {

	DENGYU(1, "="),
	BUDENGYU(2, "!="),
	DAYU(3, ">"),
	DAYUDENGYU(4, ">="),
	XIAOYU(5,"<"),
	XIAOYUDENGYU(6, "<="),
	BAOHAN(7, "包含"),
	JIEYU(8, "介于"),
	BUBAOHAN(9, "不包含"),
	CONGSHUYU(10,"从属于");
	
	private Integer code;
	private String desc;

	JudgeTypeEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static Integer getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		JudgeTypeEnum[] values = JudgeTypeEnum.values();
		for(JudgeTypeEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static Integer getCodeByDesc(String desc){
		JudgeTypeEnum[] values = JudgeTypeEnum.values();
		for(JudgeTypeEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(Integer code){
		JudgeTypeEnum[] values = JudgeTypeEnum.values();
		for(JudgeTypeEnum value : values){
			if(value.getCode().equals(code)){
				return value.getDesc();
			}
		}
		return null;
	}

	/** 
     * 转换为List集合 
     *  
     * @returnMap<String, String> 
     */  
    public static List<Map<String,Object>> toList() {  
    	
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
      
        JudgeTypeEnum[] enums = JudgeTypeEnum.values();  
        for (int i = 0; i < enums.length; i++) {  
        	Map<String, Object> map = new HashMap<String, Object>();  
        	map.put("code", enums[i].getCode());
        	map.put("desc", enums[i].getDesc()); 
            list.add(map);
        }  
        return list;  
    }  
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	
	
}

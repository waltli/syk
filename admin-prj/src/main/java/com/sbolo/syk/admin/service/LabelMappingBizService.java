package com.sbolo.syk.admin.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sbolo.syk.admin.dao.LabelMappingEntityMapper;
import com.sbolo.syk.admin.po.LabelMappingEntity;

@Service
public class LabelMappingBizService {
	private LabelMappingEntityMapper labelMappingEntityMapper;
	
	public void batchAdd(List<LabelMappingEntity> labels){
		labelMappingEntityMapper.batchInsert(labels);
	}
	
	public List<String> getLabels(){
		List<String> labels = labelMappingEntityMapper.selectLabelsGroupLabel();
		Collections.sort(labels, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				int o1Len = o1.length();
				int o2Len = o2.length();
				if(o1Len > o2Len){
	                return 1;
	            }else if (o1Len < o2Len){
	                return -1;
	            }else {
	                return 0;
	            }
			}
		});
		return labels;
	}
}

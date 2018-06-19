package com.sbolo.syk.fetch.vo;

import java.util.List;

import com.sbolo.syk.fetch.po.LabelMappingEntity;
import com.sbolo.syk.fetch.po.LocationMappingEntity;
import com.sbolo.syk.fetch.po.MovieInfoEntity;
import com.sbolo.syk.fetch.po.ResourceInfoEntity;

public class ConcludeVO {
	private MovieInfoEntity fetchMovie;
	private List<ResourceInfoEntity> resources;
	private List<LabelMappingEntity> labels;
	private List<LocationMappingEntity> locations;
	
	
	public ConcludeVO(){}
	
	public ConcludeVO(MovieInfoEntity fetchMovie,
			List<ResourceInfoEntity> resources, 
			List<LabelMappingEntity> labels,
			List<LocationMappingEntity> locations) {
		this.fetchMovie = fetchMovie;
		this.resources = resources;
		this.labels = labels;
		this.locations = locations;
	}
	public MovieInfoEntity getFetchMovie() {
		return fetchMovie;
	}

	public void setFetchMovie(MovieInfoEntity fetchMovie) {
		this.fetchMovie = fetchMovie;
	}
	public List<ResourceInfoEntity> getResources() {
		return resources;
	}
	public void setResources(List<ResourceInfoEntity> resources) {
		this.resources = resources;
	}
	public List<LabelMappingEntity> getLabels() {
		return labels;
	}
	public void setLabels(List<LabelMappingEntity> labels) {
		this.labels = labels;
	}
	public List<LocationMappingEntity> getLocations() {
		return locations;
	}
	public void setLocations(List<LocationMappingEntity> locations) {
		this.locations = locations;
	}
	
}

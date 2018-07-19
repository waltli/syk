package com.sbolo.syk.fetch.vo;

import java.util.List;

import com.sbolo.syk.fetch.entity.MovieFileIndexEntity;

public class ConcludeVO {
	private MovieInfoVO fetchMovie;
	private List<ResourceInfoVO> fetchResources;
	private List<MovieFileIndexEntity> fileIdxList;
	
	public ConcludeVO(){}
	
	public ConcludeVO(MovieInfoVO fetchMovie,
			List<ResourceInfoVO> fetchResources,
			List<MovieFileIndexEntity> fileIdxList) {
		this.fetchMovie = fetchMovie;
		this.fetchResources = fetchResources;
		this.fileIdxList = fileIdxList;
	}

	public List<MovieFileIndexEntity> getFileIdxList() {
		return fileIdxList;
	}

	public void setFileIdxList(List<MovieFileIndexEntity> fileIdxList) {
		this.fileIdxList = fileIdxList;
	}

	public MovieInfoVO getFetchMovie() {
		return fetchMovie;
	}

	public void setFetchMovie(MovieInfoVO fetchMovie) {
		this.fetchMovie = fetchMovie;
	}

	public List<ResourceInfoVO> getFetchResources() {
		return fetchResources;
	}

	public void setFetchResources(List<ResourceInfoVO> fetchResources) {
		this.fetchResources = fetchResources;
	}

	
	
}

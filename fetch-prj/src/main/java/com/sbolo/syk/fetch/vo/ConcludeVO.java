package com.sbolo.syk.fetch.vo;

import java.util.List;

import com.sbolo.syk.fetch.entity.MovieFileIndexEntity;

public class ConcludeVO {
	private MovieInfoVO fetchMovie;
	private List<ResourceInfoVO> fetchResources;
	
	public ConcludeVO(){}
	
	public ConcludeVO(MovieInfoVO fetchMovie,
			List<ResourceInfoVO> fetchResources) {
		this.fetchMovie = fetchMovie;
		this.fetchResources = fetchResources;
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

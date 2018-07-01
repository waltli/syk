package com.sbolo.syk.fetch.vo;

import java.util.List;

public class ConcludeVO {
	private MovieInfoVO fetchMovie;
	private List<ResourceInfoVO> resources;
	
	public ConcludeVO(){}
	
	public ConcludeVO(MovieInfoVO fetchMovie,
			List<ResourceInfoVO> resources) {
		this.fetchMovie = fetchMovie;
		this.resources = resources;
	}

	public MovieInfoVO getFetchMovie() {
		return fetchMovie;
	}

	public void setFetchMovie(MovieInfoVO fetchMovie) {
		this.fetchMovie = fetchMovie;
	}

	public List<ResourceInfoVO> getResources() {
		return resources;
	}

	public void setResources(List<ResourceInfoVO> resources) {
		this.resources = resources;
	}
	
	
}

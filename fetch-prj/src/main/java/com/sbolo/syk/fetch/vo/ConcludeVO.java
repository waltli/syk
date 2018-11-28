package com.sbolo.syk.fetch.vo;

import java.util.List;

public class ConcludeVO {
	private MovieInfoVO fetchMovie;
	private List<ResourceInfoVO> fetchResources;
	private List<MovieDictVO> movieDicts;
	
	public ConcludeVO(){}
	
	public ConcludeVO(MovieInfoVO fetchMovie,
			List<ResourceInfoVO> fetchResources, 
			List<MovieDictVO> movieDicts) {
		this.fetchMovie = fetchMovie;
		this.fetchResources = fetchResources;
		this.movieDicts = movieDicts;
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

	public List<MovieDictVO> getMovieDicts() {
		return movieDicts;
	}

	public void setMovieDicts(List<MovieDictVO> movieDicts) {
		this.movieDicts = movieDicts;
	}
	
}

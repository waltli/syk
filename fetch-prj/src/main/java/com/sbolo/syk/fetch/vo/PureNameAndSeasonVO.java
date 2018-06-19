package com.sbolo.syk.fetch.vo;

public class PureNameAndSeasonVO {
	private String pureName;
	private Integer season;
	private String cnSeason;
	
	public PureNameAndSeasonVO(String pureName, Integer season, String cnSeason) {
		this.pureName = pureName;
		this.season = season;
		this.cnSeason = cnSeason;
	}

	public String getCnSeason() {
		return cnSeason;
	}

	public void setCnSeason(String cnSeason) {
		this.cnSeason = cnSeason;
	}

	public String getPureName() {
		return pureName;
	}
	public void setPureName(String pureName) {
		this.pureName = pureName;
	}
	public Integer getSeason() {
		return season;
	}
	public void setSeason(Integer season) {
		this.season = season;
	}
}

package com.chinatelecom.xjdh.bean;

import java.io.Serializable;
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class StationImg implements Serializable {
	@org.codehaus.jackson.annotate.JsonProperty("id")
	private java.lang.Integer id;
	@org.codehaus.jackson.annotate.JsonProperty("stationImage")
	private String stationImage;
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public String getStationImage() {
		return stationImage;
	}
	public void setStationImage(String stationImage) {
		this.stationImage = stationImage;
	}
	
}

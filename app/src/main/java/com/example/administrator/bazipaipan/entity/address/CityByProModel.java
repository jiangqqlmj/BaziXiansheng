package com.example.administrator.bazipaipan.entity.address;

import java.io.Serializable;

public class CityByProModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3536587546119930224L;
	private String proID;
	private String cityName;
	private String citySort;

	public CityByProModel() {
		// TODO Auto-generated constructor stub
	}

	public CityByProModel(String proID, String cityName, String citySort) {
		super();
		this.proID = proID;
		this.cityName = cityName;
		this.citySort = citySort;
	}

	public String getProID() {
		return proID;
	}

	public void setProID(String proID) {
		this.proID = proID;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCitySort() {
		return citySort;
	}

	public void setCitySort(String citySort) {
		this.citySort = citySort;
	}

}

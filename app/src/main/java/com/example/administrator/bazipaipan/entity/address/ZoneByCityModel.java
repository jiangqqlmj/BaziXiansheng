package com.example.administrator.bazipaipan.entity.address;

import java.io.Serializable;

public class ZoneByCityModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1264944824099620075L;
	private String ZoneID;
	private String ZoneName;
	private String CityID;

	public ZoneByCityModel() {
		// TODO Auto-generated constructor stub
	}

	public ZoneByCityModel(String zoneID, String zoneName, String cityID) {
		super();
		ZoneID = zoneID;
		ZoneName = zoneName;
		CityID = cityID;
	}

	public String getZoneID() {
		return ZoneID;
	}

	public void setZoneID(String zoneID) {
		ZoneID = zoneID;
	}

	public String getZoneName() {
		return ZoneName;
	}

	public void setZoneName(String zoneName) {
		ZoneName = zoneName;
	}

	public String getCityID() {
		return CityID;
	}

	public void setCityID(String cityID) {
		CityID = cityID;
	}

}

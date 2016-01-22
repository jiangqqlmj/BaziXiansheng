package com.example.administrator.bazipaipan.entity.address;

import java.io.Serializable;

public class ProvinceModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7539748840730110553L;
	private String proSort;
	private String proName;

	public ProvinceModel() {
		// TODO Auto-generated constructor stub
	}

	public ProvinceModel(String proName, String proSort) {
		this.proName = proName;
		this.proSort = proSort;
	}

	public String getProSort() {
		return proSort;
	}

	public void setProSort(String proSort) {
		this.proSort = proSort;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

}

package com.example.administrator.bazipaipan.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.example.administrator.bazipaipan.entity.address.CityByProModel;
import com.example.administrator.bazipaipan.entity.address.ProvinceModel;
import com.example.administrator.bazipaipan.entity.address.ZoneByCityModel;

public class AddressDAO {
	 //private SDDBOpenHelper dbOpenHelper;
	 DataBaseContext context;
	private DBOpenHelper dbOpenHelper;

	public AddressDAO(Context base) {
		context = new DataBaseContext(base);
		//dbOpenHelper = new SDDBOpenHelper(context);
		dbOpenHelper = new DBOpenHelper(base);
	}

	/**
	 * 获取所有省份
	 * 
	 * @return
	 */
	public List<Object> getAllProvince() {
		List<Object> proList = null;
		Cursor cursor = context.openOrCreateDatabase().query("T_Province",
				new String[] { "ProName", "ProSort" }, null, null, null, null,
				"ProSort asc");
		if (cursor != null) {
			proList = new ArrayList<Object>();
			while (cursor.moveToNext()) {
				String proName = cursor.getString(cursor
						.getColumnIndex("ProName"));
				String proSort = cursor.getString(cursor
						.getColumnIndex("ProSort"));
				ProvinceModel model = new ProvinceModel(proName, proSort);
				proList.add(model);
			}
			cursor.close();
		}
		return proList;
	}

	/**
	 * 根据省份查询市
	 * 
	 * @param proSort
	 * @return
	 */
	public List<Object> getAllCityByProvince(String proSort) {
		List<Object> cityList = null;
		Cursor cursor =  context.openOrCreateDatabase().query("T_City",
				new String[] { "CityName", "ProID", "CitySort" }, "ProID=?",
				new String[] { proSort }, null, null, "CitySort asc");
		if (cursor != null) {
			cityList = new ArrayList<Object>();
			while (cursor.moveToNext()) {
				String cityName = cursor.getString(cursor
						.getColumnIndex("CityName"));
				String proID = cursor.getString(cursor.getColumnIndex("ProID"));
				String citySort = cursor.getString(cursor
						.getColumnIndex("CitySort"));
				CityByProModel model = new CityByProModel(proID, cityName,
						citySort);
				cityList.add(model);
			}
			cursor.close();
		}
		return cityList;
	}

	/**
	 * 根据市查询区
	 * 
	 * @param citySort
	 * @return
	 */
	public List<Object> getAllDistrictByCity(String citySort) {
		List<Object> cityList = null;
		Cursor cursor =  context.openOrCreateDatabase().query("T_Zone",
				new String[] { "ZoneName", "ZoneID", "CityID" }, "CityID=?",
				new String[] { citySort }, null, null, "ZoneID asc");
		if (cursor != null) {
			cityList = new ArrayList<Object>();
			while (cursor.moveToNext()) {
				String zoneName = cursor.getString(cursor
						.getColumnIndex("ZoneName"));
				String zoneID = cursor.getString(cursor
						.getColumnIndex("ZoneID"));
				String cityID = cursor.getString(cursor
						.getColumnIndex("CityID"));
				ZoneByCityModel model = new ZoneByCityModel(zoneID, zoneName,
						cityID);
				cityList.add(model);
			}
			cursor.close();
		}
		return cityList;
	}

	/**
	 * 根据区ID 查询区
	 * 
	 * @param zoneID
	 * @return
	 */
	public ZoneByCityModel getDistrictByID(String zoneID) {
		ZoneByCityModel zoneModel = null;
		Cursor cursor =  context.openOrCreateDatabase().query("T_Zone",
				new String[] { "ZoneName", "CityID" }, "ZoneID=?",
				new String[] { zoneID }, null, null, null);
		if (cursor != null) {
			zoneModel = new ZoneByCityModel();
			if (cursor.moveToNext()) {
				zoneModel.setZoneID(zoneID);
				zoneModel.setCityID(cursor.getString(cursor
						.getColumnIndex("CityID")));
				zoneModel.setZoneName(cursor.getString(cursor
						.getColumnIndex("ZoneName")));
			}
		}
		return zoneModel;
	}

	/**
	 * 根据cityID查询城市
	 * 
	 * @param cityID
	 * @return
	 */
	public CityByProModel getCityByCityID(String cityID) {
		CityByProModel cityModel = null;
		Cursor cursor =  context.openOrCreateDatabase().query("T_City",
				new String[] { "CityName", "ProID" }, "CitySort=?",
				new String[] { cityID }, null, null, null);
		if (cursor != null) {
			cityModel = new CityByProModel();
			if (cursor.moveToNext()) {
				cityModel.setCitySort(cityID);
				cityModel.setProID(cursor.getString(cursor
						.getColumnIndex("ProID")));
				cityModel.setCityName(cursor.getString(cursor
						.getColumnIndex("CityName")));
			}
		}
		return cityModel;
	}
	/**
	 * 根据省份ID查询省份
	 * @param proID
	 * @return
	 */
	public ProvinceModel getProByProID(String proID) {
		ProvinceModel proModel = null;
		Cursor cursor = context.openOrCreateDatabase().query("T_Province",
				new String[] { "ProName" }, "ProSort=?",
				new String[] { proID }, null, null, null);
		if (cursor != null) {
			proModel = new ProvinceModel();
			if (cursor.moveToNext()) {
				proModel.setProSort(proID);
				proModel.setProName(cursor.getString(cursor
						.getColumnIndex("ProName")));
			}
		}
		return proModel;
	}
}

package com.example.administrator.bazipaipan.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import android.content.Context;

import com.example.administrator.bazipaipan.common.CacheConfig;

public class DBFileUtil {
	public static boolean writeSD(Context context) {
		InputStream is = null;
		FileOutputStream fos = null;
		String fileName = "china_province_city_zone.db";
		try {
			if (MemoryStatus.externalMemoryAvailable()) {
				is = context.getAssets().open(fileName);
				File dir = new File(CacheConfig.DB_PATH);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File dbFile = new File(CacheConfig.DB_PATH, fileName);
				if (dbFile.exists()) {
					dbFile.delete();
				}
				fos = new FileOutputStream(dbFile);
				int length = 0;
				byte[] data = new byte[1024];
				while ((length = is.read(data)) != -1) {
					fos.write(data, 0, length);
				}
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}

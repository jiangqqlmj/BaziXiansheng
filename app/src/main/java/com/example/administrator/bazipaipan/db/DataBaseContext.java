package com.example.administrator.bazipaipan.db;

import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.example.administrator.bazipaipan.common.CacheConfig;
import com.example.administrator.bazipaipan.utils.MemoryStatus;

public class DataBaseContext extends ContextWrapper {

	public DataBaseContext(Context base) {
		super(base);
	}

	@Override
	public File getDatabasePath(String name) {
		File dbFile = null;
		if (MemoryStatus.externalMemoryAvailable()) {
			File dir = new File(CacheConfig.DB_PATH);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			dbFile = new File(dir, name);
			if (!dbFile.exists()) {
				
			}
		}
		return dbFile;
	}

	public SQLiteDatabase openOrCreateDatabase() {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
				getDatabasePath(DBConfig.DB_NAME), null);
		return db;
	}


}

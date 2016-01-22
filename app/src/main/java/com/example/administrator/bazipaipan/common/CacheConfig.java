package com.example.administrator.bazipaipan.common;

import android.os.Environment;

/**
 * 文件等一些常量配置
 * 
 * @author jiangqq
 * 
 */
public class CacheConfig {
	// 数据库缓存配置
	public static final String DB_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/.bzxs/ztt_bzxs/db_cache/";

}

package com.example.administrator.bazipaipan.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

/**
 * 这个类进行计算储存空间
 * 
 * @author jiangqq
 * 
 */
public class MemoryStatus {
	private static final int ERROR = -1;

	// 判断SD卡是否存在
	static public boolean externalMemoryAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	// 获取内部存储器有用空间大小
	static public long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	// 获取内部存储器空间总大小
	static public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	// 获取SD卡有用空间大小，错误返回-1
	static public long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} else {
			return ERROR;
		}
	}

	// 获取SD卡总空间大小，错误返回-1
	static public long getTotalExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;
		} else {
			return ERROR;
		}
	}

	/**
	 * 根据给定的文件的路径来计算文件夹的大小
	 * 
	 * @param dir
	 *            文件的路径
	 * @return 文件夹的大小
	 */
	static public long getFileSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize=0;
		File[] files=dir.listFiles();
		for (File file : files) {
			if(file.isFile())
			{
				dirSize+=file.length();
			}else if (file.isDirectory()) {
				dirSize+=getFileSize(file); //如果是目录,那就进行递归 来计算文件的大小
			}
		}
		return dirSize;
	}

	// 把文件大小转化字符串
	static public String formatSize(long size) {
		Log.d("zttjiangqq", "文件的大小为:"+size);
		String suffix = null;

		if(size==0)
		{
			return "";
		}
		
		if (size >= 1024) {
			suffix = "KB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MB";
				size /= 1024;
				if (size >= 1024) {
					suffix = "G";
					size /= 1024;
				}
			}
		}
		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}
		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}
}

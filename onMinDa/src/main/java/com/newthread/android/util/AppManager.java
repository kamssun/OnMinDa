package com.newthread.android.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppManager {
	// 获取当前应用版本号
	public static String getVersionName(Context con) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = con.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				con.getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}
}

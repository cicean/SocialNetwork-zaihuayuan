package com.gzqining.inthegarden.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Tools {
//	public static String getImei(Context context) {
//		TelephonyManager tm = (TelephonyManager) context
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		String imei = tm.getDeviceId();
//		return imei;
//	}

	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
	public static String getUUID(Context context){
		return Md5Util.md5(getLocalMacAddress(context));
	}
	
}

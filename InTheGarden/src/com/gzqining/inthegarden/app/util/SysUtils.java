package com.gzqining.inthegarden.app.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class SysUtils {
	
	private static Map<String,SoftReference<Object>> PARAMS; 
	static{
		PARAMS = new HashMap<String, SoftReference<Object>>();
	}
	
	private static void putValue(String key, Object obj){
		PARAMS.put(key, new SoftReference<Object>(obj));
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T getValue(String key){
		if(PARAMS.containsKey(key)){
			SoftReference<Object> refTypeface= PARAMS.get(key);
			if(refTypeface != null)
				return (T) refTypeface.get();
		}
		return null;
	}

	public static String getImei(Context context) {
		String imei = getValue("sys::imei");
		if(TextUtils.isEmpty(imei)){
			TelephonyManager t = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
			imei = t.getDeviceId();
			putValue("sys::imei", imei); 
		}
		return imei;
	}
	
	public static String getDeviceId(Context context){
		String androidId = getValue("sys::androidId");
		if(TextUtils.isEmpty(androidId)){
			androidId = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			putValue("sys::androidId", androidId);
		}
		return androidId;
	}
	
	/** 获取友盟渠道名称 */
	public static String getChannelName(Context context) {
		Object obj = getMetaData(context, "UMENG_CHANNEL");
		return obj != null ? obj.toString() : "";
	}
	
	public static Object getMetaData(Context context, String name) {
		try {
			Object value = getValue("metaData::"+name);
			if(value == null){
				ApplicationInfo appi = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
				Bundle bundle = appi.metaData;
				value = bundle.get(name);
				putValue("metaData::"+name, value);
			}
			return value;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @return 程序版本
	 */
	public static String getVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "1.0";
			}
		} catch (Exception e) {
			return "1.0";
		}
		return versionName;
	}

	/**
	 * @return 程序版本号
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 1;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = pi.versionCode;
		} catch (Exception e) {
		}
		return versionCode;
	}
	
}

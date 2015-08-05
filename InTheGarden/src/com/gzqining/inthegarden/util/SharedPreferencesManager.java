package com.gzqining.inthegarden.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
	private SharedPreferences settings;
	Context cont;
	private final static String PREFS_NAME = "qnxx";
	private final static String JSESSIONID = "JSESSIONID";
	public SharedPreferencesManager(Context cont){
		this.cont=cont;
		settings = cont.getSharedPreferences(PREFS_NAME, 0);
	}
	public void setSessionid(String id){
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(JSESSIONID, id);
		editor.commit();
	}
	public String getSessionid(){
		String ret=settings.getString(JSESSIONID, "");
		return ret;
	}

}

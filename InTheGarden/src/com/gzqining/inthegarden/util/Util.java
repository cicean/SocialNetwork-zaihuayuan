package com.gzqining.inthegarden.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class Util {
	public static int getWindowWidth(Context context) {
		DisplayMetrics dm = new android.util.DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getWindowHeight(Context context) {
		DisplayMetrics dm = new android.util.DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	public static boolean isSuccess(String json) {
		JSONObject obj;
		try {
			obj = new JSONObject(json);
			String code = obj.getString("code");
			if (code != null && code.equals("00000")) {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ProgressDialog showLoading(Context context) {
		return showLoading(context, "数据加载中...");
	}

	public static ProgressDialog showLoading(Context context, String msg) {
		final ProgressDialog ringProgressDialog = ProgressDialog.show(context, null, msg, true);
		ringProgressDialog.setCancelable(false);
		ringProgressDialog.show();
		return ringProgressDialog;
	}

	public static void showToast(Context context, String msg, int duration) {
		Toast.makeText(context, msg, duration).show();
	}

	public static void showToast(Context context, String msg) {
		showToast(context, msg, Toast.LENGTH_SHORT);
	}

	public static void showToastNetWorkError(Context context) {
		showToast(context, "网络异常", Toast.LENGTH_SHORT);
	}

	public static String getOauthToken(Context context) {
		SharedPreferences sp = context.getSharedPreferences("idcard", 0);
		return sp.getString("oauth_token", null);
	}

	public static String getOauthTokenSecret(Context context) {
		SharedPreferences sp = context.getSharedPreferences("idcard", 0);
		return sp.getString("oauth_token_secret", null);
	}

	public static String getUserId(Context context) {
		SharedPreferences sp = context.getSharedPreferences("idcard", 0);
		return sp.getString("uid", "0");
	}
}

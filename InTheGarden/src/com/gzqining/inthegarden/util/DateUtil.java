package com.gzqining.inthegarden.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;

public class DateUtil {
	public static String getDate2() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
		Date d=new Date();
		d.setDate(d.getDate()+1);
		return sdf.format(d);

	}
	public static String getDate3() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
		Date d=new Date();
		d.setDate(d.getDate()+2);
		return sdf.format(d);

	}
	public static String getDate4() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
		Date d=new Date();
		d.setDate(d.getDate()+3);
		return sdf.format(d);

	}
	public static String getDate5() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
		Date d=new Date();
		d.setDate(d.getDate()+4);
		return sdf.format(d);

	}
	public static String getDate6() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date d=new Date();
		d.setDate(d.getDate()+5);
		return sdf.format(d);

	}
	public static String getNow() {
		Date date = new Date();
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String timeStr = sdf.format(date);
		return timeStr;
	}
	public static String getNow(long t) {
		Date date = new Date(t);
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String timeStr = sdf.format(date);
		return timeStr;
	}
	//获取系统时间
	public static String getCurrentDate(){
		Date d = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(d);
	}
	//时间戳转字符窜
	public static String getDateToString(long time,String format_string){
		Date d = new Date(time);
		SimpleDateFormat sf = new SimpleDateFormat(format_string);
		return sf.format(d);
	}
	//字符串转时间戳
	public static long getStringToDate(String time){
		Date d = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			d = sf.parse(time);
		}catch(Exception e){
			e.printStackTrace();
		}
		return d.getTime();
	}

	/**
	 * 获取屏幕的比例
	 * 
	 * @return
	 */
	public static float getScaledDensity(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		float value = dm.scaledDensity;
		return value;
	}

	/**
	 * 将dip转换为px
	 * 
	 * @return
	 */
	public static int dip2Px(Context context, float dip) {
		return (int) (context.getResources().getDisplayMetrics().density * dip);
	}

	/**
	 * 将px转换为dip
	 * 
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * base64编码后调用
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeReplace(String str) {
		str = str.replace("+", "_").replace("=", "%").replace("/", "*");
		return str;

	}

	/**
	 * base64解码前调用
	 * 
	 * @param str
	 * @return
	 */
	public static String decodeReplace(String str) {
		str = str.replace("_", "+").replace("%", "=").replace("*", "/");
		return str;

	}

	/**
	 * 适用于Adapter中简化ViewHolder相关代码
	 * 
	 * @param convertView
	 * @param id
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T obtainView(View convertView, int id) {
		SparseArray<View> holder = (SparseArray<View>) convertView.getTag();
		if (holder == null) {
			holder = new SparseArray<View>();
			convertView.setTag(holder);
		}
		View childView = holder.get(id);
		if (childView == null) {
			childView = convertView.findViewById(id);
			holder.put(id, childView);
		}
		return (T) childView;
	}

	/**
	 * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
	 * 
	 * @param view
	 * @return
	 */
	public static int getViewMeasuredHeight(View view) {
		calcViewMeasure(view);
		return view.getMeasuredHeight();
	}

	/**
	 * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
	 * 
	 * @param view
	 * @return
	 */
	public static int getViewMeasuredWidth(View view) {
		calcViewMeasure(view);
		return view.getMeasuredWidth();
	}

	/**
	 * 测量控件的尺寸
	 * 
	 * @param view
	 */
	public static void calcViewMeasure(View view) {
		int width = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int expandSpec = View.MeasureSpec.makeMeasureSpec(
				Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
		view.measure(width, expandSpec);
	}
	
	public static String splitDateString(String date){
		//1942年
		return date.split(" ")[0];
	}
	
	/**
	 * 获取屏幕宽度(像素)
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return windowManager.getDefaultDisplay().getWidth();
	}
	/**
	 * 获取屏幕高度(像素)
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return windowManager.getDefaultDisplay().getHeight();
	}
}

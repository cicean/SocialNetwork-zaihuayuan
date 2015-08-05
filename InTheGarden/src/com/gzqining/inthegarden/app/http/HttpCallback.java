package com.gzqining.inthegarden.app.http;

/**
 * 方法回调
 * @author xjm
 *
 * @param <T>
 */
public abstract class HttpCallback {
	
	public void onRun(Object obj){ }

	public abstract void onCallback(Object obj);
	
}

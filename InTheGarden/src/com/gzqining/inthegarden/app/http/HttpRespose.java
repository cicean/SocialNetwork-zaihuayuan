package com.gzqining.inthegarden.app.http;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class HttpRespose {
	
	private static final Handler handler = new Handler(Looper.getMainLooper());
	
	private String url;

	private AjaxParams params;
	
	public HttpRespose(String url, AjaxParams params){
		this.url = url;
		this.params = params;
	}

	private <T> void callback(final HttpCallback callback, final Object obj){
		if(callback != null){
			callback.onRun(obj);
			handler.post(new Runnable() {
				@Override
				public void run() {
					Log.i("HttpRespose", "obj = "+obj);
					callback.onCallback(obj);
				}
			});
		}
	}
	

	
	public <T> void request(final HttpCallback callback){
		new Thread(new Runnable() {
			@Override
			public void run() {
				FinalHttp http = new FinalHttp();
				Object obj = http.postSync(url, params);
				callback(callback, obj);
			}
		}).start();
	}

}

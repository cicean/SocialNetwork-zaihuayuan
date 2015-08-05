package com.gzqining.inthegarden.util;

import java.io.UnsupportedEncodingException;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.entity.StringEntity;
import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;

import android.app.Dialog;


public class TaskUtil extends AjaxCallBack{
	private BaseActivity context;
	String tag=TaskUtil.class.getName();
	
	private int type=0;
	FinalHttp fh = null;
	Dialog dialog;
	String JSESSIONID=null;
	SharedPreferencesManager spm;
	
	public TaskUtil(BaseActivity context)
	{
		this.context=context;
		fh = new FinalHttp();
		fh.configTimeout(90*60*1000);
	}	
	
	public void post(AjaxParams params,String url){
		//Header[] headers={new BasicHeader("Cookie","JSESSIONID="+spm.getSessionid())};		
		
		//fh.post(url,headers, params,"text/html", this);
		fh.post(url,params,this);
	}
	
	public void postsync(String params,String url){
		try {
			fh.postSync(url, new StringEntity(params, "utf-8"),
					"text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void get(String url){
		Logger.d(tag, "url:"+url);
		fh.get(url, this);
		//fh.get(url,headers,null, this);
		
	}
	
	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
//		if(dialog!=null){
//			dialog.show();
//		}
		
	}

	public void onFailure(Throwable t, String strMsg) {
		// TODO Auto-generated method stub
//		if(dialog!=null){
//			dialog.cancel();
//		}
		
		DialogUtil.toast(context, context.getString(R.string.request_data_error)+strMsg);
		Logger.e(tag, "onFailure");
	}

	@Override
	public void onSuccess(Object t) {
		// TODO Auto-generated method stub
//		if(dialog!=null){
//			dialog.cancel();
//		}
		Logger.d(tag, "context:"+context);
		Logger.d(tag, "t="+t);
		if(type==0)
		{
			context.taskCallBack((String)t);
		}
		else
		{
			context.taskCallBack((String)t, type);
			
		}
		
	}

}

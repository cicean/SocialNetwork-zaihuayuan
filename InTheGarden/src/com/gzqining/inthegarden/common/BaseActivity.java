package com.gzqining.inthegarden.common;


import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.util.DialogUtil;
import com.gzqining.inthegarden.util.TaskUtil;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import net.tsz.afinal.http.AjaxParams;

public class BaseActivity extends FragmentActivity {
	String ret="";
	public TaskUtil task=null;
	public AjaxParams params=new AjaxParams();
	Dialog dialog;
	Handler hand=null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		task=new TaskUtil(this);
		
	}
	public void showDialog(){
		dialog=DialogUtil.showProgressDialog(this, getString(R.string.request_data), false);
		dialog.show();
	}
	public void showDialogloading(String text){
		dialog=DialogUtil.showProgressDialog(this, text, true);
		dialog.show();
	}
	public void canelDialog(){
		if(dialog!=null){
			//dialog.cancel();
			dialog.dismiss();
		}
		
	}
	
	
	public void taskCallBack(String jsonObject,int type) 
	{
		
	}
	public void taskCallBack(String t) {
		// TODO Auto-generated method stub
		
	}
	public void taskLogin(String t) 
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}

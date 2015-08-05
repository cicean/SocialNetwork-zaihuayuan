package com.gzqining.inthegarden.act;

import com.gzqining.inthegarden.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.tsz.afinal.http.AjaxParams;

import com.gzqining.inthegarden.adapter.MyFansListAdapter;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.TaskUtil;
import com.gzqining.inthegarden.vo.FansAttentionBack;
import com.gzqining.inthegarden.waterflow.XListView;
import com.gzqining.inthegarden.waterflow.XListView.IXListViewListener;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
 

public class MyFansActivity extends BaseActivity implements IXListViewListener{
	String tag = LoginActivity.class.getName();
	XListView mAdapterView = null;
	MyFansListAdapter adapter = null;
	private ImageButton back_btn;
	int page;
	Object obj;
	private ImageView no_fans;
	@Override
	public void onCreate( Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myfans);
		
		mAdapterView = (XListView) findViewById(R.id.fans_list);
		back_btn = (ImageButton) findViewById(R.id.top_layout_myfans_back_btn);
		no_fans = (ImageView) findViewById(R.id.image_no_fans);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapterView.setPullLoadEnable(true);
		fans();
//		no_fans.setVisibility(View.VISIBLE);
//		mAdapterView.setVisibility(View.GONE);
//		adapter = new MyFansListAdapter(MyFansActivity.this);
		//mAdapterView.setAdapter(adapter);
		
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());		
		mAdapterView.setRefreshTime(sdf.format(new Date()));
		mAdapterView.stopRefresh();
		fans();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());		
		mAdapterView.setLoadTime(sdf.format(new Date()));
		mAdapterView.stopLoadMore();
		fans();
	}
	/**
	 * 接口
	 * */
	public void fans() {
		this.showDialog();
		SharedPreferences sp = this.getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		Logger.d(tag, "ready");
		this.task = new TaskUtil(this);
		String url = Constans.fans;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("page",""+page);
		this.task.post(params, url);
		//this.task.postsync(encodesstr, url);
		
		this.task.setType(Constans.REQ_Fans);
	}
	
	/**
	 * 接口返回
	 * */
	@Override
	public void taskCallBack(String jsonObject, int type) {
		Logger.d(tag, "type:" + type + ",jsonObject:" + jsonObject);
		this.canelDialog();
		switch (type) {
		case Constans.REQ_Fans:
			//Toast.makeText(this, back.Getoauth_token(), Toast.LENGTH_LONG).show();
			FansAttentionBack back = (FansAttentionBack) ReqJsonUtil.changeToObject(jsonObject, FansAttentionBack.class);
			if(back.getCode()==00000){
				if(back.getList()!=null){
//				Toast.makeText(this, back.getMessage(), Toast.LENGTH_LONG).show();
		        adapter = new MyFansListAdapter(MyFansActivity.this,back.getList());
		        mAdapterView.setAdapter(adapter);
				adapter.notifyDataSetChanged();//在修改适配器绑定的数组后，不用重新刷新Activity，通知Activity更新ListView
				}else{
					no_fans.setVisibility(View.VISIBLE);
					mAdapterView.setVisibility(View.GONE);
				}
			}else{
				Toast.makeText(this, back.getMessage(), Toast.LENGTH_LONG).show();
				
			}
			
			break;
		}
	}
}

package com.gzqining.inthegarden.act;

import com.gzqining.inthegarden.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.tsz.afinal.http.AjaxParams;

import com.gzqining.inthegarden.adapter.MyAttentionListAdapter;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.TaskUtil;
import com.gzqining.inthegarden.vo.FansAttentionBack;
import com.gzqining.inthegarden.waterflow.XListView;
import com.gzqining.inthegarden.waterflow.XListView.IXListViewListener;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


 

public class MyAttentionActivity extends BaseActivity implements IXListViewListener{
	String tag = MyAttentionActivity.class.getName();
	int page;
	Object obj;
	XListView mAdapterView = null;
	MyAttentionListAdapter adapter = null;
	private ImageButton back_btn;
	private LinearLayout attention_ll;
	private ImageView no_friend;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myattention);
		attention();
		
		mAdapterView = (XListView) findViewById(R.id.attention_list);
		back_btn = (ImageButton) findViewById(R.id.top_layout_myattention_back_btn);
		attention_ll = (LinearLayout) findViewById(R.id.attention_ll);
		no_friend = (ImageView) findViewById(R.id.image_no_friend);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapterView.setPullLoadEnable(true);
//		adapter = new MyAttentionListAdapter(MyAttentionActivity.this);
//		mAdapterView.setAdapter(adapter);
//		no_friend.setVisibility(View.VISIBLE);
//		mAdapterView.setVisibility(View.GONE);
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
		int page = 1;
		attention();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());		
		mAdapterView.setLoadTime(sdf.format(new Date()));
		mAdapterView.stopLoadMore();
		attention();
	}
	/**
	 * 接口
	 * */
	public void attention() {
		this.showDialog();
		SharedPreferences sp = this.getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		Logger.d(tag, "ready");
		this.task = new TaskUtil(this);
		String url = Constans.attention;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("page",""+page);
		this.task.post(params, url);
		//this.task.postsync(encodesstr, url);
		
		this.task.setType(Constans.REQ_Attention);
	}
	
	/**
	 * 接口返回
	 * */
	@Override
	public void taskCallBack(String jsonObject, int type) {
		Logger.d(tag, "type:" + type + ",jsonObject:" + jsonObject);
		this.canelDialog();
		switch (type) {
		case Constans.REQ_Attention:
			//Toast.makeText(this, back.Getoauth_token(), Toast.LENGTH_LONG).show();
			FansAttentionBack back = (FansAttentionBack) ReqJsonUtil.changeToObject(jsonObject, FansAttentionBack.class);
			if(back.getCode()==00000){
				if(back.getList()!=null){
//					Toast.makeText(this, back.getMessage(), Toast.LENGTH_LONG).show();
			        adapter = new MyAttentionListAdapter(MyAttentionActivity.this,back.getList());
			        mAdapterView.setAdapter(adapter);
					adapter.notifyDataSetChanged();//在修改适配器绑定的数组后，不用重新刷新Activity，通知Activity更新ListView
				}else{
//					Resources resources = getBaseContext().getResources();   
//					Drawable btnDrawable = resources.getDrawable(R.drawable.nofriend);  
//					attention_ll.setBackgroundDrawable(btnDrawable); 
					no_friend.setVisibility(View.VISIBLE);
					mAdapterView.setVisibility(View.GONE);
					
				}
			}else{
				Toast.makeText(this, back.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			break;
		}
	}
}

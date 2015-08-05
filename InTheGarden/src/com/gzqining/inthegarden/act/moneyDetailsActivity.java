package com.gzqining.inthegarden.act;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.tsz.afinal.http.AjaxParams;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.adapter.moneyDetailsListAdapter;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.TaskUtil;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.FansAttentionBack;
import com.gzqining.inthegarden.vo.moneyDetailBack;
import com.gzqining.inthegarden.vo.moneyDetailBean;
import com.gzqining.inthegarden.vo.sendGiftBean;
import com.gzqining.inthegarden.waterflow.XListView;
import com.gzqining.inthegarden.waterflow.XListView.IXListViewListener;

public class moneyDetailsActivity extends BaseActivity implements IXListViewListener {
	String tag = LoginActivity.class.getName();
	XListView mAdapterView = null;
	moneyDetailsListAdapter adapter = null;
	private ImageButton back_btn;
	private ImageView no_fans;
	String encodesstr = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_money_details);
		AppManager.getAppManager().addActivity(this);
		
		mAdapterView = (XListView) findViewById(R.id.moneydetails_list);
		back_btn = (ImageButton) findViewById(R.id.top_layout_moneydetails_back_btn);
		no_fans = (ImageView) findViewById(R.id.image_no_moneydetails);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapterView.setPullLoadEnable(true);
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		sendedGift();
		}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());		
		mAdapterView.setRefreshTime(sdf.format(new Date()));
		mAdapterView.stopRefresh();
		sendedGift();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());		
		mAdapterView.setLoadTime(sdf.format(new Date()));
		mAdapterView.stopLoadMore();
		sendedGift();
	}
	/**
	 * 接口
	 * */
	public void sendedGift() {
		this.showDialog();
		SharedPreferences sp = this.getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		Logger.d(tag, "ready");
		moneyDetailBean bean = new moneyDetailBean();
		bean.setType("1");
		String jsonstr = JsonHelper.toJsonString(bean);
		
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.task = new TaskUtil(this);
		String url = Constans.creditDetail;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("cont", encodesstr);
//		params.put("page",""+page);
		this.task.post(params, url);
		//this.task.postsync(encodesstr, url);
		
		this.task.setType(Constans.REQ_creditDetail);
	}
	
	/**
	 * 接口返回
	 * */
	@Override
	public void taskCallBack(String jsonObject, int type) {
		Logger.d(tag, "type:" + type + ",jsonObject:" + jsonObject);
		this.canelDialog();
		switch (type) {
		case Constans.REQ_creditDetail:
			//Toast.makeText(this, back.Getoauth_token(), Toast.LENGTH_LONG).show();
			moneyDetailBack back = (moneyDetailBack) ReqJsonUtil.changeToObject(jsonObject, moneyDetailBack.class);
			if(back.getCode().equals("00000")){
				if(back.getData().size() != 0){
//				Toast.makeText(this, back.getMessage(), Toast.LENGTH_LONG).show();
		        adapter = new moneyDetailsListAdapter(moneyDetailsActivity.this,back.getData());
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

package com.gzqining.inthegarden.act;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.editUserDataBean;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class NickNameSetActivity extends BaseActivity implements OnClickListener{
	
	private ImageButton back_btn;
	private ImageButton right;
	private EditText nickName_et;
	Object obj;
	Handler handler;
	String encodesstr = "";
	CommonBack back;
	String nickName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_nickname_set);
		AppManager.getAppManager().addActivity(this);
		nickName = this.getIntent().getStringExtra("nickName");
		initView();
		initEvents();
	}
	
	/**
	 * 初始化组件
	 */
	private void initView() {
		back_btn = (ImageButton)findViewById(R.id.top_layout_nickname_back_btn);
		right = (ImageButton) findViewById(R.id.top_layout_nickname_right_btn);
		nickName_et = (EditText) findViewById(R.id.nickname_set);
		nickName_et.setText(nickName);
	}
	
	/**
	 * 绑定事件
	 */
	private void initEvents() {

		back_btn.setOnClickListener(this);
		nickName_et.setOnClickListener(this);
		right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (nickName_et.getText().toString()!=null){
					signHttpPost();
				}else{
					Intent intent = new Intent(NickNameSetActivity.this, MyInfoActivity.class);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.top_layout_nickname_back_btn:
			
			finish();
				
			break;
		default:
			break;
		}
	}
	public void signHttpPost() {
		
		SharedPreferences sp = this.getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		editUserDataBean bean = new editUserDataBean();
		bean.setNickname(nickName_et.getText().toString());
		String jsonstr = JsonHelper.toJsonString(bean);
		
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = Constans.editUserData;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
				params.put("cont", encodesstr);
				
				FinalHttp http = new FinalHttp();
				obj =  http.postSync(url, params);
				handler.sendEmptyMessage(1);
				
			}
		}).start();

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					try {
						CommonBack back = (CommonBack) ReqJsonUtil.changeToObject(obj.toString(), CommonBack.class);
						System.out.print(back);
						if(back.getCode()==00000){
							Intent intent = new Intent(NickNameSetActivity.this,MyInfoActivity.class);
							startActivity(intent);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
}
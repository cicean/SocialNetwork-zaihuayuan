package com.gzqining.inthegarden.act;

import net.tsz.afinal.http.AjaxParams;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.TaskUtil;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.PhoneBean;
import com.gzqining.inthegarden.vo.resetPwdBean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ResetPWActivity extends BaseActivity {
	private ImageButton back;
	private Button confirm;
	private EditText password1;
	private EditText card;
	Object obj;
	static String str;
	String sid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_resetpw);
		AppManager.getAppManager().addActivity(this);
		Intent intent = getIntent();
		sid = intent.getStringExtra("sid");
		confirm = (Button) findViewById(R.id.confirm_resetpw);
		back = (ImageButton) findViewById(R.id.top_layout_resetpw_back_btn);
		password1 = (EditText) findViewById(R.id.password1);
		card = (EditText) findViewById(R.id.IDCord);
		//登录按钮
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(ResetPWActivity.this,LoginActivity.class);
//				startActivity(intent);
				PhoneSpw();
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(ResetPWActivity.this,LoginActivity.class);
//				startActivity(intent);
				finish();
			}
		});
	}
	public void PhoneSpw() {
		this.showDialog();
		this.task = new TaskUtil(this);
		String url = Constans.Phone_PwRetrieve;
		resetPwdBean bean = new resetPwdBean();
		bean.setPasswd(password1.getText().toString());
		bean.setRepasswd(card.getText().toString());
		String jsonstr = JsonHelper.toJsonString(bean);
		String encodesstr = "";
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			//System.out.println(encodesstr);
			Logger.d("encodesstr", encodesstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AjaxParams params = new AjaxParams();
		params.put("cont", encodesstr);
		params.put("sid", sid);
		this.task.post(params, url);
		this.task.setType(Constans.REQ_Phone_PwRetrieve);
	}
	
	/**
	 * 接口返回
	 * */
	@Override
	public void taskCallBack(String jsonObject, int type) {
		this.canelDialog();
		switch (type) {
		case Constans.REQ_Phone_PwRetrieve:
			//Toast.makeText(this, back.Getoauth_token(), Toast.LENGTH_LONG).show();
			CommonBack back = (CommonBack) ReqJsonUtil.changeToObject(jsonObject, CommonBack.class);
			if(back.getCode()==00000){
				Toast.makeText(this, back.getMessage(), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(ResetPWActivity.this,LoginActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(this, back.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			break;
		}
	}
}

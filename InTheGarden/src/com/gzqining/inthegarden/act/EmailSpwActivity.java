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
import com.gzqining.inthegarden.vo.EmailBean;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EmailSpwActivity extends BaseActivity {
	String tag = LoginActivity.class.getName();
	private Button confirm;//确认
	private ImageButton back;
	private EditText Email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_emailspw);
		AppManager.getAppManager().addActivity(this);
		
		confirm = (Button) findViewById(R.id.confirm_emailspw);
		back = (ImageButton) findViewById(R.id.top_layout_emailspw_back_btn);
		Email = (EditText) findViewById(R.id.emailspw_edt);
		//登录按钮
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(EmailSpwActivity.this,ResetPWActivity.class);
//				startActivity(intent);
				EmailSpw();
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	/**
	 * 接口
	 * */
	public void EmailSpw() {
		this.showDialog();
		this.task = new TaskUtil(this);
		String url = Constans.Email_PwRetrieve;
		EmailBean bean = new EmailBean();
		bean.setEmail(Email.getText().toString());
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
		this.task.post(params, url);
		this.task.setType(Constans.REQ_Email_PwRetrieve);
	}
	
	/**
	 * 接口返回
	 * */
	@Override
	public void taskCallBack(String jsonObject, int type) {
		Logger.d(tag, "type:" + type + ",jsonObject:" + jsonObject);
		this.canelDialog();
		switch (type) {
		case Constans.REQ_Email_PwRetrieve:
			//Toast.makeText(this, back.Getoauth_token(), Toast.LENGTH_LONG).show();
			CommonBack back = (CommonBack) ReqJsonUtil.changeToObject(jsonObject, CommonBack.class);
			if(back.getCode()==00000){
				Toast.makeText(this, back.getMessage(), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(EmailSpwActivity.this,ResetPWActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(this, back.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			break;
		}
	}
}

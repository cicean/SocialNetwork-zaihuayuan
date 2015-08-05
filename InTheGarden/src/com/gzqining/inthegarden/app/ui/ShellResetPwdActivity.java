package com.gzqining.inthegarden.app.ui;

import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.ShellActivity;
import com.gzqining.inthegarden.app.ShellUtil;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.Extra;
import com.gzqining.inthegarden.app.injection.ViewById;
import com.gzqining.inthegarden.app.util.StringUtils;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.EmailBean;

@EActivity(layout = R.layout.ui_activity_reset_pwd, inject = true)
public class ShellResetPwdActivity extends ShellActivity{
	public static final String EXTRA_RESET_PHONE = "reset_phone";
	@Extra(EXTRA_RESET_PHONE)
	private boolean resetPhone;
	@ViewById
	private EditText editPhone;
	@ViewById
	private EditText editEmail;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		findViewById(R.id.phoneLayout).setVisibility(resetPhone ? View.VISIBLE : View.GONE);
		findViewById(R.id.emailLayout).setVisibility(resetPhone ? View.GONE : View.VISIBLE);
		
	}
	
	@Click({
		R.id.btnBack,
		R.id.btnNext
	})
	void onClick(View v) {
		int id = v.getId();
		if(id == R.id.btnBack) {
			finish();
		} else if(id == R.id.btnNext) {
			if(resetPhone) {
				String phone = editPhone.getText().toString().trim();
				if(!StringUtils.isPhoneNumberValid(phone)) {
					Toast.makeText(getActivity(), "手机号码格式不正确", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Intent intent = new Intent();
				intent.putExtra(ShellResetPwdVerifyActivity.EXTRA_PHONE, phone);
				ShellUtil.execute(getActivity(), ShellResetPwdVerifyActivity.class,intent);
			} else {
				String email = editEmail.getText().toString().trim();
				if(!StringUtils.isEmail(email)) {
					Toast.makeText(getActivity(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
					return;
				}
				
				emailSpw(email);
			}
		}
	}
	
	
	private void emailSpw(String email) {
		String url = Constans.Email_PwRetrieve;
		EmailBean bean = new EmailBean();
		bean.setEmail(email);
		String jsonstr = JsonHelper.toJsonString(bean);
		String encodesstr = "";
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		AjaxParams params = new AjaxParams();
		params.put("cont", encodesstr);
		
		showDialogFragment(false);
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if(isFinishing())
					return;
				removeDialogFragment();
				if(obj == null || TextUtils.isEmpty((String)obj)) {
					Toast.makeText(getActivity(), "操作失败",Toast.LENGTH_SHORT).show();
					return;
				}
				
				CommonBack back = (CommonBack) ReqJsonUtil.changeToObject((String)obj, CommonBack.class);
				if(back.getCode()==00000){
					Toast.makeText(getActivity(), back.getMessage(), Toast.LENGTH_SHORT).show();
					/*Intent intent = new Intent(EmailSpwActivity.this,ResetPWActivity.class);
					startActivity(intent);*/
				}else{
					Toast.makeText(getActivity(), back.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
}

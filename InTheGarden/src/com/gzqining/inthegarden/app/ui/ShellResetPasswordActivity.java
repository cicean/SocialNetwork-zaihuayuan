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
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.resetPwdBean;

@EActivity(layout = R.layout.ui_activity_reset_password, inject = true)
public class ShellResetPasswordActivity extends ShellActivity{
	public static final String EXTRA_SID = "sid";
	
	@Extra(EXTRA_SID)
	private String sid;
	
	@ViewById
	private EditText editPassword1;
	@ViewById
	private EditText editPassword2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			String pwd1 = editPassword1.getText().toString();
			String pwd2 = editPassword2.getText().toString();
			if(TextUtils.isEmpty(pwd1) || pwd1.length() < 6) {
				Toast.makeText(getActivity(), "密码格式不正确", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(TextUtils.isEmpty(pwd2) || !pwd1.equals(pwd2)) {
				Toast.makeText(getActivity(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
				return;
			}
			
			phonepwd(pwd1,pwd2);
			
		}
	}
	
	private void phonepwd(String pwd1, String pwd2) {
		
		String url = Constans.Phone_PwRetrieve;
		resetPwdBean bean = new resetPwdBean();
		bean.setPasswd(pwd1);
		bean.setRepasswd(pwd2);
		String jsonstr = JsonHelper.toJsonString(bean);
		String encodesstr = "";
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		AjaxParams params = new AjaxParams();
		params.put("cont", encodesstr);
		params.put("sid", sid);
		
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
					Toast.makeText(getActivity(), back.getMessage(), Toast.LENGTH_LONG).show();
					
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					ShellUtil.execute(getActivity(), ShellLoginActivity.class);
				}else{
					Toast.makeText(getActivity(), back.getMessage(), Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
	}
}

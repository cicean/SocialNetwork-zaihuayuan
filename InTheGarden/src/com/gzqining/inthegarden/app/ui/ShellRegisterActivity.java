package com.gzqining.inthegarden.app.ui;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.UserRuleActivity;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.ShellActivity;
import com.gzqining.inthegarden.app.ShellUtil;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.ViewById;
import com.gzqining.inthegarden.app.util.StringUtils;
import com.gzqining.inthegarden.util.Constans;

@EActivity(layout = R.layout.ui_activity_register_phone, inject = true)
public class ShellRegisterActivity extends ShellActivity{

	private boolean checkRule = true;
	@ViewById
	private ImageView checkUserRule;
	@ViewById
	private EditText editPhone;
	@ViewById
	private EditText editEmail;
	@ViewById
	private EditText editPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		checkUserRule.setImageResource(checkRule ? R.drawable.check_bg_selected : R.drawable.check_bg_normal);
	}
	
	
	@Click({
		R.id.btnBack,
		R.id.btnNext,
		R.id.btnUserRule,
		R.id.checkUserRule
	})
	void onClick(View v) {
		int id = v.getId();
		switch(id) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnNext:
			String phone = editPhone.getText().toString();
			String email = editEmail.getText().toString();
			String password = editPassword.getText().toString();
			
			if(!StringUtils.isPhoneNumberValid(phone)) {
				Toast.makeText(getActivity(), "手机号码格式不正确", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!StringUtils.isEmail(email)) {
				Toast.makeText(getActivity(), "邮箱账号格式不正确", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(TextUtils.isEmpty(password) || password.length() < 6) {
				Toast.makeText(getActivity(), "密码格式不正确", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(checkRule == false) {
				Toast.makeText(getActivity(), "您尚未同意《用户协议》", Toast.LENGTH_SHORT).show();
				return;
			}
			
			dataVerify(phone, email, password);
			break;
		case R.id.btnUserRule:
			startActivity(new Intent(getActivity(), UserRuleActivity.class));		
			break;
		case R.id.checkUserRule:
			checkRule = !checkRule;
			checkUserRule.setImageResource(checkRule ? R.drawable.check_bg_selected : R.drawable.check_bg_normal);
			break;
		}
	}
	
	
	void dataVerify(final String phone, final String email, final String password) {
		showDialogFragment(false);
		
		String url = Constans.dataVerify;
		AjaxParams params = new AjaxParams();
		params.put("phone", phone);
		params.put("email", email);
		
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if(isFinishing()) return;
				
				removeDialogFragment();
				if(TextUtils.isEmpty((String)obj)) {
					Toast.makeText(getActivity(), "号码或邮箱验证失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				JSONObject json = null;
				try {
					json = (JSONObject) new JSONTokener((String) obj).nextValue();
				} catch (JSONException e) {
					// e.printStackTrace();
				}
				
				if (json == null) {
					Toast.makeText(getActivity(), "号码或邮箱验证失败", Toast.LENGTH_SHORT).show();
					return;
				}
			
				if(!"00000".equals(json.optString("code"))) {
					Toast.makeText(getActivity(), json.optString("message"), Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				Intent intent = new Intent();
				intent.putExtra("phone", phone);
				intent.putExtra("email", email);
				intent.putExtra("password", password);
				ShellUtil.execute(getActivity(), ShellRegisterVerifyActivity.class, intent, 999);
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 999) {
			setResult(RESULT_OK);
			finish();
		}
	}
}

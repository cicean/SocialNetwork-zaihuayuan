package com.gzqining.inthegarden.app.ui;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.ShellActivity;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.Extra;
import com.gzqining.inthegarden.app.injection.ViewById;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.VerifyIdCard;
import com.gzqining.inthegarden.vo.CommonConstants;

@EActivity(layout = R.layout.ui_activity_authentication, inject = true)
public class ShellAuthenticationActivity extends ShellActivity{

	public static final String EXTRA_PHOTO  = "base64Img";
	
	@Extra(EXTRA_PHOTO)
	private String base64Img;
	
	@ViewById
	private EditText editName;
	@ViewById
	private EditText editSfz;
	@ViewById
	private View btnCleanName;
	@ViewById
	private View btnCleanSfz;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextWatcher mTextWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void afterTextChanged(Editable s) {
				changeText();
			}
		};
		
		editName.addTextChangedListener(mTextWatcher);
		editSfz.addTextChangedListener(mTextWatcher);
	}
	
	void changeText() {
		btnCleanName.setVisibility(editName.length() > 0 ? View.VISIBLE : View.INVISIBLE);
		btnCleanSfz.setVisibility(editSfz.length() > 0 ? View.VISIBLE : View.INVISIBLE);
	}
	
	
	@Click({
		R.id.btnBack,
		R.id.btnNext,
		R.id.btnCleanName,
		R.id.btnCleanSfz,
	})
	void onClick(View v) {
		int id = v.getId();
		switch(id) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnNext:
			String uname = editName.getText().toString();
			String idcard = editSfz.getText().toString();
			if(TextUtils.isEmpty(uname)) {
				Toast.makeText(getActivity(), "请输入姓名", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(!new VerifyIdCard().verify(idcard)) {
				Toast.makeText(getActivity(), "身份证号码格式不正确", Toast.LENGTH_SHORT).show();
				return;
			}
			
			approve(uname, idcard);
			
			break;
		case R.id.btnCleanName:
			editName.setText("");
			changeText();
			break;
		case R.id.btnCleanSfz:
			editSfz.setText("");
			changeText();
			break;
		}
		
	}
	
	void approve(String uname, String idcard) {
		
		showDialogFragment(false);
		SharedPreferences sp = getActivity().getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		String url = Constans.approve;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		
		try {
			String cont = String.format("{\"uname\":\"%s\",\"idcard\":\"%s\"}", uname, idcard);
			cont = AesUtils.Encrypt(cont, CommonConstants.AES_KEY);
			params.put("cont", cont);
			params.put("img", base64Img);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if(isFinishing()) return;
				
				removeDialogFragment();
				if(TextUtils.isEmpty((String)obj)) {
					Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				JSONObject json = null;
				try {
					json = (JSONObject) new JSONTokener((String) obj).nextValue();
				} catch (JSONException e) {
					// e.printStackTrace();
				}
				
				if (json == null || !"00000".equals(json.optString("code"))) {
					Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//提现成功
				Toast.makeText(getActivity(), json.optString("message"), Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		
	}
	
}

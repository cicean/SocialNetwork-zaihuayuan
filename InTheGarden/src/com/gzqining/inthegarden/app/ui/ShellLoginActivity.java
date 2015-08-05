package com.gzqining.inthegarden.app.ui;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.EmailSpwActivity;
import com.gzqining.inthegarden.act.MainActivity;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.ShellActivity;
import com.gzqining.inthegarden.app.ShellUtil;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.ViewById;
import com.gzqining.inthegarden.app.util.StringUtils;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.GetTokenVo;
import com.gzqining.inthegarden.vo.LoginBack;
import com.gzqining.inthegarden.vo.LoginBean;

@EActivity(layout = R.layout.ui_activity_login_phone, inject = true)
public class ShellLoginActivity extends ShellActivity {

	@ViewById
	private EditText editPhone;
	@ViewById
	private EditText editPassword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Click({ R.id.btnBack, R.id.btnNext, R.id.btnGetPwd })
	void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnNext:
			String account = editPhone.getText().toString();
			String password = editPassword.getText().toString();
			if (!StringUtils.isEmail(account) && !StringUtils.isPhoneNumberValid(account)) {
				Toast.makeText(getActivity(), "账号格式不正确", Toast.LENGTH_SHORT).show();
				return;
			}

			if (TextUtils.isEmpty(password) || password.length() < 6) {
				Toast.makeText(getActivity(), "密码格式不正确", Toast.LENGTH_SHORT).show();
				return;
			}

			login(account, password);
			break;
		case R.id.btnGetPwd:

			final BottomView bv = new BottomView(getActivity(), R.layout.ui_activity_login_phone_resetpwd);
			bv.showBottomView(true);
			bv.getView().findViewById(R.id.phonespw).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra(ShellResetPwdActivity.EXTRA_RESET_PHONE, true);
					ShellUtil.execute(getActivity(), ShellResetPwdActivity.class, intent);
					bv.dismissBottomView();
				}
			});
			bv.getView().findViewById(R.id.emailspw).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					/*
					 * Intent intent = new Intent();
					 * intent.putExtra(ShellResetPwdActivity.EXTRA_RESET_PHONE,
					 * true); ShellUtil.execute(getActivity(),
					 * ShellResetPwdActivity.class, intent);
					 */

					startActivity(new Intent(getActivity(), EmailSpwActivity.class));
					bv.dismissBottomView();
				}
			});
			bv.getView().findViewById(R.id.cancel_login).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					bv.dismissBottomView();
				}
			});

			break;
		}
	}

	private void login(final String account, final String password) {

		String url = Constans.authorize;
		LoginBean bean = new LoginBean();
		bean.setUid(account);
		bean.setLocat("213.123113,134.324234");
		bean.setPasswd(password);
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
				if (isFinishing())
					return;
				removeDialogFragment();
				if (obj == null || TextUtils.isEmpty((String) obj)) {
					Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
					return;
				}

				LoginBack back = (LoginBack) ReqJsonUtil.changeToObject((String) obj, LoginBack.class);
				String oauth_token = back.Getoauth_token();
				String oauth_token_secret = back.Getoauth_token_secret();
				if (back.Getcode() == 0000) {
					// 保存授权到本地
					SharedPreferences preferences = getActivity().getSharedPreferences("idcard", Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = preferences.edit();
					editor.clear();
					editor.putString("uid", back.Getuid());
					editor.putString("oauth_token", oauth_token);
					editor.putString("oauth_token_secret", oauth_token_secret);
					editor.putString("userName", account);
					editor.putString("passWord", password);
					editor.putString("token", back.getToken());
					editor.putInt("is_vip", back.getIs_vip());
					editor.putInt("is_approve", back.getIs_approve());
					editor.putInt("is_first", back.getIs_first());
					editor.commit();

					getChatToken();

				} else {
					Toast.makeText(getActivity(), back.Getmessage(), Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	private void getChatToken() {
		String url = Constans.getToken;
		AjaxParams params = new AjaxParams();
		SharedPreferences preferences = getActivity().getSharedPreferences("idcard", Activity.MODE_PRIVATE);
		params.put("oauth_token", preferences.getString("oauth_token", null));
		params.put("oauth_token_secret", preferences.getString("oauth_token_secret", null));

		showDialogFragment(false);
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if (isFinishing())
					return;

				if (obj == null || TextUtils.isEmpty((String) obj)) {
					removeDialogFragment();
					Toast.makeText(getActivity(), "登陆失败", Toast.LENGTH_SHORT).show();
					return;
				}

				GetTokenVo tokenVo = (GetTokenVo) ReqJsonUtil.changeToObject((String) obj, GetTokenVo.class);
				if (tokenVo.getCode() != 200) {
					removeDialogFragment();
					Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
					return;
				}

				// 连接融云服务器。
				try {
					RongIM.connect(tokenVo.getToken(), new RongIMClient.ConnectCallback() {
						@Override
						public void onSuccess(String s) {
							removeDialogFragment();
							startActivity(new Intent(getActivity(), MainActivity.class));
							setResult(RESULT_OK);
							finish();
						}

						@Override
						public void onError(ErrorCode errorCode) {
							removeDialogFragment();
							Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

}

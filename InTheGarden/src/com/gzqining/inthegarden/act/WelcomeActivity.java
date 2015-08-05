package com.gzqining.inthegarden.act;

import java.util.Timer;
import java.util.TimerTask;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectCallback.ErrorCode;
import net.tsz.afinal.http.AjaxParams;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.TaskUtil;
import com.gzqining.inthegarden.vo.GetTokenVo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class WelcomeActivity extends BaseActivity {

	private Handler hander;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.act_welcome);
		AppManager.getAppManager().addActivity(this);
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String userName = sp.getString("userName", null);
		final String passWord = sp.getString("passWord", null);
		hander = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				getToken();
			}
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);

					if (userName != null && passWord != null) {
						hander.sendEmptyMessage(0);
					} else {
						startActivity(new Intent(WelcomeActivity.this,
								BeginViewActivity.class));
						finish();
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}
	

	/**
	 * 接口
	 * */
	public void getToken() {
		this.task = new TaskUtil(this);
		String url = Constans.getToken;

		AjaxParams params = new AjaxParams();
		SharedPreferences preferences;
		preferences = getSharedPreferences("idcard", 0);
		params.put("oauth_token", preferences.getString("oauth_token", null));
		params.put("oauth_token_secret",
				preferences.getString("oauth_token_secret", null));
		this.task.post(params, url);

		this.task.setType(Constans.REQ_getToken);
	}

	/**
	 * 接口返回
	 * */
	@Override
	public void taskCallBack(String jsonObject, int type) {
		if (type == Constans.REQ_getToken) {
			this.canelDialog();
		}
		switch (type) {

		case Constans.REQ_getToken:
			if (TextUtils.isEmpty(jsonObject)) {
				Toast.makeText(WelcomeActivity.this, "登陆失败", Toast.LENGTH_SHORT)
						.show();
				startActivity(new Intent(WelcomeActivity.this,
						BeginViewActivity.class));
				return;
			}

			GetTokenVo tokenVo = (GetTokenVo) ReqJsonUtil.changeToObject(
					jsonObject, GetTokenVo.class);
			if (tokenVo.getCode() != 200) {
				Toast.makeText(WelcomeActivity.this, "登录失败", Toast.LENGTH_SHORT)
						.show();
				startActivity(new Intent(WelcomeActivity.this,
						BeginViewActivity.class));
				return;
			}

			if (tokenVo.getCode() == 200) {

				// 连接融云服务器。
				try {
					RongIM.connect(tokenVo.getToken(),
							new RongIMClient.ConnectCallback() {

								@Override
								public void onSuccess(String s) {
									// 此处处理连接成功。
									Log.d("Connect:", "Login successfully.");
									Intent intent = new Intent(
											WelcomeActivity.this,
											MainActivity.class);
									startActivity(intent);
									finish();
								}

								@Override
								public void onError(ErrorCode errorCode) {
									// 此处处理连接错误。
									Log.d("Connect:", "Login failed.");
									Toast.makeText(WelcomeActivity.this,
											"登录失败", Toast.LENGTH_SHORT).show();
								}
							});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
			}

			break;
		}
	}
}

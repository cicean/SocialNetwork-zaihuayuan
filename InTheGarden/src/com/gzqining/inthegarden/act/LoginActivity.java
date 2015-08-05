package com.gzqining.inthegarden.act;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.TaskUtil;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.GetTokenVo;
import com.gzqining.inthegarden.vo.LoginBack;
import com.gzqining.inthegarden.vo.LoginBean;

public class LoginActivity extends BaseActivity implements OnClickListener {
	String tag = LoginActivity.class.getName();

	private Button login_bt;// 登录
	private Button forgetPW_bt;// 忘记密码
	private Button phoneSPW_bt;// 手机找回密码
	private Button emailSPW_bt;// 邮箱找回密码
	private Button cancel;// 取消
	private ImageButton back;
	private View menu;// 下面菜单
	private EditText userName;
	private EditText passWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);
		AppManager.getAppManager().addActivity(this);
		login_bt = (Button) findViewById(R.id.login_bt);
		forgetPW_bt = (Button) findViewById(R.id.forgetPW_bt);
		phoneSPW_bt = (Button) findViewById(R.id.phonespw);
		emailSPW_bt = (Button) findViewById(R.id.emailspw);
		cancel = (Button) findViewById(R.id.cancel_login);
		back = (ImageButton) findViewById(R.id.top_layout_login_back_btn);
		userName = (EditText) findViewById(R.id.username);
		passWord = (EditText) findViewById(R.id.password);
		menu = findViewById(R.id.menu);
		menu.setVisibility(View.INVISIBLE);// 设置下面菜单默认不可见
		final SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		userName.setText("lylong");
		passWord.setText("888888");
//		userName.setText(sp.getString("userName", null));
//		passWord.setText(sp.getString("passWord", null));
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		// 登录按钮
		login_bt.setOnClickListener(this);
		// 忘记密码
		forgetPW_bt.setOnClickListener(this);
		// 取消
		cancel.setOnClickListener(this);
		// 手机找回密码
		phoneSPW_bt.setOnClickListener(this);
		// 邮箱找回密码
		emailSPW_bt.setOnClickListener(this);
		// 返回
		back.setOnClickListener(this);
		userName.setOnClickListener(this);
		passWord.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.login_bt:
			login("", "");
			break;
		case R.id.forgetPW_bt:
			menu.setVisibility(View.VISIBLE);
			break;
		case R.id.phonespw:
			intent = new Intent(LoginActivity.this, PhoneSpwActivity.class);
			startActivity(intent);
			menu.setVisibility(View.INVISIBLE);
			finish();
			break;
		case R.id.emailspw:
			intent = new Intent(LoginActivity.this, EmailSpwActivity.class);
			startActivity(intent);
			menu.setVisibility(View.INVISIBLE);
			finish();
			break;
		case R.id.cancel_login:
			menu.setVisibility(View.INVISIBLE);
			break;
		case R.id.top_layout_login_back_btn:
			finish();
			break;
		case R.id.username:
			userName.setText("");
			passWord.setText("");
			break;
		case R.id.password:
			passWord.setText("");
			break;
		default:
			break;
		}

	}

	/**
	 * 接口
	 * */
	public void login(String account, String password) {
		String uid = userName.getText().toString();
		String passwd = passWord.getText().toString();
		Logger.d(tag, "ready");
		this.task = new TaskUtil(this);
		String url = Constans.authorize;
		LoginBean bean = new LoginBean();
		bean.setUid(uid);
		bean.setLocat("213.123113,134.324234");
		bean.setPasswd(passwd);
		String jsonstr = JsonHelper.toJsonString(bean);
		// Log.d("jsonstr:", jsonstr);
		// System.out.println(jsonstr);

		String encodesstr = "";
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			// System.out.println(encodesstr);
			Logger.d("encodesstr", encodesstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AjaxParams params = new AjaxParams();
		params.put("cont", encodesstr);

		this.task.post(params, url);
		// this.task.postsync(encodesstr, url);

		this.task.setType(Constans.REQ_Authorize);
	}

	/**
	 * 接口
	 * */
	public void getToken() {
		Logger.d(tag, "ready");
		this.task = new TaskUtil(this);
		String url = Constans.getToken;

		AjaxParams params = new AjaxParams();
		SharedPreferences preferences;
		preferences = getSharedPreferences("idcard", 0);
		params.put("oauth_token", preferences.getString("oauth_token", null));
		params.put("oauth_token_secret",preferences.getString("oauth_token_secret", null));
		this.task.post(params, url);

		this.task.setType(Constans.REQ_getToken);
	}

	/**
	 * 接口返回
	 * */
	@Override
	public void taskCallBack(String jsonObject, int type) {
		Logger.d(tag, "type:" + type + ",jsonObject:" + jsonObject);
		if (type == Constans.REQ_getToken) {
			this.canelDialog();
		}
		switch (type) {
		case Constans.REQ_Authorize:
			// Toast.makeText(this, back.Getoauth_token(),
			// Toast.LENGTH_LONG).show();
			LoginBack back = (LoginBack) ReqJsonUtil.changeToObject(jsonObject,
					LoginBack.class);
			String oauth_token = back.Getoauth_token();
			String oauth_token_secret = back.Getoauth_token_secret();
			if (back.Getcode() == 0000) {
				// 保存授权到本地
				SharedPreferences preferences;
				SharedPreferences.Editor editor;
				preferences = getSharedPreferences("idcard", 0);
				editor = preferences.edit();
				editor.clear();
				editor.putString("uid", back.Getuid());
				editor.putString("oauth_token", oauth_token);
				editor.putString("oauth_token_secret", oauth_token_secret);
				editor.putString("userName", userName.getText().toString());
				editor.putString("passWord", passWord.getText().toString());
				editor.commit();

				getToken();

			} else {
				Toast.makeText(this, back.Getmessage(), Toast.LENGTH_LONG)
						.show();
				this.canelDialog();
			}

			break;

		case Constans.REQ_getToken:
			GetTokenVo tokenVo = (GetTokenVo) ReqJsonUtil.changeToObject(
					jsonObject, GetTokenVo.class);
			if (tokenVo.getCode() == 200) {

				// 连接融云服务器。
				try {
					RongIM.connect(tokenVo.getToken(), new RongIMClient.ConnectCallback() {

						@Override
						public void onSuccess(String s) {
							// 此处处理连接成功。
							Log.d("Connect:", "Login successfully.");
							Intent intent = new Intent(LoginActivity.this,MainActivity.class);
							startActivity(intent);
							finish();
						}

						@Override
						public void onError(ErrorCode errorCode) {
							// 此处处理连接错误。
							Log.d("Connect:", "Login failed.");
							Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
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

	// 监听屏幕是否被点击，隐藏下边菜单栏
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		menu.setVisibility(View.INVISIBLE);
		return false;

	}
}

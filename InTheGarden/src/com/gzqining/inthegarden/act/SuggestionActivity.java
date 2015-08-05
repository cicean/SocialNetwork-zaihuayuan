package com.gzqining.inthegarden.act;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.SuggestionBean;

public class SuggestionActivity extends Activity {
	private ImageView back_btn;
	private Button submit;
	private EditText suggestion_edt;
	Object obj;
	Handler handler;
	String encodesstr = "";
	CommonBack back;
	String string;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_suggestion);
		AppManager.getAppManager().addActivity(this);

		back_btn = (ImageView) findViewById(R.id.top_layout_suggestion_back_btn);
		submit = (Button) findViewById(R.id.top_layout_suggestion_right_btn);
		suggestion_edt = (EditText) findViewById(R.id.suggestion_edt);

		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new
				// Intent(SuggestionActivity.this,MyInfoActivity.class);
				// startActivity(intent);
				finish();
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				suggestionHttpPost();
			}
		});

	}

	public void suggestionHttpPost() {

		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		string = suggestion_edt.getText().toString();// 获取反馈意见输入值
		SuggestionBean bean = new SuggestionBean();
		bean.setFeedback_type("1");
		bean.setFeedback(string);
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
				String url = Constans.submitFeedback;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
				params.put("cont", encodesstr);

				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, params);
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
						if (back.getCode() == 00000) {
							finish();
							Toast.makeText(SuggestionActivity.this, back.getMessage(), Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(SuggestionActivity.this, back.getMessage(), Toast.LENGTH_LONG).show();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
}

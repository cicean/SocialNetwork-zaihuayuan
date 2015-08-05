package com.gzqining.inthegarden.act;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.TaskUtil;

public class BalanceActivity extends BaseActivity {
	String tag = BalanceActivity.class.getName();
	Object obj;
	private TextView money_ownself;
	private ImageButton back_bt;
	private Button top_layout_money_details_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_balance);
		AppManager.getAppManager().addActivity(this);
		
		top_layout_money_details_btn = (Button) findViewById(R.id.top_layout_money_details_btn);
		back_bt = (ImageButton) findViewById(R.id.top_layout_balance_back_btn);
		money_ownself = (TextView) findViewById(R.id.money_ownself);
		Balance();
		back_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		top_layout_money_details_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(BalanceActivity.this,moneyDetailsActivity.class);
				startActivity(intent);
			}
		});
		}
	/**
	 * 接口
	 * */
	public void Balance() {
		this.showDialog();
		SharedPreferences sp = this.getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		Logger.d(tag, "ready");
		this.task = new TaskUtil(this);
		String url = Constans.myScore;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		this.task.post(params, url);
		this.task.setType(Constans.REQ_myScore);
	}
	
	/**
	 * 接口返回
	 * */
	@Override
	public void taskCallBack(String jsonObject, int type) {
		Logger.d(tag, "type:" + type + ",jsonObject:" + jsonObject);
		this.canelDialog();
		switch (type) {
		case Constans.REQ_myScore:
			//Toast.makeText(this, back.Getoauth_token(), Toast.LENGTH_LONG).show();
			try {
				JSONObject jsonObject2 = new JSONObject(jsonObject.toString());
				String message = jsonObject2.getString("message");
				String code = jsonObject2.getString("code");
				String credit = jsonObject2.getString("credit");
				JSONObject jsonObject3 = new JSONObject(credit);
				String experience = jsonObject3.getString("experience");
				JSONObject jsonObject4 = new JSONObject(experience);
				String experienceValue = jsonObject4.getString("value");
				String experienceAlias = jsonObject4.getString("alias");
				String score = jsonObject3.getString("score");
				JSONObject jsonObject5 = new JSONObject(score);
				String scoreValue = jsonObject5.getString("value");
				String scoreAlias = jsonObject5.getString("alias");
				String cashScore = jsonObject3.getString("cashScore");
				JSONObject jsonObject6 = new JSONObject(cashScore);
				String cashScoreValue = jsonObject6.getString("value");
				String cashScoreAlias = jsonObject6.getString("alias");
				String lockScore = jsonObject3.getString("lockScore");
				JSONObject jsonObject7 = new JSONObject(lockScore);
				String lockScoreValue = jsonObject7.getString("value");
				String lockScoreAlias = jsonObject7.getString("alias");
				if(code.equals("00000")){
					money_ownself.setText("￥"+scoreValue);
				}else{
					Toast.makeText(this, message, Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			break;
		}
	}
}

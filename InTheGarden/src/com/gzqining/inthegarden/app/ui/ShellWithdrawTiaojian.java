package com.gzqining.inthegarden.app.ui;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.VipActivity;
import com.gzqining.inthegarden.act.WithdrawalsActivity;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.ShellActivity;
import com.gzqining.inthegarden.app.ShellUtil;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.Injection;
import com.gzqining.inthegarden.util.Constans;

@EActivity(layout = R.layout.ui_withdraw_tiaojian, inject = true)
public class ShellWithdrawTiaojian extends ShellActivity {

	private Injection inject;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inject = getInjection();
		refreshView();
		myState();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	synchronized void refreshView() {
		SharedPreferences sp = getActivity().getSharedPreferences("idcard", 0);
		int is_vip = sp.getInt("is_vip", 0);
		inject.find(R.id.isVip).setVisibility(is_vip == 1 ? View.VISIBLE : View.INVISIBLE);
		inject.find(R.id.unVip).setVisibility(is_vip == 1 ? View.INVISIBLE : View.VISIBLE);

		int is_approve = sp.getInt("is_approve", 0);
		inject.find(R.id.is_approve).setVisibility(is_approve == 2 ? View.VISIBLE : View.INVISIBLE);
		inject.find(R.id.un_approve).setVisibility(is_approve == 2 ? View.INVISIBLE : View.VISIBLE);
		inject.find(R.id.approveMsg, TextView.class).setText(is_approve==1 ? "审核中" : is_approve==2 ? "已认证" : "未认证");

		inject.find(R.id.btnSumbit).setVisibility((is_vip == 1 && is_approve == 2) ? View.VISIBLE : View.GONE);
		inject.find(R.id.btnUnSumbit).setVisibility((is_vip == 1 && is_approve == 2) ? View.GONE : View.VISIBLE);
	}

	@Click({ R.id.btnBack, R.id.item1, R.id.item2, R.id.item3, R.id.btnSumbit })
	void onClick(View v) {
		SharedPreferences sp = getActivity().getSharedPreferences("idcard", 0);
		int id = v.getId();
		switch (id) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.item1:
			int is_vip = sp.getInt("is_vip", 0);
			if (is_vip == 0) {
				startActivity(new Intent(getActivity(), VipActivity.class));
			}
			break;
		case R.id.item2:
			ShellUtil.execute(getActivity(), ShellAuthenticationPhotoActivity.class);
			break;
		case R.id.item3:
			startActivity(new Intent(getActivity(), WithdrawalsActivity.class));
			break;
		case R.id.btnSumbit:
			finish();
			break;
		}

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 999 &&  resultCode == RESULT_OK) {
			myState();
		}
	}

	void myState() {
		showDialogFragment(false);
		SharedPreferences sp = getActivity().getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		String url = Constans.myState;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);

		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				removeDialogFragment();
				if (TextUtils.isEmpty((String) obj)) {
					return;
				}

				JSONObject json = null;
				try {
					json = (JSONObject) new JSONTokener((String) obj).nextValue();
				} catch (JSONException e) {
					// e.printStackTrace();
				}

				if (json == null || !"00000".equals(json.optString("code"))) {
					return;
				}

				SharedPreferences preferences = getActivity().getSharedPreferences("idcard", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putInt("is_vip", json.optInt("is_vip"));
				editor.putInt("is_approve", json.optInt("is_approve"));
				editor.putInt("is_first", json.optInt("is_first"));
				editor.commit();

				refreshView();
			}
		});

	}

}

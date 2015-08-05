package com.gzqining.inthegarden.app.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.WithdrawalsActivity;
import com.gzqining.inthegarden.app.AppActivity;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.ShellUtil;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.Extra;
import com.gzqining.inthegarden.app.injection.Injection;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.bankInfo;

/**
 * 申请提现
 * @author xjm
 */
@EActivity(layout = R.layout.ui_withdraw_tixian, inject = true)
public class WithdrawTixianActivity extends AppActivity{

	public static final String EXTRA_MONEY = "money";
	
	@Extra(EXTRA_MONEY)
	private String money;
	
	interface Callback<T> {
		public void onCallback(T t);
	}
	
	private Injection inject;
	
	private List<bankInfo> list;
	
	private bankInfo defInfo;
	
	final Map<String, String> bankID = new HashMap<String, String>();
	{
		bankID.put("10", "中国工商银行");
		bankID.put("11", "招商银行");
		bankID.put("12", "中国光大银行");
		bankID.put("13", "中信银行");
		bankID.put("14", "浦发银行");
		bankID.put("15", "广发银行");
		bankID.put("16", "华厦银行");
		bankID.put("17", "中国建设银行");
		bankID.put("18", "交通银行");
		bankID.put("19", "中国银行");
		bankID.put("20", "兴业银行");
		bankID.put("21", "平安银行");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		inject = getInjection();
		
		inject.find(R.id.money, TextView.class).setText("¥"+money);
		
		SharedPreferences sp = getSharedPreferences("idcard", 0);
		int is_vip = sp.getInt("is_vip", 0);
		int is_approve = sp.getInt("is_approve", 0);
		if(is_vip!=1 || is_approve!=2) {
			ShellUtil.execute(this, ShellWithdrawTiaojian.class);
			finish();
			return;
		}
		
		getBankList(new Callback<List<bankInfo>>() {
			@Override
			public void onCallback(List<bankInfo> list) {
				WithdrawTixianActivity.this.list = list;
				if(list == null || list.isEmpty()) {
					new AlertDialog.Builder(WithdrawTixianActivity.this)
					.setCancelable(false)
					.setMessage("您还没有设置提现方式，是否现在设置？")
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					})
					.setPositiveButton("设置", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startActivity(new Intent(WithdrawTixianActivity.this,WithdrawalsActivity.class));	
							finish();
						}
					})
					.show();
				} else {
					for(bankInfo info : list) {
						if("1".equals(info.getIs_default())) {
							defInfo = info;
							break;
						}
					}
					if(defInfo == null)
						defInfo = list.get(0);
					
					inject.find(R.id.content).setVisibility(View.VISIBLE);
					inject.find(R.id.bid).setVisibility(View.VISIBLE);
					inject.find(R.id.btnGenhuan).setVisibility(View.VISIBLE);
					inject.find(R.id.btnSumbit).setVisibility(View.VISIBLE);
					
					inject.find(R.id.textSumbit).setVisibility(View.GONE);
					inject.find(R.id.textMsg1).setVisibility(View.GONE);
					inject.find(R.id.textMsg2).setVisibility(View.GONE);
					inject.find(R.id.btnComplete).setVisibility(View.GONE);
					
					inject.find(R.id.title, TextView.class).setText("确认提现");
					// 截取尾号
					String str = defInfo.getCard_num().substring(defInfo.getCard_num().length() - 4, defInfo.getCard_num().length());
					String text = String.format("提现至%s银行（尾号%s）", bankID.get(defInfo.getBank_id()), str);
					inject.find(R.id.bid, TextView.class).setText(text);
					
				}
			}
		});
	}
	
	@Click({
		R.id.btnClose,
		R.id.btnSumbit,
		R.id.btnComplete,
		R.id.btnGenhuan
		})
	void onClick(View v) {
		int id = v.getId();
		switch(id) {
		case R.id.btnClose:
		case R.id.btnComplete:
			finish();
			break;
		case R.id.btnGenhuan:
			new AlertDialog.Builder(this)
			.setAdapter(new ArrayAdapter<bankInfo>(this, android.R.layout.simple_list_item_1, list){
				
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						TextView textV =  (TextView) super.getView(position, convertView, parent);
						bankInfo item = getItem(position);
						// 截取尾号
						String str = item.getCard_num().substring(item.getCard_num().length() - 4, item.getCard_num().length());
						String text = String.format("%s银行（尾号%s）", bankID.get(defInfo.getBank_id()), str);
						textV.setText(text);
						return textV;
					}
				
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						defInfo = list.get(which);
						// 截取尾号
						String str = defInfo.getCard_num().substring(defInfo.getCard_num().length() - 4, defInfo.getCard_num().length());
						String text = String.format("提现至%s银行（尾号%s）", bankID.get(defInfo.getBank_id()), str);
						inject.find(R.id.bid, TextView.class).setText(text);
					}
			})
			.show();
			break;
		case R.id.btnSumbit:
			getCash();
			break;
		}
	}
	
	
	//提现
	void getCash() {
		
		showDialogFragment(false);
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		String url = Constans.getCash;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		
		String cont = String.format("{\"bid\":\"%s\",\"money\":\"%s\"}", defInfo.getBank_id(), money);
		try {
			cont = AesUtils.Encrypt(cont, CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.put("cont", cont);
		
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if (isFinishing())
					return;

				removeDialogFragment();
				if (TextUtils.isEmpty((String) obj)) {
					Toast.makeText(WithdrawTixianActivity.this, "提现失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				JSONObject json = null;
				try {
					json = (JSONObject) new JSONTokener((String) obj).nextValue();
				} catch (JSONException e) {
					// e.printStackTrace();
				}
				
				if (json == null || !"00000".equals(json.optString("code"))) {
					Toast.makeText(WithdrawTixianActivity.this, "提现失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//提现成功
				Toast.makeText(WithdrawTixianActivity.this, json.optString("message"), Toast.LENGTH_SHORT).show();
				
				inject.find(R.id.content).setVisibility(View.VISIBLE);
				inject.find(R.id.bid).setVisibility(View.GONE);
				inject.find(R.id.btnGenhuan).setVisibility(View.GONE);
				inject.find(R.id.btnSumbit).setVisibility(View.GONE);
				
				inject.find(R.id.textSumbit).setVisibility(View.VISIBLE);
				inject.find(R.id.textMsg1).setVisibility(View.VISIBLE);
				inject.find(R.id.textMsg2).setVisibility(View.VISIBLE);
				inject.find(R.id.btnComplete).setVisibility(View.VISIBLE);
				
				inject.find(R.id.title, TextView.class).setText("确认提交");
				
				long milliseconds = System.currentTimeMillis()+(1000*60*60*48);
				Date date= new Date(milliseconds);
				inject.find(R.id.textMsg2, TextView.class).setText("预计到账时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date));
				// 截取尾号
				String str = defInfo.getCard_num().substring(defInfo.getCard_num().length() - 4, defInfo.getCard_num().length());
				String text = String.format("提现至%s银行（尾号%s）", bankID.get(defInfo.getBank_id()), str);
				inject.find(R.id.textMsg2, TextView.class).setText(text);
				
			}
		});
	}
	
	
	//提现方式列表
	public void getBankList(final Callback<List<bankInfo>> callback) {
		showDialogFragment(false);
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		String url = Constans.binkList;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);

		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if (isFinishing())
					return;

				removeDialogFragment();
				if (TextUtils.isEmpty((String) obj)) {
					Toast.makeText(WithdrawTixianActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					finish();
					return;
				}

				JSONObject json = null;
				try {
					json = (JSONObject) new JSONTokener((String) obj).nextValue();
				} catch (JSONException e) {
					// e.printStackTrace();
				}

				if (json == null || (!"00000".equals(json.optString("code")) && TextUtils.isEmpty(json.optString("data")))) {
					Toast.makeText(WithdrawTixianActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					finish();
					return;
				}

				String data = json.optString("data");

				List<bankInfo> list = new Gson().fromJson(data, new TypeToken<List<bankInfo>>() {}.getType());
				callback.onCallback(list);
			}
		});

	}
	
}

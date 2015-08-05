package com.gzqining.inthegarden.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.app.AppActivity;
import com.gzqining.inthegarden.app.ArrayAdapter;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.ItemClick;
import com.gzqining.inthegarden.app.injection.ViewById;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.bankInfo;

@EActivity(layout = R.layout.act_withdrawals, inject = true)
public class WithdrawalsActivity extends AppActivity {

	@ViewById
	private View bottomToolbar;
	
	@ViewById(R.id.bank_list)
	ListView listview = null;
	
	ArrayAdapter<bankInfo> adapter;
	
	int selectorPosition = -1;
	
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
		
		adapter = new ArrayAdapter<bankInfo>(this, 0){
			@Override
			public View getView(int position, View v, ViewGroup parent) {
				if(v == null) 
					v = View.inflate(getContext(), R.layout.item_bank_list, null);
				
				bankInfo item = getItem(position);
				
				TextView nameV = (TextView) v.findViewById(R.id.name_bank_tv);
				TextView defaultV = (TextView) v.findViewById(R.id.default_bank);
				TextView codeV = (TextView) v.findViewById(R.id.bank_end_tv);
				ImageView checkImg = (ImageView) v.findViewById(R.id.gouxuan_bank);
				
				nameV.setText(bankID.get(item.getBank_id()));
				// 截取尾号
				String str = item.getCard_num().substring(item.getCard_num().length() - 4, item.getCard_num().length());
				codeV.setText("尾号" + str+"  储蓄卡");
				defaultV.setVisibility("1".equals(item.getIs_default()) ? View.VISIBLE : View.GONE);
				checkImg.setImageResource(selectorPosition == position ? R.drawable.bank_selected_ic : R.drawable.bank_normal_ic);
				
				return v;
			}
		};
		
		listview.setAdapter(adapter);
		
		getBankList();
	}
	
	@Click({
		R.id.btnBack,
		R.id.btnNext,
		R.id.grade_vip_bt
	})
	void onClick(View v) {
		int id = v.getId();
		if(id == R.id.btnBack) {
			finish();
		} else if(id == R.id.btnNext) {
			Intent intent = new Intent(WithdrawalsActivity.this, BankSetActivity.class);
			startActivity(intent);
		} else if(id == R.id.grade_vip_bt) {
			if(selectorPosition == -1) {
				Toast.makeText(this, "请先选择", Toast.LENGTH_SHORT).show();
				return;
			}
			
			setBinkDefault(adapter.getItem(selectorPosition).getId());
			
		}
	}
	
	@ItemClick(R.id.bank_list)
	void onItemClick(int position) {
		selectorPosition = position;
		adapter.notifyDataSetChanged();
		if(bottomToolbar.getVisibility() != View.VISIBLE)
			bottomToolbar.setVisibility(View.VISIBLE);
	}

	/**
	 * 接口
	 * */
	public void getBankList() {
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
					Toast.makeText(WithdrawalsActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					return;
				}

				JSONObject json = null;
				try {
					json = (JSONObject) new JSONTokener((String) obj).nextValue();
				} catch (JSONException e) {
					// e.printStackTrace();
				}

				if (json == null || (!"00000".equals(json.optString("code")) && TextUtils.isEmpty(json.optString("data")))) {
					Toast.makeText(WithdrawalsActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					return;
				}

				String data = json.optString("data");

				List<bankInfo> list = new Gson().fromJson(data, new TypeToken<List<bankInfo>>() {}.getType());
				if (list != null && !list.isEmpty()) {
					adapter.setNotifyOnChange(false);
					adapter.clear();
					for(int i=0,size=list.size();i<size;i++) {
						bankInfo item = list.get(i);
						if("1".equals(item.getIs_default()))
							selectorPosition = i;
						adapter.add(item);
					}
					adapter.notifyDataSetChanged();
					bottomToolbar.setVisibility(View.VISIBLE);
				}
			}
		});

	}
	
	void setBinkDefault(String bid) {
		showDialogFragment(false);
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		String url = Constans.setBinkDefault;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		
		String cont = "{\"bid\":\""+bid+"\"}";
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
					Toast.makeText(WithdrawalsActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				JSONObject json = null;
				try {
					json = (JSONObject) new JSONTokener((String) obj).nextValue();
				} catch (JSONException e) {
					// e.printStackTrace();
				}

				if (json == null) {
					Toast.makeText(WithdrawalsActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String message = json.optString("message");
				Toast.makeText(WithdrawalsActivity.this, message, Toast.LENGTH_SHORT).show();
				if("00000".equals(json.optString("code"))) {
					
					selectorPosition = -1;
					bottomToolbar.setVisibility(View.GONE);
					adapter.clear();
					getBankList();
				}
				
			}
		});
		
	}

	
}

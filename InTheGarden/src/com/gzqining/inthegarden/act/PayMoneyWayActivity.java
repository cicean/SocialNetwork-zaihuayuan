package com.gzqining.inthegarden.act;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;

public class PayMoneyWayActivity extends BaseActivity implements View.OnClickListener{
	
	private ImageView payWay;
	
	private boolean selector = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_pay_money_way);
		AppManager.getAppManager().addActivity(this);
		
		findViewById(R.id.top_layout_pay_money_back_btn).setOnClickListener(this);
		findViewById(R.id.top_layout_save_payway_btn).setOnClickListener(this);
		findViewById(R.id.remaining_sum_wallet).setOnClickListener(this);
		findViewById(R.id.selected_pay_way).setOnClickListener(this);
		
		SharedPreferences sp = getSharedPreferences("idcard", 0);
		String uid = sp.getString("uid", "");
		
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		selector = sp.getBoolean("PayMoneyWay_selector_"+uid, false);
		payWay = (ImageView) findViewById(R.id.selected_pay_way);
		payWay.setImageResource(selector ? R.drawable.bank_selected_ic : R.drawable.bank_normal_ic);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.top_layout_pay_money_back_btn:
			finish();
			break;
		case R.id.top_layout_save_payway_btn:
			SharedPreferences sp = getSharedPreferences("idcard", 0);
			String uid = sp.getString("uid", "");
			
			sp = PreferenceManager.getDefaultSharedPreferences(this);
			sp.edit().putBoolean("PayMoneyWay_selector_"+uid, selector).commit();
			finish();
			break;
		case R.id.remaining_sum_wallet:
		case R.id.selected_pay_way:
			selector = !selector;
			payWay.setImageResource(selector ? R.drawable.bank_selected_ic : R.drawable.bank_normal_ic);
			break;
		}
	}
}

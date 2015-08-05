package com.gzqining.inthegarden.act;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;

public class VipActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout see_vip_privilege;
	private RelativeLayout upgrade_vip;
	// private RelativeLayout recommend_vip;
	// private RelativeLayout daizhifu_vip;
	private ImageButton back_bt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vip);
		AppManager.getAppManager().addActivity(this);
		initView();
		updateViews();
	}

	@Override
	protected void onResume() {
		updateViews();
		super.onResume();
	}

	public void initView() {
		see_vip_privilege = (RelativeLayout) findViewById(R.id.see_vip_privilege);
		upgrade_vip = (RelativeLayout) findViewById(R.id.upgrade_vip);
		// recommend_vip = (RelativeLayout) findViewById(R.id.recommend_vip);
		// daizhifu_vip = (RelativeLayout) findViewById(R.id.daizhifu_vip);
		back_bt = (ImageButton) findViewById(R.id.top_layout_vip_back_btn);
		see_vip_privilege.setOnClickListener(this);
		upgrade_vip.setOnClickListener(this);
		// recommend_vip.setOnClickListener(this);
		// daizhifu_vip.setOnClickListener(this);
		back_bt.setOnClickListener(this);
	}

	private void updateViews() {
		ImageView imageView = (ImageView) findViewById(R.id.imageView_vip);
		SharedPreferences preferences;
		preferences = getSharedPreferences("idcard", 0);
		int vip = preferences.getInt("is_vip", 0);
		if (vip == 1) {
			imageView.setImageResource(R.drawable.vip);
		} else {
			imageView.setImageResource(R.drawable.vip_no);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.see_vip_privilege:
			intent = new Intent(VipActivity.this, VipPrivilegeActivity.class);
			startActivity(intent);
			break;
		case R.id.upgrade_vip:
			intent = new Intent(VipActivity.this, VipUpgradeActivity.class);
			startActivity(intent);
			break;
		// case R.id.recommend_vip:
		//
		// break;
		// case R.id.daizhifu_vip:
		//
		// break;
		case R.id.top_layout_vip_back_btn:
			finish();
			break;
		}
	}
}

package com.gzqining.inthegarden.act;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;

public class VipPrivilegeActivity extends BaseActivity {
	private ImageButton back_bt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vip_privilege);
		AppManager.getAppManager().addActivity(this);
		back_bt = (ImageButton) findViewById(R.id.top_layout_vip_privilege_back_btn);
		back_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}

package com.gzqining.inthegarden.act;

import com.gzqining.inthegarden.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class UserRuleActivity extends Activity {
	private ImageButton back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_user_rule);
		AppManager.getAppManager().addActivity(this);
		back = (ImageButton) findViewById(R.id.top_layout_user_rule_back_btn);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}

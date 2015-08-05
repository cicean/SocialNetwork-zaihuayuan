package com.gzqining.inthegarden.act;

import com.gzqining.inthegarden.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class AboutIntheGardenActivity extends Activity {
	private RelativeLayout rule;
	private RelativeLayout update;
	private ImageButton back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_about_inthegarden);
		AppManager.getAppManager().addActivity(this);
		
		rule = (RelativeLayout) findViewById(R.id.rule);
		update = (RelativeLayout) findViewById(R.id.update);
		back = (ImageButton) findViewById(R.id.top_layout_about_inthegarden_back_btn);
		
		
		
		rule.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(AboutIntheGardenActivity.this, UserRuleActivity.class);
				startActivity(intent);
			}
		});
		
		update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Intent intent = new Intent(AboutIntheGardenActivity.this,SystemSetupActivity.class);
				startActivity(intent);*/
				finish();
			}
		});
	}
}

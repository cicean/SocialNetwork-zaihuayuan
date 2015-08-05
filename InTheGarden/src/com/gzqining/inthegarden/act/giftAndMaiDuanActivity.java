package com.gzqining.inthegarden.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;

public class giftAndMaiDuanActivity extends BaseActivity {
	View gift_bt;
	View maiduan_bt;
	ImageButton back;
	String fid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_news_right);
		AppManager.getAppManager().addActivity(this);
		fid = getIntent().getData().getQueryParameter("targetId");
		gift_bt = findViewById(R.id.rl_giftlist);
		maiduan_bt = findViewById(R.id.rl_maiduan);
		back = (ImageButton) findViewById(R.id.top_layout_rightchoise_back_btn);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		gift_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(giftAndMaiDuanActivity.this, giftListActivity.class);
				intent.putExtra("fid", fid);
				startActivity(intent);
			}
		});

		maiduan_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(giftAndMaiDuanActivity.this, maiDuanActivity.class);
				intent.putExtra("fid", fid);
				startActivity(intent);
			}
		});
	}
}

package com.gzqining.inthegarden.act;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class RelationOfMineActivity extends BaseActivity implements OnClickListener{
	
	private ImageButton back_btn;
	private RelativeLayout fans;
	private RelativeLayout attention;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_relation_mine);
		AppManager.getAppManager().addActivity(this);
		initView();
		initEvents();
	}
	
	/**
	 * 初始化组件
	 */
	private void initView() {
		back_btn = (ImageButton)findViewById(R.id.top_layout_relation_back_btn);
		fans = (RelativeLayout) findViewById(R.id.rl_my_fans);
		attention = (RelativeLayout) findViewById(R.id.rl_my_attention);
	}
	
	/**
	 * 绑定事件
	 */
	private void initEvents() {

		back_btn.setOnClickListener(this);
		fans.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RelationOfMineActivity.this,MyFansActivity.class);
				startActivity(intent);
			}
		});
		attention.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RelationOfMineActivity.this,MyAttentionActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.top_layout_relation_back_btn:
			
			finish();
				
			break;
			
		default:
			break;
		}
	}

}

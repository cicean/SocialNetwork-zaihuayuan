package com.gzqining.inthegarden.act;


import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.Fragment.EmailViewFragment;
import com.gzqining.inthegarden.act.Fragment.PhoneViewFragment;
import com.gzqining.inthegarden.common.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class RegisterActivity extends BaseActivity {
	private ImageButton back;
	private Button[] mTabs;
	private int index;
	// 当前fragment的index
	private int currentTabIndex;
	private Fragment[] fragments;
	private PhoneViewFragment phontFragment;
	private EmailViewFragment emailFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_register);
		AppManager.getAppManager().addActivity(this);
		initView();
		
		
		
		phontFragment = new PhoneViewFragment();
		emailFragment = new EmailViewFragment();
		fragments = new Fragment[] { phontFragment, emailFragment };
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, phontFragment)
				.add(R.id.fragment_container, emailFragment)
				.hide(emailFragment)
				.show(phontFragment)
				.commit();
		
	}
	
	/**
	 * 初始化组件
	 */
	private void initView() {
		mTabs = new Button[2];
		mTabs[0] = (Button) findViewById(R.id.register_phone_btn);
		mTabs[1] = (Button) findViewById(R.id.register_email_btn);
		back = (ImageButton) findViewById(R.id.top_layout_register_back_btn);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RegisterActivity.this,BeginViewActivity.class);
				startActivity(intent);
			}
		});

	}
	
	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void topClicked(View view) {
		switch (view.getId()) {
		case R.id.register_phone_btn:
			index = 0;
			break;
		case R.id.register_email_btn:
			index = 1;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}
	

}

package com.gzqining.inthegarden.act;

import io.rong.imkit.demo.DemoNotificationCycleSettingActivity;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.dialog.Clearn_News_Dialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class SystemSetupActivity extends Activity {
	private ImageButton back;
	private Button secede;
	private Button cancel;
	
	private Button secede_account_number_btn;
	private RelativeLayout news_setup;
//	private RelativeLayout clean_news;
	private RelativeLayout about_inthegarden;
	private RelativeLayout suggestion;
	private RelativeLayout menu;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_system_setup);
		AppManager.getAppManager().addActivity(this);
		final Clearn_News_Dialog dialog = new Clearn_News_Dialog(SystemSetupActivity.this, R.style.MyDialog);
		back = (ImageButton) findViewById(R.id.top_layout_systemsetup_back_btn);
		secede = (Button) findViewById(R.id.secede_login_btn);
		cancel = (Button) findViewById(R.id.cancel_system_setup);
		secede_account_number_btn = (Button) findViewById(R.id.secede_account_number_btn);
		news_setup = (RelativeLayout) findViewById(R.id.news_setup_rl);
//		clean_news = (RelativeLayout) findViewById(R.id.clean_news_rl);
		about_inthegarden = (RelativeLayout) findViewById(R.id.about_inthegarden_rl);
		suggestion = (RelativeLayout) findViewById(R.id.suggestion_rl);
		menu = (RelativeLayout) findViewById(R.id.menu_system_setup_rl);
		menu.setVisibility(View.INVISIBLE);
		secede_account_number_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences sp = getSharedPreferences("idcard",0);
				SharedPreferences.Editor editor = sp.edit();
				editor.clear().commit();
				Intent intent = new Intent(SystemSetupActivity.this,BeginViewActivity.class);
				startActivity(intent);
				finish();
			}
		});
		//返回主页我的界面
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					finish();
			}
		});
		
		secede.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu.setVisibility(View.VISIBLE);
			}
		});

		news_setup.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SystemSetupActivity.this,DemoNotificationCycleSettingActivity.class);
				startActivity(intent);
//				Intent intent = new Intent(SystemSetupActivity.this,NewsSetupActivity.class);
////				intent.setData(Uri.parse("rong://com.gzqining.inthegarden/conversationsetting/[private|discussion]?targetId={"+3+"}"));
//				startActivity(intent);
			}
		});
//		clean_news.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				dialog.show();
//			}
//		});
		about_inthegarden.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SystemSetupActivity.this,AboutIntheGardenActivity.class);
				startActivity(intent);
			}
		});
		suggestion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SystemSetupActivity.this,SuggestionActivity.class);
				startActivity(intent);
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu.setVisibility(View.INVISIBLE);
			}
		});
	}
	// 监听屏幕是否被点击，隐藏下边菜单栏
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		menu.setVisibility(View.INVISIBLE);
		return false;

	}
}

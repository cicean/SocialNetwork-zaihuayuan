package com.gzqining.inthegarden.act;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.Fragment.Fragment_Img1;
import com.gzqining.inthegarden.act.Fragment.Fragment_Img2;
import com.gzqining.inthegarden.act.Fragment.Fragment_Img3;
import com.gzqining.inthegarden.act.Fragment.Fragment_Img4;
import com.gzqining.inthegarden.app.ShellUtil;
import com.gzqining.inthegarden.app.ui.ShellLoginActivity;
import com.gzqining.inthegarden.app.ui.ShellRegisterActivity;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.TaskUtil;
import com.gzqining.inthegarden.vo.GetTokenVo;

public class BeginViewActivity extends BaseActivity {
	String tag = LoginActivity.class.getName();
	private ViewPager viewPager;
	private FragmentPagerAdapter Adapter;
	private List<Fragment> listDate;
	private ImageView circle_bg1;
	private ImageView circle_bg2;
	private ImageView circle_bg3;
	private ImageView circle_bg4;
	private TextView name_txt;
	private TextView sign_txt;
	private Button register;
	private Button login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.act_begin_view);
		AppManager.getAppManager().addActivity(this);
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String userName = sp.getString("userName", null);
		final String passWord = sp.getString("passWord", null);
		register = (Button) findViewById(R.id.choise_register);
		login = (Button) findViewById(R.id.choise_login);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
					ShellUtil.execute(BeginViewActivity.this, ShellLoginActivity.class, 999);
				

			}
		});

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(BeginViewActivity.this, RegisterActivity.class);
				startActivity(intent);
				finish();*/
				
				ShellUtil.execute(BeginViewActivity.this, ShellRegisterActivity.class, 999);
			}
		});
		initView();

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 999) {
			setResult(RESULT_OK);
			finish();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		name_txt = (TextView) findViewById(R.id.name_txt);
		sign_txt = (TextView) findViewById(R.id.sign_txt);
		circle_bg1 = (ImageView) findViewById(R.id.circle_bg1);
		circle_bg2 = (ImageView) findViewById(R.id.circle_bg2);
		circle_bg3 = (ImageView) findViewById(R.id.circle_bg3);
		circle_bg4 = (ImageView) findViewById(R.id.circle_bg4);

		listDate = new ArrayList<Fragment>();
		Fragment_Img1 fragment_img1 = new Fragment_Img1();
		Fragment_Img2 fragment_img2 = new Fragment_Img2();
		Fragment_Img3 fragment_img3 = new Fragment_Img3();
		Fragment_Img4 fragment_img4 = new Fragment_Img4();
		// �ĸ����ּ����б�
		listDate.add(fragment_img1);
		listDate.add(fragment_img2);
		listDate.add(fragment_img3);
		listDate.add(fragment_img4);
		// ViewPager�൱��һ������� ʵ��ҳ���л�
		Adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return listDate.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return listDate.get(arg0);
			}
		};
		// ����������
		viewPager.setAdapter(Adapter);

		// �����任ͼ��
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					circle_bg1.setImageResource(R.drawable.circle_red);
					circle_bg2.setImageResource(R.drawable.circle_gray);
					circle_bg3.setImageResource(R.drawable.circle_gray);
					circle_bg4.setImageResource(R.drawable.circle_gray);
					name_txt.setText("thivaritthisa");
					sign_txt.setText("迷失在远行的季节~");
					break;
				case 1:
					circle_bg1.setImageResource(R.drawable.circle_gray);
					circle_bg2.setImageResource(R.drawable.circle_red);
					circle_bg3.setImageResource(R.drawable.circle_gray);
					circle_bg4.setImageResource(R.drawable.circle_gray);
					name_txt.setText("rosiehw");
					sign_txt.setText("迷失在远行的季节~");
					break;
				case 2:
					circle_bg1.setImageResource(R.drawable.circle_gray);
					circle_bg2.setImageResource(R.drawable.circle_gray);
					circle_bg3.setImageResource(R.drawable.circle_red);
					circle_bg4.setImageResource(R.drawable.circle_gray);
					name_txt.setText("Jane chen");
					sign_txt.setText("迷失在远行的季节~");
					break;
				case 3:
					circle_bg1.setImageResource(R.drawable.circle_gray);
					circle_bg2.setImageResource(R.drawable.circle_gray);
					circle_bg3.setImageResource(R.drawable.circle_gray);
					circle_bg4.setImageResource(R.drawable.circle_red);
					name_txt.setText("Alexa");
					sign_txt.setText("迷失在远行的季节~");
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
        {  
            exitBy2Click();	
        }
		return false;
	} 
    private static Boolean isExit = false;
    private void exitBy2Click(){
    	Timer tExit = null;
    	if(isExit==false){
    		isExit = true;
    		Toast.makeText(this,"再按一次退出程序" , Toast.LENGTH_SHORT).show();
    		tExit = new Timer();
    		tExit.schedule(new TimerTask() {
				  @Override
				public void run() {
				       isExit = false;
					
				}
			},2000);   //2秒
    		
    	 }else{
    		 AppManager.getAppManager().AppExit(this);  
    	 }
   }

//	/**
//	 * 接口
//	 * */
//	public void getToken() {
//		Logger.d(tag, "ready");
//		this.task = new TaskUtil(this);
//		String url = Constans.getToken;
//
//		AjaxParams params = new AjaxParams();
//		SharedPreferences preferences;
//		preferences = getSharedPreferences("idcard", 0);
//		params.put("oauth_token", preferences.getString("oauth_token", null));
//		params.put("oauth_token_secret", preferences.getString("oauth_token_secret", null));
//		this.task.post(params, url);
//
//		this.task.setType(Constans.REQ_getToken);
//	}
//
//	/**
//	 * 接口返回
//	 * */
//	@Override
//	public void taskCallBack(String jsonObject, int type) {
//		Logger.d(tag, "type:" + type + ",jsonObject:" + jsonObject);
//		if (type == Constans.REQ_getToken) {
//			this.canelDialog();
//		}
//		switch (type) {
//
//		case Constans.REQ_getToken:
//			if (TextUtils.isEmpty(jsonObject)) {
//				Toast.makeText(BeginViewActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
//				return;
//			}
//
//			GetTokenVo tokenVo = (GetTokenVo) ReqJsonUtil.changeToObject(jsonObject, GetTokenVo.class);
//			if (tokenVo.getCode() != 200) {
//				Toast.makeText(BeginViewActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			
//			if (tokenVo.getCode() == 200) {
//
//				// 连接融云服务器。
//				try {
//					RongIM.connect(tokenVo.getToken(), new RongIMClient.ConnectCallback() {
//
//						@Override
//						public void onSuccess(String s) {
//							// 此处处理连接成功。
//							Log.d("Connect:", "Login successfully.");
//							Intent intent = new Intent(BeginViewActivity.this, MainActivity.class);
//							startActivity(intent);
//							finish();
//						}
//
//						@Override
//						public void onError(ErrorCode errorCode) {
//							// 此处处理连接错误。
//							Log.d("Connect:", "Login failed.");
//							Toast.makeText(BeginViewActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//						}
//					});
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			} else {
//
//				Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
//			}
//
//			break;
//		}
//	}
}

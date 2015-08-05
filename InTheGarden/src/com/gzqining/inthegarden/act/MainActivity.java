package com.gzqining.inthegarden.act;




import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalBitmap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.Fragment.FindViewFragment;
import com.gzqining.inthegarden.act.Fragment.MessageViewFragment;
import com.gzqining.inthegarden.act.Fragment.MineViewFragment;
import com.gzqining.inthegarden.act.Fragment.WorldViewFragment;
import com.gzqining.inthegarden.adapter.FindListAdapter;
import com.gzqining.inthegarden.adapter.WorldListAdapter;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.FindBack;
import com.gzqining.inthegarden.vo.UserInfoBack;
import com.gzqining.inthegarden.vo.WorldBack;

public class MainActivity extends BaseActivity {
	String tag = MainActivity.class.getName();
	private Button[] mTabs;
	private int index;
	// 当前fragment的index
	private int currentTabIndex;
	private Fragment[] fragments;
	private FindViewFragment findViewFragment;
//	private LinearLayout find_add_ll;
	private MessageViewFragment messageViewFragment;
	private WorldViewFragment worldViewFragment;
	private MineViewFragment mineViewFragment;
	
	FinalBitmap fb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.act_activity_main);
		AppManager.getAppManager().addActivity(this);
		initView();
		fb = FinalBitmap.create(this);
		findViewFragment = new FindViewFragment();
		messageViewFragment = new MessageViewFragment();
		worldViewFragment = new WorldViewFragment();
		mineViewFragment = new MineViewFragment();
		fragments = new Fragment[] { findViewFragment, messageViewFragment, worldViewFragment, mineViewFragment };
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, findViewFragment)
				.add(R.id.fragment_container, messageViewFragment)
				.add(R.id.fragment_container, worldViewFragment)
				.add(R.id.fragment_container, mineViewFragment)
				.hide(messageViewFragment)
				.hide(worldViewFragment)
				.hide(mineViewFragment)
				.show(findViewFragment)
				.commit();
		
	}
	
	/**
	 * 初始化组件
	 */
	 private void initView() {
		mTabs = new Button[4];
		mTabs[0] = (Button) findViewById(R.id.btn_find);
		mTabs[1] = (Button) findViewById(R.id.btn_message);
		mTabs[2] = (Button) findViewById(R.id.btn_world);
		mTabs[3] = (Button) findViewById(R.id.btn_mine);
//		find_add_ll = (LinearLayout) getFragmentManager().findFragmentById(0).getView().findViewById(R.id.find_add_ll);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);

	}
	
	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_find:
			index = 0;
			break;
		case R.id.btn_message:
			index = 1;
			break;
		case R.id.btn_world:
			index = 2;
			break;
		case R.id.btn_mine:
			index = 3;
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
	
	@Override
	public void onBackPressed(){
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
    
    /**
	 * 接口回调
	 * */
	public void taskCallBack(String jsonObject, int type) {
		Log.d(tag, "type:" + type + ",jsonObject:" + jsonObject);
		System.out.println(tag + "   " + type + ":" + jsonObject);
		this.canelDialog();
		switch (type) {
		case Constans.REQ_Looking_User:
			FindBack findBack = (FindBack) ReqJsonUtil.changeToObject(jsonObject, FindBack.class);
			if(findBack.getCode().equals("00000")){
				if(findBack.getData() != null){
					findViewFragment.page = findBack.getNowPage();
					findViewFragment.adapter = new FindListAdapter(this, findBack.getData());
			        
					findViewFragment.mAdapterView.setAdapter(findViewFragment.adapter);
					findViewFragment.adapter.notifyDataSetChanged();//在修改适配器绑定的数组后，不用重新刷新Activity，通知Activity更新ListView
					
					
				} else{
					findViewFragment.no_users.setVisibility(View.VISIBLE);
					findViewFragment.mAdapterView.setVisibility(View.GONE);
				}
			}else{
				Toast.makeText(this, findBack.getMessage(),Toast.LENGTH_LONG).show();
			}
			
			break;
			
		case Constans.REQ_World:
			WorldBack back = (WorldBack) ReqJsonUtil.changeToObject(jsonObject, WorldBack.class);
			if (back.getCode().equals("00000")) {
				if(back.getData() != null){
					worldViewFragment.page = back.getNowPage()+1;
					worldViewFragment.adapter = new WorldListAdapter(this,back.getData());
					worldViewFragment.mAdapterView.setAdapter(worldViewFragment.adapter);
					worldViewFragment.adapter.notifyDataSetChanged();//在修改适配器绑定的数组后，不用重新刷新Activity，通知Activity更新ListView
					} else{
						worldViewFragment.no_world.setVisibility(View.VISIBLE);
						worldViewFragment.mAdapterView.setVisibility(View.GONE);
					}
			}else {
					Toast.makeText(this, back.getMessage(),Toast.LENGTH_LONG).show();
			}	
			break;
			
		case Constans.REQ_UserInfo:
			UserInfoBack uib = (UserInfoBack) ReqJsonUtil.changeToObject(jsonObject, UserInfoBack.class);

			fb.display(mineViewFragment.head_mine, uib.getAvatar_original() + "#" + new Date().getTime());
			break;
		}
	}
	
	
 // 监听屏幕是否被点击，隐藏下边菜单栏
// 		public boolean onTouchEvent(MotionEvent event) {
// 			find_add_ll.setVisibility(View.GONE);
// 			return false;
//
// 		}
}


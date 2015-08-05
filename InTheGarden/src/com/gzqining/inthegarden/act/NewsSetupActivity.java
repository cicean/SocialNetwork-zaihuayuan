package com.gzqining.inthegarden.act;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;

import android.app.Activity;  
import android.os.Bundle;  
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMChatManager;
  
public class NewsSetupActivity extends Activity {  
	
//	private ImageView sound_on;
//	private ImageView vibrate_on;
//	private ImageView sound_off;
//	private ImageView vibrate_off;
//	private FrameLayout fl_switch_sound;
//	private FrameLayout fl_switch_vibrate;
	private ImageButton back;
	
	private EMChatOptions chatOptions;
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.act_news_setup); 
        AppManager.getAppManager().addActivity(this);
        
//        back = (ImageButton) findViewById(R.id.top_layout_newssetup_back_btn);
//        sound_on = (ImageView) findViewById(R.id.iv_switch_open_notification_sound);
//        sound_off = (ImageView) findViewById(R.id.iv_switch_close_notification_sound);
//        vibrate_on = (ImageView) findViewById(R.id.iv_switch_open_notification_vibrate);
//        vibrate_off = (ImageView) findViewById(R.id.iv_switch_close_notification_vibrate);
//        fl_switch_sound = (FrameLayout) findViewById(R.id.fl_switch_notification_sound);
//		fl_switch_vibrate = (FrameLayout) findViewById(R.id.fl_switch_notification_vibrate);
		
//		chatOptions = EMChatManager.getInstance().getChatOptions();
//		if (chatOptions.getNoticedBySound()) {
//			sound_on.setVisibility(View.VISIBLE);
//			sound_off.setVisibility(View.INVISIBLE);
//		} else {
//			sound_on.setVisibility(View.INVISIBLE);
//			sound_off.setVisibility(View.VISIBLE);
//		}
//		if (chatOptions.getNoticedByVibrate()) {
//			vibrate_on.setVisibility(View.VISIBLE);
//			vibrate_off.setVisibility(View.INVISIBLE);
//		} else {
//			vibrate_on.setVisibility(View.INVISIBLE);
//			vibrate_off.setVisibility(View.VISIBLE);
//		}
//		
//		
//		back.setOnClickListener(this);
//		fl_switch_sound.setOnClickListener(this);
//		fl_switch_vibrate.setOnClickListener(this);
//    }
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.fl_switch_notification_sound:
//			if (sound_on.getVisibility() == View.VISIBLE) {
//				sound_on.setVisibility(View.INVISIBLE);
//				sound_off.setVisibility(View.VISIBLE);
//			} else {
//				sound_on.setVisibility(View.VISIBLE);
//				sound_off.setVisibility(View.INVISIBLE);
//			}
//			break;
//		case R.id.fl_switch_notification_vibrate:
//			if (vibrate_on.getVisibility() == View.VISIBLE) {
//				vibrate_on.setVisibility(View.INVISIBLE);
//				vibrate_off.setVisibility(View.VISIBLE);
//			} else {
//				vibrate_on.setVisibility(View.VISIBLE);
//				vibrate_off.setVisibility(View.INVISIBLE);
//			}
//			break;
//		case R.id.top_layout_newssetup_back_btn:
//			
//			finish();
//				
//			break;
//			
//		default:
//			break;
//		}
	}  
  
} 

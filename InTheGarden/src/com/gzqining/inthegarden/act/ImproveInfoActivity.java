package com.gzqining.inthegarden.act;

import java.io.File;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.Picture_Deel;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.editUserDataBean;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ImproveInfoActivity extends Picture_Deel {
	String tag = LoginActivity.class.getName();
	private ImageButton back;
	private Button next;
	private ImageView camera;
	private EditText nickName;
	private Bitmap mybitmap;
	Object mineObj;
	FinalBitmap fb;
	static Handler mineHandler;
	String encodesstr = "";
	String headImg;//图片url
	String tp;//图片的base64
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_improve_info);
		AppManager.getAppManager().addActivity(this);
		
		next = (Button) findViewById(R.id.next_btn);
		back = (ImageButton) findViewById(R.id.top_layout_improveinfo_back_btn);
		camera = (ImageView) findViewById(R.id.camera_img_improveinfo);
		head_img = (RelativeLayout) findViewById(R.id.rl_head_img);
		nickName = (EditText) findViewById(R.id.nickname);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ImproveInfoActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MineHttpPost();
//				Intent intent = new Intent(ImproveInfoActivity.this,SpecificInfoActivity.class);
//				startActivity(intent);
			}
		});
		
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//调用系统相机
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(),
								"xiaoma.jpg")));
				startActivityForResult(intent, 2);
			}
		});
	}
	public void MineHttpPost() {
		SharedPreferences sp = this.getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
//		tp = Picture_Deel.
		editUserDataBean bean = new editUserDataBean();
		bean.setHead(tp);
		bean.setNickname(nickName.getText().toString());
		String jsonstr = JsonHelper.toJsonString(bean);
		
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = Constans.editUserData;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
				params.put("cont", encodesstr);
				
				FinalHttp http = new FinalHttp();
				mineObj =  http.postSync(url, params);
				mineHandler.sendEmptyMessage(1);
				
			}
		}).start();

		mineHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					CommonBack cb = (CommonBack) ReqJsonUtil.changeToObject(mineObj.toString(), CommonBack.class);
					if(cb.getCode()==00000)	{
						Toast.makeText(ImproveInfoActivity.this, cb.getMessage(),Toast.LENGTH_LONG).show();
						Intent intent = new Intent(ImproveInfoActivity.this,SpecificInfoActivity.class);
						startActivity(intent);
						finish();
					}else{
						Toast.makeText(ImproveInfoActivity.this, cb.getMessage(),Toast.LENGTH_LONG).show();
					}
				}
			}
		};
		
	}
	
}

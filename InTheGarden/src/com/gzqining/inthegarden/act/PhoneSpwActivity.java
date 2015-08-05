package com.gzqining.inthegarden.act;


import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.TaskUtil;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.PhoneBean;
import com.gzqining.inthegarden.vo.phoneCordBean;
import com.gzqining.inthegarden.vo.pictureCodeBack;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class PhoneSpwActivity extends BaseActivity {
	String tag = LoginActivity.class.getName();
	FinalBitmap fb;
	Object obj;
	static Handler handler;
	static String str;
	String sid;
	private Button confirm;//确认
	private Button cord_phone_bt;//获取手机验证码
	private EditText IDCord_spw;//手机验证码输入框
	private ImageButton back;
	private EditText phoneNumber;
	private EditText picture_cord;//验证码
	private ImageView cord_img;//验证码输入框
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_phonespw);
		AppManager.getAppManager().addActivity(this);
		fb=FinalBitmap.create(this);
		confirm = (Button) findViewById(R.id.confirm_phonespw);
		back = (ImageButton) findViewById(R.id.top_layout_phonespw_back_btn);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber_phone);
		cord_phone_bt = (Button) findViewById(R.id.get_idCord_spw);
		IDCord_spw = (EditText) findViewById(R.id.IDCord_spw);
		picture_cord = (EditText) findViewById(R.id.picture_cord);
		cord_img = (ImageView) findViewById(R.id.cord_img_phone);
		getPicture();
		cord_phone_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPhoneCord();
			}
		});
		cord_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				getPicture();
			}
		});
		//登录按钮
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(PhoneSpwActivity.this,ResetPWActivity.class);
//				startActivity(intent);
				PhoneSpw();
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	//获取图片验证码
	public void getPicture(){
//				data = new Date().getTime();
//				String url = Constans.get_PictureCode+"#"+data;
//				fb.display(image_cord, url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String jsonstr = null;
				String url = Constans.get_PictureCode;
				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, null);
				handler.sendEmptyMessage(1);
				
			}
		}).start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					try {
						pictureCodeBack back = (pictureCodeBack) ReqJsonUtil
								.changeToObject(obj.toString(), pictureCodeBack.class);
						System.out.println(back);
						if (back.getCode().equals("00000") ) {
							
							Bitmap bt = stringtoBitmap(back.getImg());
							sid = back.getSid();
							cord_img.setImageBitmap(bt);
//									canelDialog();
//									Toast.makeText(getActivity(), back.getMessage(),
//											Toast.LENGTH_LONG).show();
						} else {
							canelDialog();
							Toast.makeText(PhoneSpwActivity.this, back.getMessage(),
									Toast.LENGTH_LONG).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
	}
	//获取手机验证码
	public void getPhoneCord(){
		String phone = phoneNumber.getText().toString();
		String code = picture_cord.getText().toString();
		final phoneCordBean bean = new phoneCordBean();
		
		  bean.setPhone(phone); 
		  bean.setCode(code);
		  bean.setType("0");
		new Thread(new Runnable() {
			@Override
			public void run() {
				String jsonstr = null;
				String url = Constans.get_PhoneCode;
				String json = JsonHelper.toJsonString(bean);
				try {
					jsonstr = AesUtils.Encrypt(json, CommonConstants.AES_KEY);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AjaxParams params = new AjaxParams();
				params.put("cont", jsonstr);
				params.put("sid", sid);
				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, params);
				handler.sendEmptyMessage(1);
				
			}
		}).start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					try {
						CommonBack back = (CommonBack) ReqJsonUtil
								.changeToObject(obj.toString(), CommonBack.class);
						System.out.println(back);
						if (back.getCode() == 00000) {
							canelDialog();
							Toast.makeText(PhoneSpwActivity.this, back.getMessage(),
									Toast.LENGTH_LONG).show();
						} else {
							canelDialog();
							Toast.makeText(PhoneSpwActivity.this, back.getMessage(),
									Toast.LENGTH_LONG).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
	}
	public void PhoneSpw() {
		this.showDialog();
		this.task = new TaskUtil(this);
		String url = Constans.Phone_PwRetrieve;
		PhoneBean bean = new PhoneBean();
		bean.setPhone(phoneNumber.getText().toString());
		bean.setCode(IDCord_spw.getText().toString());
		String jsonstr = JsonHelper.toJsonString(bean);
		String encodesstr = "";
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			//System.out.println(encodesstr);
			Logger.d("encodesstr", encodesstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AjaxParams params = new AjaxParams();
		params.put("cont", encodesstr);
		params.put("sid", sid);
		this.task.post(params, url);
		this.task.setType(Constans.REQ_Phone_PwRetrieve);
	}
	
	/**
	 * 接口返回
	 * */
	@Override
	public void taskCallBack(String jsonObject, int type) {
		Logger.d(tag, "type:" + type + ",jsonObject:" + jsonObject);
		this.canelDialog();
		switch (type) {
		case Constans.REQ_Phone_PwRetrieve:
			//Toast.makeText(this, back.Getoauth_token(), Toast.LENGTH_LONG).show();
			CommonBack back = (CommonBack) ReqJsonUtil.changeToObject(jsonObject, CommonBack.class);
			if(back.getCode()==00000){
				Toast.makeText(this, back.getMessage(), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(PhoneSpwActivity.this,ResetPWActivity.class);
				intent.putExtra("sid", sid);
				startActivity(intent);
			}else{
				Toast.makeText(this, back.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			break;
		}
	}
	public Bitmap stringtoBitmap(String string){
	    //将字符串转换成Bitmap类型
	    Bitmap bitmap=null;
	    try {
	    byte[]bitmapArray;
	    bitmapArray=Base64.decode(string, Base64.DEFAULT);
	    bitmap=BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	   
	    return bitmap;
    }
}

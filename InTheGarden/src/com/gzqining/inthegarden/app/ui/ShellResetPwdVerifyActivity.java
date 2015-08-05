package com.gzqining.inthegarden.app.ui;

import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.app.AppUtil;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.ShellActivity;
import com.gzqining.inthegarden.app.ShellUtil;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.Extra;
import com.gzqining.inthegarden.app.injection.ViewById;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.PhoneBean;
import com.gzqining.inthegarden.vo.phoneCordBean;
import com.gzqining.inthegarden.vo.pictureCodeBack;

@EActivity(layout = R.layout.ui_activity_reset_pwd_verify, inject = true)
public class ShellResetPwdVerifyActivity extends ShellActivity{
	
public static final String EXTRA_PHONE = "phone";
	
	@Extra(EXTRA_PHONE)
	private String phone;
	
	@ViewById
	private EditText editImgVerify;
	@ViewById
	private EditText editPhoneVerify;
	@ViewById
	private ImageView imageCode;

	private String sid;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getPicture();
	}

	@Click({ 
		R.id.btnBack, 
		R.id.btnNext, 
		R.id.btnGetVerify ,
		R.id.imageCode
		})
	void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btnBack) {
			finish();
		} else if (id == R.id.btnNext) {
			String phoneVerify = editPhoneVerify.getText().toString();
			if(TextUtils.isEmpty(phoneVerify)) {
				Toast.makeText(getActivity(), "请输入手机验证码", Toast.LENGTH_SHORT).show();
				return;
			}
			
			phoneSpw(phoneVerify, phone);
		} else if (id == R.id.btnGetVerify) {
			String imgCode = editImgVerify.getText().toString();
			if(TextUtils.isEmpty(imgCode)) {
				Toast.makeText(getActivity(), "请输入图片验证码", Toast.LENGTH_SHORT).show();
				return;
			}
			
			getPhoneCord(imgCode, phone);
			
		} else if(id == R.id.imageCode) {
			getPicture();
		}
	}

	// 获取图片验证码
	private void getPicture() {
		showDialogFragment(false);
		String url = Constans.get_PictureCode;
		HttpRespose http = new HttpRespose(url, null);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if(isFinishing())
					return;
				removeDialogFragment();
				if(obj == null || TextUtils.isEmpty((String)obj)) {
					Toast.makeText(getActivity(), "获取图片验证码失败",Toast.LENGTH_SHORT).show();
					return;
				}
				
				pictureCodeBack back = (pictureCodeBack) ReqJsonUtil.changeToObject(obj.toString(), pictureCodeBack.class);
				if (back.getCode().equals("00000") ) {
					
					Bitmap bt = AppUtil.stringtoBitmap(back.getImg());
					sid = back.getSid();
					imageCode.setImageBitmap(bt);
				} else {
					Toast.makeText(getActivity(), back.getMessage(),Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	private void getPhoneCord(String imgCode, String phone) {
		phoneCordBean bean = new phoneCordBean();
		  bean.setPhone(phone); 
		  bean.setCode(imgCode);
		  bean.setType("0");
		
		String jsonEnc = null;
		String json = JsonHelper.toJsonString(bean);
		try {
			jsonEnc = AesUtils.Encrypt(json, CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		AjaxParams params = new AjaxParams();
		params.put("cont", jsonEnc);
		params.put("sid", sid);
		
		String url = Constans.get_PhoneCode;
		showDialogFragment(false);
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if(isFinishing())
					return;
				removeDialogFragment();
				if(obj == null || TextUtils.isEmpty((String)obj)) {
					Toast.makeText(getActivity(), "获取手机验证码失败",Toast.LENGTH_SHORT).show();
					return;
				}
				
				CommonBack back = (CommonBack) ReqJsonUtil.changeToObject((String)obj, CommonBack.class);
				if (back.getCode() == 00000) {
					Toast.makeText(getActivity(), back.getMessage(), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), back.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
			
	}
	
	
	private void phoneSpw(String phoneVerify, String phone) {
		
		String url = Constans.Phone_PwRetrieve;
		PhoneBean bean = new PhoneBean();
		bean.setPhone(phone);
		bean.setCode(phoneVerify);
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
		
		showDialogFragment(false);
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if(isFinishing())
					return;
				removeDialogFragment();
				if(obj == null || TextUtils.isEmpty((String)obj)) {
					Toast.makeText(getActivity(), "操作失败",Toast.LENGTH_SHORT).show();
					return;
				}
				
				CommonBack back = (CommonBack) ReqJsonUtil.changeToObject((String)obj, CommonBack.class);
				if(back.getCode()==00000){
					Toast.makeText(getActivity(), back.getMessage(), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.putExtra(ShellResetPasswordActivity.EXTRA_SID, sid);
					ShellUtil.execute(getActivity(), ShellResetPasswordActivity.class, intent);
				}else{
					Toast.makeText(getActivity(), back.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
}

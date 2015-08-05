package com.gzqining.inthegarden.app.ui;

import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.phoneCordBean;
import com.gzqining.inthegarden.vo.pictureCodeBack;

@EActivity(layout = R.layout.ui_activity_register_verify, inject = true)
public class ShellRegisterVerifyActivity extends ShellActivity {

	
	@Extra
	private String phone;
	@Extra
	String email;
	@Extra
	private String password;
	
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
			if(TextUtils.isEmpty(sid)) {
				Toast.makeText(getActivity(), "图片验证码加载失败，请重试！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			String phoneVerify = editPhoneVerify.getText().toString();
			if(TextUtils.isEmpty(phoneVerify)) {
				Toast.makeText(getActivity(), "请输入手机验证码", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent intent = new Intent();
			intent.putExtra("phone", phone);
			intent.putExtra("email", email);
			intent.putExtra("password", password);
			intent.putExtra("phoneVerify", phoneVerify);
			intent.putExtra("sid", sid);
			ShellUtil.execute(getActivity(), ShellRegisterHeaderActivity.class, intent, 999);
		} else if (id == R.id.btnGetVerify) {
			if(TextUtils.isEmpty(sid)) {
				Toast.makeText(getActivity(), "图片验证码加载失败，请重试！", Toast.LENGTH_SHORT).show();
				return;
			}
			
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 999) {
			setResult(RESULT_OK);
			finish();
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
		  bean.setType("1");
		
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

}

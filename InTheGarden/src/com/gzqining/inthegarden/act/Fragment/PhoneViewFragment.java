package com.gzqining.inthegarden.act.Fragment;



import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.ImproveInfoActivity;
import com.gzqining.inthegarden.act.UserRuleActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.DialogUtil;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.LoginBack;
import com.gzqining.inthegarden.vo.RegisterPhoneBean;
import com.gzqining.inthegarden.vo.phoneCordBean;
import com.gzqining.inthegarden.vo.pictureCodeBack;

	public class PhoneViewFragment extends Fragment implements OnClickListener {
		Dialog dialog;
		Object obj;
		FinalBitmap fb;
		static Handler handler;
		static String str;
		public View v;
		private Button confirm;
		private ImageView check;
		private ImageView checked;
		private ImageView image_cord;//获取图片验证码
		private FrameLayout check_phone_fl;
		private TextView rule_tv;
		private Button get_phone_Cord;//获取手机验证码btn
		private EMChatOptions chatOptions;
		private EditText phoneNumber;//手机号
		private EditText password;//密码
		private EditText IDCord;//图片验证码
		private EditText phone_cord_spw;//手机验证码
		long data;
		String sid;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			v = inflater.inflate(R.layout.fragment_register_phone, container, false);
			fb=FinalBitmap.create(getActivity());
			
			confirm = (Button) v.findViewById(R.id.confirm_phone_register);
			get_phone_Cord = (Button) v.findViewById(R.id.get_phone_Cord);
			checked = (ImageView) v.findViewById(R.id.check_phone_img_selected);
			check = (ImageView) v.findViewById(R.id.check_phone_img_normal);
			image_cord = (ImageView) v.findViewById(R.id.image_cord_register);
			check_phone_fl = (FrameLayout) v.findViewById(R.id.check_phone_fl);
			rule_tv = (TextView) v.findViewById(R.id.check_phone);
			IDCord = (EditText) v.findViewById(R.id.IDCord_register);
			password = (EditText) v.findViewById(R.id.password_register);
			phoneNumber = (EditText) v.findViewById(R.id.phoneNumber);
			phone_cord_spw = (EditText) v.findViewById(R.id.phone_cord_spw);
			getPicture();
			init();
			chatOptions = EMChatManager.getInstance().getChatOptions();
			if (chatOptions.getNoticedBySound()) {
				checked.setVisibility(View.VISIBLE);
				check.setVisibility(View.INVISIBLE);
			} else {
				checked.setVisibility(View.INVISIBLE);
				check.setVisibility(View.VISIBLE);
			}
			init();
			return v;
		}
		
		
		private void init() {
			get_phone_Cord.setOnClickListener(this);
			image_cord.setOnClickListener(this);
			confirm.setOnClickListener(this);
			rule_tv.setOnClickListener(this);
			check_phone_fl.setOnClickListener(this);
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
				switch (v.getId()){
					case R.id.get_phone_Cord:
						getPhoneCord();
						break;
					case R.id.confirm_phone_register:
						registerHttpPost();
//						Intent intent = new Intent();
//						intent.setClass(getActivity(),
//								ImproveInfoActivity.class);
//						startActivity(intent);
						break;
					case R.id.image_cord_register:
						getPicture();
						break;
					case R.id.check_phone:
						Intent intent1 = new Intent();
						intent1.setClass(getActivity(), UserRuleActivity.class);
						startActivity(intent1);			
						break;
					case R.id.check_phone_fl:
						if (check.getVisibility() == View.VISIBLE) {
							check.setVisibility(View.INVISIBLE);
							checked.setVisibility(View.VISIBLE);
						} else {
							check.setVisibility(View.VISIBLE);
							checked.setVisibility(View.INVISIBLE);
						}
						break;
					default:
						break;
				}
		}
		//获取图片验证码
		public void getPicture(){
//			data = new Date().getTime();
//			String url = Constans.get_PictureCode+"#"+data;
//			fb.display(image_cord, url);
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
								image_cord.setImageBitmap(bt);
//								canelDialog();
//								Toast.makeText(getActivity(), back.getMessage(),
//										Toast.LENGTH_LONG).show();
							} else {
								canelDialog();
								Toast.makeText(getActivity(), back.getMessage(),
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
			String code = IDCord.getText().toString();
			final phoneCordBean bean = new phoneCordBean();
			
			  bean.setPhone(phone); 
			  bean.setCode(code);
			  bean.setType("1");
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
								Toast.makeText(getActivity(), back.getMessage(),
										Toast.LENGTH_LONG).show();
							} else {
								canelDialog();
								Toast.makeText(getActivity(), back.getMessage(),
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
		private void registerHttpPost() {
			showDialog();
			String phone = phoneNumber.getText().toString();
			String passwd = password.getText().toString();
//			String code = IDCord.getText().toString();
			String phone_cord = phone_cord_spw.getText().toString();
			final RegisterPhoneBean bean = new RegisterPhoneBean();
			
			  bean.setPhone(phone); 
			  bean.setPasswd(passwd);
			  bean.setrepasswd(passwd);
			  bean.setCode(phone_cord);
			  bean.setHead("");
			new Thread(new Runnable() {
				@Override
				public void run() {
					String jsonstr = null;
					String url = Constans.registPhone;
					String json = JsonHelper.toJsonString(bean);
					/* Log.d("json", json); */
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
							/*JSONObject jsonObject = new JSONObject(obj.toString());
							System.out.println(jsonObject);*/
							LoginBack back = (LoginBack) ReqJsonUtil
									.changeToObject(obj.toString(), LoginBack.class);
							System.out.println(back);
							String oauth_token = back.Getoauth_token();
							String oauth_token_secret = back.Getoauth_token_secret();
							if (back.Getcode() == 0000) {
								canelDialog();
								// 保存授权到本地
								SharedPreferences.Editor editor;
								SharedPreferences preferences = getActivity().getSharedPreferences("idcard",0);
								editor = preferences.edit();
								editor.clear();
								editor.putString("uid", back.Getuid());
								editor.putString("oauth_token", oauth_token);
								editor.putString("oauth_token_secret", oauth_token_secret);
								editor.commit();
//								Toast.makeText(getActivity(), back.Getmessage(),
//										Toast.LENGTH_LONG).show();
								Intent intent = new Intent();
								intent.setClass(getActivity(),
										ImproveInfoActivity.class);
								startActivity(intent);

							} else {
								canelDialog();
								Toast.makeText(getActivity(), back.Getmessage(),
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
		public void showDialog() {
			dialog = DialogUtil.showProgressDialog(getActivity(),
					getString(R.string.request_data), false);
			dialog.show();
		}

		public void showDialogloading(String text) {
			dialog = DialogUtil.showProgressDialog(getActivity(), text, true);
			dialog.show();
		}

		public void canelDialog() {
			if (dialog != null) {
				// dialog.cancel();
				dialog.dismiss();
			}
		}
		
}

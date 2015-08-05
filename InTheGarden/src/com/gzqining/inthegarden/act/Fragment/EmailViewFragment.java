package com.gzqining.inthegarden.act.Fragment;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.gzqining.inthegarden.act.RegisterActivity;
import com.gzqining.inthegarden.act.UserRuleActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.DialogUtil;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.LoginBack;
import com.gzqining.inthegarden.vo.RegisterEmailBean;

public class EmailViewFragment extends Fragment {
	String tag = RegisterActivity.class.getName();
	public View v;
	private Button confirm;
	private ImageView check;
	private ImageView checked;
	private FrameLayout check_email_fl;
	private TextView rule_tv;
	private EditText email_et;
	private EditText passwd_et;
	private EMChatOptions chatOptions;
	Dialog dialog;
	Object obj;
	static Handler handler;
	static String str;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.fragment_register_email, container, false);
		confirm = (Button) v.findViewById(R.id.confirm_email_register);
		check = (ImageView) v.findViewById(R.id.check_email_img_normal);
		checked = (ImageView) v.findViewById(R.id.check_email_img_selected);
		rule_tv = (TextView) v.findViewById(R.id.check_email);
		email_et = (EditText) v.findViewById(R.id.email_et);
		passwd_et = (EditText) v.findViewById(R.id.password_email);
		check_email_fl = (FrameLayout) v.findViewById(R.id.check_email_fl);
		chatOptions = EMChatManager.getInstance().getChatOptions();
		if (chatOptions.getNoticedBySound()) {
			checked.setVisibility(View.VISIBLE);
			check.setVisibility(View.INVISIBLE);
		} else {
			checked.setVisibility(View.INVISIBLE);
			check.setVisibility(View.VISIBLE);
		}
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checked.getVisibility() == View.VISIBLE){
					registerHttpPost();
				}else{
					Toast.makeText(getActivity(), "请阅读用户协议！",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		rule_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), UserRuleActivity.class);
				startActivity(intent);
			}
		});
		check_email_fl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (check.getVisibility() == View.VISIBLE) {
					check.setVisibility(View.INVISIBLE);
					checked.setVisibility(View.VISIBLE);
				} else {
					check.setVisibility(View.VISIBLE);
					checked.setVisibility(View.INVISIBLE);
				}
			}
		});

		return v;

	}

	private void registerHttpPost() {
		showDialog();
		String email = email_et.getText().toString();
		String passwd = passwd_et.getText().toString();
		final RegisterEmailBean bean = new RegisterEmailBean();
		
		  bean.setEmail(email); 
		  bean.setPasswd(passwd);
		  bean.setRepasswd(passwd);

		new Thread(new Runnable() {
			@Override
			public void run() {
				String jsonstr = null;
				String url = Constans.register;
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
				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, params);
				// System.out.println( obj);
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
						String oauth_token = back.Getoauth_token();
						String oauth_token_secret = back.Getoauth_token_secret();
						System.out.println(back);
						if (back.Getcode() == 00000) {
							canelDialog();
//							SharedPreferences.Editor editor;
//							SharedPreferences preferences = getActivity().getSharedPreferences("idcard",0);
//							editor = preferences.edit();
////							editor.clear();
//							editor.putString("uid", back.Getuid());
//							editor.putString("oauth_token", oauth_token);
//							editor.putString("oauth_token_secret", oauth_token_secret);
//							editor.commit();
							Toast.makeText(getActivity(), back.Getmessage(),Toast.LENGTH_LONG).show();
							Intent intent = new Intent(getActivity(),ImproveInfoActivity.class);
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

package com.gzqining.inthegarden.act;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.WheelView.ArrayWheelAdapter;
import com.gzqining.inthegarden.WheelView.WheelView;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.dialog.LostBalanceDialog;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.AlipayUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.Util;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.UserInfoBack;
import com.gzqining.inthegarden.vo.UserInfoBean;
import com.gzqining.inthegarden.vo.giftMessageBean;
import com.gzqining.inthegarden.vo.giftUserIdBack;
import com.gzqining.inthegarden.vo.sendGiftBean;
import com.gzqining.inthegarden.vo.zhiFuBaoBack;
import com.gzqining.inthegarden.vo.zhiFuBaoBean;

public class maiDuanActivity extends BaseActivity implements OnClickListener {
	ImageButton back_btn;
	ImageView head_img_maiduan, gift_maiduan_img;
	TextView nickname, to_who, time_et, gift_name_maiduan, gift_price;
	EditText shiXiang;
	Button confirm_maiduan;
	String category_time[] = new String[21];
	Dialog dialog;
	LayoutInflater myInflater;
	Button right, left;
	TextView title;
	int j = 10;
	FinalBitmap fb;
	int number;
	int price;
	int cont;
	Object obj;
	Handler handler;
	String encodesstr = "";
	CommonBack back;
	String fid;
	String giftid;
	String img;
	String name;
	String txtInfo;
	String str;
	String time;
	String sendInfo;
	String giftUserId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_maiduan);
		AppManager.getAppManager().addActivity(this);
		myInflater = LayoutInflater.from(this);
		fid = this.getIntent().getStringExtra("fid");
		fb = FinalBitmap.create(this);
		specificInfo();
		initView();
		if (this.getIntent().getStringExtra("name") != null) {
			img = this.getIntent().getStringExtra("img");
			name = this.getIntent().getStringExtra("name");
			giftid = this.getIntent().getStringExtra("giftid");
			number = this.getIntent().getIntExtra("number", 1);
			price = this.getIntent().getIntExtra("price", 0);
			fid = this.getIntent().getStringExtra("fid");
			time = this.getIntent().getStringExtra("time");
			sendInfo = this.getIntent().getStringExtra("sendInfo");
			time_et.setText(time);
			shiXiang.setText(sendInfo);
			gift_name_maiduan.setText(name);
			if (img != null) {
				fb.display(gift_maiduan_img, img);
			}
			cont = price * number;
			gift_price.setText("￥" + cont + "");
		}
		for (int i = 0; i <= 20; i++) {
			category_time[i] = j + "天";
			j++;
		}

	}

	private void initView() {

		back_btn = (ImageButton) findViewById(R.id.top_layout_maiduan_back_btn);
		head_img_maiduan = (ImageView) findViewById(R.id.head_img_maiduan);
		nickname = (TextView) findViewById(R.id.nickname_maiduan);
		to_who = (TextView) findViewById(R.id.to_her_or_him);
		time_et = (TextView) findViewById(R.id.time_maiduan_tv);
		shiXiang = (EditText) findViewById(R.id.liyou_maiduan_et);
		gift_maiduan_img = (ImageView) findViewById(R.id.gift_maiduan_img);
		gift_name_maiduan = (TextView) findViewById(R.id.gift_name_maiduan);
		gift_price = (TextView) findViewById(R.id.gift_price);
		confirm_maiduan = (Button) findViewById(R.id.confirm_maiduan);
		back_btn.setOnClickListener(this);
		head_img_maiduan.setOnClickListener(this);
		nickname.setOnClickListener(this);
		to_who.setOnClickListener(this);
		time_et.setOnClickListener(this);
		shiXiang.setOnClickListener(this);
		gift_maiduan_img.setOnClickListener(this);
		gift_name_maiduan.setOnClickListener(this);
		gift_price.setOnClickListener(this);
		confirm_maiduan.setOnClickListener(this);

	}

	private void getLastMoney() {
		final ProgressDialog dialog = Util.showLoading(maiDuanActivity.this);
		SharedPreferences sp = maiDuanActivity.this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		String url = Constans.myScore;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);

		FinalHttp http = new FinalHttp();
		http.post(url, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
				Util.showToastNetWorkError(maiDuanActivity.this);
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				dialog.dismiss();
				try {
					if (Util.isSuccess(t)) {
						JSONObject obj = new JSONObject(t);
						final int score = obj.getJSONObject("credit").getJSONObject("score").getInt("value");
						if (score >= price) {
							new AlertDialog.Builder(maiDuanActivity.this).setTitle("提示").setMessage("从余额中扣除" + (price * number) + "元?").setNegativeButton("是", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									sendGiftPost();
								}
							}).setNeutralButton("否", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).show();

						}
						// 余额不足
						else {
							final LostBalanceDialog dialog = new LostBalanceDialog(maiDuanActivity.this, R.style.MyDialog);
							dialog.show();
							dialog.textViewPirze.setText("￥" + (price - score) + "元");
							dialog.buttonPay.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
									payMoney(price - score);
								}
							});
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 获取订单号
	@SuppressLint("ShowToast")
	public void payMoney(final int prize) {

		zhiFuBaoBean bean = new zhiFuBaoBean();
		bean.setOrderPrice("" + prize);
		String jsonstr = JsonHelper.toJsonString(bean);

		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);

		} catch (Exception e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = Constans.createOrder;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", Util.getOauthToken(maiDuanActivity.this));
				params.put("oauth_token_secret", Util.getOauthTokenSecret(maiDuanActivity.this));
				params.put("cont", encodesstr);
				params.put("orderPrice", prize + "");
				params.put("type", "0");
				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, params);
				handler.sendEmptyMessage(2);

			}
		}).start();
		final ProgressDialog dialog = Util.showLoading(maiDuanActivity.this, "订单生成中...");
		handler = new Handler() {
			@SuppressLint("ShowToast")
			@Override
			public void handleMessage(Message msg) {
				dialog.dismiss();
				if (msg.what == 2) {
					try {
						zhiFuBaoBack back = (zhiFuBaoBack) ReqJsonUtil.changeToObject(obj.toString(), zhiFuBaoBack.class);
						Log.e("anan", obj.toString());
						if (back.getCode().equals("00000")) {
							String dingdanID = back.getOrderId();
							AlipayUtils ga = new AlipayUtils((Activity) maiDuanActivity.this, 0, null, null);
							ga.pay(maiDuanActivity.this, dingdanID, prize);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.top_layout_maiduan_back_btn:
			// intent = new
			// Intent(maiDuanActivity.this,giftAndMaiDuanActivity.class);
			// startActivity(intent);
			finish();
			break;
		case R.id.confirm_maiduan:
			txtInfo = shiXiang.getText().toString();
			if (giftid == null) {
				Toast.makeText(maiDuanActivity.this, "请选择礼物", Toast.LENGTH_SHORT).show();
				return;
			}
			// 截取天数
			if (time_et.getText().toString().length() != 0) {
				str = time_et.getText().toString().substring(0, time_et.getText().toString().length() - 1);
				getLastMoney();
			} else {
				Toast.makeText(maiDuanActivity.this, "请选择买断时间", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.gift_maiduan_img:
			intent = new Intent(maiDuanActivity.this, maiduanGiftListActivity.class);
			intent.putExtra("fid", fid);
			intent.putExtra("time", time_et.getText().toString());
			intent.putExtra("sendInfo", shiXiang.getText().toString());
			startActivity(intent);
			finish();
			break;
		case R.id.time_maiduan_tv:
			v = myInflater.inflate(R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("选择买断时间");
			final WheelView wheelView_sex = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_sex.setAdapter(new ArrayWheelAdapter<String>(category_time));
			wheelView_sex.setVisibleItems(5);
			wheelView_sex.setCyclic(false);
			wheelView_sex.setCurrentItem(0);
			dialog = new Dialog(this, R.style.MyDialog);
			dialog.setContentView(v);
			dialog.show();
			dialogShow();
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					time_et.setText(category_time[wheelView_sex.getCurrentItem()]);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			break;
		default:
			break;
		}
	}

	// 买断接口
	public void sendGiftPost() {
		sendGiftBean bean = new sendGiftBean();
		bean.setFri_ids(fid);
		bean.setGiftCount(number + "");
		bean.setGiftId(giftid);
		bean.setType("1");
		bean.setDays(str);
		bean.setSendInfo(txtInfo);
		String jsonstr = JsonHelper.toJsonString(bean);

		try {
			Log.e("anan", jsonstr);
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);

		} catch (Exception e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = Constans.sendGift;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", Util.getOauthToken(getApplication()));
				params.put("oauth_token_secret", Util.getOauthTokenSecret(getApplication()));
				params.put("cont", encodesstr);
				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, params);
				handler.sendEmptyMessage(1);

			}
		}).start();
		final Dialog dialog = Util.showLoading(this, "数据发送中...");
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					try {
						giftUserIdBack back = (giftUserIdBack) ReqJsonUtil.changeToObject(obj.toString(), giftUserIdBack.class);
						System.out.print(back);
						dialog.dismiss();
						if (back.getCode() == 00000) {
							giftUserId = back.getGiftUserId();
							Toast.makeText(maiDuanActivity.this, back.getMessage(), Toast.LENGTH_LONG);
							giftMessaget();
							Intent intent = new Intent(maiDuanActivity.this, ChatConversationActivity.class);
							startActivity(intent);
						}
						Toast.makeText(maiDuanActivity.this, back.getMessage(), Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	}

	// 推消息接口
	@SuppressLint("ShowToast")
	public void giftMessaget() {

		giftMessageBean bean = new giftMessageBean();
		bean.setGiftUserId(giftUserId);
		bean.setPushData("0");
		String jsonstr = JsonHelper.toJsonString(bean);

		try {
			Logger.e("anan", jsonstr);
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);

		} catch (Exception e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = Constans.giftMessage;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", Util.getOauthToken(getApplication()));
				params.put("oauth_token_secret", Util.getOauthTokenSecret(getApplication()));
				params.put("cont", encodesstr);
				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, params);
				handler.sendEmptyMessage(4);

			}
		}).start();

		handler = new Handler() {
			@SuppressLint("ShowToast")
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 4) {
					try {
						giftUserIdBack back = (giftUserIdBack) ReqJsonUtil.changeToObject(obj.toString(), giftUserIdBack.class);
						System.out.print(back);
						if (back.getCode() == 00000) {

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	}

	/**
	 * 获取用户头像接口
	 * */
	public void specificInfo() {
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		final String ID = sp.getString("uid", null);
		new Thread(new Runnable() {

			@Override
			public void run() {

				String url = Constans.userinfo;
				UserInfoBean bean = new UserInfoBean();
				bean.setUid(fid);
				String jsonstr = JsonHelper.toJsonString(bean);
				String encodesstr = "";
				try {
					encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
					// System.out.println(encodesstr);
					Logger.d("encodesstr", encodesstr);
				} catch (Exception e) {
					e.printStackTrace();
				}
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
				params.put("cont", encodesstr);
				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, params);
				handler.sendEmptyMessage(2);
			}

		}).start();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 2) {
					UserInfoBack back = (UserInfoBack) ReqJsonUtil.changeToObject(obj.toString(), UserInfoBack.class);
					if (back.getCode().equals("00000")) {
						fb.display(head_img_maiduan, back.getAvatar_original());
						nickname.setText(back.getNickname());
					} else {

					}
				}
			}
		};
	}

	public void dialogShow() {
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (display.getWidth()); // 设置宽度
		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public static void hideSoftInputFromWindow(Activity activity) {
		InputMethodManager manager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (manager != null) {
			View view = activity.getCurrentFocus();
			if (view == null) {
				return;
			}
			IBinder token = view.getWindowToken();
			if (token != null) {
				manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
}

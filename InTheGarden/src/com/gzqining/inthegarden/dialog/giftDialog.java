package com.gzqining.inthegarden.dialog;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.ChatConversationActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.AlipayUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.Util;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.giftMessageBean;
import com.gzqining.inthegarden.vo.giftUserIdBack;
import com.gzqining.inthegarden.vo.sendGiftBean;
import com.gzqining.inthegarden.vo.zhiFuBaoBack;
import com.gzqining.inthegarden.vo.zhiFuBaoBean;

public class giftDialog extends Dialog implements android.view.View.OnClickListener {
	Context context;
	private Button paymoney_bt, reduce_dialog, add_dialog;
	private ImageView cancel_dialog, dialog_gift_img;
	private TextView gift_number_dialog, dialog_price_tv;
	private LinearLayout dialog_gift_by_ll;
	int number = 1;
	int price;
	int cont;
	int scoreValue;
	Object obj;
	Handler handler;
	String encodesstr = "";
	CommonBack back;
	String oauth_token;
	String oauth_token_secret;
	String fid;
	String giftid;
	String img;
	FinalBitmap fb;
	String dingdanID;
	String giftUserId;

	public giftDialog(Context context) {
		super(context);
		this.context = context;
	}

	public giftDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public giftDialog(Context context, int theme, int price, String oauth_token, String oauth_token_secret, String fid, String giftid, String img) {
		super(context, theme);
		this.context = context;
		this.price = price;
		this.oauth_token = oauth_token;
		this.oauth_token_secret = oauth_token_secret;
		this.fid = fid;
		this.giftid = giftid;
		this.img = img;
		fb = FinalBitmap.create(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_gift_buy);
		balanceMoney();
		dialog_gift_by_ll = (LinearLayout) findViewById(R.id.dialog_gift_by_ll);
		paymoney_bt = (Button) findViewById(R.id.paymoney_dialog_buy);
		cancel_dialog = (ImageView) findViewById(R.id.gift_dialog_cancel);
		dialog_gift_img = (ImageView) findViewById(R.id.dialog_gift_img);
		reduce_dialog = (Button) findViewById(R.id.reduce_dialog);
		gift_number_dialog = (TextView) findViewById(R.id.gift_number_dialog);
		add_dialog = (Button) findViewById(R.id.add_dialog);
		dialog_price_tv = (TextView) findViewById(R.id.dialog_price_tv);
		fb.display(dialog_gift_img, img);
		paymoney_bt.setOnClickListener(this);
		cancel_dialog.setOnClickListener(this);
		reduce_dialog.setOnClickListener(this);
		gift_number_dialog.setOnClickListener(this);
		add_dialog.setOnClickListener(this);
		dialog_price_tv.setOnClickListener(this);
		dialog_price_tv.setText(price + "");
		//
	}

	private void getLastMoney() {
		final ProgressDialog dialog = Util.showLoading(context);
		SharedPreferences sp = context.getSharedPreferences("idcard", 0);
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
				Util.showToastNetWorkError(context);
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
							new AlertDialog.Builder(context).setTitle("提示").setMessage("从余额中扣除" + (price * number) + "元?").setNegativeButton("是", new DialogInterface.OnClickListener() {

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
							final LostBalanceDialog dialog = new LostBalanceDialog(context, R.style.MyDialog);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gift_dialog_cancel:
			dismiss();
			break;
		case R.id.paymoney_dialog_buy:
			// payMoney();
			getLastMoney();

			break;
		case R.id.add_dialog:
			if (gift_number_dialog.getText().length() == 0) {
			} else {
				number = Integer.parseInt(gift_number_dialog.getText().toString());
				if (number > 0) {
					number++;
					gift_number_dialog.setText(number + "");
					cont = number * price;
					dialog_price_tv.setText("" + cont + "");
				} else {
					gift_number_dialog.setText("1");
					dialog_price_tv.setText(price + "");
				}
			}
			break;
		case R.id.reduce_dialog:
			if (gift_number_dialog.getText().length() == 0) {
			} else {
				number = Integer.parseInt(gift_number_dialog.getText().toString());
				if (number > 1) {
					number--;
					gift_number_dialog.setText(number + "");
					cont = number * price;
					dialog_price_tv.setText("" + cont + "");
				}
			}
			break;

		default:
			break;
		}
	}

	// 送礼物接口
	@SuppressLint("ShowToast")
	public void sendGiftPost() {

		sendGiftBean bean = new sendGiftBean();
		bean.setFri_ids(fid);
		bean.setGiftCount(number + "");
		bean.setGiftId(giftid);
		bean.setType("0");
		String jsonstr = JsonHelper.toJsonString(bean);
		System.out.println(jsonstr);
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
				String url = Constans.sendGift;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
				params.put("cont", encodesstr);
				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, params);
				handler.sendEmptyMessage(1);

			}
		}).start();
		final ProgressDialog dialog = Util.showLoading(context, "正在赠送礼物...");
		handler = new Handler() {
			@SuppressLint("ShowToast")
			@Override
			public void handleMessage(Message msg) {
				dialog.dismiss();
				if (msg.what == 1) {
					try {
						CommonBack back = (CommonBack) ReqJsonUtil.changeToObject(obj.toString(), CommonBack.class);
						System.out.print(back);
						JSONObject jo = new JSONObject(obj.toString());
						giftUserId = jo.getInt("giftUserId") + "";
						if (back.getCode() == 00000) {
							dismiss();
							Toast.makeText(getContext(), back.getMessage(), Toast.LENGTH_LONG).show();
							if (cont > scoreValue) {
								giftMessaget();
								int money = cont - scoreValue;
								AlipayUtils ga = new AlipayUtils(context, 0);
								// ga.pay(context,money);
								// 测试用，alipayUtils需改回int类型
								ga.pay(context, dingdanID, money);

							} else {
								giftMessaget();
								Intent intent = new Intent(getContext(), ChatConversationActivity.class);
								context.startActivity(intent);
							}
							//
						}
						dismiss();
						Toast.makeText(getContext(), back.getMessage(), Toast.LENGTH_LONG).show();
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
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
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
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
				params.put("cont", encodesstr);
				params.put("orderPrice", prize + "");
				params.put("type", "0");
				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, params);
				handler.sendEmptyMessage(2);

			}
		}).start();
		final ProgressDialog dialog = Util.showLoading(context, "订单生成中...");
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
							dismiss();
							dingdanID = back.getOrderId();
							AlipayUtils ga = new AlipayUtils((Activity) context, 0, oauth_token, oauth_token_secret);
							ga.pay(context, dingdanID, prize);
						}
						dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

	}

	// 获取余额
	@SuppressLint("ShowToast")
	public void balanceMoney() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = Constans.myScore;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, params);
				handler.sendEmptyMessage(3);

			}
		}).start();
		handler = new Handler() {
			@SuppressLint("ShowToast")
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 3) {
					try {
						JSONObject jsonObject2 = new JSONObject(obj.toString());
						String message = jsonObject2.getString("message");
						String code = jsonObject2.getString("code");
						String credit = jsonObject2.getString("credit");
						JSONObject jsonObject3 = new JSONObject(credit);
						String experience = jsonObject3.getString("experience");
						JSONObject jsonObject4 = new JSONObject(experience);
						String experienceValue = jsonObject4.getString("value");
						String experienceAlias = jsonObject4.getString("alias");
						String score = jsonObject3.getString("score");
						JSONObject jsonObject5 = new JSONObject(score);
						scoreValue = jsonObject5.getInt("value");
						String scoreAlias = jsonObject5.getString("alias");
						String cashScore = jsonObject3.getString("cashScore");
						JSONObject jsonObject6 = new JSONObject(cashScore);
						String cashScoreValue = jsonObject6.getString("value");
						String cashScoreAlias = jsonObject6.getString("alias");
						String lockScore = jsonObject3.getString("lockScore");
						JSONObject jsonObject7 = new JSONObject(lockScore);
						String lockScoreValue = jsonObject7.getString("value");
						String lockScoreAlias = jsonObject7.getString("alias");
						if (code.equals("00000")) {
							System.out.println("￥" + scoreValue + "");
							// money_ownself.setText("￥"+scoreValue);
						} else {
							Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		};

	}
}

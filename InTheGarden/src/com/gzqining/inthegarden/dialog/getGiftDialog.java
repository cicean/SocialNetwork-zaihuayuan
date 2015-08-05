package com.gzqining.inthegarden.dialog;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.GetUserInfoCallback;
import io.rong.imlib.RongIMClient.UserInfo;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.Util;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.giftDetailsBack;

public class getGiftDialog extends Dialog implements android.view.View.OnClickListener {
	Context context;
	ImageView gift_dialog_cancel, dialog_gift_img;
	TextView gift_name_get, dialog_price_get_tv, dialog_zhuangtai_get_tv;
	TextView textViewTitle;
	Button buttonRecall;
	Button buttonState;
	LinearLayout layoutRecall;
	LinearLayout layoutState;
	int number;
	int price;
	Object obj;
	Handler handler;
	String encodesstr = "";
	giftDetailsBack back;
	String giftid;
	String img;
	String name;
	FinalBitmap fb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_gift_get);
		fb = FinalBitmap.create(getContext());
		initView();
	}

	private void setupMaiduanLayout(JSONObject obj) throws JSONException {
		this.setContentView(R.layout.dialog_maiduan);
		layoutState = (LinearLayout)findViewById(R.id.layoutState);
		buttonRecall = (Button) findViewById(R.id.buttonRecall);
		layoutRecall = (LinearLayout) findViewById(R.id.linearLayoutCancle);
		gift_dialog_cancel = (ImageView) findViewById(R.id.gift_dialog_cancel_get);
		dialog_gift_img = (ImageView) findViewById(R.id.dialog_gift_img_get);
		gift_name_get = (TextView) findViewById(R.id.gift_name_get);
		textViewTitle = (TextView) findViewById(R.id.textViewTitle);
		dialog_price_get_tv = (TextView) findViewById(R.id.dialog_price_get_tv);
		dialog_zhuangtai_get_tv = (TextView) findViewById(R.id.dialog_zhuangtai_get_tv);
		gift_dialog_cancel.setOnClickListener(this);
		buttonState = (Button) findViewById(R.id.buttonState);
		final TextView textViewNickname = (TextView) findViewById(R.id.textView_nickname);
		final ImageView imageViewHead = (ImageView) findViewById(R.id.imageView_userhead);
		TextView textViewUserRemark = (TextView) findViewById(R.id.textView_user_remark);
		TextView textViewRemark = (TextView) findViewById(R.id.textView_remark);
		TextView textViewTime = (TextView) findViewById(R.id.textView_time);

		textViewRemark.setText("留言：" + obj.getString("sendInfo"));
		textViewTime.setText("时间：" + obj.getString("days") + "天");

		gift_name_get.setText(obj.getString("giftName"));
		dialog_price_get_tv.setText("￥" + obj.getString("giftPrice")+".00");
		layoutRecall.setVisibility(View.GONE);
		dialog_zhuangtai_get_tv.setVisibility(View.VISIBLE);
		fb.display(dialog_gift_img, obj.getString("giftImg"));

		int fromUserId = obj.getInt("fromUserId");
		int toUserId = obj.getInt("toUserId");
		RongIM r = RongIM.getInstance();
		r.getRongIMClient().getUserInfo(toUserId + "", new GetUserInfoCallback() {

			@Override
			public void onError(ErrorCode arg0) {

			}

			@Override
			public void onSuccess(UserInfo ui) {
				textViewNickname.setText(ui.getName());
				fb.display(imageViewHead, ui.getPortraitUri());
			}
		});
		
		switch (Integer.parseInt(obj.getString("status"))) {
		case 0:
			buttonState.setVisibility(View.GONE);
			layoutState.setVisibility(View.VISIBLE);
			dialog_zhuangtai_get_tv.setText("等待确认");	
			//dialog_zhuangtai_get_tv.setVisibility(View.VISIBLE);
			if (fromUserId == Integer.parseInt(Util.getUserId(context))) {
				textViewUserRemark.setText("对TA的买断请求");
			} else {
				textViewUserRemark.setText("对TA的买断请求");
				layoutRecall.setVisibility(View.VISIBLE);
				layoutState.setVisibility(View.GONE);
			}
			break;
		case 1:
			//dialog_zhuangtai_get_tv.setVisibility(View.GONE);
			buttonState.setVisibility(View.VISIBLE);
			//dialog_zhuangtai_get_tv.setText("已确认");
			buttonState.setText("已接受");
			layoutState.setVisibility(View.GONE);
			layoutRecall.setVisibility(View.GONE);
			break;
		case 2:
			//dialog_zhuangtai_get_tv.setVisibility(View.GONE);
			buttonState.setVisibility(View.VISIBLE);
			//dialog_zhuangtai_get_tv.setText("已拒绝");
			buttonState.setText("已拒绝");
			layoutState.setVisibility(View.GONE);
			layoutRecall.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		

		

		findViewById(R.id.buttonReject).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				reviewMaiduan(2);
			}
		});
		findViewById(R.id.buttonAccept).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				reviewMaiduan(1);
			}
		});
	}

	public void initView() {
		buttonRecall = (Button) findViewById(R.id.buttonRecall);
		layoutRecall = (LinearLayout) findViewById(R.id.linearLayoutCancle);
		gift_dialog_cancel = (ImageView) findViewById(R.id.gift_dialog_cancel_get);
		dialog_gift_img = (ImageView) findViewById(R.id.dialog_gift_img_get);
		gift_name_get = (TextView) findViewById(R.id.gift_name_get);
		textViewTitle = (TextView) findViewById(R.id.textViewTitle);
		dialog_price_get_tv = (TextView) findViewById(R.id.dialog_price_get_tv);
		dialog_zhuangtai_get_tv = (TextView) findViewById(R.id.dialog_zhuangtai_get_tv);
		gift_dialog_cancel.setOnClickListener(this);

		String url = Constans.giftDetails;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", Util.getOauthToken(getContext()));
		params.put("oauth_token_secret", Util.getOauthTokenSecret(getContext()));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("giftId", giftid);
		try {
			encodesstr = AesUtils.Encrypt(new Gson().toJson(map), CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.put("cont", encodesstr);

		FinalHttp http = new FinalHttp();
		final ProgressDialog dialog = Util.showLoading(getContext());
		http.post(url, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				System.out.println("info:" + t);
				dialog.dismiss();
				try {
					JSONObject obj = new JSONObject(t);

					// 买断
					if (obj.getString("type").equals("1")) {
						setupMaiduanLayout(obj);
						return;
					}else {
					}

					gift_name_get.setText(obj.getString("giftName"));
					dialog_price_get_tv.setText("" + obj.getString("giftPrice")+".00");
					layoutRecall.setVisibility(View.GONE);
					dialog_zhuangtai_get_tv.setVisibility(View.VISIBLE);
					
					switch (Integer.parseInt(obj.getString("giftStatus"))) {
					case 0:
						layoutRecall.setVisibility(View.VISIBLE);
						dialog_zhuangtai_get_tv.setVisibility(View.GONE);
						dialog_zhuangtai_get_tv.setText("-冻结中-");
						break;
					case 1:
						dialog_zhuangtai_get_tv.setText("-已到账-");
						break;
					case 2:
						dialog_zhuangtai_get_tv.setText("-已撤回-");
						break;
					default:
						break;
					}

					int fromUserId = obj.getInt("fromUserId");
					if (fromUserId == Integer.parseInt(Util.getUserId(context))) {
						textViewTitle.setText("发出礼物");
						buttonRecall.setVisibility(View.VISIBLE);
						
						//layoutState.setVisibility(View.GONE);
					} else {
						textViewTitle.setText("收到礼物");
						dialog_zhuangtai_get_tv.setVisibility(View.VISIBLE);
						buttonRecall.setVisibility(View.GONE);
						layoutRecall.setVisibility(View.GONE);
						//layoutState.setVisibility(View.GONE);
					}

					

					fb.display(dialog_gift_img, obj.getString("giftImg"));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
			}
		});

		buttonRecall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = Constans.recallGift;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", Util.getOauthToken(getContext()));
				params.put("oauth_token_secret", Util.getOauthTokenSecret(getContext()));
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("giftUserId", giftid);
				map.put("reason", "");
				try {
					encodesstr = AesUtils.Encrypt(new Gson().toJson(map), CommonConstants.AES_KEY);
				} catch (Exception e) {
					e.printStackTrace();
				}
				params.put("cont", encodesstr);
				final ProgressDialog dialog = Util.showLoading(context);
				FinalHttp http = new FinalHttp();
				http.post(url, params, new AjaxCallBack<String>() {

					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						dialog.dismiss();
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);

						dialog.dismiss();
						dismiss();
					}

				});
			}
		});
	}

	public getGiftDialog(Context context) {
		super(context);
		this.context = context;
	}

	public getGiftDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public getGiftDialog(Context context, int theme, String giftId) {
		super(context, theme);
		this.context = context;
		this.number = price;
		this.giftid = giftId;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gift_dialog_cancel_get:
			dismiss();
			break;
		default:
			break;
		}
	}

	private void reviewMaiduan(int status) {
		String url = Constans.checkGift;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", Util.getOauthToken(getContext()));
		params.put("oauth_token_secret", Util.getOauthTokenSecret(getContext()));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("giftUserId", giftid);
		map.put("status", status);
		try {
			encodesstr = AesUtils.Encrypt(new Gson().toJson(map), CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.put("cont", encodesstr);

		FinalHttp http = new FinalHttp();
		final ProgressDialog dialog = Util.showLoading(getContext());
		http.post(url, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				System.out.println("info:" + t);
				dialog.dismiss();
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getString("code").equals("00000")) {
						dismiss();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
			}
		});
	}

}

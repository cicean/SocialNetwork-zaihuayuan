package com.gzqining.inthegarden.act;

import io.rong.imkit.Res;
import io.rong.imkit.RongIM;
import io.rong.imkit.view.ActionBar;
import io.rong.imlib.RongIMClient.ConversationType;
import io.rong.imlib.RongIMClient.GetUserInfoCallback;
import io.rong.imlib.RongIMClient.UserInfo;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.Util;

public class ChatConversationActivity extends BaseActivity {

	String tag = ChatConversationActivity.class.getName();
	private ActionBar mActionBar;
	private ImageView mSettingView;
	private String title, targetId;

	FinalBitmap fb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_chat_conversation);

		fb = FinalBitmap.create(this);

		targetId = getIntent().getData().getQueryParameter("targetId");

		RongIM.getInstance().getRongIMClient().getUserInfo(targetId, new GetUserInfoCallback() {

			@Override
			public void onSuccess(UserInfo arg0) {
				title = arg0.getName();
			}

			@Override
			public void onError(ErrorCode arg0) {
				title = "";
			}
		});

		mActionBar = (ActionBar) findViewById(R.id.rc_actionbar);
		if (targetId.equals("10001")) {
			mActionBar.getTitleTextView().setText("礼物消息");
		} else {
			mActionBar.getTitleTextView().setText(title);
		}

		mActionBar.setOnBackClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		View view = LayoutInflater.from(this).inflate(Res.getInstance(this).layout("rc_action_bar_conversation_settings"), mActionBar, false);
		mSettingView = (ImageView) view.findViewById(R.id.rc_conversation_settings_image);

		mSettingView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				RongIM.getInstance().startConversationSetting(ChatConversationActivity.this, ConversationType.valueOf(getIntent().getData().getLastPathSegment().toUpperCase()), getIntent().getData().getQueryParameter("targetId"));

			}
		});

		mActionBar.addView(mSettingView);

		RongIM.getInstance().getRongIMClient().getUserInfo(targetId, new GetUserInfoCallback() {

			@Override
			public void onError(ErrorCode arg0) {

			}

			@Override
			public void onSuccess(UserInfo arg0) {
				mActionBar.getTitleTextView().setText(arg0.getName());
			}
		});

		chenkBuyout();
	}

	private void chenkBuyout() {
		String url = Constans.chenkBuyout;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", Util.getOauthToken(this));
		params.put("oauth_token_secret", Util.getOauthTokenSecret(this));
		params.put("uid", this.targetId);

		FinalHttp http = new FinalHttp();
		final ProgressDialog dialog = Util.showLoading(this);
		http.post(url, params, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				System.out.println("info:" + t);
				dialog.dismiss();
				try {
					JSONObject obj = new JSONObject(t);
					if (obj.getInt("status") != 0) {
						new AlertDialog.Builder(ChatConversationActivity.this).setTitle(obj.getString("tips")).setNeutralButton("我知道了", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						}).setCancelable(false).show();

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

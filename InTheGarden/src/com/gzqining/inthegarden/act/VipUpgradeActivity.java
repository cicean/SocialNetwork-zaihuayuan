package com.gzqining.inthegarden.act;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.AlipayUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.zhiFuBaoBack;
import com.gzqining.inthegarden.vo.zhiFuBaoBean;

public class VipUpgradeActivity extends BaseActivity {
	private ImageButton back_bt;
	Button grade_vip_bt;
	Object obj;
	Handler handler;
	String encodesstr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vip_upgrade);
		AppManager.getAppManager().addActivity(this);
		back_bt = (ImageButton) findViewById(R.id.top_layout_vip_upgrade_back_btn);
		grade_vip_bt = (Button) findViewById(R.id.grade_vip_bt);
		back_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		grade_vip_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				payMoney();
			}
		});
	}

	// 获取订单号
	@SuppressLint("ShowToast")
	public void payMoney() {
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		zhiFuBaoBean bean = new zhiFuBaoBean();
		bean.setOrderPrice("2000");
		bean.setType(1);
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

				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, params);
				handler.sendEmptyMessage(2);

			}
		}).start();
		handler = new Handler() {
			@SuppressLint("ShowToast")
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 2) {
					try {
						zhiFuBaoBack back = (zhiFuBaoBack) ReqJsonUtil.changeToObject(obj.toString(), zhiFuBaoBack.class);
						System.out.println(obj.toString());
						if (back.getCode().equals("00000")) {
							String dingdanID = back.getOrderId();
							Toast.makeText(VipUpgradeActivity.this, back.getMessage(), Toast.LENGTH_LONG);
							AlipayUtils ga = new AlipayUtils(VipUpgradeActivity.this, 1, null, null);
							System.out.println("dingdanID:" + dingdanID);
							ga.pay(VipUpgradeActivity.this, dingdanID, 0.01);
						}
						Toast.makeText(VipUpgradeActivity.this, back.getMessage(), Toast.LENGTH_LONG);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

	}
}

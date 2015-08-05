package com.gzqining.inthegarden.dialog;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.maiDuanActivity;
import com.gzqining.inthegarden.vo.CommonBack;

public class maiduanGiftDialog extends Dialog implements android.view.View.OnClickListener {
	Context context;
	private Button paymoney_bt, reduce_dialog, add_dialog;
	private ImageView cancel_dialog, dialog_gift_img_maiduan;
	private TextView gift_number_dialog, dialog_price_tv;
	int number = 1;
	int price;
	Object obj;
	Handler handler;
	String encodesstr = "";
	CommonBack back;
	String oauth_token;
	String oauth_token_secret;
	String fid;
	String giftid;
	String img;
	String name;
	FinalBitmap fb;
	String time;
	String sendInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_gift_maiduan);
		paymoney_bt = (Button) findViewById(R.id.paymoney_dialog_maiduan);
		cancel_dialog = (ImageView) findViewById(R.id.gift_dialog_cancel_maiduan);
		dialog_gift_img_maiduan = (ImageView) findViewById(R.id.dialog_gift_img_maiduan);
		reduce_dialog = (Button) findViewById(R.id.reduce_dialog_maiduan);
		gift_number_dialog = (TextView) findViewById(R.id.gift_number_dialog_maiduan);
		add_dialog = (Button) findViewById(R.id.add_dialog_maiduan);
		dialog_price_tv = (TextView) findViewById(R.id.dialog_price_tv_maiduan);
		fb.display(dialog_gift_img_maiduan, img);
		paymoney_bt.setOnClickListener(this);
		cancel_dialog.setOnClickListener(this);
		reduce_dialog.setOnClickListener(this);
		gift_number_dialog.setOnClickListener(this);
		add_dialog.setOnClickListener(this);
		dialog_price_tv.setOnClickListener(this);
		dialog_price_tv.setText("￥" + price + "");
	}

	public maiduanGiftDialog(Context context) {
		super(context);
		this.context = context;
	}

	public maiduanGiftDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public maiduanGiftDialog(Context context, int theme, int price, String oauth_token, String oauth_token_secret, String fid, String giftid, String img, String name, String time, String sendInfo) {
		super(context, theme);
		this.context = context;
		this.price = price;
		this.oauth_token = oauth_token;
		this.oauth_token_secret = oauth_token_secret;
		this.fid = fid;
		this.giftid = giftid;
		this.img = img;
		this.name = name;
		this.time = time;
		this.sendInfo = sendInfo;
		fb = FinalBitmap.create(context);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.gift_dialog_cancel_maiduan:
			dismiss();
			break;
		case R.id.paymoney_dialog_maiduan:
			Intent intent = new Intent(getContext(), maiDuanActivity.class);
			intent.putExtra("img", img);
			intent.putExtra("fid", fid);
			intent.putExtra("number", number);
			intent.putExtra("name", name);
			intent.putExtra("giftid", giftid);
			intent.putExtra("price", price);
			intent.putExtra("time", time);
			intent.putExtra("sendInfo", sendInfo);
			context.startActivity(intent);
			((Activity) context).finish();
			break;
		case R.id.add_dialog_maiduan:
			if (gift_number_dialog.getText().length() == 0) {
			} else {
				number = Integer.parseInt(gift_number_dialog.getText().toString());
				if (number > 0) {
					number++;
					gift_number_dialog.setText(number + "");
					int cont = number * price;
					dialog_price_tv.setText("￥" + cont + "");
				} else {
					gift_number_dialog.setText("0");
					dialog_price_tv.setText("￥" + price + "");
				}
			}
			break;
		case R.id.reduce_dialog_maiduan:
			if (gift_number_dialog.getText().length() == 0) {
			} else {
				number = Integer.parseInt(gift_number_dialog.getText().toString());
				if (number > 1) {
					number--;
					gift_number_dialog.setText(number + "");
					int cont = number * price;
					dialog_price_tv.setText("￥" + cont + "");
				}
			}
			break;

		default:
			break;
		}
	}
}

package com.gzqining.inthegarden.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gzqining.inthegarden.R;

public class LostBalanceDialog extends Dialog {
	public TextView textViewPirze;
	public Button buttonPay;

	public LostBalanceDialog(Context context, int theme) {
		super(context, theme);
	}

	private void setup() {
		textViewPirze = (TextView) findViewById(R.id.dialog_price_tv);
		buttonPay = (Button) findViewById(R.id.paymoney_dialog_buy);
		findViewById(R.id.gift_dialog_cancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_lostbalance);
		this.setup();
	}
}

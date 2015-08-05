package com.gzqining.inthegarden.dialog;


import com.gzqining.inthegarden.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

@SuppressLint("Instantiatable") public class Clearn_News_Dialog extends Dialog {
	
	private Button confirm_dialog;
	private Button cancel_dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_clean_news);
		confirm_dialog = (Button) findViewById(R.id.dialog_confirm);
		cancel_dialog = (Button) findViewById(R.id.dialog_cancel);
		confirm_dialog.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		cancel_dialog.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
	}
	Context context;

	public Clearn_News_Dialog(Context context) {
		super(context);
		this.context = context;
	}

	public Clearn_News_Dialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}


}


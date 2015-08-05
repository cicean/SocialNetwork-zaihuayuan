package com.gzqining.inthegarden.app.ui;

import android.os.Bundle;
import android.view.View;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.ShellActivity;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.Injection;

@EActivity(layout = R.layout.ui_activity_register_detail_info, inject = true)
public class ShellRegisterDetailActivity extends ShellActivity{

	private Injection injection;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		injection = getInjection();
	}
	
	
	
	@Click({
		R.id.btnBack,
		R.id.btnNext,
		R.id.sex,
		R.id.birthday,
		R.id.address,
		R.id.marry,
		R.id.height,
		R.id.weight,
		R.id.xueli,
		R.id.occupation,
		R.id.income,
		R.id.faith
	})
	void onClick(View v) {
		int id = v.getId();
		switch(id) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnNext:
			
			break;
		case R.id.sex:
			
			break;
		case R.id.birthday:
			
			break;
		case R.id.address:
			
			break;
		case R.id.marry:
			
			break;
		case R.id.height:
			
			break;
		case R.id.weight:
			
			break;
		case R.id.xueli:
			
			break;
		case R.id.occupation:
			
			break;
		case R.id.income:
			
			break;
		case R.id.faith:
			
			break;
		}
	}
	
}

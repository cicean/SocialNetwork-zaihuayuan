package com.gzqining.inthegarden.act;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.app.ShellUtil;
import com.gzqining.inthegarden.app.ui.ShellWithdrawTiaojian;
import com.gzqining.inthegarden.app.ui.WithdrawTixianActivity;
import com.gzqining.inthegarden.common.BaseActivity;

public class WalletActivity extends BaseActivity implements android.view.View.OnClickListener {
	private RelativeLayout remaining_sum_wallet;
	private RelativeLayout withdraw_deposit_wallet;
	private RelativeLayout paymentpay_wallet;
	private RelativeLayout present_wallet;
	private RelativeLayout buy_out_wallet;
	private ImageButton back_bt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_wallet);
		AppManager.getAppManager().addActivity(this);
		
		initView();
		
		}
	public void initView(){
		remaining_sum_wallet = (RelativeLayout) findViewById(R.id.remaining_sum_wallet);
		withdraw_deposit_wallet = (RelativeLayout) findViewById(R.id.withdraw_deposit_wallet);
		paymentpay_wallet = (RelativeLayout) findViewById(R.id.paymentpay_wallet);
		present_wallet = (RelativeLayout) findViewById(R.id.present_wallet);
		buy_out_wallet = (RelativeLayout) findViewById(R.id.buy_out_wallet);
		back_bt = (ImageButton) findViewById(R.id.top_layout_wallet_back_btn);
		remaining_sum_wallet.setOnClickListener(this);
		withdraw_deposit_wallet.setOnClickListener(this);
		paymentpay_wallet.setOnClickListener(this);
		present_wallet.setOnClickListener(this);
		buy_out_wallet.setOnClickListener(this);
		back_bt.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(v.getId()){
		case R.id.remaining_sum_wallet:
			intent = new Intent(WalletActivity.this,BalanceActivity.class);
			startActivity(intent);
			break;
		case R.id.withdraw_deposit_wallet:
			intent = new Intent(WalletActivity.this,WithdrawalsActivity.class);
			startActivity(intent);		
			break;
		case R.id.paymentpay_wallet:
			intent = new Intent(WalletActivity.this,PayMoneyWayActivity.class);
			startActivity(intent);
			break;
		case R.id.present_wallet:
			//intent = new Intent(WalletActivity.this,presentWalletListActivity.class);
			//startActivity(intent);
			new AlertDialog.Builder(this)
			.setItems(new String[]{"礼物", "测试提现流程"}, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which == 0) {
						startActivity(new Intent(WalletActivity.this,presentWalletListActivity.class));
						return;
					} else {
						
						SharedPreferences sp = getSharedPreferences("idcard", 0);
						int is_vip = sp.getInt("is_vip", 0);
						int is_approve = sp.getInt("is_approve", 0);
						if(is_vip!=1 || is_approve!=2) {
							ShellUtil.execute(WalletActivity.this, ShellWithdrawTiaojian.class);
						} else {
							Intent ii = new Intent(WalletActivity.this,WithdrawTixianActivity.class);
							ii.putExtra(WithdrawTixianActivity.EXTRA_MONEY, "300.00");
							startActivity(new Intent(ii));
						}
					}
				}
			})
			.show();
			
			break;
		case R.id.buy_out_wallet:
			intent = new Intent(WalletActivity.this,maiduanRecordActivity.class);
			startActivity(intent);
			break;
		case R.id.top_layout_wallet_back_btn:
			finish();
			break;
			default:
				break;
		}
	}
}

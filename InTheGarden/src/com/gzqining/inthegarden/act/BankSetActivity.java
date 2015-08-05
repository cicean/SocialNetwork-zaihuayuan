package com.gzqining.inthegarden.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.http.AjaxParams;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.WheelView.ArrayWheelAdapter;
import com.gzqining.inthegarden.WheelView.WheelView;
import com.gzqining.inthegarden.app.AppActivity;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.ViewById;
import com.gzqining.inthegarden.app.injection.ViewsById;
import com.gzqining.inthegarden.app.util.StringUtils;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.VerifyIdCard;
import com.gzqining.inthegarden.vo.AddBankBean;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;

@EActivity(layout = R.layout.act_bank_set, inject = true)
public class BankSetActivity extends AppActivity {

	@ViewById
	private TextView choose_bank_tv;
	
	@ViewsById({
		R.id.bank_number_et,
		R.id.name_bank_et,
		R.id.idcard_bank_et,
		R.id.phone_bank_et
	})
	private List<EditText> editViews;
	
	@ViewsById({
		R.id.bank_number_iv,
		R.id.name_bank_iv,
		R.id.idcard_bank_iv,
		R.id.phone_bank_iv
	})
	private List<ImageView> clearViews;
	
	@Click({
		R.id.add_bank_bt,
		R.id.btnBack,
		R.id.choose_bank_tv,
		R.id.bank_number_iv,
		R.id.name_bank_iv,
		R.id.idcard_bank_iv,
		R.id.phone_bank_iv })
	void onClick(View v) {
		int id = v.getId();
		switch(id) {
		case R.id.add_bank_bt:
			bankHttpPost();
			break;
		case R.id.btnBack:
			finish();
			break;
		case R.id.choose_bank_tv:
			Button right;
			Button left;
			TextView title;
			v = View.inflate(this, R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("请选择银行卡类型");
			left = (Button) v.findViewById(R.id.quxiao);
			final WheelView wheelView_sex = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_sex.setAdapter(new ArrayWheelAdapter<String>(category_bank));
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
					choose_bank_tv.setText(category_bank[wheelView_sex.getCurrentItem()]);
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
		case R.id.bank_number_iv:
			editViews.get(0).setText("");
			etChange();
			break;
		case R.id.name_bank_iv:
			editViews.get(1).setText("");
			etChange();
			break;
		case R.id.idcard_bank_iv:
			editViews.get(2).setText("");
			etChange();
			break;
		case R.id.phone_bank_iv:
			editViews.get(3).setText("");
			etChange();
			break;
		}
	}
	
	
	private Dialog dialog;
	
	String category_bank[] = new String[] { 
			"中国工商银行", 
			"招商银行", 
			"中国光大银行", 
			"中信银行", 
			"浦发银行", 
			"广发银行", 
			"华厦银行", 
			"中国建设银行", 
			"交通银行", 
			"中国银行", 
			"兴业银行",
			"平安银行" 
			};
	final Map<String, String> bankID = new HashMap<String, String>();
	{
		bankID.put("中国工商银行", "10");
		bankID.put("招商银行", "11");
		bankID.put("中国光大银行", "12");
		bankID.put("中信银行", "13");
		bankID.put("浦发银行", "14");
		bankID.put("广发银行", "15");
		bankID.put("华厦银行", "16");
		bankID.put("中国建设银行", "17");
		bankID.put("交通银行", "18");
		bankID.put("中国银行", "19");
		bankID.put("兴业银行", "20");
		bankID.put("平安银行", "21");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 绑定事件
		for (EditText edit : editViews) {
			edit.addTextChangedListener(textWatcher);
		}
		etChange();
	}
	
	
	/**
	 * 校验银行卡卡号
	 * 
	 * @param cardId
	 * @return
	 */
	public static boolean checkBankCard(String cardId) {
		char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
		if (bit == 'N') {
			return false;
		}
		return cardId.charAt(cardId.length() - 1) == bit;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * 
	 * @param nonCheckCodeCardId
	 * @return
	 */
	public static char getBankCardCheckCode(String nonCheckCodeCardId) {
		if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0 || !nonCheckCodeCardId.matches("\\d+")) {
			// 如果传的不是数据返回N
			return 'N';
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}


	public void bankHttpPost() {

		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);

		AddBankBean bean = new AddBankBean();

		String bank = choose_bank_tv.getText().toString();
		String bank_id = bankID.get(bank);
		bean.setBank_id(bank_id);

		String card_num = editViews.get(0).getText().toString().trim();
		bean.setCard_num(card_num);
		if(!checkBankCard(card_num)) {
			Toast.makeText(BankSetActivity.this, "卡号格式不正确", Toast.LENGTH_SHORT).show();
			return;
		}

		String uname = editViews.get(1).getText().toString().trim();
		bean.setUname(uname);
		if(TextUtils.isEmpty(uname)) {
			Toast.makeText(BankSetActivity.this, "请输入姓名", Toast.LENGTH_SHORT).show();
			return;
		}

		String IDC_num = editViews.get(2).getText().toString().trim();
		bean.setIDC_num(IDC_num);
		
		if(!new VerifyIdCard().verify(IDC_num)) {
			Toast.makeText(BankSetActivity.this, "身份证号码格式不正确", Toast.LENGTH_SHORT).show();
			return;
		}

		String tel = editViews.get(3).getText().toString().trim();
		bean.setTel(tel);
		if(!StringUtils.isPhoneNumberValid(tel)) {
			Toast.makeText(BankSetActivity.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
			return;
		}

		String jsonstr = new Gson().toJson(bean);

		String encodesstr = null;
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url = Constans.editBinkInfo;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("cont", encodesstr);
		
		showDialogFragment(false);
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if(isFinishing()) return;
				
				removeDialogFragment();
				if(TextUtils.isEmpty((String)obj)) {
					Toast.makeText(BankSetActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				CommonBack back = (CommonBack) ReqJsonUtil.changeToObject((String)obj, CommonBack.class);
				Toast.makeText(BankSetActivity.this, back.getMessage(), Toast.LENGTH_SHORT).show();
				if (back.getCode() == 00000) {
					Intent intent = new Intent(BankSetActivity.this, MyInfoActivity.class);
					startActivity(intent);
					finish();
				}
				
			}
		});
		
	}

	// 设置底部，宽full
	@SuppressWarnings("deprecation")
	public void dialogShow() {
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (display.getWidth()); // 设置宽度
		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
	}

	// 监听输入框变化，刷新右边的X显示
	private TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			etChange();
		}
	};

	// 判断输入框是否为空，并实现相应X按钮是否显示
	private void etChange() {
		for(int i=0,size=editViews.size();i<size;i++) {
			EditText editV = editViews.get(i);
			clearViews.get(i).setVisibility(editV.length() == 0 ? View.GONE : View.VISIBLE);
		}
	}
}
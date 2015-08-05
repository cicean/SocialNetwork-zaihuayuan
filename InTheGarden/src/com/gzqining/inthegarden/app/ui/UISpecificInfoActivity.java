package com.gzqining.inthegarden.app.ui;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.WheelView.ArrayWheelAdapter;
import com.gzqining.inthegarden.WheelView.WheelView;
import com.gzqining.inthegarden.WheelView.WheelView.OnWheelChangedListener;
import com.gzqining.inthegarden.act.MainActivity;
import com.gzqining.inthegarden.act.dateselector.DateSelectorWheelView;
import com.gzqining.inthegarden.act.dateselector.DateTimeSelectorDialogBuilder;
import com.gzqining.inthegarden.act.dateselector.DateTimeSelectorDialogBuilder.OnSaveListener;
import com.gzqining.inthegarden.app.AppActivity;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.Extra;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.GetTokenVo;
import com.gzqining.inthegarden.vo.LoginBack;
import com.gzqining.inthegarden.vo.editUserDataBean;

@EActivity(layout = R.layout.ui_specific_info, inject = true)
public class UISpecificInfoActivity extends AppActivity implements OnClickListener, OnSaveListener, OnWheelChangedListener {
	
	@Extra
	private String phone;
	@Extra
	String email;
	@Extra
	private String password;
	@Extra
	private String phoneVerify;
	@Extra
	private String sid;
	@Extra
	private String nickname;
	@Extra
	private String headBase64;
	
	
	String tag = UISpecificInfoActivity.class.getName();
	private DateTimeSelectorDialogBuilder dialogBuilder;
	LayoutInflater myInflater;
	Intent intent;
	Dialog dialog;
	String id;
	Handler handler;
	Object obj;
	String provinceID;
	String cityID;
	private String file = "/sdcard/city.json";
	private JSONObject mJsonObj;
	// 性别
	private RelativeLayout sex_rl;
	private TextView sex_tv;
	// 生日
	private RelativeLayout birthday_rl;
	private TextView birthday_tv;
	// 出生地
	private RelativeLayout address_rl;
	private TextView address_tv;
	// 婚恋
	private RelativeLayout marry_rl;
	private TextView marry_tv;
	// 身高
	private RelativeLayout height_rl;
	private TextView height_tv;
	// 体重
	private RelativeLayout weight_rl;
	private TextView weight_tv;
	// 学历
	private RelativeLayout xueli_rl;
	private TextView xueli_tv;
	// 职业
	private RelativeLayout occupation_rl;
	private TextView occupation_tv;
	// 年收入
	private RelativeLayout income_rl;
	private TextView income_tv;
	// 信仰
	private RelativeLayout faith_rl;
	private TextView faith_tv;
	// 吸烟
	private RelativeLayout smoke_rl;
	private TextView smoke_tv;
	// 饮酒
	private RelativeLayout drink_rl;
	private TextView drink_tv;
	// 语言
	private RelativeLayout language_rl;
	private TextView language_tv;

	JSONObject jsonObject1 = null;
	WheelView wheelView_province;
	WheelView wheelView_city;
	WheelView wheelView_area;
	/**
	 * 当前省的名称
	 */
	private String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	private String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	private String mCurrentAreaName = "";
	String province[] = new String[34];
	Map<String, String> map1 = new HashMap<String, String>();
	Map<String, String> map_city_id = new HashMap<String, String>();
	Map<String, String> map_area_id = new HashMap<String, String>();
	Map<String, String[]> map_city = new HashMap<String, String[]>();
	// Map<Object, String[]> map_area = new HashMap<Object, String[]>();
	String category_sex[] = new String[] { "男", "女" };
	String category_merry[] = new String[] { "保密", "单身", "恋爱中", "已婚" };
	String category_xueli[] = new String[] { "专科", "本科", "硕士", "博士", "博士后" };
	String category_faith[] = new String[] { "佛教徒", "基督徒", "伊斯兰教", "其他", "无信仰" };
	String category_drink[] = new String[] { "是", "否" };
	String category_smoke[] = new String[] { "是", "否" };
	String category_career[] = new String[] { "行政", "艺术", "教育", "工程/科学", "财务", "管理", "法律", "医疗", "军事", "政府", "零售/餐饮", "销售/市场", "学生", "其他",
			"退休" };
	String category_language[] = new String[] { "国语", "粤语", "英语", "法语", "德语", "拉丁语", "日语", "韩语", "其他" };
	String category_money[] = new String[] { "10万以上", "10万-20万", "20万-30万", "30-50万", "50-100万", "100万以上" };
	String category_height[] = new String[100];
	String category_weight[] = new String[120];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int j = 120;
		int h = 30;
		for (int i = 0; i < 100; i++) {
			category_height[i] = j + "cm";
			j++;
		}
		for (int i = 0; i < 120; i++) {
			category_weight[i] = h + "kg";
			h++;
		}
		initJsonData();
		Area_List();
		// specificInfo();
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		myInflater = LayoutInflater.from(this);

		sex_rl = (RelativeLayout) findViewById(R.id.sex_rl);
		sex_tv = (TextView) findViewById(R.id.sex_tv);
		birthday_rl = (RelativeLayout) findViewById(R.id.birthday_rl);
		birthday_tv = (TextView) findViewById(R.id.birthday_tv);
		address_rl = (RelativeLayout) findViewById(R.id.address_rl);
		address_tv = (TextView) findViewById(R.id.address_tv);
		marry_rl = (RelativeLayout) findViewById(R.id.marry_rl);
		marry_tv = (TextView) findViewById(R.id.marry_tv);
		height_rl = (RelativeLayout) findViewById(R.id.height_rl);
		height_tv = (TextView) findViewById(R.id.height_tv);
		weight_rl = (RelativeLayout) findViewById(R.id.weight_rl);
		weight_tv = (TextView) findViewById(R.id.weight_tv);
		xueli_rl = (RelativeLayout) findViewById(R.id.xueli_rl);
		xueli_tv = (TextView) findViewById(R.id.xueli_tv);
		occupation_rl = (RelativeLayout) findViewById(R.id.occupation_rl);
		occupation_tv = (TextView) findViewById(R.id.occupation_tv);
		income_rl = (RelativeLayout) findViewById(R.id.income_rl);
		income_tv = (TextView) findViewById(R.id.income_tv);
		faith_rl = (RelativeLayout) findViewById(R.id.faith_rl);
		faith_tv = (TextView) findViewById(R.id.faith_tv);
		smoke_rl = (RelativeLayout) findViewById(R.id.smoke_rl);
		smoke_tv = (TextView) findViewById(R.id.smoke_tv);
		drink_rl = (RelativeLayout) findViewById(R.id.drink_rl);
		drink_tv = (TextView) findViewById(R.id.drink_tv);
		language_rl = (RelativeLayout) findViewById(R.id.language_rl);
		language_tv = (TextView) findViewById(R.id.language_tv);

		birthday_rl.setOnClickListener(this);
		sex_rl.setOnClickListener(this);
		address_rl.setOnClickListener(this);
		marry_rl.setOnClickListener(this);
		height_rl.setOnClickListener(this);
		weight_rl.setOnClickListener(this);
		xueli_rl.setOnClickListener(this);
		occupation_rl.setOnClickListener(this);
		income_rl.setOnClickListener(this);
		faith_rl.setOnClickListener(this);
		smoke_rl.setOnClickListener(this);
		drink_rl.setOnClickListener(this);
		language_rl.setOnClickListener(this);
		
		///////////////////////
		sex_tv.setText("");
		birthday_tv.setText("");
		address_tv.setText("");
		marry_tv.setText("");
		height_tv.setText("");
		weight_tv.setText("");
		xueli_tv.setText("");
		occupation_tv.setText("");
		income_tv.setText("");
		faith_tv.setText("");
		smoke_tv.setText("");
		drink_tv.setText("");
		language_tv.setText("");

	}
	
	@Click({
		R.id.btnBack,
		R.id.btnNext
	})
	void btnClick(View v) {
		int id = v.getId();
		if(id == R.id.btnBack) {
			finish();
		} else if(id == R.id.btnNext) {
			
			if(isEmpty(sex_tv)) {
				Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
				return;
			}
			if(isEmpty(birthday_tv)) {
				Toast.makeText(this, "请选择生日", Toast.LENGTH_SHORT).show();
				return;
			}
			if(isEmpty(address_tv)) {
				Toast.makeText(this, "请选择出生地", Toast.LENGTH_SHORT).show();
				return;
			}
			if(isEmpty(marry_tv)) {
				Toast.makeText(this, "请选择婚恋", Toast.LENGTH_SHORT).show();
				return;
			}
				if(isEmpty(height_tv)) {
				Toast.makeText(this, "请选择身高", Toast.LENGTH_SHORT).show();
				return;
			}
			if(isEmpty(weight_tv)) {
				Toast.makeText(this, "请选择体重", Toast.LENGTH_SHORT).show();
				return;
			}
			if(isEmpty(xueli_tv)) {
				Toast.makeText(this, "请选择学历", Toast.LENGTH_SHORT).show();
				return;
			}
			if(isEmpty(occupation_tv)) {
				Toast.makeText(this, "请选择职业", Toast.LENGTH_SHORT).show();
				return;
			}
			if(isEmpty(income_tv)) {
				Toast.makeText(this, "请选择年收入", Toast.LENGTH_SHORT).show();
				return;
			}
			if(isEmpty(faith_tv)) {
				Toast.makeText(this, "请选择信仰", Toast.LENGTH_SHORT).show();
				return;
			}
			if(isEmpty(smoke_tv)) {
				Toast.makeText(this, "请选择是否吸烟", Toast.LENGTH_SHORT).show();
				return;
			}
			if(isEmpty(drink_tv)) {
				Toast.makeText(this, "请选择是否饮酒", Toast.LENGTH_SHORT).show();
				return;
			}
			if(isEmpty(language_tv)) {
				Toast.makeText(this, "请选择语言", Toast.LENGTH_SHORT).show();
				return;
			}
			
			registPhone();
		}
	}

	@Override
	public void onClick(View v) {
		Button right;
		Button left;
		TextView title;
		switch (v.getId()) {
		case R.id.birthday_rl:
			dialogBuilder = new DateTimeSelectorDialogBuilder(this, v);
			dialogBuilder.setOnSaveListener(this);
			Window window = dialogBuilder.getWindow();
			window.setGravity(Gravity.BOTTOM);
			dialogBuilder.show();
			break;
		case R.id.sex_rl:
			v = myInflater.inflate(R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("性别");
			final WheelView wheelView_sex = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_sex.setAdapter(new ArrayWheelAdapter<String>(category_sex));
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
					// TODO Auto-generated method stub
					sex_tv.setText(category_sex[wheelView_sex.getCurrentItem()]);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;
		case R.id.address_rl:

			v = myInflater.inflate(R.layout.dialog_city_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			wheelView_province = (WheelView) v.findViewById(R.id.wv_province);
			wheelView_province.setAdapter(new ArrayWheelAdapter<String>(province));
			wheelView_province.setVisibleItems(5);
			wheelView_province.setCyclic(true);
			wheelView_province.setCurrentItem(0);
			wheelView_city = (WheelView) v.findViewById(R.id.wv_city);

			wheelView_city.setCyclic(false);
			// wheelView_area = (WheelView)v.findViewById(R.id.wv_area);
			// wheelView_area.setCyclic(false);
			// 添加change事件
			wheelView_province.addChangingListener(this);
			wheelView_city.addChangingListener(this);
			// wheelView_area.addChangingListener(this);

			wheelView_province.setVisibleItems(5);
			wheelView_city.setVisibleItems(5);
			// wheelView_area.setVisibleItems(5);
			updateCities();
			// updateAreas();
			dialog = new Dialog(this, R.style.MyDialog);
			dialog.setContentView(v);
			dialog.show();
			dialogShow();
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int pCurrent = wheelView_city.getCurrentItem();
					mCurrentCityName = map_city.get(mCurrentProviceName)[pCurrent];
					provinceID = map1.get(mCurrentProviceName);
					cityID = map_city_id.get(mCurrentCityName);
					// Toast.makeText(SpecificInfoActivity.this,cityID,
					// 1).show();
					address_tv.setText(province[wheelView_province.getCurrentItem()] + mCurrentCityName);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;
		case R.id.marry_rl:
			v = myInflater.inflate(R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("婚恋");
			final WheelView wheelView_merry = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_merry.setAdapter(new ArrayWheelAdapter<String>(category_merry));
			wheelView_merry.setVisibleItems(5);
			wheelView_merry.setCyclic(false);
			wheelView_merry.setCurrentItem(0);
			dialog = new Dialog(this, R.style.MyDialog);
			dialog.setContentView(v);
			dialog.show();
			dialogShow();
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					marry_tv.setText(category_merry[wheelView_merry.getCurrentItem()]);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;

		case R.id.xueli_rl:
			v = myInflater.inflate(R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("学历");
			final WheelView wheelView_xueli = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_xueli.setAdapter(new ArrayWheelAdapter<String>(category_xueli));
			wheelView_xueli.setVisibleItems(5);
			wheelView_xueli.setCyclic(false);
			wheelView_xueli.setCurrentItem(0);
			dialog = new Dialog(this, R.style.MyDialog);
			dialog.setContentView(v);
			dialog.show();
			dialogShow();
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					xueli_tv.setText(category_xueli[wheelView_xueli.getCurrentItem()]);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;
		case R.id.faith_rl:
			v = myInflater.inflate(R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("信仰");
			final WheelView wheelView_faith = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_faith.setAdapter(new ArrayWheelAdapter<String>(category_faith));
			wheelView_faith.setVisibleItems(5);
			wheelView_faith.setCyclic(false);
			wheelView_faith.setCurrentItem(0);
			dialog = new Dialog(this, R.style.MyDialog);
			dialog.setContentView(v);
			dialog.show();
			dialogShow();
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					faith_tv.setText(category_faith[wheelView_faith.getCurrentItem()]);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;

		case R.id.drink_rl:
			v = myInflater.inflate(R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("喝酒");
			final WheelView wheelView_drink = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_drink.setAdapter(new ArrayWheelAdapter<String>(category_drink));
			wheelView_drink.setVisibleItems(5);
			wheelView_drink.setCyclic(false);
			wheelView_drink.setCurrentItem(0);
			dialog = new Dialog(this, R.style.MyDialog);
			dialog.setContentView(v);
			dialog.show();
			dialogShow();
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					drink_tv.setText(category_drink[wheelView_drink.getCurrentItem()]);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;

		case R.id.smoke_rl:
			v = myInflater.inflate(R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("吸烟");
			final WheelView wheelView_smoke = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_smoke.setAdapter(new ArrayWheelAdapter<String>(category_smoke));
			wheelView_smoke.setVisibleItems(5);
			wheelView_smoke.setCyclic(false);
			wheelView_smoke.setCurrentItem(0);
			dialog = new Dialog(this, R.style.MyDialog);
			dialog.setContentView(v);
			dialog.show();
			dialogShow();
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					smoke_tv.setText(category_smoke[wheelView_smoke.getCurrentItem()]);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;
		case R.id.income_rl:
			v = myInflater.inflate(R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("收入");
			final WheelView wheelView_income = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_income.setAdapter(new ArrayWheelAdapter<String>(category_money));
			wheelView_income.setVisibleItems(5);
			wheelView_income.setCyclic(false);
			wheelView_income.setCurrentItem(0);
			dialog = new Dialog(this, R.style.MyDialog);
			dialog.setContentView(v);
			dialog.show();
			dialogShow();
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					income_tv.setText(category_money[wheelView_income.getCurrentItem()]);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;
		case R.id.height_rl:
			v = myInflater.inflate(R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("身高");
			final WheelView wheelView_height = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_height.setAdapter(new ArrayWheelAdapter<String>(category_height));
			wheelView_height.setVisibleItems(5);
			wheelView_height.setCyclic(false);
			wheelView_height.setCurrentItem(0);
			dialog = new Dialog(this, R.style.MyDialog);
			dialog.setContentView(v);
			dialog.show();
			dialogShow();
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					height_tv.setText(category_height[wheelView_height.getCurrentItem()]);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;
		case R.id.weight_rl:
			v = myInflater.inflate(R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("体重");
			final WheelView wheelView_weight = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_weight.setAdapter(new ArrayWheelAdapter<String>(category_weight));
			wheelView_weight.setVisibleItems(5);
			wheelView_weight.setCyclic(false);
			wheelView_weight.setCurrentItem(0);
			dialog = new Dialog(this, R.style.MyDialog);
			WindowManager m = getWindowManager();
			dialog.setContentView(v);
			dialog.show();
			dialogShow();
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					weight_tv.setText(category_weight[wheelView_weight.getCurrentItem()]);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;
		case R.id.occupation_rl:
			v = myInflater.inflate(R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("职业");
			final WheelView wheelView_occupation = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_occupation.setAdapter(new ArrayWheelAdapter<String>(category_career));
			wheelView_occupation.setVisibleItems(5);
			wheelView_occupation.setCyclic(false);
			wheelView_occupation.setCurrentItem(0);
			dialog = new Dialog(this, R.style.MyDialog);
			dialog.setContentView(v);
			dialog.show();
			dialogShow();
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					occupation_tv.setText(category_career[wheelView_occupation.getCurrentItem()]);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;
		case R.id.language_rl:
			v = myInflater.inflate(R.layout.dialog_specific_layout, null);
			right = (Button) v.findViewById(R.id.queding);
			left = (Button) v.findViewById(R.id.quxiao);
			title = (TextView) v.findViewById(R.id.title_choise);
			title.setText("语言");
			final WheelView wheelView_language = (WheelView) v.findViewById(R.id.income_wv);
			wheelView_language.setAdapter(new ArrayWheelAdapter<String>(category_language));
			wheelView_language.setVisibleItems(5);
			wheelView_language.setCyclic(false);
			wheelView_language.setCurrentItem(0);
			dialog = new Dialog(this, R.style.MyDialog);
			dialog.setContentView(v);
			dialog.show();
			dialogShow();
			right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					language_tv.setText(category_language[wheelView_language.getCurrentItem()]);
					dialog.dismiss();
				}
			});
			left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;
		default:
			break;
		}
	}
	
	public static class RegistParams {
		
		public String phone;
		public String email;
		public String passwd;
		public String repasswd;
		public String code;
		public String head;
		public String nickname;
		
	}
	
	private void registPhone() {
		
		RegistParams bean = new RegistParams();
		bean.phone = this.phone;
		bean.email = this.email;
		bean.passwd = this.password;
		bean.repasswd = this.password;
		bean.code = this.phoneVerify;
		bean.head = this.headBase64;
		bean.nickname = this.nickname;
		
		String jsonstr = null;
		String url = Constans.registPhone;
		String json = JsonHelper.toJsonString(bean);
		try {
			jsonstr = AesUtils.Encrypt(json, CommonConstants.AES_KEY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AjaxParams params = new AjaxParams();
		params.put("cont", jsonstr);
		params.put("sid", sid);
		
		showDialogFragment(false);
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if(isFinishing())
					return;
				removeDialogFragment();
				if(obj == null || TextUtils.isEmpty((String)obj)) {
					Toast.makeText(UISpecificInfoActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				LoginBack back = (LoginBack) ReqJsonUtil.changeToObject((String)obj, LoginBack.class);
				String oauth_token = back.Getoauth_token();
				String oauth_token_secret = back.Getoauth_token_secret();
				if (back.Getcode() == 00000) {
					// 保存授权到本地
					SharedPreferences.Editor editor;
					SharedPreferences preferences = getSharedPreferences("idcard",0);
					editor = preferences.edit();
					editor.clear();
					editor.putString("uid", back.Getuid());
					editor.putString("oauth_token", oauth_token);
					editor.putString("oauth_token_secret", oauth_token_secret);
					editor.putString("token", back.getToken());
					editor.putInt("is_vip", back.getIs_vip());
					editor.putInt("is_approve", back.getIs_approve());
					editor.putInt("is_first", back.getIs_first());
					editor.commit();
					
					Toast.makeText(UISpecificInfoActivity.this, back.Getmessage(),Toast.LENGTH_SHORT).show();

					editUserData();
					
				} else {
					Toast.makeText(UISpecificInfoActivity.this, back.Getmessage(),Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	boolean isEmpty(TextView tv) {
		return TextUtils.isEmpty(tv.getText());
	}

	/*
	 * 提交修改数据
	 */
	public void editUserData() {
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);

		String url = Constans.editUserData;
		editUserDataBean bean = new editUserDataBean();
		
		if(isEmpty(sex_tv)) {
			Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
			return;
		}
		if (sex_tv.getText().toString().equals("男")) {
			bean.setSex("1");
		} else {
			bean.setSex("2");
		}
		
		if(isEmpty(birthday_tv)) {
			Toast.makeText(this, "请选择生日", Toast.LENGTH_SHORT).show();
			return;
		}
		bean.setBirthday(birthday_tv.getText().toString());
		
		if(isEmpty(address_tv)) {
			Toast.makeText(this, "请选择出生地", Toast.LENGTH_SHORT).show();
			return;
		}
		bean.setCity(cityID);
		bean.setProvince(provinceID);
		
		if(isEmpty(marry_tv)) {
			Toast.makeText(this, "请选择婚恋", Toast.LENGTH_SHORT).show();
			return;
		}
		bean.setMarriage(marry_tv.getText().toString());
		
		if(isEmpty(height_tv)) {
			Toast.makeText(this, "请选择身高", Toast.LENGTH_SHORT).show();
			return;
		}
		bean.setHeight(height_tv.getText().toString());
		
		if(isEmpty(weight_tv)) {
			Toast.makeText(this, "请选择体重", Toast.LENGTH_SHORT).show();
			return;
		}
		bean.setWeight(weight_tv.getText().toString());
		
		if(isEmpty(xueli_tv)) {
			Toast.makeText(this, "请选择学历", Toast.LENGTH_SHORT).show();
			return;
		}
		bean.setDegrees(xueli_tv.getText().toString());
		
		if(isEmpty(occupation_tv)) {
			Toast.makeText(this, "请选择职业", Toast.LENGTH_SHORT).show();
			return;
		}
		bean.setCareer(occupation_tv.getText().toString());
		
		if(isEmpty(income_tv)) {
			Toast.makeText(this, "请选择年收入", Toast.LENGTH_SHORT).show();
			return;
		}
		bean.setIncome(income_tv.getText().toString());
		
		if(isEmpty(faith_tv)) {
			Toast.makeText(this, "请选择信仰", Toast.LENGTH_SHORT).show();
			return;
		}
		bean.setFaith(faith_tv.getText().toString());
		
		
		if(isEmpty(smoke_tv)) {
			Toast.makeText(this, "请选择是否吸烟", Toast.LENGTH_SHORT).show();
			return;
		}
		bean.setSmoking(smoke_tv.getText().toString());
		
		if(isEmpty(drink_tv)) {
			Toast.makeText(this, "请选择是否饮酒", Toast.LENGTH_SHORT).show();
			return;
		}
		bean.setDrinking(drink_tv.getText().toString());
		
		if(isEmpty(language_tv)) {
			Toast.makeText(this, "请选择语言", Toast.LENGTH_SHORT).show();
			return;
		}
		bean.setLanguage(language_tv.getText().toString());
		
		
		String jsonstr = JsonHelper.toJsonString(bean);
		String encodesstr = "";
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("cont", encodesstr);
		
		
		showDialogFragment(false);
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if(isFinishing()) 
					return;
				removeDialogFragment();
				/*CommonBack cb = (CommonBack) ReqJsonUtil.changeToObject((String)obj, CommonBack.class);
				if (cb.getCode() == 00000) {

					Toast.makeText(UISpecificInfoActivity.this, cb.getMessage(), Toast.LENGTH_LONG).show();
					Intent intent = new Intent(UISpecificInfoActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(UISpecificInfoActivity.this, cb.getMessage(), Toast.LENGTH_LONG).show();
				}*/
				
				getChatToken();
				
			}
		});
		
	}
	
	
	private void getChatToken() {
		String url = Constans.getToken;
		AjaxParams params = new AjaxParams();
		SharedPreferences preferences = getSharedPreferences("idcard", Activity.MODE_PRIVATE);
		params.put("oauth_token", preferences.getString("oauth_token", null));
		params.put("oauth_token_secret",preferences.getString("oauth_token_secret", null));
		
		showDialogFragment(false);
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if(isFinishing()) 
					return;
				
				if(obj == null || TextUtils.isEmpty((String)obj)) {
					removeDialogFragment();
					Intent intent = new Intent(UISpecificInfoActivity.this, MainActivity.class);
					startActivity(intent);
					setResult(RESULT_OK);
					finish();
					return;
				}
				
				GetTokenVo tokenVo = (GetTokenVo) ReqJsonUtil.changeToObject((String) obj, GetTokenVo.class);
				if(tokenVo.getCode() != 200) {
					removeDialogFragment();
					Intent intent = new Intent(UISpecificInfoActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
					return;
				}
				
				// 连接融云服务器。
				try {
					RongIM.connect(tokenVo.getToken(), new RongIMClient.ConnectCallback() {
						@Override
						public void onSuccess(String s) {
							removeDialogFragment();
							Intent intent = new Intent(UISpecificInfoActivity.this, MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();
						}

						@Override
						public void onError(ErrorCode errorCode) {
							removeDialogFragment();
							Intent intent = new Intent(UISpecificInfoActivity.this, MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	

	/**
	 * 接口
	 * */
	/*
	 * public void specificInfo() { this.showDialog(); SharedPreferences sp =
	 * this.getSharedPreferences("idcard",0); final String oauth_token =
	 * sp.getString("oauth_token", null); final String oauth_token_secret =
	 * sp.getString("oauth_token_secret", null); final String ID =
	 * sp.getString("uid", null); Logger.d(tag, "ready"); this.task = new
	 * TaskUtil(this); String url = Constans.userinfo; UserInfoBean bean = new
	 * UserInfoBean(); bean.setUid(ID); String jsonstr =
	 * JsonHelper.toJsonString(bean); String encodesstr = ""; try { encodesstr =
	 * AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
	 * //System.out.println(encodesstr); Logger.d("encodesstr", encodesstr); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } AjaxParams params = new AjaxParams();
	 * params.put("oauth_token", oauth_token); params.put("oauth_token_secret",
	 * oauth_token_secret); params.put("cont", encodesstr);
	 * this.task.post(params, url); //this.task.postsync(encodesstr, url);
	 * 
	 * this.task.setType(Constans.REQ_UserInfo); }
	 */
	/**
	 * 接口
	 * */
	public void Area_List() {
		// SharedPreferences sp = this.getSharedPreferences("idcard",0);
		// final String oauth_token = sp.getString("oauth_token", null);
		// final String oauth_token_secret = sp.getString("oauth_token_secret",
		// null);
		// String url = Constans.area_list;
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// String url = Constans.editUserData;
		// AjaxParams params = new AjaxParams();
		// params.put("oauth_token", oauth_token);
		// params.put("oauth_token_secret", oauth_token_secret);
		//
		// FinalHttp http = new FinalHttp();
		// obj = http.postSync(url, params);
		// handler.sendEmptyMessage(1);
		//
		// }
		// }).start();
		//
		// handler = new Handler() {
		// @Override
		// public void handleMessage(Message msg) {
		// if (msg.what == 1) {
		JSONArray jsonArray1 = null;
		try {
			for (int k = 0; k < 34; k++) {
				String l = k + "";
				// jsonObject1 = new JSONObject(obj.toString());
				// JSONObject jsonObject2 = jsonObject1.getJSONObject(l);
				JSONObject jsonObject2 = mJsonObj.getJSONObject(l);
				jsonArray1 = jsonObject2.getJSONArray("child");
				String child = jsonObject2.getString("child");
				String id = jsonObject2.getString("id");
				String title = jsonObject2.getString("title");
				String pid = jsonObject2.getString("pid");
				String level = jsonObject2.getString("level");
				province[k] = title;
				map1.put(title, id);
				String[] mCitisData = new String[jsonArray1.length()];
				String id_city = null, title_city = null, pid_city = null, level_city = null;
				for (int j = 0; j < jsonArray1.length(); j++) {
					JSONObject json_city = (JSONObject) jsonArray1.opt(j);
					JSONArray jsonArray2 = json_city.getJSONArray("child");
					// String child_city = json_city.getString("child");
					id_city = json_city.getString("id");
					title_city = json_city.getString("title");
					pid_city = json_city.getString("pid");
					level_city = json_city.getString("level");
					mCitisData[j] = title_city;
					map_city_id.put(title_city, id_city);
					// for(int i = 0;i<jsonArray2.length();i++){
					// JSONObject json_area = (JSONObject) jsonArray2.opt(i);
					// String id_area = json_area.getString("id");
					// String title_area = json_area.getString("title");
					// String pid_area = json_area.getString("pid");
					// String level_area = json_area.getString("level");
					// String[] mAreaData = new String[jsonArray2.length()];
					// mAreaData[i] = title_area;
					// map_area.put(title_city,mAreaData);
					// map_area_id.put(title_area,id_area);
					// }
				}
				map_city.put(title, mCitisData);

				// saveAsFileWriter(json_city);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// }
		// }
		// };
	}

	// 设置底部，宽full
	public void dialogShow() {
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (display.getWidth()); // 设置宽度
		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
	}

	@Override
	// 时间滚轮
	public void onSaveSelectedDate(DateSelectorWheelView dateWheelView, View view) {
		// TODO Auto-generated method stub
		String birthDate = dateWheelView.getSelectedDate();
		String[] dataStr = birthDate.split("-");
		Date date = new Date();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdFormat.format(date);
		String[] nowStr = nowDate.split("-");
		int birthYear = Integer.parseInt(dataStr[0]);
		int birthMonth = Integer.parseInt(dataStr[1]);
		int birthDay = Integer.parseInt(dataStr[2]);
		int nowYear = Integer.parseInt(nowStr[0]);
		int nowMonth = Integer.parseInt(nowStr[1]);
		int nowDay = Integer.parseInt(nowStr[2]);

		if (birthYear > nowYear) {// 判断用户所选的出生日期是否有误
			Toast.makeText(getApplicationContext(), "你所选的日期有误", Toast.LENGTH_SHORT).show();
			return;
		} else if (birthYear == nowYear) {
			if (birthMonth > nowMonth) {
				Toast.makeText(getApplicationContext(), "你所选的日期有误", Toast.LENGTH_SHORT).show();
				return;
			} else if (birthMonth == nowMonth && birthDay > nowDay) {
				Toast.makeText(getApplicationContext(), "你所选的日期有误", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		birthday_tv.setText(birthYear + "-" + birthMonth + "-" + birthDay + "");

	}

	// 时间戳转化为时间
	public String getStrTime(String time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = null;
		if (time.equals("")) {
			return "";
		}
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		long loc_time = Long.valueOf(time);
		re_StrTime = sdf.format(new Date(loc_time * 1000L));
		return re_StrTime;
	}

	// // 监听屏幕是否被点击，隐藏下边菜单栏
	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// dialog.dismiss();
	// dialogBuilder.dismiss();
	// return true;
	//
	// }
	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	// private void updateAreas() {
	// // TODO Auto-generated method stub
	// int pCurrent = wheelView_city.getCurrentItem();
	// mCurrentCityName = map_city.get(mCurrentProviceName)[pCurrent];
	//
	// String[] areas = map_area.get(mCurrentCityName);
	//
	// if (areas == null)
	// {
	// areas = new String[] { "" };
	// }
	// wheelView_area.setAdapter(new ArrayWheelAdapter<String>(areas));
	// wheelView_area.setCurrentItem(0);
	// }
	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		// TODO Auto-generated method stub
		int pCurrent = wheelView_province.getCurrentItem();
		mCurrentProviceName = province[pCurrent];
		String[] cities = map_city.get(mCurrentProviceName);

		if (cities == null) {
			cities = new String[] { "" };
		}
		wheelView_city.setAdapter(new ArrayWheelAdapter<String>(cities));
		wheelView_city.setCurrentItem(0);
		mCurrentCityName = cities[wheelView_city.getCurrentItem()];
		// updateAreas();
	}

	// 保存字符串到文件中
	private void saveAsFileWriter(String content) {

		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(file);
			fwriter.write(content);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void initJsonData() {
		try {
			StringBuffer sb = new StringBuffer();
			InputStream is = getAssets().open("city.json");
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1) {
				sb.append(new String(buf, 0, len, "gbk"));
			}
			is.close();
			mJsonObj = new JSONObject(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == wheelView_province) {
			updateCities();
		}
	}

	public void showChoose(View view) {
		Toast.makeText(this, mCurrentProviceName + mCurrentCityName + mCurrentAreaName, 1).show();
	}
}

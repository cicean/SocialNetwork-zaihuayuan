package com.gzqining.inthegarden.gridview;

import io.rong.imkit.RongIM;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.adapter.AlbumListAdapter;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.gridview.PullToRefreshView.OnFooterRefreshListener;
import com.gzqining.inthegarden.gridview.PullToRefreshView.OnHeaderRefreshListener;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.AlbumBack;
import com.gzqining.inthegarden.vo.AlbumBackList;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.UserInfoBack;
import com.gzqining.inthegarden.vo.UserInfoBean;
import com.gzqining.inthegarden.vo.unAndoFollowBean;

/***
 * 
 * ScrollView 嵌套ListView 嵌套GridView的上拉加载更多，下拉刷新。
 * 
 * 逻辑在适配器做了处理
 * 
 * 我们只让ListView加载2个数据Item，第一个是item对象，第二个是一个对象
 * 
 * 
 * @author lyy
 * 
 */
public class PersonActivity extends BaseActivity implements OnTouchListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	private String tag = PersonActivity.class.getName();
	
	private MyAdapter myAdapter;
	private MyAdapter myAdapterPrivate;
	private boolean isVip;
	
	// 自定义的GridView的上下拉动刷新
	private PullToRefreshView mPullToRefreshView;
	private MyListView gridView;
	private MyListView gridViewPrivate;
	private List<Integer> data;
	private List<String> gridViewData;
	private View view1, viewPrivate, view2;// 下划线
	private View tabLayout1, tabLayout2;
	private FinalBitmap fb;
	private URL myFileUrl = null;
	private ImageView person_homepage_img;
	private Button back_btn, sixin_btn, guanzhu_btn;
	// tab
	private TextView guanzhu_tv, fans_tv, nickname_person;
	public TextView sex_tv, birthday_tv, address_tv, marrage_tv, height_tv, weight_tv, degrees_tv, faith_tv, income_tv, drink_tv, smoke_tv, occupation_tv, language_tv;
	private LinearLayout my_album_tv, my_album_tv_private, personinfo_tv;
	private int index;
	private String nickname, follower;
	private static String following;
	private String uid;
	// private PullToRefreshGridView gridView = null;
	private AlbumListAdapter adapter = null;
	private Object albumObj;
	private Handler handler;
	private String encodesstr = "";
	private int page = 0;
	private int pagePrivate = 0;
	private LinearLayout ll1;
	private LinearLayout  layout;
	private int value;
	
	// private ScrollView scrollView;
	private int y;
	
	//用户信息
	UserInfoBack back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_person);

		initView();
		initEvent();
		
		uid = this.getIntent().getStringExtra("uid");
		nickname = this.getIntent().getStringExtra("nickname");
		// 是否关注：0未关注，1已关注 （following，follower都为1则为互相关
		follower = this.getIntent().getStringExtra("follower");
		// 是否粉丝：0不是当前账号粉丝，1是（following，follower都为1则为互相关注）
		following = this.getIntent().getStringExtra("following");
		System.out.println("follower==="+follower);
		System.out.println("following==="+following);
		isVip =  this.getIntent().getBooleanExtra("isVip", false);

		fb = FinalBitmap.create(this);

		gridViewData = new ArrayList<String>();

		nickname_person.setText(nickname);
		// 判断两人的关注状态
		if (following.equals("0")) {
			guanzhu_btn.setText("关注");
		} else {
			guanzhu_btn.setText("已关注");
		}
		
		person_Info();
	}

	// 控件初始化
	private void initView() {
		// 标题
		nickname_person = (TextView) findViewById(R.id.nickname_person_homepage);
		// 返回
		back_btn = (Button) findViewById(R.id.top_layout_personInfo_back_btn);

		person_homepage_img = (ImageView) findViewById(R.id.head_img_person_homepage);

		android.view.ViewGroup.LayoutParams para;
		para = person_homepage_img.getLayoutParams();
		// 获取屏幕宽高
		DisplayMetrics dm = getResources().getDisplayMetrics();
		para.height = dm.widthPixels;
		para.width = dm.widthPixels;

		guanzhu_tv = (TextView) findViewById(R.id.guanzhu_tv);
		fans_tv = (TextView) findViewById(R.id.fans_tv);

		sixin_btn = (Button) findViewById(R.id.sixin_btn);
		guanzhu_btn = (Button) findViewById(R.id.guanzhu_btn);
		my_album_tv = (LinearLayout) findViewById(R.id.my_album_tv);
		my_album_tv_private = (LinearLayout) findViewById(R.id.my_album_tv_private);
		personinfo_tv = (LinearLayout) findViewById(R.id.personinfo_tv);

		view1 = findViewById(R.id.view1);
		viewPrivate = findViewById(R.id.viewPrivate);
		view2 = findViewById(R.id.view2);
		layout = (LinearLayout) findViewById(R.id.layoutView);
		// 个人
		ll1 = (LinearLayout) findViewById(R.id.ll1);

		sex_tv = (TextView) findViewById(R.id.sex_personinfo_tv);
		birthday_tv = (TextView) findViewById(R.id.birthday_personinfo_tv);
		address_tv = (TextView) findViewById(R.id.address_personinfo_tv);
		marrage_tv = (TextView) findViewById(R.id.marry_personinfo_tv);
		height_tv = (TextView) findViewById(R.id.height_personinfo_tv);
		weight_tv = (TextView) findViewById(R.id.weight_personinfo_tv);
		degrees_tv = (TextView) findViewById(R.id.xueli_personinfo_tv);

		faith_tv = (TextView) findViewById(R.id.faith_personinfo_tv);
		income_tv = (TextView) findViewById(R.id.income_personinfo_tv);
		drink_tv = (TextView) findViewById(R.id.drink_personinfo_tv);
		smoke_tv = (TextView) findViewById(R.id.smoke_personinfo_tv);
		occupation_tv = (TextView) findViewById(R.id.occupation_personinfo_tv);
		language_tv = (TextView) findViewById(R.id.language_personinfo_tv);

		tabLayout1 = findViewById(R.id.tabLayout1);
		tabLayout2 = findViewById(R.id.tabLayout2);

		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		gridView = (MyListView) findViewById(R.id.gridView1);
		gridViewPrivate = (MyListView) findViewById(R.id.gridViewPrivate);
		
		myAdapter = new MyAdapter(this, 0, isVip);
		myAdapterPrivate = new MyAdapter(this, 1, isVip);
		
		gridView.setAdapter(myAdapter);
		gridViewPrivate.setAdapter(myAdapterPrivate);

		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mPullToRefreshView.setLastUpdated(new Date().toLocaleString());

	}

	// 事件初始化
	private void initEvent() {
		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 私信
		sixin_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				RongIM.getInstance().startPrivateChat(PersonActivity.this, uid, nickname);
			}
		});
		// 关注
		guanzhu_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				guanzhu_btn.setClickable(false);
				attentionChange();
			}
		});
		// 相册
		my_album_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				myAlbum();
			}
		});
		// 私密
		my_album_tv_private.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				myAlbumPrivate();
			}
		});
		// 个人
		personinfo_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				myInfo();
			}
		});
		
		AlbumHttpPost(1);
		AlbumHttpPostPrivate(1);
		
	}
	
	public static int max(int x,int y){
		if(x>y)
			return x;
		else
			return y;
	}
	
	private int getH() {
		int h1 = gridView.getHeight();
		int h2 = gridViewPrivate.getHeight();
		int h3 =1650;
		value = max(max(h1,h2),h3);
		return value;
		
		
	}

	// 相册
	@SuppressLint("NewApi")
	private void myAlbum() {
		
		// AlbumHttpPost();
		tabLayout1.setVisibility(View.VISIBLE);
		view1.setVisibility(View.VISIBLE);
		tabLayout2.setVisibility(View.GONE);
		viewPrivate.setVisibility(View.GONE);
		ll1.setVisibility(View.GONE);
		view2.setVisibility(View.GONE);
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layout.getLayoutParams(); // 取控件mGrid当前的布局参数
		linearParams.height = getH();
		layout.setLayoutParams(linearParams); 
		
	}
	
	// 私密
	@SuppressLint("NewApi")
	private void myAlbumPrivate() {
		
		tabLayout1.setVisibility(View.GONE);
		view1.setVisibility(View.GONE);
		tabLayout2.setVisibility(View.VISIBLE);
		viewPrivate.setVisibility(View.VISIBLE);
		ll1.setVisibility(View.GONE);
		view2.setVisibility(View.GONE);
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layout.getLayoutParams(); // 取控件mGrid当前的布局参数
		linearParams.height = getH();	  
		layout.setLayoutParams(linearParams);
	}

	// 个人
	private void myInfo() {
		
		// person_Info();
		tabLayout1.setVisibility(View.GONE);
		view1.setVisibility(View.GONE);
		tabLayout2.setVisibility(View.GONE);
		viewPrivate.setVisibility(View.GONE);
		ll1.setVisibility(View.VISIBLE);
		view2.setVisibility(View.VISIBLE);
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layout.getLayoutParams(); // 取控件mGrid当前的布局参数
		linearParams.height = 1650;
	     layout.setLayoutParams(linearParams); 
	}
	
	// 获取私密相册
	private void AlbumHttpPost(final int indexPage) {
		String cont = String.format("{\"uid\":\"%s\",\"privacy\":\"%s\",\"page\":\"%s\"}", uid, 0, indexPage);
		try {
			cont = AesUtils.Encrypt(cont, CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final String contParam = cont;
		
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		
		String url = Constans.album;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("page", page + "");
		params.put("cont", contParam);
		
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			
			@Override
			public void onCallback(Object obj) {
				if(isFinishing()) return;
				
				mPullToRefreshView.onHeaderRefreshComplete("更新于:" + Calendar.getInstance().getTime().toLocaleString());
				mPullToRefreshView.onHeaderRefreshComplete();
				mPullToRefreshView.onFooterRefreshComplete();
				if(TextUtils.isEmpty((String)obj)) {
					return;
				}
				
				AlbumBack albumBack = (AlbumBack) ReqJsonUtil.changeToObject(obj.toString(), AlbumBack.class);
				if(albumBack != null && albumBack.getData() != null) {
					if(indexPage > albumBack.getTotalPages()) {
						Toast.makeText(PersonActivity.this, "没有更多相片了！", Toast.LENGTH_SHORT).show();
						return;
					}
					
					List<String> gridViewData = new ArrayList<String>();
					for(AlbumBackList item : albumBack.getData()) {
						gridViewData.add(item.getSavepath());
					}
					
					myAdapter.setGridViewData(1, gridViewData);
					myAdapter.notifyDataSetChanged();
					page = indexPage;
				}
				
				tabLayout1.findViewById(R.id.nodataLayout1).setVisibility(myAdapter.isEmpty() ? View.VISIBLE : View.INVISIBLE);
				
			}
		});
	}
	
	
	
	
	// 获取私密相册
	private void AlbumHttpPostPrivate(final int indexPage) {
		String cont = String.format("{\"uid\":\"%s\",\"privacy\":\"%s\",\"page\":\"%s\"}", uid, 1, indexPage);
		try {
			cont = AesUtils.Encrypt(cont, CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final String contParam = cont;
		
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		
		String url = Constans.album;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("page", page + "");
		params.put("cont", contParam);
		
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			
			@Override
			public void onCallback(Object obj) {
				if(isFinishing()) return;
				
				mPullToRefreshView.onHeaderRefreshComplete("更新于:" + Calendar.getInstance().getTime().toLocaleString());
				mPullToRefreshView.onHeaderRefreshComplete();
				mPullToRefreshView.onFooterRefreshComplete();
				if(TextUtils.isEmpty((String)obj)) {
					return;
				}
				
				AlbumBack albumBack = (AlbumBack) ReqJsonUtil.changeToObject(obj.toString(), AlbumBack.class);
				if(albumBack != null && albumBack.getData() != null) {
					if(indexPage > albumBack.getTotalPages()) {
						Toast.makeText(PersonActivity.this, "没有更多相片了！", Toast.LENGTH_SHORT).show();
						return;
					}
					
					List<String> gridViewData = new ArrayList<String>();
					for(AlbumBackList item : albumBack.getData()) {
						gridViewData.add(item.getSavepath());
					}
					
					myAdapterPrivate.setGridViewData(1, gridViewData);
					myAdapterPrivate.notifyDataSetChanged();
					pagePrivate = indexPage;
				}
				
				tabLayout2.findViewById(R.id.nodataLayout2).setVisibility(myAdapterPrivate.isEmpty() ? View.VISIBLE : View.INVISIBLE);
				
			}
		});
	}

	// 关注状态变化接口
	public void attentionChange() {
		String type;
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		if (following.equals("0")) {// 关注
			type = "0";
		} else {
			type ="1";
		}
		String url = Constans.unAndoFollow;
		unAndoFollowBean bean = new unAndoFollowBean();
		bean.setFid(uid);
		bean.setType(type);
		String jsonstr = JsonHelper.toJsonString(bean);
		String encodesstr = "";
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("cont", encodesstr);
		this.task.post(params, url);
		this.task.setType(Constans.REQ_unAndoFollow);
	}

	// 个人资料接口
	public void person_Info() {
		Logger.d(tag, "ready");
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		String url = Constans.userinfo;
		UserInfoBean bean = new UserInfoBean();
		bean.setUid(uid);
		String jsonstr = JsonHelper.toJsonString(bean);
		String encodesstr = "";
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("cont", encodesstr);
		this.task.post(params, url);
		this.task.setType(Constans.REQ_UserInfo);
	}

	/**
	 * 接口返回
	 * */
	public void taskCallBack(String jsonObject, int type) {
		Logger.d(tag, "type:" + type + ",jsonObject:" + jsonObject);
		System.out.println(tag + "   " + type + ":" + jsonObject);
		switch (type) {
		case Constans.REQ_UserInfo:
			back = (UserInfoBack) ReqJsonUtil.changeToObject(jsonObject.toString(), UserInfoBack.class);
			if (back.getCode().equals("00000")) {
				fans_tv.setText(back.getCount_info().getFollower_count()+"");
				guanzhu_tv.setText(back.getCount_info().getFollowing_count()+"");
				// 动态设置imageview显示的长宽
				// android.view.ViewGroup.LayoutParams para;
				// para = person_homepage_img.getLayoutParams();
				// //获取屏幕宽高
				// DisplayMetrics dm = getResources().getDisplayMetrics();
				// para.height = dm.widthPixels;
				// para.width = dm.widthPixels;
				// person_homepage_img.setLayoutParams(para);
				fb.display(person_homepage_img, back.getAvatar_original());
				if (back.getSex().equals("1")) {
					sex_tv.setText("女");
				} else {
					sex_tv.setText("男");
				}
				Map map = new HashMap();
				try {

					JSONObject jsonObject1 = new JSONObject(jsonObject.toString());
					JSONArray jsonArray = jsonObject1.getJSONArray("user_profile");
					// StringBuffer sb = new StringBuffer();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject json = (JSONObject) jsonArray.opt(i);
						String field_id = json.getString("field_id");
						String field_key = json.getString("field_key");
						String field_name = json.getString("field_name");
						String form_type = json.getString("form_type");
						String form_default_value = json.getString("form_default_value");
						String field_data = json.getString("field_data");
						map.put(field_name, field_data);
					}
					System.out.println(map);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String date = getStrTime(map.get("生日").toString());
				birthday_tv.setText(date);
				address_tv.setText((CharSequence) map.get("出生地"));
				marrage_tv.setText((CharSequence) map.get("婚恋"));
				height_tv.setText((CharSequence) map.get("身高"));
				weight_tv.setText((CharSequence) map.get("体重"));
				degrees_tv.setText((CharSequence) map.get("学历"));
				faith_tv.setText((CharSequence) map.get("信仰"));
				income_tv.setText((CharSequence) map.get("收入"));
				drink_tv.setText((CharSequence) map.get("饮酒"));
				smoke_tv.setText((CharSequence) map.get("吸烟"));
				occupation_tv.setText((CharSequence) map.get("职业"));
				language_tv.setText((CharSequence) map.get("语言"));
			} else {
				Toast.makeText(PersonActivity.this, back.getMessage(), Toast.LENGTH_LONG).show();
			}
			break;
		case Constans.REQ_unAndoFollow:
			
			CommonBack cb = (CommonBack) ReqJsonUtil.changeToObject(jsonObject.toString(), CommonBack.class);
				if (following.equals("0")) {
					guanzhu_btn.setText("已关注");
					following = "1";
				} else if (following.equals("1")) {
					guanzhu_btn.setText("关注");
					following = "0";
				}
				guanzhu_btn.setClickable(true);
				person_Info();
				Toast.makeText(PersonActivity.this, cb.getMessage(), Toast.LENGTH_LONG).show();
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// 可以监听到ScrollView的滚动事件
			// y = scrollView.getScrollY();
			// System.out.println(scrollView.getScrollY());
		}
		return false;
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// scrollView.scrollTo(0, y);// 改变滚动条的位置
		}
	};

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

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		AlbumHttpPostPrivate(1);
		if(view1.getVisibility() == View.VISIBLE) {
			AlbumHttpPost(1);
		} else if(viewPrivate.getVisibility() == View.VISIBLE){
			AlbumHttpPostPrivate(1);
		} else {
			mPullToRefreshView.onHeaderRefreshComplete();
		}
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		if(view1.getVisibility() == View.VISIBLE) {
			AlbumHttpPost(page+1);
		} else if(viewPrivate.getVisibility() == View.VISIBLE){
			AlbumHttpPostPrivate(pagePrivate+1);
		} else {
			mPullToRefreshView.onFooterRefreshComplete();	
		}
	}

}

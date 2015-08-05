package com.gzqining.inthegarden.act;

import net.tsz.afinal.FinalBitmap;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

@SuppressLint("SetJavaScriptEnabled") public class WorldSpecificActivity extends BaseActivity {
	String tag = WorldSpecificActivity.class.getName();
	private String news;
	FinalBitmap fb;
	private ImageButton back;
	private WebView world_specific;
//	private ImageView act_img;
//	private TextView article_title;
//	private TextView article_date;
//	private TextView article;
	private View my_view;
	String id;
	String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_world_specific);
		AppManager.getAppManager().addActivity(this);
		fb=FinalBitmap.create(this);
		url = getIntent().getStringExtra("url");
		id = getIntent().getStringExtra("news_id");
		
		init();
		
//		news_specific();
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	private void init() {
		// TODO Auto-generated method stub
		back = (ImageButton) findViewById(R.id.top_layout_worldspecific_back_btn);
		world_specific = (WebView) findViewById(R.id.world_specific);
		my_view = (View) findViewById(R.id.my_view);
//		act_img = (ImageView) findViewById(R.id.act_img_world_specific);
//		article_title = (TextView) findViewById(R.id.article_title_world_specific);
//		article_date = (TextView) findViewById(R.id.date_world_specific);
//		article = (TextView) findViewById(R.id.article);
		//设置webView属性，能够执行JavaScript脚本
		world_specific.getSettings().setJavaScriptEnabled(true);
		//加载需要显示的网页
		world_specific.loadUrl(url);
		//设置web视图
		world_specific.setWebViewClient(new webViewClient());
		
	}
	class webViewClient extends WebViewClient{
		
		public boolean shouldOverriderUrlLoading (WebView view, String url){
			view.loadUrl(url);
			return true;
		}
		
		public void onPageFinished(WebView view,String url){
			my_view.setVisibility(View.GONE);
			super.onPageFinished(view, url);
		}
		
	}
//	public void news_specific(){
//	    Logger.d(tag, "ready");
//		SharedPreferences sp = this.getSharedPreferences("idcard",0);
//		final String oauth_token = sp.getString("oauth_token", null);
//		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
//		String url = Constans.world;
//		NewsBean bean = new NewsBean();
//		bean.setId(id);
//		String jsonstr = JsonHelper.toJsonString(bean);
//		String encodesstr = "";
//		try {
//			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
//			Logger.d("encodesstr", encodesstr);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		AjaxParams params = new AjaxParams();
//		params.put("oauth_token", oauth_token);
//		params.put("oauth_token_secret", oauth_token_secret);
//		params.put("cont",encodesstr);		
//		this.task.post(params, url);
//		this.task.setType(Constans.REQ_World);
//	}
//	/**
//	 * 接口返回
//	 * */
//	@Override
//	public void taskCallBack(String jsonObject, int type) {
//		Logger.d(tag, "type:" + type + ",jsonObject:" + jsonObject);
//		this.canelDialog();
//		switch (type) {
//		case Constans.REQ_World:
//
//			News_ListBack back = (News_ListBack) ReqJsonUtil.changeToObject(jsonObject, News_ListBack.class);
//			world_specific.loadUrl(url);
////			article_title.setText(back.getData().getNews_title());
////			String article_time=DateUtil.getDateToString(Long.parseLong(back.getData().getCreated()),"yyyy-MM-dd | hh:mm");
////			
////			article_date.setText(article_time);
////			
////			article.setText(back.getData().getNews_content());
////			fb.display(act_img, back.getData().getImage());
//			break;
//		}
//	}
}

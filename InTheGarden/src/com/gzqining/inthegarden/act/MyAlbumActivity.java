package com.gzqining.inthegarden.act;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.adapter.AlbumSelfListAdapter;
import com.gzqining.inthegarden.album.albumSelect;
import com.gzqining.inthegarden.common.Base64Coder;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.gridview.PersonActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.AlbumBack;
import com.gzqining.inthegarden.vo.AlbumBean;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class MyAlbumActivity extends BaseActivity implements OnClickListener{
	Bitmap bitmap = null;
	private PullToRefreshGridView gridView = null;
	AlbumSelfListAdapter adapter = null;
	private RelativeLayout menu_album;//下面隐藏选项
	private Button cancel_myinfo;//取消
	private Button takephoto;//拍照
	private Button choise_from_album;//从手机相册选择
	private ImageButton back_btn,add_btn;
	List<String> picPath = new ArrayList<String>();
	private int mItemCount = 10;
	private int page = 1;
	Object albumObj;
	Handler albumHandler;
	String encodesstr = "";
	String tp;//图片的base64
	Object obj;
	FinalBitmap fb;
	static Handler handler;
	CommonBack back;
	private ImageView no_photo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_person_album);
		
		gridView = (PullToRefreshGridView)findViewById(R.id.my_person_album_view);
		menu_album = (RelativeLayout) findViewById(R.id.menu_album);
//		gridView.setAdapter(adapter);
		back_btn = (ImageButton)findViewById(R.id.top_layout_album_back_btn);
		add_btn = (ImageButton)findViewById(R.id.top_layout_album_add_btn);
		no_photo = (ImageView) findViewById(R.id.image_no_photo);
		cancel_myinfo = (Button) findViewById(R.id.cancel_myalbum);
		takephoto = (Button) findViewById(R.id.takephoto_album);
		choise_from_album = (Button) findViewById(R.id.album_choose_from_album);
//		no_photo.setVisibility(View.VISIBLE);
//		gridView.setVisibility(View.GONE);
		getAlbum();
		initEvents();
		initIndicator();
		gridView.setOnRefreshListener(new OnRefreshListener2<GridView>()
				{

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView)
			{
				Log.e("TAG", "onPullDownToRefresh"); // Do work to
				String label = DateUtils.formatDateTime(
						getApplicationContext(),
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(label);
				page = 1;
				new GetDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView)
			{
				Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
													// the list here.
				new GetDataTask().execute();
			}
		});
	}
	private void initIndicator()
	{
		ILoadingLayout startLabels = gridView.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("松开载入更多...");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = gridView.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在刷新...");// 刷新时
		endLabels.setReleaseLabel("松开载入更多...");// 下来达到一定距离时，显示的提示
	}
	private void getAlbum(){
		SharedPreferences sp = this.getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		String uid = sp.getString("uid", null);
		AlbumBean albumBean = new AlbumBean();
		albumBean.setUid(uid);
		String jsonstr = JsonHelper.toJsonString(albumBean);
		
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = Constans.album;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
				params.put("page", page+"");
				params.put("cont", encodesstr);
				
				FinalHttp http = new FinalHttp();
				albumObj =  http.postSync(url, params);
				albumHandler.sendEmptyMessage(1);
				
			}
		}).start();

		albumHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					try {
						
						AlbumBack albumBack = (AlbumBack) ReqJsonUtil.changeToObject(albumObj.toString(), AlbumBack.class);
						if((page-1)!=albumBack.getTotalPages()){
						if (albumBack.getCode().equals("00000")) {
							
							if(albumBack.getData()!=null){
							
								//把后台返回的图片地址一一取出后再装入picPath
//								if (page==1){
////									Resources res = getResources();
////									Bitmap btm = BitmapFactory.decodeResource(res, R.drawable.add_picture);
//									picPath.add(0,"btm");
//									for(int j = 0;j<albumBack.getData().size();j++){
//										picPath.add(j+1,albumBack.getData().get(j).getSavepath());
//									}
//									System.out.println(picPath);
//									//把picPath装入adapter
//									for(int i=0;i<picPath.size();i++){
//										adapter = new AlbumSelfListAdapter(MyAlbumActivity.this, picPath);
//										adapter.notifyDataSetChanged();
//									}
//								}else{
////									picPath.clear();
//							for(int j = 0;j<albumBack.getData().size();j++){
//								picPath.add(albumBack.getData().get(j).getSavepath());
//							}
//							System.out.println("---------------------------------------");
//							System.out.println(picPath);
//									System.out.println(picPath);
//									//把picPath装入adapter
//									for(int i=0;i<picPath.size();i++){
							adapter = new AlbumSelfListAdapter(MyAlbumActivity.this, albumBack.getData());
//									}
//								}
//							adapter = new AlbumSelfListAdapter(MyAlbumActivity.this, picPath);
							page++;
					        gridView.setAdapter(adapter);
							adapter.notifyDataSetChanged();//在修改适配器绑定的数组后，不用重新刷新Activity，通知Activity更新ListView
							//Toast.makeText(getActivity(), albumBack.getMessage(),Toast.LENGTH_LONG).show();
							
							
							
						} else{
//								Toast.makeText(MyAlbumActivity.this, "暂时没有相册信息！", Toast.LENGTH_LONG).show();
							no_photo.setVisibility(View.VISIBLE);
							gridView.setVisibility(View.GONE);
							}
					}else {
							//Toast.makeText(getActivity(), albumBack.getMessage(),Toast.LENGTH_LONG).show();
						}
						}else{
							Toast.makeText(MyAlbumActivity.this, "没有更多相片了！", Toast.LENGTH_LONG).show();
						}	
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
	}

	/**
	 * 绑定事件
	 */
	private void initEvents() {

		back_btn.setOnClickListener(this);
		add_btn.setOnClickListener(this);
		gridView.setOnItemClickListener(mOnClickListener);
		cancel_myinfo.setOnClickListener(this);
		takephoto.setOnClickListener(this);
		choise_from_album.setOnClickListener(this);
//		gridView.setOnItemLongClickListener(mOnLongClickListener);
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.top_layout_album_back_btn:
			
			finish();
				
			break;
		case R.id.top_layout_album_add_btn:
			menu_album.setVisibility(View.VISIBLE);
//			Intent intent = new Intent(MyAlbumActivity.this,albumSelect.class);
//    		startActivity(intent);
			break;
		case R.id.album_choose_from_album:
			Intent intent = new Intent(MyAlbumActivity.this,albumSelect.class);
    		startActivity(intent);
    		menu_album.setVisibility(View.GONE);
			break;	
		case R.id.cancel_myalbum:
			menu_album.setVisibility(View.GONE);
			break;
		case R.id.takephoto_album:
			Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//下面这句指定调用相机拍照后的照片存储的路径
			long data = new Date().getTime();
			intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment.getExternalStorageDirectory(),data+".jpg")));
//			intent1.setClass(MyAlbumActivity.this, ShowPictrue.class);
			startActivityForResult(intent1, 2);
			menu_album.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
		public void onItemClick(AdapterView<?> mAdapter, View v, int position,long id) {
                // TODO Auto-generated method stub
//        	if(position==0){
//        		
//        	}
        }
    };

//	private AdapterView.OnItemLongClickListener mOnLongClickListener = new AdapterView.OnItemLongClickListener() {
//	@Override
//	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
//						return false;
//	        // TODO Auto-generated method stub
//		}
//	};
    private class GetDataTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params)
		{
			try
			{
				Thread.sleep(2000);
			} catch (InterruptedException e)
			{
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			getAlbum();
//			picPath.add("" + mItemCount++);
			adapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			gridView.onRefreshComplete();
		}
	}
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			startPhotoZoom(data.getData());
			break;
		// 如果是调用相机拍照时
		case 2:
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/xiaoma.jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话，
			 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException
			 * 
			 */
			if(data != null){
				setPicToView(data);
			}
			break;
		default:
			break;
	
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
		 * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
		 * 制做的了...吼吼
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray();
			tp = new String(Base64Coder.encodeLines(b));
			toPost(tp);
//			MineHttpPost();
			/**
			 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上
			 * 传到服务器，QQ头像上传采用的方法跟这个类似
			 */
			
			/*ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray();
			// 将图片流以字符串形式存储下来
			
			tp = new String(Base64Coder.encodeLines(b));*/
			//这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了，
			//服务器处理的方法是服务器那边的事了，吼吼
			
			//如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换
			//为我们可以用的图片类型就OK啦...吼吼
			/*Bitmap dBitmap = BitmapFactory.decodeFile(tp);
			Drawable drawable1 = new BitmapDrawable(dBitmap);*/
			
//			head_img.setImageDrawable(drawable);
			
		}
	}
	private void toPost(String string) {
		SharedPreferences sp = this.getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				long data = new Date().getTime();
				String url = Constans.upload_picture;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
				params.put("img",tp);
				params.put("expand","jpg");
				params.put("info","");
				params.put("name",data+"");
//				params.put("imgId","0");
				FinalHttp http = new FinalHttp();
				albumObj =  http.postSync(url, params);
				mHandler.sendEmptyMessage(1);
			}
		}).start();
}
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg){
			switch(msg.what){
			case 1:
//				canelDialog();
				try {
					CommonBack back = (CommonBack) ReqJsonUtil.changeToObject(albumObj.toString(), CommonBack.class);
					if(back.getCode() == 00000){
						Intent intent =new Intent(MyAlbumActivity.this, MyAlbumActivity.class);
						startActivity(intent);
						finish();
					}else{
						Toast.makeText(MyAlbumActivity.this, back.getMessage(),Toast.LENGTH_LONG).show();
					}
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
  //监听屏幕是否被点击，隐藏下边菜单栏
  	@Override
  	public boolean onTouchEvent(MotionEvent event){
  		menu_album.setVisibility(View.GONE);
  		return false;
  	}
}

package com.gzqining.inthegarden.act;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.app.ShellUtil;
import com.gzqining.inthegarden.app.ui.ShellMyAlbumActivity;
import com.gzqining.inthegarden.common.Base64Coder;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.Util;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.UserInfoBack;
import com.gzqining.inthegarden.vo.UserInfoBean;
import com.gzqining.inthegarden.vo.editUserDataBean;

public class MyInfoActivity extends BaseActivity implements OnClickListener {

	private ImageButton back_btn;// 顶左部返回
	private TextView nickname_tv;
	private TextView sign_tv;
	private RelativeLayout menu_mine;// 下面隐藏选项
	private RelativeLayout nickname_rl;// 昵称
	private RelativeLayout sign_rl;// 签名
	private RelativeLayout infodetail_rl;
	private RelativeLayout head_rl;// 头像
	private RelativeLayout album_rl;
	private RelativeLayout phone_number_rl;
	private ImageView head_img;// 头像
	private Button cancel_myinfo;// 取消
	private Button takephoto;// 拍照
	private Button choise_from_album;// 从手机相册选择
	private Button album_addpw;// 相片加密
	Object mineObj;
	FinalBitmap fb;
	Bitmap photo;
	static Handler mineHandler;
	String encodesstr = "";
	CommonBack back_cb;
	UserInfoBack back_uib;
	String headImg;// 图片url
	String tp;// 图片的base64
	String sign;
	String nickName;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myinfo);
		initView();
		initEvents();

	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		back_btn = (ImageButton) findViewById(R.id.top_layout_myinfo_back_btn);
		menu_mine = (RelativeLayout) findViewById(R.id.menu_mine);
		nickname_rl = (RelativeLayout) findViewById(R.id.nickname_rl);
		sign_rl = (RelativeLayout) findViewById(R.id.sign_rl);
		infodetail_rl = (RelativeLayout) findViewById(R.id.infodetail_rl);
		head_rl = (RelativeLayout) findViewById(R.id.head_rl);
		album_rl = (RelativeLayout) findViewById(R.id.album_rl);
		phone_number_rl = (RelativeLayout) findViewById(R.id.phone_number_rl);
		head_img = (ImageView) findViewById(R.id.head_img);
		nickname_tv = (TextView) findViewById(R.id.nickname_tv);
		sign_tv = (TextView) findViewById(R.id.sign_tv);
		cancel_myinfo = (Button) findViewById(R.id.cancel_myinfo);
		takephoto = (Button) findViewById(R.id.takephoto_mine);
		choise_from_album = (Button) findViewById(R.id.choose_from_album);
		album_addpw = (Button) findViewById(R.id.album_addpw);
		fb = FinalBitmap.create(this);
		mineInfoPost();
	}

	/**
	 * 绑定事件
	 */
	private void initEvents() {

		menu_mine.setVisibility(View.GONE);// 设置下面选项不可见
		head_rl.setOnClickListener(this);
		cancel_myinfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu_mine.setVisibility(View.GONE);
			}
		});
		nickname_rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyInfoActivity.this, NickNameSetActivity.class);
				intent.putExtra("nickName", back_uib.getNickname());
				startActivity(intent);
			}
		});
		sign_rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyInfoActivity.this, SignSetActivity.class);
				intent.putExtra("sign", back_uib.getIntro());
				startActivity(intent);
			}
		});
		album_rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到相册
				/*
				 * Intent intent = new Intent(MyInfoActivity.this,
				 * MyAlbumActivity.class); startActivity(intent);
				 */
				ShellUtil.execute(MyInfoActivity.this, ShellMyAlbumActivity.class);
			}
		});
		phone_number_rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyInfoActivity.this, PhoneSetActivity.class);
				startActivity(intent);
			}
		});

		infodetail_rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyInfoActivity.this, SpecificInfoActivityed.class);
				startActivity(intent);
			}
		});

		// 相片加密
		album_addpw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyInfoActivity.this, AlbumAddpwActivity.class);
				intent.putExtra("head_img", headImg);
				startActivityForResult(intent, 101);
				menu_mine.setVisibility(View.GONE);
			}

		});
		// 拍照获取图片
		takephoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 下面这句指定调用相机拍照后的照片存储的路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "xiaoma.jpg")));
				startActivityForResult(intent, 2);
				menu_mine.setVisibility(View.GONE);
			}
		});
		// 手机相册获取图片
		choise_from_album.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK, null);

				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 1);
				menu_mine.setVisibility(View.GONE);
			}
		});

		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// menu_mine.setVisibility(View.VISIBLE);
				/*
				 * Intent intent = new
				 * Intent(MyInfoActivity.this,MainActivity.class);
				 * startActivity(intent);
				 */
				// 把头像传到MineViewFragment
				// Intent intent = new Intent(MyInfoActivity.this,
				// MineViewFragment.class);
				// intent.putExtra("headImg", photo);
				// startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.head_rl:

			menu_mine.setVisibility(View.VISIBLE);

			break;

		default:
			break;
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
			File temp = new File(Environment.getExternalStorageDirectory() + "/xiaoma.jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃 当前功能时，会报NullException
			 * 
			 */
			if (data != null) {
				setPicToView(data);
			}
			break;
		case 101:
			if (data != null) {
				uploadAdvert(data.getStringExtra("path"));
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
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
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray();
			tp = new String(Base64Coder.encodeLines(b));
			mineHttpPost();
			head_img.setImageDrawable(drawable);
		}
	}

	private Handler handlerBase64End = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mineHttpPost();
			System.out.println("base64完成");
		}

	};

	private Bitmap resizeBitmap(Bitmap bitmap, float sWidth, float sHeight) {
		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();
		// 缩放图片的尺寸
		float scaleWidth = (float) sWidth / bmpWidth; // 按固定大小缩放 sWidth 写多大就多大
		float scaleHeight = (float) sHeight / bmpHeight; //
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);// 产生缩放后的Bitmap对象
		Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, false);
		bitmap.recycle();
		return resizeBitmap;
	}

	private void uploadAdvert(final String path) {
		progressDialog = Util.showLoading(this);
		final Bitmap bitmap = resizeBitmap(BitmapFactory.decodeFile(path), 200, 200);
		head_img.setImageBitmap(bitmap);

		new Thread(new Runnable() {

			@Override
			public void run() {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
				byte[] byteArray = stream.toByteArray();
				tp = new String(Base64Coder.encodeLines(byteArray));
				handlerBase64End.sendEmptyMessage(0);
			}
		}).start();
	}

	// 监听屏幕是否被点击，隐藏下边菜单栏
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		menu_mine.setVisibility(View.GONE);
		return false;
	}

	public void mineHttpPost() {

		final SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		if (progressDialog == null) {
			progressDialog = Util.showLoading(this);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				final String oauth_token = sp.getString("oauth_token", null);
				final String oauth_token_secret = sp.getString("oauth_token_secret", null);
				editUserDataBean bean = new editUserDataBean();
				bean.setHead(tp);
				String jsonstr = JsonHelper.toJsonString(bean);
				System.out.println("json 完成");
				try {
					encodesstr = jsonstr;// AesUtils.Encrypt(jsonstr,
											// CommonConstants.AES_KEY);
					Logger.d("encodesstr", encodesstr);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String url = Constans.editUserData;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
				// params.put("cont", encodesstr);

				params.put("sex", bean.getSex());
				params.put("nickname", bean.getNickname());
				params.put("location", bean.getLocation());
				params.put("province", bean.getProvince());
				params.put("city", bean.getCity());
				params.put("area", bean.getArea());
				params.put("intro", bean.getIntro());
				params.put("birthday", bean.getBirthday());
				params.put("career", bean.getCareer());
				params.put("degrees", bean.getDegrees());
				params.put("drinking", bean.getDrinking());
				params.put("faith", bean.getFaith());
				params.put("height", bean.getHeight());
				params.put("income", bean.getIncome());
				params.put("language", bean.getLanguage());
				params.put("marriage", bean.getMarriage());
				params.put("smoking", bean.getSmoking());
				params.put("weight", bean.getWeight());
				params.put("head", bean.getHead());

				FinalHttp http = new FinalHttp();
				mineObj = http.postSync(url, params);
				mineHandler.sendEmptyMessage(1);

			}
		}).start();

		mineHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				if (msg.what == 1) {
					try {
						back_cb = (CommonBack) ReqJsonUtil.changeToObject(mineObj.toString(), CommonBack.class);
						System.out.print(back_cb);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

	}

	private void mineInfoPost() {
		SharedPreferences sp = this.getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		String uid = sp.getString("uid", null);
		UserInfoBean bean = new UserInfoBean();
		bean.setUid(uid);
		String jsonstr = JsonHelper.toJsonString(bean);

		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = Constans.userinfo;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
				params.put("cont", encodesstr);

				FinalHttp http = new FinalHttp();
				mineObj = http.postSync(url, params);
				mineHandler.sendEmptyMessage(1);

			}
		}).start();

		mineHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					try {
						back_uib = (UserInfoBack) ReqJsonUtil.changeToObject(mineObj.toString(), UserInfoBack.class);

						headImg = back_uib.getAvatar_original() + "#" + new Date().getTime();
						;
						fb.display(head_img, headImg);
						nickname_tv.setText(back_uib.getNickname());
						sign_tv.setText(back_uib.getIntro());
						System.out.print(back_uib);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

	}
}

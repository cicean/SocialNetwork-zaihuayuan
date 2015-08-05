package com.gzqining.inthegarden.album;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

import com.gzqining.inthegarden.act.AppManager;
import com.gzqining.inthegarden.act.LoginActivity;
import com.gzqining.inthegarden.act.MyAlbumActivity;
import com.gzqining.inthegarden.album.ListImageDirPopupWindow.OnImageDirSelected;
import com.gzqining.inthegarden.common.Base64Coder;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.DialogUtil;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.UploadAlbumBean;
import com.gzqining.inthegarden.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class albumSelect extends Activity implements OnImageDirSelected {
	String tag = albumSelect.class.getName();
	private ProgressDialog mProgressDialog;
	private ProgressDialog mProgressDialog2;
	private ImageButton back;
	private Button uploadImage;
	Bitmap bitmap;
	String tp;
	Object albumObj;
	String encodesstr = "";
	Dialog dialog;
	String jsonstr = "";
	/**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 图片数量最多的文件夹
	 */
	private File mImgDir;
	/**
	 * 所有的图片
	 */
	private List<String> mImgs;

	private GridView mGirdView;
	private MyAdapter mAdapter;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();

	/**
	 * 扫描拿到所有的图片文件夹
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	private RelativeLayout mBottomLy;

//	private TextView mChooseDir;
	private TextView mImageCount;
	int totalCount = 0;

	private int mScreenHeight;

	private ListImageDirPopupWindow mListImageDirPopupWindow;

	

	/**
	 * 为View绑定数据
	 */
	private void data2View()
	{
		if (mImgDir == null)
		{
			Toast.makeText(getApplicationContext(), "没有扫描到图片哦",
					Toast.LENGTH_SHORT).show();
			return;
		}

		mImgs = Arrays.asList(mImgDir.list());
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new MyAdapter(getApplicationContext(), mImgs,
				R.layout.grid_item, mImgDir.getAbsolutePath());
		mGirdView.setAdapter(mAdapter);
		mImageCount.setText(totalCount + "张");
	};

	/**
	 * 初始化展示文件夹的popupWindw
	 */
	private void initListDirPopupWindw()
	{
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
				LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
				mImageFloders, LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.list_dir, null));

		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// 设置选择文件夹的回调
		mListImageDirPopupWindow.setOnImageDirSelected(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_album);
		AppManager.getAppManager().addActivity(this);
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;
		
		initView();
		getImages();
		initEvent();

	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages()
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = albumSelect.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png"},
						MediaStore.Images.Media.DATE_MODIFIED);

				Log.e("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext())
				{
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					Log.e("TAG", path);
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(dirPath))
					{
						continue;
					} else
					{
						mDirPaths.add(dirPath);
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}

					int picSize = parentFile.list(new FilenameFilter()
					{
						@Override
						public boolean accept(File dir, String filename)
						{
							if (filename.endsWith(".jpg")
									|| filename.endsWith(".png")
									|| filename.endsWith(".jpeg"))
								return true;
							return false;
						}
					}).length;
					totalCount += picSize;

					imageFloder.setCount(picSize);
					mImageFloders.add(imageFloder);

					if (picSize > mPicsSize)
					{
						mPicsSize = picSize;
						mImgDir = parentFile;
					}
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;

				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}

	/**
	 * 初始化View
	 */
	private void initView()
	{
		mGirdView = (GridView) findViewById(R.id.id_gridView);
//		mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
		mImageCount = (TextView) findViewById(R.id.id_total_count);
		mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
		back = (ImageButton) findViewById(R.id.top_layout_album2_back_btn);
		uploadImage = (Button) findViewById(R.id.top_layout_album2_upload_btn);
	}

	private void initEvent()
	{
		/**
		 * 为底部的布局设置点击事件，弹出popupWindow
		 */
		mBottomLy.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mListImageDirPopupWindow
						.setAnimationStyle(R.style.anim_popup_dir);
				mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = .3f;
				getWindow().setAttributes(lp);
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		uploadImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				uploadPicture();
				//线程睡一段时间，让uploadPicture();执行完再执行toPost(tp);
				try {

					Thread.sleep(500);

					} catch (InterruptedException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

					}
				toPost(tp);
			}
		});
	}
	public void uploadPicture(){
		final HashMap<String, SoftReference<Bitmap>> imageCache = null;
		new Thread(new Runnable() {
			public void run() {
				for (int i=0;i<MyAdapter.mSelectedImage.size();i++){
					String filePath = MyAdapter.mSelectedImage.get(i);
					System.out.println(filePath);
//					bitmap=BitmapFactory.decodeFile(filePath);
					Bitmap bitmap = BitmapFactory.decodeByteArray(PictureUtil.decodeBitmap(filePath), 0, PictureUtil.decodeBitmap(filePath).length);  
//					imageCache.put(filePath, new SoftReference<Bitmap>(bitmap));
					/**
					 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上
					 * 传到服务器，QQ头像上传采用的方法跟这个类似
					 */
					ByteArrayOutputStream stream = new ByteArrayOutputStream();        
		//			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			        if( stream.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出    
			        	stream.reset();//重置stream即清空stream
			            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);//这里压缩50%，把压缩后的数据存放到baos中
			        }
					
					bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
					byte[] b = stream.toByteArray();
					// 将图片流以字符串形式存储下来
					stream.reset();
					tp = new String(Base64Coder.encodeLines(b));
					
				}
			}
		}).start();
	}
	private void toPost(String string) {
//		showDialog();
//		long data = new Date().getTime();  
//		UploadAlbumBean bean = new UploadAlbumBean();
//		bean.setImg(tp);
//		bean.setExpand("jpg");
//		bean.setInfo("123");
//		bean.setName("123");
//		editUserDataBean bean = new editUserDataBean();
//		bean.setAvatar_original(tp);
//		jsonstr = JsonHelper.toJsonString(tp);
		// 显示进度条
		mProgressDialog2 = ProgressDialog.show(this, null, "正在上传...");
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
			case 0x110:
				mProgressDialog.dismiss();
				// 为View绑定数据
				data2View();
				// 初始化展示文件夹的popupWindw
				initListDirPopupWindw();
				break;
			case 1:
//				canelDialog();
				try {
					CommonBack back = (CommonBack) ReqJsonUtil.changeToObject(albumObj.toString(), CommonBack.class);
					if(back.getCode() == 00000){
						mProgressDialog2.dismiss();
						Intent intent =new Intent(albumSelect.this, MyAlbumActivity.class);
						startActivity(intent);
						finish();
					}else{
						mProgressDialog2.dismiss();
						Toast.makeText(albumSelect.this, back.getMessage(),Toast.LENGTH_LONG).show();
					}
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	@Override
	public void selected(ImageFloder floder)
	{

		mImgDir = new File(floder.getDir());
		mImgs = Arrays.asList(mImgDir.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String filename)
			{
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
						|| filename.endsWith(".jpeg"))
					return true;
				return false;
			}
		}));
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new MyAdapter(getApplicationContext(), mImgs,
				R.layout.grid_item, mImgDir.getAbsolutePath());
		mGirdView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		mImageCount.setText(floder.getCount() + "张");
//		mChooseDir.setText(floder.getName());
		mListImageDirPopupWindow.dismiss();

	}
//	public void showDialog() {
//		dialog = DialogUtil.showProgressDialog(albumSelect.this,getString(R.string.request_data), false);
//		dialog.show();
//	}
//
//	public void showDialogloading(String text) {
//		dialog = DialogUtil.showProgressDialog(albumSelect.this, text, true);
//		dialog.show();
//	}
//
//	public void canelDialog() {
//		if (dialog != null) {
//			// dialog.cancel();
//			dialog.dismiss();
//		}
//	}
}

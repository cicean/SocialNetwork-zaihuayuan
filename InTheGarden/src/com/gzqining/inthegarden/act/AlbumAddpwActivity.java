package com.gzqining.inthegarden.act;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.common.BaseActivity;

public class AlbumAddpwActivity extends BaseActivity {
	// 声明控件
	private ImageButton back_btn;// 返回键
	private Button finish_btn;// 返回键
	private ImageView mask1;// 面具1
	private ImageView mask2;// 面具2
	private ImageView mask3;// 面具3
	private ImageView mask4;// 面具4
	private ImageView imageViewSrc;
	// 自定义变量
	Bitmap bitmapMash;// 面罩
	Bitmap bitmapSrc;// 原始图片
	Bitmap bitmapNew;// 生成新图
	URL myFileUrl = null;
	public static final int NONE = 0;
	public static final int DRAG = 1;
	public static final int ZOOM = 2;

	RelativeLayout dragLayout;
	ImageView imageViewMash;
	float x_down = 0;
	float y_down = 0;
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	float oldRotation = 0;
	Matrix matrix = new Matrix();
	Matrix matrix1 = new Matrix();
	Matrix matrix2 = new Matrix();
	Matrix savedMatrix = new Matrix();

	int mode = NONE;

	boolean matrixCheck = false;

	int widthScreen;
	int heightScreen;
	int startImgWidth;
	int startImgHeight;
	double d;
	Activity mActivity;
	HorizontalScrollView bottomParent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = this;
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		widthScreen = dm.widthPixels;
		heightScreen = dm.heightPixels;
		matrix = new Matrix();
		Intent i = getIntent();
		String myFileUrl = i.getStringExtra("head_img");

		getBitmap(myFileUrl);

		setContentView(R.layout.act_album_addpw_view);
		AppManager.getAppManager().addActivity(this);

		back_btn = (ImageButton) findViewById(R.id.top_layout_album_back_btn);
		finish_btn = (Button) findViewById(R.id.top_layout_album_save_btn);
		mask1 = (ImageView) findViewById(R.id.mask1);
		mask2 = (ImageView) findViewById(R.id.mask2);
		mask3 = (ImageView) findViewById(R.id.mask3);
		mask4 = (ImageView) findViewById(R.id.mask4);
		imageViewMash = (ImageView) findViewById(R.id.img);
		imageViewSrc = (ImageView) findViewById(R.id.imageViewSrc);

		bottomParent = (HorizontalScrollView) findViewById(R.id.panel_layout);
		dragLayout = (RelativeLayout) findViewById(R.id.drag_layout);
		bitmapMash = BitmapFactory.decodeResource(getResources(), R.drawable.mask);
		startImgWidth = 250;// bitmapMash.getWidth();
		startImgHeight = 250 * bitmapMash.getHeight() / bitmapMash.getWidth();
		// imageViewMash.setImageBitmap(Bitmap.createScaledBitmap(bitmapMash,
		// startImgWidth / 3, startImgHeight / 3, true));
		initEvents();

		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dragLayout.getLayoutParams();
		lp.height = widthScreen;
		dragLayout.setLayoutParams(lp);
	}

	protected void onDraw(Canvas canvas) {
		// 去除锯齿毛边
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

		canvas.save();
		canvas.drawBitmap(bitmapMash, matrix, null);
		canvas.restore();
	}

	public Bitmap screenShot(View view) {
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	private String saveBitmap(Bitmap bitmap) {
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp.png";
		try {
			FileOutputStream out = new FileOutputStream(path);
			bitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public void initEvents() {
		// 返回按钮
		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		finish_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bitmapNew = screenShot(dragLayout);
				bitmapMash = null;
				imageViewMash.setImageBitmap(null);
				imageViewSrc.setImageBitmap(bitmapNew);
				String path = saveBitmap(bitmapNew);
				Intent data = new Intent();
				data.putExtra("path", path);
				setResult(101, data);
				finish();
			}
		});
		mask1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				bitmapMash = getImageFromAssets("mask.fw.png");
				imageViewMash.setImageBitmap(Bitmap.createScaledBitmap(bitmapMash, startImgWidth, startImgHeight, true));
			}

		});
		mask2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				bitmapMash = getImageFromAssets("mask2.png");
				imageViewMash.setImageBitmap(Bitmap.createScaledBitmap(bitmapMash, startImgWidth, startImgHeight, true));
			}
		});
		mask3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				bitmapMash = getImageFromAssets("mask3.png");
				imageViewMash.setImageBitmap(Bitmap.createScaledBitmap(bitmapMash, startImgWidth, startImgHeight, true));
			}
		});
		mask4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				bitmapMash = getImageFromAssets("mask4.png");
				imageViewMash.setImageBitmap(Bitmap.createScaledBitmap(bitmapMash, startImgWidth, startImgHeight, true));
			}
		});
	}

	// 从Assets中获取图片
	private Bitmap getImageFromAssets(String fileName) {
		Bitmap imageAssets = null;
		AssetManager am = getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			imageAssets = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageAssets;
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (bitmapMash == null) {
			return true;
		}
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mode = DRAG;
			x_down = event.getX();
			y_down = event.getY();
			savedMatrix.set(matrix);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			mode = ZOOM;
			oldDist = spacing(event);
			oldRotation = rotation(event);
			savedMatrix.set(matrix);
			midPoint(mid, event);
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == ZOOM) {
				matrix1.set(savedMatrix);
				float rotation = rotation(event) - oldRotation;
				float newDist = spacing(event);
				float scale = newDist / oldDist;
				matrix1.postScale(scale, scale, mid.x, mid.y);// 縮放
				matrix1.postRotate(rotation, mid.x, mid.y);// 旋轉
				matrixCheck = matrixCheck();
				if (matrixCheck == false) {
					matrix.set(matrix1);
				}
			} else if (mode == DRAG) {
				matrix1.set(savedMatrix);
				matrix1.postTranslate(event.getX() - x_down, event.getY() - y_down);// 平移
				matrixCheck = matrixCheck();
				if (matrixCheck == false) {
					matrix.set(matrix1);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		}

		imageViewMash.setImageMatrix(matrix);
		return true;
	}

	private boolean matrixCheck() {
		float[] f = new float[9];
		matrix1.getValues(f);
		// 图片4个顶点的坐标
		float x1 = f[0] * 0 + f[1] * 0 + f[2];
		float y1 = f[3] * 0 + f[4] * 0 + f[5];
		float x2 = f[0] * bitmapMash.getWidth() + f[1] * 0 + f[2];
		float y2 = f[3] * bitmapMash.getWidth() + f[4] * 0 + f[5];
		float x3 = f[0] * 0 + f[1] * bitmapMash.getHeight() + f[2];
		float y3 = f[3] * 0 + f[4] * bitmapMash.getHeight() + f[5];
		float x4 = f[0] * bitmapMash.getWidth() + f[1] * bitmapMash.getHeight() + f[2];
		float y4 = f[3] * bitmapMash.getWidth() + f[4] * bitmapMash.getHeight() + f[5];
		// 图片现宽�?
		double width = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		// 缩放比率判断
		if (width < widthScreen / 3 || width > widthScreen * 3) {
			return true;
		}
		// 出界判断
		if ((x1 < widthScreen / 3 && x2 < widthScreen / 3 && x3 < widthScreen / 3 && x4 < widthScreen / 3) || (x1 > widthScreen * 2 / 3 && x2 > widthScreen * 2 / 3 && x3 > widthScreen * 2 / 3 && x4 > widthScreen * 2 / 3) || (y1 < heightScreen / 3 && y2 < heightScreen / 3 && y3 < heightScreen / 3 && y4 < heightScreen / 3) || (y1 > heightScreen * 2 / 3 && y2 > heightScreen * 2 / 3 && y3 > heightScreen * 2 / 3 && y4 > heightScreen * 2 / 3)) {
			return true;
		}
		return false;
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	private float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}

	/**
	 * 网络图片转成Bitmap
	 * 
	 * @param url
	 */
	public void getBitmap(String url) {
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				Bitmap bitmap = null;
				try {
					HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
					conn.setDoInput(true);
					conn.connect();
					InputStream is = conn.getInputStream();
					bitmap = BitmapFactory.decodeStream(is);
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				Message _handlerMsg = mHandler.obtainMessage();
				_handlerMsg.what = 1;
				_handlerMsg.obj = bitmap;
				mHandler.sendMessage(_handlerMsg);
			}

		}).start();
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				imageViewSrc.setImageBitmap((Bitmap) msg.obj);
				bitmapSrc = (Bitmap) msg.obj;
				break;
			}
		}
	};
}

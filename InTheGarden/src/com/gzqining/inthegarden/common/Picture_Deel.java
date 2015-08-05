package com.gzqining.inthegarden.common;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.RelativeLayout;

public class Picture_Deel extends BaseActivity {

	protected RelativeLayout head_img;

	public Picture_Deel() {
		super();
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
			Bitmap output = toRoundCorner(photo, 360.0f);
			Drawable drawable = new BitmapDrawable(output);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray();
			String tp = new String(Base64Coder.encodeLines(b));
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
			
			head_img.setBackgroundDrawable(drawable);
		}
	}
	/**
	 * 圆角头像设置
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
		System.out.println("图片是否变成圆角模式了+++++++++++++");
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		System.out.println("pixels+++++++" + pixels);
		return output;
	}
}
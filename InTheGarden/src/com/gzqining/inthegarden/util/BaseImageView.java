package com.gzqining.inthegarden.util;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public abstract class BaseImageView extends ImageView {
	private static final String TAG = BaseImageView.class.getSimpleName();

	protected Context mContext;

	private static final Xfermode sXfermode = new PorterDuffXfermode(
			PorterDuff.Mode.DST_IN);
	// private BitmapShader mBitmapShader;
	private Bitmap mMaskBitmap;
	private Paint mPaint;
	private WeakReference<Bitmap> mWeakBitmap;

	public BaseImageView(Context context) {
		super(context);
		sharedConstructor(context);
	}

	public BaseImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		sharedConstructor(context);
	}

	public BaseImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		sharedConstructor(context);
	}

	private void sharedConstructor(Context context) {
		mContext = context;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	public void invalidate() {
		mWeakBitmap = null;
		if (mMaskBitmap != null) {
			mMaskBitmap.recycle();
		}
		super.invalidate();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		if (!isInEditMode()) {
			int i = canvas.saveLayer(0.0f, 0.0f, getWidth(), getHeight(), null,
					Canvas.ALL_SAVE_FLAG);
			try {
				Bitmap bitmap = mWeakBitmap != null ? mWeakBitmap.get() : null;
				// Bitmap not loaded.
				if (bitmap == null || bitmap.isRecycled()) {
					Drawable drawable = getDrawable();
					// 获取drawable的宽和高
					int dWidth = drawable.getIntrinsicWidth();
					int dHeight = drawable.getIntrinsicHeight();
					if (drawable != null) {
						// Allocation onDraw but it's ok because it will not
						// always be called.
						bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
								Bitmap.Config.ARGB_8888);
						float scale = 1.0f;
						Canvas bitmapCanvas = new Canvas(bitmap);
						// 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放
						scale = Math.max(getWidth() * 1.0f / dWidth, getHeight()
								* 1.0f / dHeight);
						drawable.setBounds(0, 0, (int) (scale * dWidth),
								(int) (scale * dHeight));
						drawable.draw(bitmapCanvas);

						// If mask is already set, skip and use cached mask.
						if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {
							mMaskBitmap = getBitmap();
						}

						// Draw Bitmap.
						mPaint.reset();
						mPaint.setFilterBitmap(false);
						mPaint.setXfermode(sXfermode);
						// mBitmapShader = new BitmapShader(mMaskBitmap,
						// Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
						// mPaint.setShader(mBitmapShader);
						bitmapCanvas
								.drawBitmap(mMaskBitmap, 0.0f, 0.0f, mPaint);

						mWeakBitmap = new WeakReference<Bitmap>(bitmap);

						invalidate(); // 这是我后来添加的

					}
				}
				// Bitmap already loaded.
				if (bitmap != null) {
					mPaint.setXfermode(null);
					// mPaint.setShader(null);
					canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint);
					return;
				}
			} catch (Exception e) {
				System.gc();

			} finally {
				canvas.restoreToCount(i);
			}
		} else {
			super.onDraw(canvas);
		}
	}

	public abstract Bitmap getBitmap();
}

package com.gzqining.inthegarden.app.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.ShellActivity;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.Extra;
import com.gzqining.inthegarden.app.injection.ViewById;
import com.gzqining.inthegarden.common.Base64Coder;

@EActivity(layout = R.layout.ui_activity_register_nick_header, inject = true)
public class ShellRegisterHeaderActivity extends ShellActivity{

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
	
	@ViewById
	private ImageView headerImg;
	@ViewById
	private EditText editNickname;
	
	private String headBase64;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Click({
		R.id.btnBack,
		R.id.btnNext,
		R.id.headerImg,
		R.id.cameraImg
	})
	void onClick(View v) {
		int id = v.getId();
		if(id == R.id.btnBack) {
			finish();
		} else if(id == R.id.btnNext) {
			String nickname  = editNickname.getText().toString().trim();
			if(TextUtils.isEmpty(nickname)) {
				Toast.makeText(getActivity(), "请输入昵称", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(TextUtils.isEmpty(headBase64)) {
				Toast.makeText(getActivity(), "请选择头像", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent intent = new Intent(getActivity(), UISpecificInfoActivity.class);
			intent.putExtra("phone", phone);
			intent.putExtra("email", email);
			intent.putExtra("password", password);
			intent.putExtra("phoneVerify", phoneVerify);
			intent.putExtra("sid", sid);
			intent.putExtra("nickname", nickname);
			intent.putExtra("headBase64", headBase64);
			startActivityForResult(intent, 999);
		} else if(id == R.id.headerImg || id == R.id.cameraImg) {
			Intent intentHeader = new Intent();
			intentHeader.setAction(Intent.ACTION_PICK);
			intentHeader.setType("image/*");
			startActivityForResult(intentHeader, 100);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 999) {
			setResult(RESULT_OK);
			finish();
		} else if(resultCode == RESULT_OK && requestCode == 100 && data != null) {
			String imgPath = null;
			Uri uri = data.getData();
			if (!TextUtils.isEmpty(uri.getAuthority())) {
				Cursor cursor = getActivity().getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
				if (null == cursor) {
					Toast.makeText(getActivity(), "图片没找到", Toast.LENGTH_SHORT).show();
					return;
				}
				cursor.moveToFirst();
				imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
				cursor.close();
			} else {
				imgPath = uri.getPath();
			}
			
			if(TextUtils.isEmpty(imgPath)) {
				Toast.makeText(getActivity(), "图片没找到", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(Uri.fromFile(new File(imgPath)), "image/*");
			//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
			intent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, 200);
			
		} else if(resultCode == RESULT_OK && requestCode == 200 && data != null) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				
				Bitmap output = toRoundCorner(photo, 360.0f);
				headerImg.setImageBitmap(output);
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				byte[] b = stream.toByteArray();
				
				headBase64 = new String(Base64Coder.encodeLines(b));
			}
		}
		
	}
	
	/**
	 * 圆角头像设置
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);
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
		return output;
	}
	
}

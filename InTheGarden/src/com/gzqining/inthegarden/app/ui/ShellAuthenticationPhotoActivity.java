package com.gzqining.inthegarden.app.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.ShellActivity;
import com.gzqining.inthegarden.app.ShellUtil;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.ViewById;
import com.gzqining.inthegarden.common.Base64Coder;

@EActivity(layout = R.layout.ui_activity_authentication_photo, inject = true)
public class ShellAuthenticationPhotoActivity extends ShellActivity{

	@ViewById
	private View hintImg;
	@ViewById
	private View description;
	@ViewById
	private ImageView photo;
	
	private String base64Img;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Click({
		R.id.btnBack,
		R.id.btnNext,
		R.id.btnPhoto
	})
	void onClick(View v) {
		int id = v.getId();
		switch(id) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnNext:
			if(TextUtils.isEmpty(base64Img)) {
				Toast.makeText(getActivity(), "请上传照片", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent();
			intent.putExtra(ShellAuthenticationActivity.EXTRA_PHOTO, base64Img);
			ShellUtil.execute(getActivity(), ShellAuthenticationActivity.class, intent, 999);
			break;
		case R.id.btnPhoto:
			final BottomView bv = new BottomView(getActivity(), R.layout.ui_activity_authentication_selector);
			bv.showBottomView(true);
			bv.getView().findViewById(R.id.item1).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					File file = new File(getActivity().getExternalCacheDir(), "tempPhoto.jpg");
					Uri uri = Uri.fromFile(file);
					Intent intentPhone = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					intentPhone.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					intentPhone.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
					startActivityForResult(intentPhone, 200);
					bv.dismissBottomView();
				}
			});
			bv.getView().findViewById(R.id.item2).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_PICK);
					intent.setType("image/*");
					startActivityForResult(intent, 100);
					bv.dismissBottomView();
				}
			});
			bv.getView().findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					bv.dismissBottomView();
				}
			});
			break;
		}
	}
	
	void crop(String path) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 300);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK && requestCode == 100 && data != null) {
			String path = null;
			Uri uri = data.getData();
			if (!TextUtils.isEmpty(uri.getAuthority())) {
				Cursor cursor = getActivity().getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
				if (null == cursor) {
					Toast.makeText(getActivity(), "图片没找到", Toast.LENGTH_SHORT).show();
					return;
				}
				cursor.moveToFirst();
				path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
				cursor.close();
				
			} else {
				path = uri.getPath();
			}
			if(TextUtils.isEmpty(path)) {
				Toast.makeText(getActivity(), "图片没找到", Toast.LENGTH_SHORT).show();
				return;
			}
			
			crop(path);
		} else if(resultCode == Activity.RESULT_OK && requestCode == 200) {
			File file = new File(getActivity().getExternalCacheDir(), "tempPhoto.jpg");
			if(!file.exists()) {
				Toast.makeText(getActivity(), "图片没找到", Toast.LENGTH_SHORT).show();
				return;
			}
			crop(file.getAbsolutePath());
		} else if(resultCode == Activity.RESULT_OK && requestCode == 300 && data != null) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap bitmap = extras.getParcelable("data");
				
				photo.setImageBitmap(bitmap);
				photo.setVisibility(View.VISIBLE);
				description.setVisibility(View.GONE);
				hintImg.setVisibility(View.GONE);
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				byte[] b = stream.toByteArray();
				this.base64Img = new String(Base64Coder.encodeLines(b));
			}
		} else if(requestCode == 999 &&  resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
		
	}
	
}

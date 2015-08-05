package com.gzqining.inthegarden.app.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.ShellActivity;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.app.injection.Click;
import com.gzqining.inthegarden.app.injection.Injection;
import com.gzqining.inthegarden.app.injection.ViewById;
import com.gzqining.inthegarden.common.Base64Coder;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.vo.CommonConstants;

@EActivity(layout = R.layout.ui_person_album, inject = true)
public class ShellMyAlbumActivity extends ShellActivity{
	@ViewById
	ViewPager pager;
	
	private MyPagerAdapter adapter;
	
	MyAlbumFragment fragment1;
	MyAlbumFragment fragment2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle mBundle = null;
		
		mBundle = new Bundle();
		mBundle.putInt("privacy", 0);
		fragment1 = new MyAlbumFragment();
		fragment1.setArguments(mBundle);
		
		mBundle = new Bundle();
		mBundle.putInt("privacy", 1);
		fragment2 = new MyAlbumFragment();
		fragment2.setArguments(mBundle);
		
		adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) { 
				Injection inject = getInjection();
				if(position == 0) {
					inject.find(R.id.itemLabel1, TextView.class).setTextColor(0xFFFFFFFF);
					inject.find(R.id.itemLabel2, TextView.class).setTextColor(0xFFF3A7BB);
					inject.find(R.id.itemLine1).setVisibility(View.VISIBLE);
					inject.find(R.id.itemLine2).setVisibility(View.INVISIBLE);
				} else {
					inject.find(R.id.itemLabel1, TextView.class).setTextColor(0xFFF3A7BB);
					inject.find(R.id.itemLabel2, TextView.class).setTextColor(0xFFFFFFFF);
					inject.find(R.id.itemLine1).setVisibility(View.INVISIBLE);
					inject.find(R.id.itemLine2).setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }
			@Override
			public void onPageScrollStateChanged(int position) { }
		});
		
	}
	
	@Click({
		R.id.btnBack,
		R.id.item1,
		R.id.item2,
		R.id.btnNext
	})
	void onClick(View v) { 
		int id = v.getId();
		switch(id) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.item1:
			pager.setCurrentItem(0);
			break;
		case R.id.item2:
			pager.setCurrentItem(1);
			break;
		case R.id.btnNext:
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
	
	void upload_picture(String base64Img) {
		
		int privacy = pager.getCurrentItem();
		
		SharedPreferences sp = getActivity().getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		
		/*String cont = null;
		try {
			JSONObject json = new JSONObject();
			json.put("img", base64Img);
			json.put("expand", "jpg");
			json.put("info", "");
			json.put("privacy", privacy);
			json.put("name", new Date().getTime());
			cont = json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			cont = AesUtils.Encrypt(cont, CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		String url = Constans.upload_picture;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		//params.put("cont", cont);
		params.put("img", base64Img);
		params.put("expand", "jpg");
		params.put("info", "");
		params.put("privacy", ""+privacy);
		params.put("name", ""+new Date().getTime());
		
		showDialogFragment(false);
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				
				if(isFinishing()) return;
				
				removeDialogFragment();
				if(TextUtils.isEmpty((String)obj)) {
					Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				JSONObject json = null;
				try {
					json = (JSONObject) new JSONTokener((String) obj).nextValue();
				} catch (JSONException e) {
					// e.printStackTrace();
				}
				
				if (json == null || !"00000".equals(json.optString("code"))) {
					Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//提现成功
				Toast.makeText(getActivity(), json.optString("message"), Toast.LENGTH_SHORT).show();
				
				if(pager.getCurrentItem() == 0) {
					fragment1.pullDownRefresh();
				} else {
					fragment2.pullDownRefresh();
				}
				
			}
		});
		
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
			
			String photoPath = file.getAbsolutePath();
			crop(photoPath);
		} else if(resultCode == Activity.RESULT_OK && requestCode == 300 && data != null) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				byte[] b = stream.toByteArray();
				
				String base64Img = new String(Base64Coder.encodeLines(b));
				upload_picture(base64Img);
			}
		}
	}
	
	
	
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "相册", "私密"};

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment mFragment = null;
			if (position == 0) {
				mFragment = fragment1;
			} else if (position == 1) {
				mFragment = fragment2;
			}
			return mFragment;
		}
	}
	
}

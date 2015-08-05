package com.gzqining.inthegarden.app.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.AlbumAddpwActivity;
import com.gzqining.inthegarden.act.VipActivity;
import com.gzqining.inthegarden.app.AppFragment;
import com.gzqining.inthegarden.app.EFragment;
import com.gzqining.inthegarden.app.ShellUtil;
import com.gzqining.inthegarden.app.http.HttpCallback;
import com.gzqining.inthegarden.app.http.HttpRespose;
import com.gzqining.inthegarden.app.injection.Extra;
import com.gzqining.inthegarden.app.injection.ViewById;
import com.gzqining.inthegarden.app.util.StringUtils;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.vo.AlbumBackList;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

@EFragment(layout = R.layout.ui_person_album_fragment, inject = true)
public class MyAlbumFragment extends AppFragment implements OnItemClickListener{

	@ViewById(R.id.gridview)
	private PullToRefreshGridView gridview;
	@ViewById
	private View nodata;
	
	private MyAdapter adapter;
	
	private int page = 1;
	
	@Extra("privacy")
	private int privacy;
	
	private String imgId;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		ILoadingLayout startLabels = gridview.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("松开载入更多...");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = gridview.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在刷新...");// 刷新时
		endLabels.setReleaseLabel("松开载入更多...");// 下来达到一定距离时，显示的提示
		
		gridview.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				getAlbum(1);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				getAlbum(page+1);
			}
		});
		gridview.getRefreshableView().setOnItemClickListener(this);
		
		adapter = new MyAdapter(getActivity(), 0);
		gridview.setAdapter(adapter);
		getAlbum(1);
	}
	
	public void pullDownRefresh() {
		getAlbum(1);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final AlbumBackList item = adapter.getItem(position);
		if(privacy == 0) {
			String ITEMS[] = {"面具加密", "查看大图", "删除"};
			new AlertDialog.Builder(getActivity())
			.setItems(ITEMS, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which == 0) {
						SharedPreferences sp = getActivity().getSharedPreferences("idcard", 0);
						int is_vip = sp.getInt("is_vip", 0);
						if(is_vip == 1) {
							imgId = item.getId();
							Intent intent = new Intent(getActivity(), AlbumAddpwActivity.class);
							intent.putExtra("head_img", "http://61.152.93.162:8380/data/upload/"+item.getSavepath());
							startActivityForResult(intent, 101);
						} else {
							new AlertDialog.Builder(getActivity())
							.setMessage("VIP特权，成为VIP？")
							.setNegativeButton("取消", null)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									startActivity(new Intent(getActivity(), VipActivity.class));
								}
							})
							.show();
						}
					} else if(which == 1) {
						Intent intent = new Intent();
						intent.putExtra("imageUrl", "http://61.152.93.162:8380/data/upload/"+item.getSavepath());
						ShellUtil.execute(MyAlbumFragment.this, ShellGalleryActivity.class, intent);
					} else if(which == 2) {
						delete_picture(item.getId());
					}
				}
			})
			.show();
		} else {
			String ITEMS[] = {"查看大图", "删除"};
			new AlertDialog.Builder(getActivity())
			.setItems(ITEMS, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which == 0) {
						Intent intent = new Intent();
						intent.putExtra("imageUrl", "http://61.152.93.162:8380/data/upload/"+item.getSavepath());
						ShellUtil.execute(MyAlbumFragment.this, ShellGalleryActivity.class, intent);
					} else if(which == 1) {
						delete_picture(item.getId());
					}
				}
			})
			.show();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 101) {
			if(resultCode == 101) {
				try {
					String path = data.getStringExtra("path");
					String base64Img = Base64.encodeFromFile(path);
					upload_picture(imgId, base64Img);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				imgId = null;
			}
		} else if(resultCode == Activity.RESULT_OK && requestCode == 999) {
			page = 0;
			getAlbum(1);
		}
	}
	
	class MyAdapter extends ArrayAdapter<AlbumBackList> {

		FinalBitmap fb;
		
		public MyAdapter(Context context, int resource) {
			super(context, resource);
			fb = FinalBitmap.create(context);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				int x = (int) (parent.getWidth() / 3);
				convertView = View.inflate(getContext(), R.layout.fragment_photo_grid_image, null);
				convertView.setLayoutParams(new AbsListView.LayoutParams(x, x));
			}
			
			AlbumBackList item = getItem(position);
			ImageView photo_img = (ImageView)convertView.findViewById(R.id.photo_img);
			String imgUrl = "http://61.152.93.162:8380/data/upload/"+item.getSavepath();
			imgUrl = StringUtils.addParamURL(imgUrl, "time="+System.currentTimeMillis());
			fb.display(photo_img, imgUrl);
			
			return convertView;
		}
	}
	
	private void getAlbum(final int indexPage){
		SharedPreferences sp = getActivity().getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		String uid = sp.getString("uid", null);
		
		String cont = String.format("{\"uid\":\"%s\",\"privacy\":\"%s\",\"page\":\"%s\"}",uid, privacy, indexPage);
		try {
			cont = AesUtils.Encrypt(cont, CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url = Constans.album;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("cont", cont);
		
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			
			@Override
			public void onCallback(Object obj) {
				if(isDetached() || getView() == null)
					return;
				
				gridview.onRefreshComplete();
				if(TextUtils.isEmpty((String)obj)) {
					return;
				}
				
				JSONObject json = null;
				try {
					json = (JSONObject) new JSONTokener((String) obj).nextValue();
				} catch (JSONException e) {
					// e.printStackTrace();
				}
				
				if(json != null) {
					if(indexPage == 1)
						adapter.clear();
					int totalPages = json.optInt("totalPages");
					if(totalPages == 0) {
						nodata.setVisibility(adapter.isEmpty() ? View.VISIBLE : View.INVISIBLE);
						return;
					} else if(indexPage > totalPages) {
						Toast.makeText(getActivity(), "没有更多相片了！", Toast.LENGTH_SHORT).show();
						return;
					}
					
					List<AlbumBackList> list = null;
					String data = json.optString("data");
					if(TextUtils.isEmpty(data)) {
						list = new ArrayList<AlbumBackList>();
					} else {
						list = new Gson().fromJson(data, new TypeToken<List<AlbumBackList>>(){}.getType());
					}
					
					if(list != null && !list.isEmpty()) {
						adapter.addAll(list);
						page = indexPage;
					}
					
				}
				nodata.setVisibility(adapter.isEmpty() ? View.VISIBLE : View.INVISIBLE);
			}
		});
		
	}
	
	
	void upload_picture(String imgId, String base64Img) {
		
		SharedPreferences sp = getActivity().getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		
		String url = Constans.upload_picture;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("img", base64Img);
		params.put("imgId", imgId);
		params.put("expand", "jpg");
		params.put("info", "");
		params.put("privacy", ""+privacy);
		params.put("name", ""+new Date().getTime());
		
		showDialogFragment(false);
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				
				if(isDetached() || getView() == null)
					return;
				
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
				
				Toast.makeText(getActivity(), json.optString("message"), Toast.LENGTH_SHORT).show();
				pullDownRefresh();
			}
		});
		
	}
	
	void delete_picture(String imgId) {
		
		SharedPreferences sp = getActivity().getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		
		String cont = String.format("{\"imgId\":\"%s\"}", imgId);
		try {
			cont = AesUtils.Encrypt(cont, CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url = Constans.delete_picture;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		//params.put("cont", cont);
		params.put("imgId", imgId);
		
		showDialogFragment(false);
		
		HttpRespose http = new HttpRespose(url, params);
		http.request(new HttpCallback() {
			@Override
			public void onCallback(Object obj) {
				if(isDetached() || getView() == null)
					return;
				removeDialogFragment();
				
				if(TextUtils.isEmpty((String)obj)) {
					Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				JSONObject json = null;
				try {
					json = (JSONObject) new JSONTokener((String) obj).nextValue();
				} catch (JSONException e) {
				}
				
				if (json == null || !"00000".equals(json.optString("code"))) {
					Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//提现成功
				Toast.makeText(getActivity(), json.optString("message"), Toast.LENGTH_SHORT).show();
				pullDownRefresh();
			}
		});
		
	}

	
	
}

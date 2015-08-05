package com.gzqining.inthegarden.gridview;

import java.util.List;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.VipActivity;
import com.gzqining.inthegarden.app.ShellUtil;
import com.gzqining.inthegarden.app.ui.ShellGalleryActivity;

/**
 * lsitView的适配器
 * 
 * @author lyy
 * 
 */
public class MyAdapter extends BaseAdapter {
	
	Context context;
	
	int privacy;
	
	boolean isVip;
	
	int count = 0;

	/***
	 * listview item position==2
	 * 
	 * GridView里面的数据
	 * 
	 */
	private List<String> gridViewData;
	private GridViewAdapter gridViewAdapter;

	/*** gridView的数据 **/
	public void setGridViewData(int count, List<String> gridViewData) {
		this.count = count;
		this.gridViewData = gridViewData;
	}

	public MyAdapter(Context context, int privacy, boolean isVip) {
		this.context = context;
		this.privacy = privacy;
		this.isVip = isVip;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// if (position == 0) {
		//
		// return listView();
		// } else {
		return gridView();
		// }

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public View gridView() {

		View view = LayoutInflater.from(context).inflate(R.layout.item1, null);
		GridView myGridView = (GridView) view.findViewById(R.id.gridView2);
		if (gridViewAdapter == null)
			gridViewAdapter = new GridViewAdapter(context, privacy);

		myGridView.setAdapter(gridViewAdapter);
		gridViewAdapter.setData(gridViewData);
		gridViewAdapter.notifyDataSetChanged();

		myGridView.setOnItemClickListener(onItemClickListener);
		return view;
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			String path = gridViewAdapter.getItem(position);
			if(privacy == 0) {
				Intent intent = new Intent();
				intent.putExtra("imageUrl", "http://61.152.93.162:8380/data/upload/"+path);
				ShellUtil.execute(context, ShellGalleryActivity.class, intent);
			} else {
				
				SharedPreferences sp = context.getSharedPreferences("idcard", 0);
				int is_vip = sp.getInt("is_vip", 0);
				
				if(is_vip != 1) {
					new AlertDialog.Builder(context)
					.setMessage("vip特权,升级才能查看私密相册.")
					.setNegativeButton("取消", null)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							context.startActivity(new Intent(context, VipActivity.class));
						}
					})
					.show();
				} else if(isVip) {
					new AlertDialog.Builder(context)
					.setMessage("对方是vip,不能查看私密相册.")
					.setNegativeButton("取消", null)
					.setPositiveButton("确定", null)
					.show();
				} else {
					Intent intent = new Intent();
					intent.putExtra("imageUrl", "http://61.152.93.162:8380/data/upload/"+path);
					ShellUtil.execute(context, ShellGalleryActivity.class, intent);
				}
				
			}
		}

	};

}

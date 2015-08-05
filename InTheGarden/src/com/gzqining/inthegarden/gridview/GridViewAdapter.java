package com.gzqining.inthegarden.gridview;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzqining.inthegarden.R;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@SuppressLint("NewApi")
public class GridViewAdapter extends BaseAdapter {
	Context context;
	private List<String> data;
	LayoutInflater minflater;
	FinalBitmap fb;
	int privacy;

	public void setData(List<String> data) {
		this.data = data;
	}

	public GridViewAdapter(Context context, int privacy) {
		this.context = context;
		this.privacy = privacy;
		fb = FinalBitmap.create(context);
	}

	@Override
	public int getCount() {
		return data != null ? data.size() : 0;
	}

	@Override
	public String getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			holder.privateText = (TextView) convertView.findViewById(R.id.privateText);
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}
		
		// 动态设置imageview显示的长宽
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);
		int height = (int) (dm.widthPixels / 3);
		holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
		holder.privateText.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
		if(privacy == 0) {
			fb.display(holder.imageView, "http://61.152.93.162:8380/data/upload/" + data.get(position));
			holder.imageView.setVisibility(View.VISIBLE);
			holder.privateText.setVisibility(View.INVISIBLE);
		} else {
			holder.imageView.setVisibility(View.INVISIBLE);
			holder.privateText.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

	public final class ViewHolder {
		ImageView imageView;
		TextView privateText;
	}
}

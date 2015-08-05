package com.gzqining.inthegarden.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.vo.giftListItem;

public class giftListAdapter extends BaseAdapter {

	List<giftListItem> data;
	Context context;
	LayoutInflater minflater;

	FinalBitmap fb;

	public giftListAdapter(Context context, List<giftListItem> data) {
		this.minflater = LayoutInflater.from(context);
		this.context = context;
		this.data = data;
		fb = FinalBitmap.create(context);
	}

	// public giftListAdapter(Context context, List<giftListItem> list){
	// this.minflater = LayoutInflater.from(context);
	// this.context = context;
	// fb = FinalBitmap.create(context);
	// }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data != null ? data.size() : 0;
		// return data!=null ? (data.size()%3>0 ? data.size()/3+1 :
		// data.size()/3) : 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.item_gift_image, null);
			holder = new ViewHolder();
			fb = FinalBitmap.create(context);
			holder.gift_img = (ImageView) convertView.findViewById(R.id.gift_img);
			holder.gift_name = (TextView) convertView.findViewById(R.id.gift_name);
			holder.jiage = (TextView) convertView.findViewById(R.id.jiage);
			// 动态设置imageview显示的长宽
			DisplayMetrics dm = new DisplayMetrics();
			// 获取屏幕信息
			WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(dm);
			float density = dm.density;
			int height;
			height = (int) (dm.widthPixels / 2);

			// Logger.d("111", String.valueOf(height));
			// holder.gift_img.setLayoutParams(new
			// LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
			// height));
			holder.gift_name.setText(data.get(position).getName());
			String price = data.get(position).getPrice() + "";
			holder.jiage.setText("￥" + price + ".00");

			fb.display(holder.gift_img, data.get(position).getImg());
			convertView.setTag(holder);// 绑定ViewHolder对象

		} else {
			holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}

		return convertView;
	}

	public final class ViewHolder {
		ImageView gift_img;
		TextView gift_name;
		TextView jiage;

	}

}

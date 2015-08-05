package com.gzqining.inthegarden.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.SystemSetupActivity;
import com.gzqining.inthegarden.vo.AlbumBackList;
import com.gzqining.inthegarden.vo.giftListItem;
import com.gzqining.inthegarden.vo.walletGiftListBean;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class walletGiftListAdapter extends BaseAdapter {

	List<walletGiftListBean> data;
	Context context; 
    LayoutInflater minflater;
    
    FinalBitmap fb;
    public walletGiftListAdapter(Context context,List<walletGiftListBean> data){
    	this.minflater = LayoutInflater.from(context);
    	this.context = context;
    	this.data = data;
    	fb = FinalBitmap.create(context);
    }
//    public giftListAdapter(Context context, List<giftListItem> list){
//    	this.minflater = LayoutInflater.from(context);
//    	this.context = context;
//    	fb = FinalBitmap.create(context);
//    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data != null ? data.size():0;
		//return data!=null ? (data.size()%3>0 ? data.size()/3+1 : data.size()/3) : 1;
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
		if(convertView==null){
			convertView = minflater.inflate(R.layout.item_gift_image, null);
			holder = new ViewHolder();
			fb = FinalBitmap.create(context);
			holder.gift_img = (ImageView)convertView.findViewById(R.id.gift_img);
			holder.gift_name = (TextView) convertView.findViewById(R.id.gift_name);
			holder.jiage = (TextView) convertView.findViewById(R.id.jiage);
			//动态设置imageview显示的长宽
			DisplayMetrics dm = new DisplayMetrics();
			// 获取屏幕信息
			WindowManager manager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(dm);
			float density = dm.density;
			int height;
			height = (int) (dm.widthPixels / 2);

			// Logger.d("111", String.valueOf(height));
			holder.gift_img.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,height));
			holder.gift_name.setText(data.get(position).getGiftName());
			String price = data.get(position).getGiftPrice();
			holder.jiage.setText(price);
					
			fb.display(holder.gift_img, "http://61.152.93.162:8380/apps/gift/Tpl/default/Public/gift/"+data.get(position).getGiftImg());
			convertView.setTag(holder);//绑定ViewHolder对象
			
		}else{
			holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
		}
		
		return convertView;
	}
	
	public final class ViewHolder{
		ImageView gift_img;
		TextView gift_name;
		TextView jiage;
		
	}
	
	
}

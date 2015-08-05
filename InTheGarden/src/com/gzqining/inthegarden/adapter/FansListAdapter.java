package com.gzqining.inthegarden.adapter;

import net.tsz.afinal.FinalBitmap;

import com.gzqining.inthegarden.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FansListAdapter extends BaseAdapter {

	//List<LifeIndexResponse> lifeList;
    Context context; 
    LayoutInflater minflater;
    
    FinalBitmap fb;
	
	public FansListAdapter(Context context) { 
		this.minflater = LayoutInflater.from(context);
        this.context = context;
        //this.lifeList = lifeList;
        fb=FinalBitmap.create(context);
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//return lifeList != null ? lifeList.size() : 0;
		return 10;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		//return lifeList.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.item_fanslist, null);
			holder=new ViewHolder();
			fb = FinalBitmap.create(context);
			fb.configLoadingImage(R.drawable.logo);
			holder.name_tv=(TextView)convertView.findViewById(R.id.name_tv);
			holder.distance_tv=(TextView)convertView.findViewById(R.id.distance_tv);
			holder.time_tv=(TextView)convertView.findViewById(R.id.time_tv);
			holder.age_tv=(TextView)convertView.findViewById(R.id.age_tv);
			holder.height_tv=(TextView)convertView.findViewById(R.id.height_tv);
			holder.weight_tv=(TextView)convertView.findViewById(R.id.weight_tv);
			holder.sign_tv=(TextView)convertView.findViewById(R.id.sign_tv);
			holder.head_img=(ImageView)convertView.findViewById(R.id.head_img);
			holder.sex_img=(ImageView)convertView.findViewById(R.id.sex_img);
			//holder.ll=(LinearLayout)convertView.findViewById(R.id.ll);
			convertView.setTag(holder);//绑定ViewHolder对象
		}else{
			holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
		}
		
		//holder.ll.setVisibility(View.GONE);
		
		return convertView;
	}
	
	public final class ViewHolder{
		TextView name_tv;
		TextView distance_tv;
		TextView time_tv;
		TextView age_tv;
		TextView height_tv;
		TextView weight_tv;
		TextView sign_tv;
		ImageView head_img;
		ImageView sex_img;
		LinearLayout ll;
	}

//	public void addItem(LifeIndexResponse item) {  
//		lifeList.add(item);  
//    }

}

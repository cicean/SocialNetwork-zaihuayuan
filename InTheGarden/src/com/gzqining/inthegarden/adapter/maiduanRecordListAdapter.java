package com.gzqining.inthegarden.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.vo.maiduanRecordList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class maiduanRecordListAdapter extends BaseAdapter {

//	List<LifeIndexResponse> lifeList;
    Context context; 
    LayoutInflater minflater;
    List<maiduanRecordList> data;
    FinalBitmap fb;
	
	public maiduanRecordListAdapter(Context context) { 
		this.minflater = LayoutInflater.from(context);
        this.context = context;
        //this.lifeList = lifeList;
    }
	public maiduanRecordListAdapter(Context context ,List<maiduanRecordList> data) { 
		this.minflater = LayoutInflater.from(context);
        this.context = context;
        //this.lifeList = lifeList;
        fb=FinalBitmap.create(context);
        this.data = data;
    }
	
	@Override
	public int getCount() {                                                                                               
		// TODO Auto-generated method stub
		//return lifeList != null ? lifeList.size() : 0;
		return data.size();
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
			convertView = minflater.inflate(R.layout.item_maiduan_recordlist, null);
			holder=new ViewHolder();
			fb = FinalBitmap.create(context);
			fb.configLoadingImage(R.drawable.logo);
			holder.name_maiduanrecord_tv=(TextView)convertView.findViewById(R.id.name_maiduanrecord_tv);
			holder.maiduanrecord_money_tv=(TextView)convertView.findViewById(R.id.maiduanrecord_money_tv);
			holder.maiduanrecord_time_tv=(TextView)convertView.findViewById(R.id.maiduanrecord_time_tv);
			holder.sign_maiduanrecord_tv=(TextView)convertView.findViewById(R.id.sign_maiduanrecord_tv);
			holder.maiduanrecord_zhuangtai_tv=(TextView)convertView.findViewById(R.id.maiduanrecord_zhuangtai_tv);
			holder.head_img=(ImageView)convertView.findViewById(R.id.sex_img);
			//holder.ll=(LinearLayout)convertView.findViewById(R.id.ll);
			holder.name_maiduanrecord_tv.setText(data.get(position).getName());
			holder.maiduanrecord_money_tv.setText("￥"+data.get(position).getPrice());
			holder.maiduanrecord_time_tv.setText(data.get(position).getChecktime());
			switch(data.get(position).getStatus()){
				case 0:
					holder.maiduanrecord_zhuangtai_tv.setText("待确认");
					break;
				case 1:
					holder.maiduanrecord_zhuangtai_tv.setText("已确认");
					break;
				case 2:
					holder.maiduanrecord_zhuangtai_tv.setText("拒绝");
					break;
 			}
			if(data.get(position).getType()==0){
				holder.sign_maiduanrecord_tv.setText("买断TA"+data.get(position).getDays()+"天");
			}else{
				holder.sign_maiduanrecord_tv.setText("被买断"+data.get(position).getDays()+"天");
			}
			fb.display(holder.head_img, data.get(position).getHeadImg());
			convertView.setTag(holder);//绑定ViewHolder对象
		}else{
			holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
		}
		
		//holder.ll.setVisibility(View.GONE);
		
		return convertView;
	}
	
	public final class ViewHolder{
		TextView name_maiduanrecord_tv;
		TextView maiduanrecord_time_tv;
		TextView maiduanrecord_money_tv;
		TextView sign_maiduanrecord_tv;
		TextView maiduanrecord_zhuangtai_tv;
		ImageView head_img;
		LinearLayout ll;
	}

//	public void addItem(LifeIndexResponse item) {  
//		lifeList.add(item);  
//    }

}

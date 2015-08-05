package com.gzqining.inthegarden.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.vo.moneyDetailList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class moneyDetailsListAdapter extends BaseAdapter {

	//List<LifeIndexResponse> lifeList;
    Context context; 
    LayoutInflater minflater;
    List<moneyDetailList> data;
    FinalBitmap fb;
	
	public moneyDetailsListAdapter(Context context) { 
		this.minflater = LayoutInflater.from(context);
        this.context = context;
        //this.lifeList = lifeList;
        fb=FinalBitmap.create(context);
    }
	public moneyDetailsListAdapter(Context context,List<moneyDetailList> data) { 
		this.minflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        fb=FinalBitmap.create(context);
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
			convertView = minflater.inflate(R.layout.item_money_details_list, null);
			holder=new ViewHolder();
			fb = FinalBitmap.create(context);
			fb.configLoadingImage(R.drawable.logo);
			holder.name_money_detail_tv=(TextView)convertView.findViewById(R.id.name_money_detail_tv);
			holder.money_detail_tv=(TextView)convertView.findViewById(R.id.money_detail_tv);
			holder.money_detail_time_tv=(TextView)convertView.findViewById(R.id.money_detail_time_tv);
			holder.money_detail_money_tv=(TextView)convertView.findViewById(R.id.money_detail_money_tv);
			holder.name_money_detail_tv.setText(data.get(position).getUid());
			String time = getStrTime(data.get(position).getcTime());
			holder.money_detail_time_tv.setText(time);
			switch(data.get(position).getCreditType()){
			case 1://1.VIP预付款
				holder.money_detail_tv.setText("VIP预付款");
				holder.money_detail_money_tv.setText(data.get(position).getPrice());
				break;
			case 2://2.礼物购买
				holder.money_detail_tv.setText("礼物购买");
				holder.money_detail_money_tv.setText(data.get(position).getPrice());
				break;
			case 3://3.礼物撤回
				holder.money_detail_tv.setText("礼物撤回");
				holder.money_detail_money_tv.setText(data.get(position).getPrice());
				break;
			case 4://4.买断礼物购买
				holder.money_detail_tv.setText("买断礼物购买");
				holder.money_detail_money_tv.setText(data.get(position).getPrice());
				break;
			case 5://5买断礼物退回
				holder.money_detail_tv.setText("买断礼物退回");
				holder.money_detail_money_tv.setText(data.get(position).getPrice());
				break;
			case 6://6.兑换成功
				holder.money_detail_tv.setText("兑换成功");
				holder.money_detail_money_tv.setText(data.get(position).getPrice());
				break;
			case 7://7.礼物收入
				holder.money_detail_tv.setText("礼物收入");
				holder.money_detail_money_tv.setText(data.get(position).getPrice());
				break;
			case 8://8.买断礼物收入
				holder.money_detail_tv.setText("买断礼物收入");
				holder.money_detail_money_tv.setText(data.get(position).getPrice());
				break;
				default:
					break;
			}
//			holder.money_detail_money_tv.setText(data.get(position).getCreditType());
			//holder.ll=(LinearLayout)convertView.findViewById(R.id.ll);
			convertView.setTag(holder);//绑定ViewHolder对象
		}else{
			holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
		}
		
		//holder.ll.setVisibility(View.GONE);
		
		return convertView;
	}
	//时间戳转化为时间
	public String getStrTime(String time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = null;
        if (time.equals("")) {
                return "";
        }
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        long loc_time = Long.valueOf(time);
        re_StrTime = sdf.format(new Date(loc_time * 1000L));
        return re_StrTime;
		}
	public final class ViewHolder{
		TextView name_money_detail_tv;
		TextView money_detail_tv;
		TextView money_detail_time_tv;
		TextView money_detail_money_tv;
	}

//	public void addItem(LifeIndexResponse item) {  
//		lifeList.add(item);  
//    }

}

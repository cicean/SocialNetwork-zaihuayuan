package com.gzqining.inthegarden.adapter;

import java.util.Date;
import java.util.List;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.gridview.PersonActivity;
import com.gzqining.inthegarden.util.StandardDate;
import com.gzqining.inthegarden.vo.FindBackList;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FindListAdapter extends BaseAdapter {

	List<FindBackList> data;
    Context context; 
    LayoutInflater minflater;
    FinalBitmap fb;
	
	public FindListAdapter(Context context, List<FindBackList> data) { 
		this.minflater = LayoutInflater.from(context);
        this.context = context;
        //this.lifeList = lifeList;
        this.data = data;
        fb=FinalBitmap.create(context);
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//return lifeList != null ? lifeList.size() : 0;
		return data != null ? data.size():0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		//return lifeList.get(position);
		return data.get(position);
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
			convertView = minflater.inflate(R.layout.item_findlist, null);
			holder=new ViewHolder();
			fb = FinalBitmap.create(context);
			holder.name_tv=(TextView)convertView.findViewById(R.id.name_tv);
			holder.distance_tv=(TextView)convertView.findViewById(R.id.distance_tv);
			holder.time_tv=(TextView)convertView.findViewById(R.id.time_tv);
			holder.age_tv=(TextView)convertView.findViewById(R.id.age_tv);
			holder.height_tv=(TextView)convertView.findViewById(R.id.height_tv);
			holder.weight_tv=(TextView)convertView.findViewById(R.id.weight_tv);
			holder.sign_tv=(TextView)convertView.findViewById(R.id.sign_tv);
			holder.head_img=(ImageView)convertView.findViewById(R.id.head_img);
			holder.sex_img=(ImageView)convertView.findViewById(R.id.sex_img);
			
			convertView.setTag(holder);//绑定ViewHolder对象
		}else{
			holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
		}
		
		final FindBackList back = data.get(position);
		System.out.println(back);
		holder.name_tv.setText(back.getNickname());
		holder.distance_tv.setText(back.getDistance());
		//转换时间戳为距离现在多久的时间
		String time = StandardDate.getStandardDate((back.getLast_login_time()*1000));
		holder.time_tv.setText(time);
//		holder.age_tv.setText((CharSequence) back.getProfile().getBirthday());
//		holder.height_tv.setText((CharSequence) back.getProfile().getHeight());
//		holder.weight_tv.setText((CharSequence) back.getProfile().getWeight());
		holder.sign_tv.setText(back.getIntro());
		if(back.getSex().equals("1")){
			holder.sex_img.setImageResource(R.drawable.woman_icon);
		}else{
			holder.sex_img.setImageResource(R.drawable.man_icon);
		}
		long data = new Date().getTime();
		if(back.getAvatar_original() != null){
			fb.display(holder.head_img, back.getAvatar_original()+"#"+data);
		}else if(back.getAvatar_big() != null){
			fb.display(holder.head_img, back.getAvatar_big()+"#"+data);
		}else if(back.getAvatar_middle() != null){
			fb.display(holder.head_img, back.getAvatar_middle()+"#"+data);
		}else if(back.getAvatar_small() != null){
			fb.display(holder.head_img, back.getAvatar_small()+"#"+data);
		}else{
			fb.display(holder.head_img, back.getAvatar_tiny()+"#"+data);
		}
		
		convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, PersonActivity.class);
				intent.putExtra("uid", back.getUid());
				intent.putExtra("nickname", back.getNickname());
				intent.putExtra("follower", back.getFollow_state().getFollower());
				intent.putExtra("following", back.getFollow_state().getFollowing());
				if(back.getIs_vip().equals("1")){
					intent.putExtra("isVip",true);
				}else {
					intent.putExtra("isVip",false);
				}
                context.startActivity(intent);
			}});
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
		
	}

	public void addItem(FindBackList item) {  
		data.add(item);  
    }

}

package com.gzqining.inthegarden.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.util.StandardDate;
import com.gzqining.inthegarden.vo.personInfo.Followers;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAttentionListAdapter extends BaseAdapter {

	List<Followers> followers;
    Context context; 
    LayoutInflater minflater;
    FinalBitmap fb;
	
	public MyAttentionListAdapter(Context context, List<Followers> followers) { 
		this.minflater = LayoutInflater.from(context);
        this.context = context;
        this.followers = followers;
        fb=FinalBitmap.create(context);
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return followers != null ? followers.size() : 0;
		//return 10;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return followers.get(position);
		//return null;
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
			convertView = minflater.inflate(R.layout.item_attentionlist, null);
			holder=new ViewHolder();
			fb = FinalBitmap.create(context);
			fb.configLoadingImage(R.drawable.logo);
			holder.name_tv=(TextView)convertView.findViewById(R.id.name_attention_tv);
			holder.distance_tv=(TextView)convertView.findViewById(R.id.distance_attention_tv);
			holder.time_tv=(TextView)convertView.findViewById(R.id.time_attention_tv);
			holder.age_tv=(TextView)convertView.findViewById(R.id.age_attention_tv);
			holder.height_tv=(TextView)convertView.findViewById(R.id.height_attention_tv);
			holder.weight_tv=(TextView)convertView.findViewById(R.id.weight_attention_tv);
			holder.sign_tv=(TextView)convertView.findViewById(R.id.sign_attention_tv);
			holder.head_img=(ImageView)convertView.findViewById(R.id.head_attention_img);
			holder.sex_img=(ImageView)convertView.findViewById(R.id.sex_attention_img);
			//holder.ll=(LinearLayout)convertView.findViewById(R.id.ll);
			convertView.setTag(holder);//绑定ViewHolder对象
		}else{
			holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
		}
		
		Followers back = followers.get(position);
		try{
			if(back!=null)
			{
				if(!back.getAvatar_small().equals("")||!back.getAvatar_middle().equals("")||!back.getAvatar_big().equals("")){
					
					DisplayMetrics dm = new DisplayMetrics();
					//获取屏幕信息
					WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
					manager.getDefaultDisplay().getMetrics(dm);
					float density = dm.density;
					int height = (int) ((dm.widthPixels - (14 * density))/2);
//					Logger.d("111", String.valueOf(height));
					holder.head_img.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, height));
					fb.display(holder.head_img, back.getAvatar_big());
				}
				
				holder.name_tv.setText(back.getNickname());
				//holder.distance_tv.setText();
				String time = StandardDate.getStandardDate((back.getLast_login_time()*1000));
				holder.time_tv.setText(time);
				//holder.age_tv.setText();
				holder.height_tv.setText((CharSequence) back.getProfile().getHeight().getValue()+"cm");
				holder.weight_tv.setText((CharSequence) back.getProfile().getWeight().getValue()+"kg");
				holder.sign_tv.setText(back.getIntro());
				if(back.getSex().equals("1")){
					holder.sex_img.setImageResource(R.drawable.man_icon);
				}else{
					holder.sex_img.setImageResource(R.drawable.woman_icon);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
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

	public void addItem(Followers item) {  
		followers.add(item);  
    }

}

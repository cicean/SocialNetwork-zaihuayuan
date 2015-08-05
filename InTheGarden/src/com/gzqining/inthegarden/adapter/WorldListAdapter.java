package com.gzqining.inthegarden.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.WorldSpecificActivity;
import com.gzqining.inthegarden.vo.WorldBackList;
import com.gzqining.inthegarden.waterflow.XListView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WorldListAdapter extends BaseAdapter {

	List<WorldBackList> worldList;
    Context context; 
    LayoutInflater minflater;
    FinalBitmap fb;
    XListView mAdapterView;
	
	public WorldListAdapter(Context context, List<WorldBackList> data) { 
		this.minflater = LayoutInflater.from(context);
        this.context = context;
        this.worldList = data;
        fb=FinalBitmap.create(context);
    }
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return worldList != null ? worldList.size() : 0;
		//return 10;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return worldList.get(position);
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
			holder=new ViewHolder();
			convertView = minflater.inflate(R.layout.item_worldlist, null);
			holder.act_img=(ImageView)convertView.findViewById(R.id.act_img_world);
			holder.article_title=(TextView)convertView.findViewById(R.id.article_title);
			holder.article=(TextView)convertView.findViewById(R.id.article);
			convertView.setTag(holder);//绑定ViewHolder对象
		}else{
			holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
		}

		final WorldBackList back = worldList.get(position);
		
		if(back!=null)
		{
			if(!back.getImage().equals("")){
				
				fb.display(holder.act_img, back.getImage());
			}
			
			holder.article_title.setText(back.getTitle_intro());
			holder.article.setText(back.getContent_intro());
		}
		
		convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, WorldSpecificActivity.class);
				intent.putExtra("news_id", back.getNews_id());
				intent.putExtra("url", back.getUrl());
	            context.startActivity(intent);
			}});
	
		return convertView;
	}
	
	public final class ViewHolder{
		public TextView article_title;
		public TextView article;
		public ImageView act_img;
	}

	public void addItem(WorldBackList item) {  
		worldList.add(item);  
    }

}

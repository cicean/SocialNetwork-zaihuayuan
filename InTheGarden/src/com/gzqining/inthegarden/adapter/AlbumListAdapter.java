package com.gzqining.inthegarden.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.vo.AlbumBackList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AlbumListAdapter extends BaseAdapter {

	List<AlbumBackList> data;
	Context context; 
    LayoutInflater minflater;
    
    FinalBitmap fb;
    
    public AlbumListAdapter(Context context,List<AlbumBackList> data){
    	this.minflater = LayoutInflater.from(context);
    	this.context = context;
    	this.data = data;
    	fb = FinalBitmap.create(context);
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data != null ? data.size():0;
//		return data!=null ? (data.size()%3>0 ? data.size()/3+1 : data.size()/3) : 1;
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
			convertView = minflater.inflate(R.layout.fragment_photo_grid_image, null);
			//获取布局的长宽，并重新设置
			int x = (int) (parent.getWidth() / 3);
			int y = (int) (parent.getWidth() / 3);
			convertView.setLayoutParams(new AbsListView.LayoutParams(x, y));
			holder = new ViewHolder();
			//初始化finalBitmap
			fb = FinalBitmap.create(context);
			
			holder.photo_img1 = (ImageView)convertView.findViewById(R.id.photo_img);
			
			AlbumBackList back = data.get(position);
//			if(position == 0){
//				holder.photo_img.setImageResource(R.drawable.add_album);
//			}else{
				fb.display(holder.photo_img1, "http://61.152.93.162:8380/data/upload/"+back.getSavepath());
//			}
			
			convertView.setTag(holder);//绑定ViewHolder对象
			
		}else{
			holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
		}
		
		return convertView;
	}

	
	public final class ViewHolder{
		ImageView photo_img1;
		
	}
	
	
}

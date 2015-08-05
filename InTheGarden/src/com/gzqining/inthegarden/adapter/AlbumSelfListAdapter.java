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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AlbumSelfListAdapter extends BaseAdapter {

	List<AlbumBackList> picPath;
	Context context; 
    LayoutInflater minflater;
    
    FinalBitmap fb;
    public AlbumSelfListAdapter(Context context,List<AlbumBackList> picPath){
    	this.minflater = LayoutInflater.from(context);
    	this.context = context;
    	this.picPath = picPath;
    	fb = FinalBitmap.create(context);
    }
//    public AlbumSelfListAdapter(Context context,List<String> picPath){
//    	this.minflater = LayoutInflater.from(context);
//    	this.context = context;
//    	this.picPath = picPath;
//    	fb = FinalBitmap.create(context);
//    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return picPath != null ? picPath.size():0;
		//return data!=null ? (data.size()%3>0 ? data.size()/3+1 : data.size()/3) : 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return picPath.get(position);
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
			int x = (int) (parent.getWidth() / 3);
			int y = (int) (parent.getWidth() / 3);
//			int y = (int) (parent.getHeight() / 6);
			convertView = minflater.inflate(R.layout.fragment_photo_grid_image, null);
			convertView.setLayoutParams(new AbsListView.LayoutParams(x, y));
			holder = new ViewHolder();
			fb = FinalBitmap.create(context);
			holder.photo_img = (ImageView)convertView.findViewById(R.id.photo_img);
			
			
//			AlbumBackList back = list.get(position);
//			if(picPath.size()<=11){
//				if(position == 0){
//					holder.photo_img.setImageResource(R.drawable.add_picture);
//	//				fb.configLoadingImage(R.drawable.add_picture);
//				}else{
	//				holder.photo_img.setImageBitmap(returnBitMap(back.getSavepath()));
					
			fb.display(holder.photo_img, "http://61.152.93.162:8380/data/upload/"+picPath.get(position).getSavepath());
//			fb.display(holder.photo_img, "http://61.152.93.162:8380/data/upload/"+picPath.get(position));
//				}
//			}else{
//				fb.display(holder.photo_img, "http://61.152.93.162:8380/data/upload/"+picPath.get(position));
//			}
			convertView.setTag(holder);//绑定ViewHolder对象
			
		}else{
			holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
		}
		
		return convertView;
	}
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Toast.makeText(context, "你点击的是：" + position, 0).show();

		}

	};
	public final class ViewHolder{
		ImageView photo_img;
		
	}
	
	
}

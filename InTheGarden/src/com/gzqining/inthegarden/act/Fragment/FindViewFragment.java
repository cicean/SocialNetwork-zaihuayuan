package com.gzqining.inthegarden.act.Fragment;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.MainActivity;
import com.gzqining.inthegarden.adapter.FindListAdapter;
import com.gzqining.inthegarden.gridview.PersonActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.TaskUtil;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.FindBack;
import com.gzqining.inthegarden.vo.FindBackList;
import com.gzqining.inthegarden.vo.FindBean;
import com.gzqining.inthegarden.waterflow.XListView;
import com.gzqining.inthegarden.waterflow.XListView.IXListViewListener;
import com.gzqining.inthegarden.waterflow.internal.PLA_AdapterView;
import com.gzqining.inthegarden.waterflow.internal.PLA_AdapterView.OnItemClickListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;



public class FindViewFragment extends Fragment implements IXListViewListener, OnClickListener{
	
	//protected static final Context context = null;
	String tag = FindViewFragment.class.getName();
	public View v;
	public XListView mAdapterView = null;
	public FindListAdapter adapter = null;
	private LinearLayout find_add_ll;
	private LinearLayout find_distence_ll;
	private LinearLayout find_hot_ll;
	private ImageView search_add_btn;
	public int page;
	Object findObj;
	Handler findHandler;
	String choice = "place";
	
	String encodesstr = "";
	public ImageView no_users;
	//初始化LocationClient
	public LocationClient mLocationClient = null;

	private static int LOCATION_COUNTS = 0;
	//private String tempcoor="bd09ll";
	
	public static String result;
	MainActivity ma;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.fragment_find_view, container, false);
		ma = (MainActivity)getActivity();
		mAdapterView = (XListView) v.findViewById(R.id.people_list);
		find_add_ll = (LinearLayout) v.findViewById(R.id.find_add_ll);
		find_distence_ll = (LinearLayout) v.findViewById(R.id.find_distence_ll);
		find_hot_ll = (LinearLayout) v.findViewById(R.id.find_hot_ll);
		search_add_btn = (ImageView) v.findViewById(R.id.search_add_btn);
		no_users = (ImageView) v.findViewById(R.id.image_no_users);
		
		//声明LocationClient类
		//mLocationClient = ((LocationApplication)getActivity().getApplication()).mLocationClient;
		mLocationClient = new LocationClient(getActivity().getApplication());
		 
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapterView.setPullLoadEnable(true);
		//adapter = new FindListAdapter(getActivity(), null);
//		mAdapterView.setAdapter(adapter);
		find_add_ll.setOnClickListener(this);
		find_distence_ll.setOnClickListener(this);
		find_hot_ll.setOnClickListener(this);
		search_add_btn.setOnClickListener(this);
		
	
		InitLocation();
		mLocationClient.registerLocationListener(new BDLocationListener(){

			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if(location == null){
					return;
				}
				/*StringBuffer sb = new StringBuffer();
				sb.append(location.getLongitude()+",");
				sb.append(location.getLatitude());*/
				result = location.getLongitude()+","+location.getLatitude();
				
			}

			@Override
			public void onReceivePoi(BDLocation arg0) {
				// TODO Auto-generated method stub
				
			}});
		/*if(mLocationClient == null){
			return;
		}*/
		/*if(mLocationClient.isStarted()){
			mLocationClient.stop();
		}else{
			mLocationClient.start();
			mLocationClient.requestLocation();
		}*/
		
		FindHttpPost();
//		no_users.setVisibility(View.VISIBLE);
//		mAdapterView.setVisibility(View.GONE);
		//item监听跳转
		mAdapterView.setOnItemClickListener(new OnItemClickListener(){  

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
//				Intent intent = new Intent(getActivity(), PersonHomePageActivity.class);
				Intent intent = new Intent(getActivity(), PersonActivity.class);
				intent.putExtra("uid", ((FindBackList)adapter.getItem(position-1)).getUid());
				intent.putExtra("nickname", ((FindBackList)adapter.getItem(position-1)).getNickname());
				intent.putExtra("follower", ((FindBackList)adapter.getItem(position-1)).getFollow_state().getFollower());
				intent.putExtra("following", ((FindBackList)adapter.getItem(position-1)).getFollow_state().getFollowing());
				if(((FindBackList)adapter.getItem(position-1)).getIs_vip().equals("1")){
					intent.putExtra("isVip",true);
				}else {
					intent.putExtra("isVip",false);
				}
                startActivity(intent);
			}  
			    });  
		
		return v;
	}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
				case R.id.search_add_btn:
					find_add_ll.setVisibility(View.VISIBLE);
					break;
				case R.id.find_distence_ll:
					choice = "place";
					FindHttpPost();
					find_add_ll.setVisibility(View.GONE);
					break;
				case R.id.find_hot_ll:
					choice = "heat";
					FindHttpPost();
					find_add_ll.setVisibility(View.GONE);
					break;
				default:
					break;
			}
		}
		
	//
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		//option.setOpenGps(true);
		//option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
		//option.setIsNeedAddress(true);
		option.setPriority(LocationClientOption.NetWorkFirst);
		option.setOpenGps(true);//设置是否打开GPS
		mLocationClient.setLocOption(option);
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());		
		mAdapterView.setRefreshTime(sdf.format(new Date()));
		mAdapterView.stopRefresh();
		FindHttpPost();
		page = 1;
		
		if(mLocationClient.isStarted()){
			mLocationClient.stop();
		}else{
			mLocationClient.start();
			mLocationClient.requestLocation();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());		
		mAdapterView.setLoadTime(sdf.format(new Date()));
		mAdapterView.stopLoadMore();
		FindHttpPost();
	}

	public void FindHttpPost(){
//		mLocationClient.start();
//		mLocationClient.requestLocation();
		//Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
		
		FindBean findBean = new FindBean();
		findBean.setLocat("113.383108,23.141269");
//		findBean.setLocat(result);
		findBean.setType(choice);
		String jsonstr = JsonHelper.toJsonString(findBean);
		
		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			//System.out.println(encodesstr);
			Logger.d("encodesstr", encodesstr);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SharedPreferences sp = getActivity().getSharedPreferences("idcard",0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		
		ma.task = new TaskUtil(ma);
		ma.params = new AjaxParams();
		ma.params.put("oauth_token", oauth_token);
		ma.params.put("oauth_token_secret", oauth_token_secret);
		ma.params.put("page",""+page);
		ma.params.put("cont", encodesstr);

		ma.task.post(ma.params, Constans.looking_user);
		ma.task.setType(Constans.REQ_Looking_User);
		
	}

}


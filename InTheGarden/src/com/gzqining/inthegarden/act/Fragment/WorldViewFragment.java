package com.gzqining.inthegarden.act.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;


import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.MainActivity;
import com.gzqining.inthegarden.act.RegisterActivity;
import com.gzqining.inthegarden.act.WorldSpecificActivity;
import com.gzqining.inthegarden.adapter.WorldListAdapter;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.DialogUtil;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.TaskUtil;
import com.gzqining.inthegarden.vo.WorldBack;
import com.gzqining.inthegarden.waterflow.XListView;
import com.gzqining.inthegarden.waterflow.XListView.IXListViewListener;
import com.gzqining.inthegarden.waterflow.internal.PLA_AdapterView;
import com.gzqining.inthegarden.waterflow.internal.PLA_AdapterView.OnItemClickListener;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class WorldViewFragment extends Fragment implements IXListViewListener {
	String tag = WorldViewFragment.class.getName();
	public static int page;
	Dialog dialog;
	public View v;
	public XListView mAdapterView = null;
	public WorldListAdapter adapter = null;
	public ImageView no_world;
	MainActivity ma;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.fragment_world_view, container, false);
		ma = (MainActivity)getActivity();
		
		initViews();
		
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapterView.setPullLoadEnable(true);

		WorldHttpPost();

		return v;
	}
	
	private void initViews(){
		mAdapterView = (XListView) v.findViewById(R.id.world_list);
		no_world = (ImageView) v.findViewById(R.id.image_no_world);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());		
		mAdapterView.setRefreshTime(sdf.format(new Date()));
		mAdapterView.stopRefresh();
		WorldHttpPost();
		page = 1;
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());		
		mAdapterView.setLoadTime(sdf.format(new Date()));
		mAdapterView.stopLoadMore();
		WorldHttpPost();
	}
	private void WorldHttpPost() {
		Logger.d(tag, "ready");
		SharedPreferences sp = getActivity().getSharedPreferences("idcard",0);
		String oauth_token = sp.getString("oauth_token", null);
		String oauth_token_secret = sp.getString("oauth_token_secret", null);
		
		ma.task = new TaskUtil(ma);
		ma.params = new AjaxParams();
		ma.params.put("oauth_token", oauth_token);
		ma.params.put("oauth_token_secret", oauth_token_secret);
		ma.params.put("page",""+page);

		ma.task.post(ma.params, Constans.world);
		ma.task.setType(Constans.REQ_World);
	
	}
	
	public void showDialog() {
		dialog = DialogUtil.showProgressDialog(getActivity(),getString(R.string.request_data), false);
		dialog.show();
	}

	public void showDialogloading(String text) {
		dialog = DialogUtil.showProgressDialog(getActivity(), text, true);
		dialog.show();
	}

	public void canelDialog() {
		if (dialog != null) {
			// dialog.cancel();
			dialog.dismiss();
		}
	}
}

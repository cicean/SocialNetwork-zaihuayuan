package com.gzqining.inthegarden.act.Fragment;

import java.util.Date;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.MainActivity;
import com.gzqining.inthegarden.act.MyInfoActivity;
import com.gzqining.inthegarden.act.RelationOfMineActivity;
import com.gzqining.inthegarden.act.SystemSetupActivity;
import com.gzqining.inthegarden.act.VipActivity;
import com.gzqining.inthegarden.act.WalletActivity;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.CircleImageView;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.util.TaskUtil;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.UserInfoBack;
import com.gzqining.inthegarden.vo.UserInfoBean;

public class MineViewFragment extends Fragment {

	public View v;
	public CircleImageView head_mine;
	private TextView nick_name;
	private RelativeLayout head_rl;
	private RelativeLayout wallet_mine;
	private RelativeLayout vip_mine;
	private RelativeLayout relation;
	private RelativeLayout setup;
	
	MainActivity ma;

	Object mineObj;
	FinalBitmap fb;
	static Handler mineHandler;
	String encodesstr = "";
	UserInfoBack back;
	long data = new Date().getTime();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.fragment_mine_view, container, false);
		fb = FinalBitmap.create(getActivity());
		ma = (MainActivity)getActivity();
		
		initViews();
		initEvents();
		
		return v;
	}
	
	private void initViews(){
		head_mine = (CircleImageView) v.findViewById(R.id.head_mine);// 头像
		nick_name = (TextView) v.findViewById(R.id.nickname_mine);// 昵称
		head_rl = (RelativeLayout) v.findViewById(R.id.head_rl);
		wallet_mine = (RelativeLayout) v.findViewById(R.id.wallet_mine);
		vip_mine = (RelativeLayout) v.findViewById(R.id.vip_mine);// 相册
		relation = (RelativeLayout) v.findViewById(R.id.relation);// 关系
		setup = (RelativeLayout) v.findViewById(R.id.setup);// 设置
	}
	
	private void initEvents(){
		head_rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到个人信息
				Intent intent = new Intent(getActivity(), MyInfoActivity.class);

				startActivity(intent);
			}
		});
		wallet_mine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到钱包
				Intent intent = new Intent(getActivity(), WalletActivity.class);
				startActivity(intent);
			}
		});
		vip_mine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到vip
				Intent intent = new Intent(getActivity(), VipActivity.class);
				startActivity(intent);
			}
		});
		relation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到关系
				Intent intent = new Intent(getActivity(), RelationOfMineActivity.class);
				startActivity(intent);
			}
		});
		setup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到设置
				Intent intent = new Intent(getActivity(), SystemSetupActivity.class);
				startActivity(intent);
			}
		});
	}

	private void MineHttpPost() {
		SharedPreferences sp = getActivity().getSharedPreferences("idcard", 0);
		final String oauth_token = sp.getString("oauth_token", null);
		final String oauth_token_secret = sp.getString("oauth_token_secret", null);
		String uid = sp.getString("uid", null);
		UserInfoBean bean = new UserInfoBean();
		bean.setUid(uid);
		String jsonstr = JsonHelper.toJsonString(bean);

		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ma.task = new TaskUtil(ma);
		ma.params = new AjaxParams();
		ma.params.put("oauth_token", oauth_token);
		ma.params.put("oauth_token_secret", oauth_token_secret);
		ma.params.put("cont", encodesstr);

		ma.task.post(ma.params, Constans.userinfo);
		ma.task.setType(Constans.REQ_UserInfo);
		
	}

	@Override
	public void onResume() {
		MineHttpPost();
		super.onResume();
	}
}

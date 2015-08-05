package com.gzqining.inthegarden.act;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.adapter.giftListAdapter;
import com.gzqining.inthegarden.adapter.walletGiftListAdapter;
import com.gzqining.inthegarden.common.BaseActivity;
import com.gzqining.inthegarden.dialog.giftDialog;
import com.gzqining.inthegarden.util.AesUtils;
import com.gzqining.inthegarden.util.Constans;
import com.gzqining.inthegarden.util.JsonHelper;
import com.gzqining.inthegarden.util.Logger;
import com.gzqining.inthegarden.util.ReqJsonUtil;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.giftListBack;
import com.gzqining.inthegarden.vo.sendGiftBean;
import com.gzqining.inthegarden.vo.walletGiftListBack;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class presentWalletListActivity extends BaseActivity {
	private PullToRefreshGridView gridView = null;
	walletGiftListAdapter adapter = null;
	private int page = 1;
	Handler giftHandler;
	String encodesstr = "";
	Object obj;
	ImageButton top_layout_gift_back_btn;
	String price;
	walletGiftListBack back;
	String oauth_token;
	String oauth_token_secret;
	String fid;
	String giftid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_gift_list);
		AppManager.getAppManager().addActivity(this);
		fid = this.getIntent().getStringExtra("fid");
		getGift();
		gridView = (PullToRefreshGridView)findViewById(R.id.gift_list_view);
		top_layout_gift_back_btn = (ImageButton) findViewById(R.id.top_layout_gift_back_btn);
		top_layout_gift_back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
//		adapter = new giftListAdapter(giftListActivity.this);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				price = back.getData().get(position).getGiftPrice();
				giftid = back.getData().get(position).getId();
//				final giftDialog dialog = new giftDialog(presentWalletListActivity.this, R.style.MyDialog,price,oauth_token,oauth_token_secret,fid,giftid);
//				dialog.show();
			}
		});
		gridView.setOnRefreshListener(new OnRefreshListener2<GridView>(){

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView)
			{
				Log.e("TAG", "onPullDownToRefresh"); // Do work to
				String label = DateUtils.formatDateTime(
						getApplicationContext(),
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(label);
				new GetDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView)
			{
				Log.e("TAG", "onPullUpToRefresh"); // Do work to refresh
													// the list here.
				new GetDataTask().execute();
			}
		});
	}

	private void initIndicator()
	{
		ILoadingLayout startLabels = gridView.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("松开载入更多...");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = gridView.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在刷新...");// 刷新时
		endLabels.setReleaseLabel("松开载入更多...");// 下来达到一定距离时，显示的提示
	}
	private class GetDataTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params)
		{
			try
			{
				Thread.sleep(2000);
			} catch (InterruptedException e)
			{
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			getGift();
//			picPath.add("" + mItemCount++);
			adapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			gridView.onRefreshComplete();
		}
	}
	private void getGift(){
		SharedPreferences sp = this.getSharedPreferences("idcard",0);
		oauth_token = sp.getString("oauth_token", null);
		oauth_token_secret = sp.getString("oauth_token_secret", null);
		final String id = sp.getString("uid", null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = Constans.toUserGift;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", oauth_token);
				params.put("oauth_token_secret", oauth_token_secret);
				params.put("id", id);
				
				FinalHttp http = new FinalHttp();
				obj =  http.postSync(url, params);
				giftHandler.sendEmptyMessage(1);
				
			}
		}).start();

		giftHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					try {
						
						back = (walletGiftListBack) ReqJsonUtil.changeToObject(obj.toString(), walletGiftListBack.class);
						if (back.getCode().equals("00000")) {
							
							
							adapter = new walletGiftListAdapter(presentWalletListActivity.this, back.getData());
//									}
//								}
							page++;
					        gridView.setAdapter(adapter);
							adapter.notifyDataSetChanged();//在修改适配器绑定的数组后，不用重新刷新Activity，通知Activity更新ListView
							//Toast.makeText(getActivity(), albumBack.getMessage(),Toast.LENGTH_LONG).show();
					}else {
							//Toast.makeText(getActivity(), albumBack.getMessage(),Toast.LENGTH_LONG).show();
				}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
	}
}

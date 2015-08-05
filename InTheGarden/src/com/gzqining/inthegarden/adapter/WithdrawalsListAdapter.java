package com.gzqining.inthegarden.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.vo.bankInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class WithdrawalsListAdapter extends BaseAdapter {
	Context context;
	LayoutInflater minflater;
	List<bankInfo> data;
	/**
	 * CheckBox 是否选择的存储集合,key 是 position , value 是该position是否选中
	 */
	@SuppressLint("UseSparseArrays")
	private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();

	/**
	 * 首先,默认情况下,所有项目都是没有选中的.这里进行初始化
	 */
	public void configCheckMap(boolean bool) {

		for (int i = 0; i < data.size(); i++) {
			isCheckMap.put(i, false);
		}

	}

	public WithdrawalsListAdapter(Context context) {
		this.minflater = LayoutInflater.from(context);
		this.context = context;
	}

	public WithdrawalsListAdapter(Context context, List<bankInfo> data) {
		this.minflater = LayoutInflater.from(context);
		this.context = context;
		this.data = data;
		configCheckMap(false);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.item_bank_list, null);
			holder = new ViewHolder();
			holder.name_bank_tv = (TextView) convertView.findViewById(R.id.name_bank_tv);
			holder.default_bank = (Button) convertView.findViewById(R.id.default_bank);
			holder.bank_end_tv = (TextView) convertView.findViewById(R.id.bank_end_tv);
			holder.gouxuan_bank = (CheckBox) convertView.findViewById(R.id.gouxuan_bank);
			// holder.ll=(LinearLayout)convertView.findViewById(R.id.ll);
			if (data.get(position).getIs_default().equals("1")) {
				holder.default_bank.setVisibility(View.VISIBLE);
			}
			holder.name_bank_tv.setText(data.get(position).getUname());
			// 截取尾号
			String str = data.get(position).getCard_num().substring(data.get(position).getCard_num().length() - 4, data.get(position).getCard_num().length());
			holder.bank_end_tv.setText("尾号：" + str);
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}
		/*
		 * 设置单选按钮的选中
		 */
		holder.gouxuan_bank.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				configCheckMap(false);
				/*
				 * 将选择项加载到map里面寄存
				 */
				isCheckMap.put(position, isChecked);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	public final class ViewHolder {
		TextView name_bank_tv;
		Button default_bank;
		TextView bank_end_tv;
		public CheckBox gouxuan_bank;
	}

}

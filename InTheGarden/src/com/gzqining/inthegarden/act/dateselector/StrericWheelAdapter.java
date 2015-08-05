package com.gzqining.inthegarden.act.dateselector;

import com.gzqining.inthegarden.act.dateselector.WheelAdapter;

public class StrericWheelAdapter implements WheelAdapter {
	
	/** The default min value */
	private String[] strContents;
	/**
	 * 构造方法
	 * @param strContents
	 */
	public StrericWheelAdapter(String[] strContents){
		this.strContents=strContents;
	}
	
	
	public String[] getStrContents() {
		return strContents;
	}


	public void setStrContents(String[] strContents) {
		this.strContents = strContents;
	}


	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			return strContents[index];
		}
		return null;
	}
	
	@Override
	public int getItemsCount() {
		return strContents.length;
	}
	/**
	 * 设置最大的宽度
	 */
	@Override
	public int getMaximumLength() {
		int maxLen=5;
		return maxLen;
	}
}

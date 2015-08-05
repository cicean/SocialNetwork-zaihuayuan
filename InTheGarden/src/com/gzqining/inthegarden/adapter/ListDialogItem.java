package com.gzqining.inthegarden.adapter;

public class ListDialogItem {

	private String value;
	private boolean isSelected;
	
	public ListDialogItem(String val) {
		// TODO Auto-generated constructor stubval
		this.value = val;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}

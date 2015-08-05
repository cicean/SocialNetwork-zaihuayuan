package com.gzqining.inthegarden.act.dateselector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.act.dateselector.DateSelectorWheelView;
import com.gzqining.inthegarden.act.dateselector.DateTimeSelectorDialogBuilder;
import com.gzqining.inthegarden.act.dateselector.NiftyDialogBuilder;

public class DateTimeSelectorDialogBuilder extends NiftyDialogBuilder implements
android.view.View.OnClickListener {

private Context context;
private RelativeLayout rlCustomLayout;
private DateSelectorWheelView dateWheelView;
private FrameLayout flSecondeCustomLayout;
private OnSaveListener saveListener;
private View view;

public interface OnSaveListener {
abstract void onSaveSelectedDate(DateSelectorWheelView dateWheelView, View view);
}

public DateTimeSelectorDialogBuilder(Context context, View view) {
super(context);
this.context = context;
this.view = view;
initDialog();
}

public DateTimeSelectorDialogBuilder(Context context, int theme, View view) {
super(context, theme);
this.context = context;
this.view = view;
initDialog();
}

private void initDialog() {
rlCustomLayout = (RelativeLayout) LayoutInflater.from(context).inflate(
		R.layout.date_time_selector_dialog_layout, null);

dateWheelView = (DateSelectorWheelView) rlCustomLayout.findViewById(R.id.pdwv_date_time_selector_wheelView);
dateWheelView.setTitleClick(this);

flSecondeCustomLayout = (FrameLayout) rlCustomLayout
		.findViewById(R.id.fl_date_time_custom_layout);

setDialogProperties();
}
/**
* 设置Dialog的属性
*/
private void setDialogProperties() {
this.withDialogWindows(800, LayoutParams.WRAP_CONTENT)
		.withTitle("输入出生日期，自动转换为年龄及星座")
		.withDialoagBackGround("#FFFFFF")
		.withPreviousTextColor("#3598da").withDuration(100)
		.setPreviousLayoutClick(this).withNextText("确定")
		.withMessageMiss(View.GONE).withNextTextColor("#3598da")
		.setNextLayoutClick(this)
		.setCustomView(rlCustomLayout, context);

      	
}

/**
* 设置自定义布局
* 
* @param view
* @param context
* @return
*/
public DateTimeSelectorDialogBuilder setSencondeCustomView(View view,
	Context context) {
if (flSecondeCustomLayout.getChildCount() > 0) {
	flSecondeCustomLayout.removeAllViews();
}
flSecondeCustomLayout.addView(view);
return this;
}

/**
* 设置自定义布局
* 
* @param resId
* @param context
* @return
*/
public DateTimeSelectorDialogBuilder setSencondeCustomView(int resId,
	Context context) {
View view = View.inflate(context, resId, null);
if (flSecondeCustomLayout.getChildCount() > 0) {
	flSecondeCustomLayout.removeAllViews();
}
flSecondeCustomLayout.addView(view);
return this;
}

@Override
public void onClick(View v) {
int id = v.getId();
switch (id) {
case R.id.rl_date_time_title:
	if (dateWheelView.getDateSelectorVisibility() == View.VISIBLE) {
		dateWheelView.setDateSelectorVisiblility(View.GONE);
	} else {
		dateWheelView.setDateSelectorVisiblility(View.VISIBLE);
	}
	break;
case R.id.fl_dialog_title_previous:
	dismiss();
	break;
case R.id.fl_dialog_title_next:
	if (null != saveListener) {
		saveListener.onSaveSelectedDate(dateWheelView, view);
	}
	dismiss();
	break;
}
}
/**
* 获取日期选择器
* @return
*/
public DateSelectorWheelView getDateWheelView() {
return dateWheelView;
}
/**
* 设置保存监听
* @param saveListener
*/
public void setOnSaveListener(OnSaveListener saveListener){
this.saveListener = saveListener;
}
/**
* 最初显示时是否可以可见
* @param visibility
*/
public void setWheelViewVisibility(int visibility){
dateWheelView.setDateSelectorVisiblility(visibility);
}

}

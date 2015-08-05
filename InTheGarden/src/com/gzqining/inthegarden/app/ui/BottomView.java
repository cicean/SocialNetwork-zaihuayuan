package com.gzqining.inthegarden.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gzqining.inthegarden.R;

public class BottomView {
	
	private View convertView;
	private Context context;
	private int theme;
	private Dialog bv;
	private int animationStyle;
	private boolean isTop = false;
	
	public BottomView(Context context, View convertView) {
		this(context, R.style.bottomview_default, convertView);
	}

	public BottomView(Context context, int theme, View convertView) {
		this.theme = theme;
		this.context = context;
		this.convertView = convertView;
	}

	public BottomView(Context context, int resource) {
		this(context, R.style.bottomview_default, resource);
	}
	
	public BottomView(Context context, int theme, int resource) {
		this.theme = theme;
		this.context = context;
		this.convertView = View.inflate(context, resource, null);
	}

	public void showBottomView(boolean canceledOnTouchOutside) {
		if (this.theme == 0)
			this.bv = new Dialog(this.context);
		else
			this.bv = new Dialog(this.context, this.theme);

		this.bv.setCanceledOnTouchOutside(canceledOnTouchOutside);
		this.bv.getWindow().requestFeature(1);
		this.bv.setContentView(this.convertView);
		Window wm = this.bv.getWindow();
		WindowManager m = wm.getWindowManager();
		Display d = m.getDefaultDisplay();
		WindowManager.LayoutParams p = wm.getAttributes();
		p.width = (d.getWidth() * 1);
		if (this.isTop)
			p.gravity = Gravity.TOP;
		else
			p.gravity = Gravity.BOTTOM;

		if (this.animationStyle != 0) {
			wm.setWindowAnimations(this.animationStyle);
		}
		wm.setAttributes(p);
		this.bv.show();
	}

	public void setTopIfNecessary() {
		this.isTop = true;
	}

	public void setAnimation(int animationStyle) {
		this.animationStyle = animationStyle;
	}

	public View getView() {
		return this.convertView;
	}

	public void dismissBottomView() {
		if (this.bv != null)
			this.bv.dismiss();
	}
}
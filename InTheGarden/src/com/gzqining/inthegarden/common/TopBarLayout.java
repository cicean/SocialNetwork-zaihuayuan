package com.gzqining.inthegarden.common;


import com.gzqining.inthegarden.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopBarLayout extends RelativeLayout implements OnClickListener {

	private String title;

	public TopBarLayout(Context context) {
		super(context);
		init();
	}

	public TopBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		
		if(attrs != null) {
			String title = attrs.getAttributeValue(null, "title");
			if(!TextUtils.isEmpty(title)) {
				int i = title.indexOf("/");
				if(i >= 0) {
					String name = title.substring(i+1);
					String value = null;
					if(title.startsWith("@string")) {
						value = getResources().getString(getResources().getIdentifier(name, "string", getContext().getPackageName()));
					} else if(title.startsWith("@android:string")) {
						value = getResources().getString(getResources().getIdentifier(name, "string", "android"));
					}
					if(!TextUtils.isEmpty(value) && titleTv != null) {
						titleTv.setText(value);
					}
				} else {
					if(titleTv != null) {
						titleTv.setText(title);
					}
				}
			}
		}
	}

	
	// 初始化界面
	private void init() {
		LayoutInflater layoutInflater = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout mainLayout = (RelativeLayout) layoutInflater.inflate(
				R.layout.common_layout_topbarlayout, null);
		this.addView(mainLayout);

		// ViewUtils.inject(this);
		titleTv = (TextView) this
				.findViewById(R.id.common_layout_topbarlayout_title);
//		backBtn = (ImageButton) this
//				.findViewById(R.id.common_layout_topbarlayout_backbtn);
//		backBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				try {
//					Activity mActivity = (Activity) TopBarLayout.this
//							.getContext();
//					mActivity.finish();
//				} catch (Exception ex) {
//
//				}
//			}
//		});
	}

	private TextView titleTv;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		titleTv.setText(title);
	}
	
//	public void setBackBtnVisibility(int viewVisiblity){
//		backBtn.setVisibility(viewVisiblity);
//	}
//
//	public void setOnBackBtnClickListener(OnClickListener clickListener) {
//		if (backBtn != null) {
//			backBtn.setOnClickListener(clickListener);
//		}
//	}

	@Override
	public void onClick(View v) {

	}

}

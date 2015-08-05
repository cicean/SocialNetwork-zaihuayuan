package com.gzqining.inthegarden.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.widget.ListView;

/**
 * 
  * 不能在一个拥有Scrollbar的组件中嵌入另一个拥有Scrollbar的组件，因为这不科学，会混淆滑动事件，导致只显示一到两行数据。那么就换一种思路，
 * 首先让子控件的内容全部显示出来，禁用了它的滚动。如果超过了父控件的范围则显示父控件的scrollbar滚动显示内容，思路是这样，一下是代码。
 * 具体的方法是自定义GridView组件，继承自GridView。重载onMeasure方法：
 * 
 * @author lyy
 * 
 */
public class MyListView extends ListView {

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyListView(Context context) {
		super(context);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/***
	 * 
	 * 改变高度 其中onMeasure函数决定了组件显示的高度与宽度；
	 * makeMeasureSpec函数中第一个函数决定布局空间的大小，第二个参数是布局模式
	 * MeasureSpec.AT_MOST的意思就是子控件需要多大的控件就扩展到多大的空间
	 * 之后在ScrollView中添加这个组件就OK了，同样的道理，ListView也适用。
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//动态设置imageview显示的长宽
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		WindowManager manager = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int height;
		height = (int) (dm.heightPixels);
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		if(height>expandSpec){
			super.onMeasure(widthMeasureSpec, height);
		}else{
			super.onMeasure(widthMeasureSpec, expandSpec);
		}
		
	}
}

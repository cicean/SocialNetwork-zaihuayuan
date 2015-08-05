package com.gzqining.inthegarden.app;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AdapterHelper {

    private final SparseArray<View> views;

    private View convertView;

    private AdapterHelper(Context context, ViewGroup parent, int layoutId) {
        this.views = new SparseArray<View>();
        convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        convertView.setTag(this);
    }

    public static AdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutId) {
    	AdapterHelper helper = null;
        if (convertView == null) {
        	helper =  new AdapterHelper(context, parent, layoutId);
        }else{
        	helper =  (AdapterHelper) convertView.getTag();
        }
        return helper;
    }
    
    public View getView() {
        return convertView;
    }
    
    @SuppressWarnings("unchecked")
	public <T extends View> T find(int viewId) {
    	 View view = views.get(viewId);
         if (view == null) {
             view = convertView.findViewById(viewId);
             views.put(viewId, view);
         }
         return (T) view;
    }

    public <T extends View> T find(int viewId, Class<T> cls) {
        return find(viewId);
    }

}

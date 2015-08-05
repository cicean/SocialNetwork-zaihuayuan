package com.gzqining.inthegarden.app.injection;

import java.lang.reflect.Method;

import android.view.View;
import android.widget.AdapterView;

class InjectListener implements View.OnClickListener, View.OnLongClickListener, AdapterView.OnItemClickListener,
		AdapterView.OnItemLongClickListener, AdapterView.OnItemSelectedListener {

	private Object target;
	private Method method;

	protected InjectListener(Object target, Method method) {
		this.target = target;
		this.method = method;
	}

	@Override
	public void onClick(View v) {
		try {
			method.setAccessible(true);
			method.invoke(target, v);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onLongClick(View v) {
		try {
			method.setAccessible(true);
			return (Boolean) method.invoke(target, v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object args[] = null;
		Class<?> cls[] = method.getParameterTypes();
		if (cls != null && cls.length > 0) {
			args = new Object[cls.length];
			for (int i = 0, length = cls.length; i < length; i++) {
				Class<?> c = cls[i];
				if (c.equals(AdapterView.class)) {
					args[i] = parent;
				} else if (c.equals(View.class)) {
					args[i] = view;
				} else if (c.equals(int.class)) {
					args[i] = position;
				} else if (c.equals(long.class)) {
					args[i] = id;
				} else {
					args[i] = null;
				}
			}
		}
		try {
			method.setAccessible(true);
			method.invoke(target, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		Object args[] = null;
		Class<?> cls[] = method.getParameterTypes();
		if (cls != null && cls.length > 0) {
			args = new Object[cls.length];
			for (int i = 0, length = cls.length; i < length; i++) {
				Class<?> c = cls[i];
				if (c.equals(AdapterView.class)) {
					args[i] = parent;
				} else if (c.equals(View.class)) {
					args[i] = view;
				} else if (c.equals(int.class)) {
					args[i] = position;
				} else if (c.equals(long.class)) {
					args[i] = id;
				} else {
					args[i] = null;
				}
			}
		}
		try {
			method.setAccessible(true);
			return (Boolean) method.invoke(target, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}


}

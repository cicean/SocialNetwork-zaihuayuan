package com.gzqining.inthegarden.app.injection;

import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

public class Injection {
	
	private static Map<Class<?>,SoftReference<List<InjectPre>>> INJECT_PRE; 
	static{
		INJECT_PRE = new HashMap<Class<?>, SoftReference<List<InjectPre>>>();
	}
	
	/** Views indexed with their IDs */
    private final SparseArray<View> views;
    
    private Object target;
    private Object source;
    private InjectFinder finder;
	
	private Injection(Object target, Object source, InjectFinder finder) {
		this.target = target;
		this.source = source;
		this.finder = finder;
		views = new SparseArray<View>();
	}
	
	public Injection(Object target, Activity source) {
		this(target, source, InjectFinder.ACTIVITY);
	}
	
	public Injection(Object target, View source) {
		this(target, source, InjectFinder.VIEW);
	}
	
	public Injection(Fragment target, View source) {
		this(target, source, InjectFinder.FRAGMENT);
	}
	
	public void execute() {
		long startT = System.currentTimeMillis();
		Class<?> targetClass = target.getClass();
		synchronized (targetClass) {
			List<InjectPre> listPres = null;
			SoftReference<List<InjectPre>> mSoftReference =  INJECT_PRE.get(targetClass);
			if(mSoftReference != null) {
				listPres = mSoftReference.get();
			}
			
			if(listPres == null) {
				listPres = new ArrayList<InjectPre>();
				findClsPre(listPres, targetClass);
				INJECT_PRE.put(targetClass, new SoftReference<List<InjectPre>>(listPres));
			}
			
			if(listPres != null) {
				
				//BeforeInject
				for(InjectPre pre : listPres) {
					if(pre.getExecute() == InjectExecute.BEFORE_INJECT) {
						pre.getExecute().execute(this, target, pre.getEntity(), pre.getAnnotation());
					}
				}
				
				//ViewInject
				for(InjectPre pre : listPres) {
					if(pre.getExecute() == InjectExecute.BEFORE_INJECT || pre.getExecute() == InjectExecute.AFTER_INJECT) 
						continue;
					pre.getExecute().execute(this, target, pre.getEntity(), pre.getAnnotation());
				}
				
				//AfterInject
				for(InjectPre pre : listPres) {
					if(pre.getExecute() == InjectExecute.AFTER_INJECT) {
						pre.getExecute().execute(this, target, pre.getEntity(), pre.getAnnotation());
					}
				}
				
			}
		}
		long endT = System.currentTimeMillis();
		Log.i("T", "###### inject time "+(endT-startT));
	}
	
	private void findClsPre(List<InjectPre>listPres, Class<?> targetClass) {
		
		//field
		Field[] fields = targetClass.getDeclaredFields();
		if(fields != null) {
			for(Field field : fields) {
				if(Modifier.isFinal(field.getModifiers()))
					continue;
				
				for(InjectExecute execute : InjectExecute.values()) {
					if(execute.type() != InjectExecute.TYPE_FIELD)
						continue;
					if(field.isAnnotationPresent(execute.annotation())) {
						Annotation annotation = field.getAnnotation(execute.annotation());
						InjectPre pre = new InjectPre();
						pre.setEntity(field);
						pre.setAnnotation(annotation);
						pre.setExecute(execute);
						listPres.add(pre);
						break;
					}
				}
			}
		}
		
		//method
		Method[] methods = targetClass.getDeclaredMethods();
		if(methods != null) {
			for(Method method : methods) {
				for(InjectExecute execute : InjectExecute.values()) {
					if(execute.type() != InjectExecute.TYPE_METHOD)
						continue;
					if(method.isAnnotationPresent(execute.annotation())) {
						Annotation annotation = method.getAnnotation(execute.annotation());
						InjectPre pre = new InjectPre();
						pre.setEntity(method);
						pre.setAnnotation(annotation);
						pre.setExecute(execute);
						listPres.add(pre);
						break;
					}
				}
			}
		}
		
		Class<?> superCls = targetClass.getSuperclass();
		if(superCls == null || superCls.getName().startsWith("java.") || superCls.getName().startsWith("javax.") || superCls.getName().startsWith("android."))
			return;
		findClsPre(listPres, superCls);
	}
	
	public Context getContext() {
		return finder.getContext(target, source);
	}
	
	public Bundle getExtras() {
		return finder.getExtras(target, source);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends View> T find(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = finder.findView(target, source, viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
    public <T extends View> T find(int viewId, Class<T> cls) {
        return find(viewId);
    }
	
}

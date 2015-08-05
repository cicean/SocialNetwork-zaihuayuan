package com.gzqining.inthegarden.app.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

/**
 * @author xjm
 */
enum InjectExecute {
	
	BEFORE_INJECT{
		@Override
		protected int type() {
			return TYPE_METHOD;
		}

		@Override
		protected Class<? extends Annotation> annotation() {
			return BeforeInject.class;
		}
		
		@Override
		protected void execute(Injection binder, Object target, Object entity, Object annotation) {
			try {
				Method method = (Method) entity;
				method.setAccessible(true);
				method.invoke(target);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}, 
	AFTER_INJECT {
		
		@Override
		protected int type() {
			return TYPE_METHOD;
		}

		@Override
		protected Class<? extends Annotation> annotation() {
			return AfterInject.class;
		}
		
		@Override
		protected void execute(Injection binder, Object target, Object entity, Object annotation) {
			try {
				Method method = (Method) entity;
				method.setAccessible(true);
				method.invoke(target);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	},
	EXTRA{

		@Override
		protected int type() {
			return TYPE_FIELD;
		}

		@Override
		protected Class<? extends Annotation> annotation() {
			return Extra.class;
		}

		@Override
		protected void execute(Injection binder, Object target, Object entity, Object annotation) {
			try {
				Bundle bundle = binder.getExtras();
				if(bundle == null) return;
				
				Field field = (Field) entity;
				String key = ((Extra) annotation).value();
				if(TextUtils.isEmpty(key))
					key = field.getName();
				
				if(!bundle.containsKey(key)) return;
				
				Object value = bundle.get(key);
				field.setAccessible(true);
				field.set(target, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	},
	VIEW_BY_ID {

		@Override
		protected int type() {
			return TYPE_FIELD;
		}

		@Override
		protected Class<? extends Annotation> annotation() {
			return ViewById.class;
		}
		
		@Override
		protected void execute(Injection binder, Object target, Object entity, Object annotation) {
			try {
				Field field = (Field) entity;
				int viewId = ((ViewById) annotation).value();
				if(viewId == View.NO_ID) {
					viewId = getIdentifier(binder.getContext(), field.getName(), "id");
					if(viewId <= 0) {
						viewId = getIdentifier(binder.getContext(), resetSrcName(field.getName()), "id");
					}
				}
				
				View view = binder.find(viewId);
				field.setAccessible(true);
				field.set(target, view);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	},
	VIEWS_BY_ID {
		
		@Override
		protected int type() {
			return TYPE_FIELD;
		}

		@Override
		protected Class<? extends Annotation> annotation() {
			return ViewsById.class;
		}
		
		@Override
		protected void execute(Injection binder, Object target, Object entity, Object annotation) {
			try {
				List<View> list = new ArrayList<View>();
				Field field = (Field) entity;
				int ids[] = ((ViewsById) annotation).value();
				for(int i=0,length=ids.length;i<length;i++) {
					if(ids[i] == View.NO_ID)
						continue;
					list.add(binder.find(ids[i]));
				}
				
				field.setAccessible(true);
				field.set(target, list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	},
	CLICK {
		
		@Override
		protected int type() {
			return TYPE_METHOD;
		}

		@Override
		protected Class<? extends Annotation> annotation() {
			return Click.class;
		}
		
		@Override
		protected void execute(Injection binder, Object target, Object entity, Object annotation) {
			Method method = (Method) entity;
			int ids[] = ((Click) annotation).value();
			for(int i=0,length=ids.length;i<length;i++) {
				if(ids[i] == View.NO_ID)
					continue;
				View view = binder.find(ids[i]);
				view.setOnClickListener(new InjectListener(target, method));
			}
		}
	},
	LONG_CLICK {
		
		@Override
		protected int type() {
			return TYPE_METHOD;
		}

		@Override
		protected Class<? extends Annotation> annotation() {
			return LongClick.class;
		}
		
		@Override
		protected void execute(Injection binder, Object target, Object entity, Object annotation) {
			Method method = (Method) entity;
			int ids[] = ((LongClick) annotation).value();
			for(int i=0,length=ids.length;i<length;i++) {
				if(ids[i] == View.NO_ID)
					continue;
				View view = binder.find(ids[i]);
				view.setOnLongClickListener(new InjectListener(target, method));
			}
		}
	},
	ITEM_CLICK {
		
		@Override
		protected int type() {
			return TYPE_METHOD;
		}

		@Override
		protected Class<? extends Annotation> annotation() {
			return ItemClick.class;
		}
		
		@Override
		protected void execute(Injection binder, Object target, Object entity, Object annotation) {
			Method method = (Method) entity;
			int viewId = ((ItemClick) annotation).value();
			View view = binder.find(viewId);
			if(view instanceof AdapterView<?>) {
				((AdapterView<?>)view).setOnItemClickListener(new InjectListener(target, method));
			}
		}
	},
	ITEM_LONG_CLICK {
		
		@Override
		protected int type() {
			return TYPE_METHOD;
		}

		@Override
		protected Class<? extends Annotation> annotation() {
			return ItemLongClick.class;
		}
		
		@Override
		protected void execute(Injection binder, Object target, Object entity, Object annotation) {
			Method method = (Method) entity;
			int viewId = ((ItemLongClick) annotation).value();
			View view = binder.find(viewId);
			if(view instanceof AdapterView<?>) {
				((AdapterView<?>)view).setOnItemLongClickListener(new InjectListener(target, method));
			}
		}
	};
	
	public static final int TYPE_FIELD = 1;
	
	public static final int TYPE_METHOD = 2;
	
	protected abstract int type();
	
	protected abstract Class<? extends Annotation> annotation();
	
	protected abstract void execute(Injection binder, Object target, Object entity, Object annotation);
	
	protected int getIdentifier(Context context, String name, String defType) {
		return context.getResources().getIdentifier(name, defType, context.getPackageName());
	}
	
	/**
	 * @param name
	 * @return "textView" reset "text_view"
	 */
	protected String resetSrcName(String name) {
		StringBuilder ss = new StringBuilder();
		char achar[] = name.toCharArray();
		for(int i=0,length=achar.length;i<length;i++) {
			char c = achar[i];
			int index = "ABCDEFGHIJKLMNOPQRSTUVWSYZ".indexOf(c);
			if(index != -1) {
				ss.append("_");
				ss.append("abcdefghijklmnopqrstuvwsyz".substring(index, index+1));
			}else {
				ss.append(c);
			}
		}
		return ss.toString();
	}
}

package com.gzqining.inthegarden.app.injection;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.app.Activity;

enum InjectFinder {

	VIEW {
		@Override
		protected View findView(Object target, Object source, int id) {
			return ((View) source).findViewById(id);
		}

		@Override
		public Context getContext(Object target, Object source) {
			return ((View) source).getContext();
		}

		@Override
		Bundle getExtras(Object target, Object source) {
			return null;
		}
	},
	ACTIVITY {
		@Override
		protected View findView(Object target, Object source, int id) {
			return ((Activity) source).findViewById(id);
		}

		@Override
		public Context getContext(Object target, Object source) {
			return (Activity) source;
		}

		@Override
		Bundle getExtras(Object target, Object source) {
			return ((Activity) source).getIntent().getExtras();
		}
	},
	FRAGMENT {
		@Override
		protected View findView(Object target, Object source, int id) {
			return ((View) source).findViewById(id);
		}

		@Override
		public Context getContext(Object target, Object source) {
			return ((Fragment) target).getActivity();
		}

		@Override
		Bundle getExtras(Object target, Object source) {
			return ((Fragment) target).getArguments();
		}
	};

	abstract View findView(Object target, Object source, int id);

	abstract Context getContext(Object target, Object source);
	
	abstract Bundle getExtras(Object target, Object source); 
	
}

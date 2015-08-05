package com.gzqining.inthegarden.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.gzqining.inthegarden.app.injection.Injection;

/**
 * android:fitsSystemWindows="true"
 * android:clipToPadding="false"
 * @author xjm
 */
@EActivity(layout = -1, inject = false)
public class AppActivity extends FragmentActivity {
	
	private Injection injection;
	
	public Injection getInjection() {
		return injection;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getClass().isAnnotationPresent(EActivity.class)) {
			EActivity mEActivity = getClass().getAnnotation(EActivity.class);
			
			if(mEActivity.layout() != -1) {
				setContentView(mEActivity.layout());
				if(mEActivity.inject()) {
					injection = new Injection(this, this);
					injection.execute();
				}
			}
			
		}
		
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void showDialogFragment(boolean cancelable) {
		Bundle args = new Bundle();  
		args.putBoolean(FragmentLoading.ARG_CANCELABLE, cancelable);  
		this.showDialogFragment("dialogfragment8954201239547", args);
	}
	
	private void showDialogFragment(String tag, Bundle args) {
		tag += hashCode(); 
		Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
		if(prev != null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.remove(prev);
			ft.commitAllowingStateLoss();
		}
		
		FragmentLoading dialog = new FragmentLoading();
		dialog.setArguments(args);
		dialog.show(getSupportFragmentManager().beginTransaction(), tag);
	}
	
	public void removeDialogFragment() {
		this.removeDialogFragment("dialogfragment8954201239547");
	}
	
	private void removeDialogFragment(String tag) {
		tag += hashCode();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
		if(prev != null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.remove(prev);
			ft.commitAllowingStateLoss();
		}
	}
	
}

package com.gzqining.inthegarden.app;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gzqining.inthegarden.app.injection.Injection;

@EActivity(layout = -1, inject = false)
@EShell(UIShellActivity.class)
public class ShellActivity {
	
	/** Standard activity result: operation canceled. */
	public static final int RESULT_CANCELED = UIShellActivity.RESULT_CANCELED;
	/** Standard activity result: operation succeeded. */
	public static final int RESULT_OK = UIShellActivity.RESULT_OK;
	/** Start of user-defined activity results. */
	public static final int RESULT_FIRST_USER = UIShellActivity.RESULT_FIRST_USER;

	private UIShellActivity activity;
	
	private Injection injection;
	
	public Injection getInjection() {
		return injection;
	}

	final void setActivity(UIShellActivity activity) {
		this.activity = activity;
	}
	
	public UIShellActivity getActivity() {
		return activity;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void onCreate(Bundle savedInstanceState) {
		
		if(getClass().isAnnotationPresent(EActivity.class)) {
			
			EActivity mEActivity = getClass().getAnnotation(EActivity.class);
			
			if(mEActivity.layout() != -1) {
				setContentView(mEActivity.layout());
				if(mEActivity.inject()) {
					injection = new Injection(this, activity);
					injection.execute();
				}
			}
			
		}
		
	}
	
	public void onNewIntent(Intent intent) {
		
	}

	public void onStart() {
		
	}

	public void onResume() {
		
	}
	
	public void onPause() {
		
	}

	public void onStop() {

	}

	public void onRestart() {

	}

	public void onDestroy() {

	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		
	}
	
	public void onSaveInstanceState(Bundle outState) {
		
	}

	
	public boolean onBackPressed() {
		return false;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean isFinishing() {
		return activity == null || activity.isFinishing();
	}
	
	public void finish() {
		activity.finish();
	}
	
	public void setResult(int resultCode) {
		activity.setResult(resultCode);
	}
	
	public void setResult(int resultCode, Intent data) {
		activity.setResult(resultCode, data);
	}
	
	public void startActivity(Intent intent) {
		startActivityForResult(intent, -1);
	}

	public void startActivityForResult(Intent intent, int requestCode) {
		activity.startActivityForResult(intent, requestCode);
	}
	
	public final void setContentView(int layoutResID) {
		this.activity.setContentView(layoutResID);
	}
	
	public final void setContentView(View view) {
		activity.setContentView(view);
	}
	
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		activity.setContentView(view, params);
    }

	public View findViewById(int id) {
		return activity.findViewById(id);
	}
	
	public Intent getIntent() {
		return activity.getIntent();
	}
	
	public Resources getResources() {
		return activity.getResources();
	}
	
	public void showDialogFragment(boolean cancelable) {
		activity.showDialogFragment(cancelable);
	}
	
	public void removeDialogFragment() {
		activity.removeDialogFragment();
	}
	
	
}

package com.gzqining.inthegarden.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

@EActivity(layout = -1, inject = false)
public class UIShellActivity extends AppActivity {

	private ShellActivity shell;

	public static final String EXTRA_SHELL_CLASSNAME = "extra.shell.classname";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String className = intent.getStringExtra(EXTRA_SHELL_CLASSNAME);
		if (TextUtils.isEmpty(className)) {
			throw new ClassCastException("className is null");
		}
		try {
			Class<?> cls = Class.forName(className);
			shell = (ShellActivity) cls.newInstance();
			shell.setActivity(this);
			shell.onCreate(savedInstanceState);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		shell.onNewIntent(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();
		shell.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		shell.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		shell.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		shell.onStop();
	}

	protected void onRestart() {
		super.onRestart();
		shell.onRestart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		shell.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (shell.onCreateOptionsMenu(menu))
			return true;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(shell.onOptionsItemSelected(item))
			return true;
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (shell.onKeyDown(keyCode, event))
			return true;
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		shell.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		shell.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		shell.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		if (shell.onBackPressed())
			return;
		super.onBackPressed();
	}

}

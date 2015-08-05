package com.gzqining.inthegarden.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import static com.gzqining.inthegarden.app.UIShellActivity.EXTRA_SHELL_CLASSNAME;

public class ShellUtil {

	public static void execute(Context target, Class<? extends ShellActivity> fraCls) {
		execute(target, fraCls, null, null);
	}
	
	public static void execute(Context target, Class<? extends ShellActivity> fraCls, Intent intent) {
		execute(target, fraCls, intent, null);
	}
	
	public static void execute(Context target, Class<? extends ShellActivity> fraCls, Integer requestCode) {
		execute(target, fraCls, null, requestCode);
	}
	
	public static void execute(Context target, Class<? extends ShellActivity> fraCls, Intent intent, Integer requestCode) {
		if(intent == null)
			intent = new Intent();
		
		EShell mEShell = fraCls.getAnnotation(EShell.class);
		
		if(target instanceof Activity) {
			intent.setClass((Activity)target, mEShell.value());
			intent.putExtra(EXTRA_SHELL_CLASSNAME, fraCls.getName());
			((Activity) target).startActivityForResult(intent, requestCode == null ? -1 : requestCode);
		} else if(target instanceof Context){
			intent.setClass((Context)target, mEShell.value());
			intent.putExtra(EXTRA_SHELL_CLASSNAME, fraCls.getName());
			((Activity) target).startActivity(intent);
		} else {
			throw new ClassCastException();
		}
	}
	
	public static void execute(Fragment target, Class<? extends ShellActivity> fraCls) {
		execute(target, fraCls, null, null);
	}
	
	public static void execute(Fragment target, Class<? extends ShellActivity> fraCls, Intent intent) {
		execute(target, fraCls, intent, null);
	}
	
	public static void execute(Fragment target, Class<? extends ShellActivity> fraCls, Integer requestCode) {
		execute(target, fraCls, null, requestCode);
	}
	
	public static void execute(Fragment target, Class<? extends ShellActivity> fraCls, Intent intent, Integer requestCode) {
		if(intent == null)
			intent = new Intent();
		
		EShell mEShell = fraCls.getAnnotation(EShell.class);
		
		intent.setClass(((Fragment)target).getActivity(), mEShell.value());
		intent.putExtra(EXTRA_SHELL_CLASSNAME, fraCls.getName());
		((Fragment) target).startActivityForResult(intent, requestCode == null ? -1 : requestCode);
	}
	
}

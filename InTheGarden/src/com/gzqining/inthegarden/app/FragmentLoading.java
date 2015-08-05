package com.gzqining.inthegarden.app;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.gzqining.inthegarden.R;

public class FragmentLoading extends DialogFragment{
	
	public static final String ARG_CANCELABLE = "argCancelable";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(getArguments() != null && getArguments().getBoolean(ARG_CANCELABLE, false));
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.fragment_loading);
		dialog.setContentView(R.layout.app_fragment_loading);
		Window wm = dialog.getWindow();
		WindowManager.LayoutParams p = wm.getAttributes();
		p.gravity = Gravity.CENTER;
		return dialog;
	}
	
}

package com.gzqining.inthegarden.app.ui;

import net.tsz.afinal.FinalBitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.gzqining.inthegarden.R;
import com.gzqining.inthegarden.app.EActivity;
import com.gzqining.inthegarden.app.ShellActivity;
import com.gzqining.inthegarden.app.injection.Extra;
import com.gzqining.inthegarden.app.injection.ViewById;

@EActivity(layout = R.layout.ui_gallery, inject = true)
public class ShellGalleryActivity extends ShellActivity{
	
	@Extra
	String imageUrl;
	
	@ViewById
	ImageView image;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FinalBitmap.create(getActivity()).display(image, imageUrl);
		
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
}

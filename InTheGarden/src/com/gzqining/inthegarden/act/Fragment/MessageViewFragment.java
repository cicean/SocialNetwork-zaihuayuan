package com.gzqining.inthegarden.act.Fragment;

import com.gzqining.inthegarden.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessageViewFragment extends Fragment {

	public View v;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.fragment_message_view, container, false);

		return v;
	}
 
}


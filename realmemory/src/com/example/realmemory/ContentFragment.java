package com.example.realmemory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ContentFragment extends Fragment {
	
	private String mContentType;
	
	public ContentFragment() { 
		this("list");
	}
	
	public ContentFragment(String contentType) {
		mContentType = contentType;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null)
			mContentType = savedInstanceState.getString("mContentType");
		
		Activity me = getActivity();
		Log.e("Activity", me.toString());
		Log.e("mContentType", mContentType);
		// construct the RelativeLayout
		LinearLayout v = new LinearLayout(me);
		if(mContentType.equals("list")){
			LinearLayout.LayoutParams editLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			editLayoutParams.topMargin = 100;
			EditText test1 = new EditText(me);
			test1.setLayoutParams(editLayoutParams);
			test1.setText("갤러리리스트형");
			v.addView(test1);
			
		}
		else if(mContentType.equals("content")){
			EditText test1 = new EditText(me);
			test1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			test1.setText("갤러리 자세히 보기");
			v.addView(test1);
		}
		
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("mContentType", mContentType);
	}
	
	
}

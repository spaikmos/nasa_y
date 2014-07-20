package com.nasax.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nasax.activities.R;

public class MeetingPicsFragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_meeting_pics, container, false);
		return v;
	}

	public static MeetingPicsFragment newInstance(String eventUserId) {
		MeetingPicsFragment f = new MeetingPicsFragment();
		Bundle args = new Bundle();
		args.putString("eventUserId", eventUserId);
		f.setArguments(args);
		return f;
	}

}

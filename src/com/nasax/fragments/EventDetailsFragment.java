package com.nasax.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nasax.activities.R;
import com.nasax.models.Event;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class EventDetailsFragment extends Fragment {
	// TODO:  This is a hack.  We need to pass in the actual eventId into the fragment
	private String eventId;
	private Event event;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		eventId = getArguments().getString("eventId");
		// Get the event data from local datastore
		ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
		query.fromLocalDatastore();
		try {
			event = (Event)query.get(eventId);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(event.getEventName());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_event_details, container,
				false);
		
		// Get view references
		ImageView ivEventImage = (ImageView) v.findViewById(R.id.ivEventMap);
		TextView tvEventDescription = (TextView) v.findViewById(R.id.tvEventDescription);
		TextView tvEventLocation = (TextView) v.findViewById(R.id.tvEventAddress);
		
		// Clear out image if it's recycled
		ivEventImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		// Populate views with data
		imageLoader.displayImage(event.getImageUrl(), ivEventImage);
		tvEventDescription.setText(event.getDescription());
		tvEventLocation.setText(event.getLocation());

		return v;
	}

}

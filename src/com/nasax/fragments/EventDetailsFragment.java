package com.nasax.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.nasax.activities.R;
import com.nasax.models.Event;
import com.nasax.models.EventUser;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class EventDetailsFragment extends Fragment implements OnItemSelectedListener {
	private EventUser eventUser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String eventUserId = getArguments().getString("eventUserId");

		// Get the event data from local datastore
		ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUser");
		query.include("event");
		query.fromLocalDatastore();
		try {
			eventUser = (EventUser) query.get(eventUserId);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(((Event) eventUser.getEvent()).getEventName());
		// actionBar.setIcon(((Drawable)event.getParseFile("imageFile")));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_event_details, container, false);

		// Get view references
		ParseImageView ivEventImage = (ParseImageView) v.findViewById(R.id.ivEventMap);
		TextView tvEventDescription = (TextView) v.findViewById(R.id.tvEventDescription);
		TextView tvEventLocation = (TextView) v.findViewById(R.id.tvEventAddress);
		Event event = (Event) eventUser.getEvent();

		// Clear out image if it's recycled
		ivEventImage.setImageResource(android.R.color.transparent);

		// Populate views with data
		// Load the image
		// The placeholder will be used before and during the fetch, to be
		// replaced by the fetched image data.
		// imageView.setPlaceholder(getResources().getDrawable(R.ic_launcher));
		ivEventImage.setParseFile(event.getImageFile());
		ivEventImage.loadInBackground(new GetDataCallback() {
			@Override
			public void done(byte[] data, ParseException e) {
				if (e != null) {
					e.printStackTrace();
				}
			}
		});
		tvEventDescription.setText(event.getDescription());
		tvEventLocation.setText(event.getLocation());

		// Configure the spinner
		Spinner spinner = (Spinner) v.findViewById(R.id.spAttending);
		spinner.setOnItemSelectedListener(OnCatSpinnerCL);

		int pos = eventUser.getIsGoing();
		if ((pos >= 0) && (pos <= 2)) {
			spinner.setSelection(pos);
		}

		// Configure the AtEvent switch
		Switch swAtEvent = (Switch) v.findViewById(R.id.swAtEvent);
		swAtEvent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean atEvent = ((Switch) v).isChecked();
				eventUser.setAtEvent(atEvent);
			}
		});
		swAtEvent.setChecked(eventUser.getAtEvent());
		return v;
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		// An item was selected.
		eventUser.setIsGoing(pos);
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

	public void onSwitchClicked(View view) {
		boolean atEvent = ((Switch) view).isChecked();
		eventUser.setAtEvent(atEvent);
	}

	public static EventDetailsFragment newInstance(String eventUserId) {
		EventDetailsFragment f = new EventDetailsFragment();
		Bundle args = new Bundle();
		args.putString("eventUserId", eventUserId);
		f.setArguments(args);
		return f;
	}

	private OnItemSelectedListener OnCatSpinnerCL = new AdapterView.OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			// TODO:  I'd like to figure out why I'm getting NPE here.  This works around
			//	the NPE, but doesn't address the root cause.
			if (parent != null) {
				TextView childView = (TextView) parent.getChildAt(0);
				if (childView != null) {
					childView.setTextColor(getResources().getColor(R.color.text));
				}
			}
		}

		public void onNothingSelected(AdapterView<?> parent) {

		}
	};
}

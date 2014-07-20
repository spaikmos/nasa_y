package com.nasax.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nasax.activities.R;
import com.nasax.adapters.EventArrayAdapter;
import com.nasax.listeners.EndlessScrollListener;
import com.nasax.models.Event;
import com.nasax.models.EventUser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class MeetingListFragment extends Fragment {
	private ArrayList<Event> events;
	private EventArrayAdapter aEvents;
	private PullToRefreshListView lvEvents;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		events = new ArrayList<Event>();
		aEvents = new EventArrayAdapter(getActivity(), events);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_meetings, container, false);
		
		// Assign our view references
		lvEvents = (PullToRefreshListView) v.findViewById(R.id.lvEvents);
		// Attach the listener to the AdapterView onCreate
		lvEvents.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				/*
				if (totalItemsCount > 1) {
					long maxId = tweets.get(totalItemsCount - 2).getUid() - 1;
					populateTimeline(maxId, 0);
				}
				*/
			}
		});

		lvEvents.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				populateList();
				lvEvents.onRefreshComplete();
			}
		});
		lvEvents.setAdapter(aEvents);

		populateList();
		
		return v;
	}
	
	private void populateList() {
		// TODO:  This is a hack to prevent events from showing up again.  Need to fix this
		//	by fixing populateList() to not populate duplicate events?
		aEvents.clear();
		// Need to pull all elements from EventUser that contain the current userId
		ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUser");
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.include("event");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> eventList, ParseException e) {
		        if (e == null) {
		        	// Query was successful.
		        	// Generate array of eventIds from EventUser objects
	        		ArrayList<ParseObject> events = new ArrayList<ParseObject>(eventList.size());
	        		for (int i=0; i<eventList.size(); i++) {
	        			EventUser eventUser = (EventUser)eventList.get(i);
	        			
	        			eventUser.pinInBackground(null);
	        			events.add(eventUser.getEvent());
	        		}		
	        		
	        		// TODO:  Need to sort the events array by starttime.
	        		aEvents.addAll(Event.fromParseObjectsList(events));
		        } else {
		            Log.d("debug", "populateList:  Parse Query failed!");
		        }
		    }
		});		
	}
}

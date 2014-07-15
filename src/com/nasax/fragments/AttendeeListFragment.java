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
import com.nasax.adapters.EventUserArrayAdapter;
import com.nasax.listeners.EndlessScrollListener;
import com.nasax.models.Event;
import com.nasax.models.EventUser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class AttendeeListFragment extends Fragment {
	private ArrayList<EventUser> eventUsers;
	private EventUserArrayAdapter aEventUsers;
	private PullToRefreshListView lvAttendees;
	private String eventId;
	private Event event;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		eventId = getArguments().getString("eventId");
		eventUsers = new ArrayList<EventUser>();
		aEventUsers = new EventUserArrayAdapter(getActivity(), eventUsers);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_attendees_list, container, false);
		
		// Assign our view references
		lvAttendees = (PullToRefreshListView) v.findViewById(R.id.lvAttendees);
		// Attach the listener to the AdapterView onCreate
		lvAttendees.setOnScrollListener(new EndlessScrollListener() {

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

		lvAttendees.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				//fetchTimelineAsync();
				//lvTweets.onRefreshComplete();
			}
		});
		lvAttendees.setAdapter(aEventUsers);

		// TODO:  This is a hack to prevent items from showing up again.  Need to fix this
		//	by fixing populateList() to not populate duplicate items?
		aEventUsers.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
		query.fromLocalDatastore();
		try {
			event = (Event)query.get(eventId);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		populateList();
		
		return v;
	}
	
	private void populateList() {
		// Need to pull all elements from EventUser that contain the current userId
		ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUser");
		query.whereEqualTo("event", event);
		query.include("user");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> eventUserList, ParseException e) {
		        if (e == null) {
		        	// Query was successful.
		        	// TODO:  Is there a more efficient way to add the eventUsers list to aEventsUsers?
		        	//	I don't think I should need a for() loop to do this.
	        		for (int i=0; i<eventUserList.size(); i++) {
	        			EventUser eU = (EventUser)eventUserList.get(i);	
	        			eU.pinInBackground(null);
	        			aEventUsers.add(eU);
	        		}		
		        } else {
		            Log.d("debug", "populateList:  Parse Query failed!");
		        }
		    }
		});		
	}
}

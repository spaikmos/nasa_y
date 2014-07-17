package com.nasax.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nasax.fragments.AttendeeListFragment;
import com.nasax.fragments.EventDetailsFragment;
import com.nasax.fragments.MeetingPicsFragment;
import com.nasax.listeners.FragmentTabListener;
import com.nasax.models.Event;
import com.nasax.models.EventUser;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class EventActivity extends FragmentActivity {
	private String eventId;
	private String eventUserId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Event event = null;
		
		super.onCreate(savedInstanceState);
		eventId = getIntent().getStringExtra("eventId");
		
		// Get the event data from local datastore
		ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
		query.fromLocalDatastore();
		try {
			event = (Event)query.get(eventId);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// Get the EventUser data from local datastore
		query = ParseQuery.getQuery("EventUser");
		query.fromLocalDatastore();
		query.whereEqualTo("event",  event);
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		try {
			EventUser eventUser = (EventUser)query.getFirst();
			eventUserId = eventUser.getObjectId();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		setContentView(R.layout.activity_event);
		setupTabs();
		// TODO:  Set the title bar of this activity to use the eventImage and eventName fields
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_action_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.miProfile:
			Intent i = new Intent(this, EventProfileActivity.class);
			i.putExtra("eventUserId", eventUserId);
			startActivity(i);
			return true;
		case R.id.miLogout:
			ParseUser.logOut();
			Intent i2 = new Intent(this, LoginActivity.class);
			startActivity(i2);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Bundle args = new Bundle();
		args.putString("eventId", eventId);
		
		Tab tab1 = actionBar
			.newTab()
			.setText("Details")
			//.setIcon(R.drawable.ic_launcher)
			.setTag("EventDetailsFragment")
			.setTabListener(
				new FragmentTabListener<EventDetailsFragment>(R.id.flEventsContainer, this, "details", args, EventDetailsFragment.class));
		
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Attendees")
			//.setIcon(R.drawable.ic_launcher)
			.setTag("AttendeeListFragment")
			.setTabListener(
			    new FragmentTabListener<AttendeeListFragment>(R.id.flEventsContainer, this, "attendees", args, AttendeeListFragment.class));
		
		actionBar.addTab(tab2);
		
		Tab tab3 = actionBar
			.newTab()
			.setText("Pics")
			//.setIcon(R.drawable.ic_launcher)
			.setTag("MeetingPicsFragment")
			.setTabListener(
			    new FragmentTabListener<MeetingPicsFragment>(R.id.flEventsContainer, this, "pics", args, MeetingPicsFragment.class));
			
			actionBar.addTab(tab3);
	}
}

package com.nasax.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.nasax.fragments.AttendeeListFragment;
import com.nasax.fragments.EventDetailsFragment;
import com.nasax.fragments.MeetingPicsFragment;
import com.nasax.listeners.FragmentTabListener;

public class EventActivity extends FragmentActivity {
	private String eventId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		eventId = getIntent().getStringExtra("eventId");
		setContentView(R.layout.activity_event);
		setupTabs();
		// TODO:  Set the title bar of this activity to use the eventImage and eventName fields
	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Bundle args = new Bundle();
		args.putString("eventId", eventId);
		// TODO:  Need to get userId from Parse client
		args.putString("userId", "SPaik");
		
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

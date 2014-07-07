package com.nasax.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.nasax.fragments.AttendeeListFragment;
import com.nasax.fragments.EventDetailsFragment;
import com.nasax.fragments.MeetingPicsFragment;
import com.nasax.listeners.FragmentTabListener;

public class EventActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		setupTabs();
		String id = getIntent().getStringExtra("eventId");
		Toast.makeText(this, "objectId = " + id, Toast.LENGTH_LONG).show();

	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Details")
			//.setIcon(R.drawable.ic_launcher)
			.setTag("EventDetailsFragment")
			.setTabListener(
				new FragmentTabListener<EventDetailsFragment>(R.id.flEventsContainer, this, "details", EventDetailsFragment.class));
		
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Attendees")
			//.setIcon(R.drawable.ic_launcher)
			.setTag("AttendeeListFragment")
			.setTabListener(
			    new FragmentTabListener<AttendeeListFragment>(R.id.flEventsContainer, this, "attendees", AttendeeListFragment.class));
		
		actionBar.addTab(tab2);
		
		Tab tab3 = actionBar
			.newTab()
			.setText("Pics")
			//.setIcon(R.drawable.ic_launcher)
			.setTag("MeetingPicsFragment")
			.setTabListener(
			    new FragmentTabListener<MeetingPicsFragment>(R.id.flEventsContainer, this, "pics", MeetingPicsFragment.class));
			
			actionBar.addTab(tab3);
	}
}

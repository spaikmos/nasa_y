package com.nasax.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.nasax.fragments.MeetingListFragment;
import com.nasax.fragments.PastMeetingsListFragment;
import com.nasax.fragments.ProfileFragment;
import com.nasax.listeners.FragmentTabListener;
import com.parse.ParseAnalytics;

public class MainActivity extends FragmentActivity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ParseAnalytics.trackAppOpened(getIntent());
		setupTabs();
	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Profile")
			//.setIcon(R.drawable.ic_launcher)
			.setTag("ProfileFragment")
			.setTabListener(
				new FragmentTabListener<ProfileFragment>(R.id.flContainer, this, "profile", ProfileFragment.class));
		
		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Meetings")
			//.setIcon(R.drawable.ic_launcher)
			.setTag("MeetingsFragment")
			.setTabListener(
			    new FragmentTabListener<MeetingListFragment>(R.id.flContainer, this, "meetings", MeetingListFragment.class));
		
		actionBar.addTab(tab2);
		
		Tab tab3 = actionBar
			.newTab()
			.setText("Past Meetings")
			//.setIcon(R.drawable.ic_launcher)
			.setTag("PastMeetingsFragment")
			.setTabListener(
			    new FragmentTabListener<PastMeetingsListFragment>(R.id.flContainer, this, "past meetings", PastMeetingsListFragment.class));
			
			actionBar.addTab(tab3);
	}

}

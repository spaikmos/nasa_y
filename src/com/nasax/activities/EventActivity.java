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
		setContentView(R.layout.activity_event);
		setupTabs();
		eventId = getIntent().getStringExtra("eventId");
		// TODO:  Set the title bar of this activity to use the eventImage and eventName fields
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
				new FragmentTabListener<EventDetailsFragment>(R.id.flEventsContainer, this, "details", EventDetailsFragment.class) {
					
					// TODO:  I have no idea how to pass the eventId into the fragment.  This isn't working...
/*					
			        private Fragment mFragment;
			    	private FragmentActivity mActivity;
			    	private String mTag;
			    	private Class<EventDetailsFragment> mClass;
			    	private int mfragmentContainerId;

			    	// This version supports specifying the container to replace with fragment content
			        // new FragmentTabListener<SomeFragment>(R.id.flContent, this, "first", SomeFragment.class))
			    	public void FragmentTabListener(int fragmentContainerId, FragmentActivity activity, 
			                String tag, Class<EventDetailsFragment> clz) {
			    		mActivity = activity;
			    		mTag = tag;
			    		mClass = clz;
			    		mfragmentContainerId = fragmentContainerId;
			    	}
			     
			    	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
			    		android.support.v4.app.FragmentTransaction sft = mActivity.getSupportFragmentManager().beginTransaction();
			    		// Check if the fragment is already initialized
			    		if (mFragment == null) {
			    			// If not, instantiate and add it to the activity
			    			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			    			sft.add(mfragmentContainerId, mFragment, mTag);
			    		} else {
			    			// If it exists, simply attach it in order to show it
			    			sft.attach(mFragment);
			    		}
			    		sft.commit();
			    	}
*/			    	
				});
		
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

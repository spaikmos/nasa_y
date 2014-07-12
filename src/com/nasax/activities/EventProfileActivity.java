package com.nasax.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.nasax.fragments.ProfileAclFragment;
import com.nasax.models.Event;
import com.nasax.models.EventUser;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class EventProfileActivity extends FragmentActivity {
	private EventUser eventUser;
	private ParseUser me;
	private Event event;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_profile);
		String eventUserId = getIntent().getStringExtra("eventUserId");
		me = ParseUser.getCurrentUser();

		Log.d("debug", "eventUserId = " + eventUserId);
		Log.d("debug", "screenname = " + me.getUsername());
		
		// Get the eventUser data from local datastore
		ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUser");
		query.fromLocalDatastore();
		try {
			eventUser = (EventUser)query.get(eventUserId);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Log.d("debug", "eventUser = " + eventUser.toString());

		// Get the event data from local datastore
		query = ParseQuery.getQuery("event");
		query.fromLocalDatastore();
		try {
			event = (Event)query.get(eventUser.getEventId());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Modify the action bar title
		ActionBar actionBar = this.getActionBar();
		actionBar.setTitle("Profile Access");
		
		// Populate textviews with user data
		TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
		tvUsername.setText(me.getUsername());
		
		// Setup fragments
		ProfileAclFragment proAclFrag = (ProfileAclFragment) getSupportFragmentManager().findFragmentById(R.id.fName);
		//proAclFrag.setupView(eventUser.getObjectId(), "showName", "name", "Name");
		/*
		tvName = (TextView) findViewById(R.id.tvName);
		tvName.setText(me.getString("name"));
		tvEmail = (TextView) findViewById(R.id.tvEmail);
		tvEmail.setText(me.getEmail());
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		tvAddress.setText(me.getString("address"));
		tvSchoolName = (TextView) findViewById(R.id.tvSchoolName);
		tvSchoolName.setText(me.getString("schoolName"));
		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyName.setText(me.getString("companyName"));
		tvOccupation = (TextView) findViewById(R.id.tvOccupation);
		tvOccupation.setText(me.getString("occupation"));
		tvAbout = (TextView) findViewById(R.id.tvAbout);
		tvAbout.setText(me.getString("about"));
		// Populate the switches
		Boolean val = eventUser.getShowAbout();
*/
	}
	
	
}

package com.nasax.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.nasax.models.EventUser;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class EventProfileActivity extends Activity {
	private EventUser eventUser;
	private ParseUser me;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String eventUserId = getIntent().getStringExtra("eventUserId");
		me = ParseUser.getCurrentUser();

		// Get the eventUser data from local datastore
		ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUser");
		query.fromLocalDatastore();
		try {
			eventUser = (EventUser)query.get(eventUserId);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		setContentView(R.layout.activity_event_profile);
	}
}

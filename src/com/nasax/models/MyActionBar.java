package com.nasax.models;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.nasax.activities.EditProfileActivity;
import com.nasax.activities.EventListActivity;
import com.nasax.activities.EventProfileActivity;
import com.nasax.activities.LoginActivity;
import com.nasax.activities.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import eu.livotov.zxscan.ZXScanHelper;

// Simple helper class to allow reusing the same action bar elements across multiple activities.
public class MyActionBar {
	static final int ZXSCAN_CODE = 12345;

	static public boolean optionSelected(Context context, MenuItem item, String eventUserId) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.miEventList:
			Intent i1 = new Intent(context, EventListActivity.class);
			context.startActivity(i1);
			return true;
		case R.id.miAddEvent:
			ZXScanHelper.setCustomScanLayout(R.layout.qr_scanner);
			ZXScanHelper.scan((Activity)context, ZXSCAN_CODE);
			return true;
		case R.id.miProfile:
			Intent i2 = new Intent(context, EditProfileActivity.class);
			context.startActivity(i2);
			return true;
		case R.id.miEventProfile:
			Intent i3 = new Intent(context, EventProfileActivity.class);
			i3.putExtra("eventUserId", eventUserId);
			context.startActivity(i3);
			return true;
		case R.id.miLogout:
			ParseUser.logOut();
			Intent i4 = new Intent(context, LoginActivity.class);
			context.startActivity(i4);
			return true;
		default:
			return false;
		}
	}

	static public void onActivityResult(final Context context, int requestCode, int resultCode,  Intent data) {
		if (resultCode == android.app.Activity.RESULT_OK && requestCode == ZXSCAN_CODE) {
			final String eventId = ZXScanHelper.getScannedCode(data);
			
			Log.d("debug", "QR code scan = " + eventId);
			// Get the event object
			ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
			query.getInBackground(eventId, new GetCallback<ParseObject>() {
				public void done(ParseObject object, ParseException e) {
			        if (e == null) {
			        	// This is a valid event.  Check if user is already in this event
			        	final Event event = (Event)object;
			        	ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUser");
			     		query.whereEqualTo("event",  event);
			     		query.whereEqualTo("user", ParseUser.getCurrentUser());
			     		query.findInBackground(new FindCallback<ParseObject>() {
							public void done(List<ParseObject> objects,
									ParseException e) {
								if (e == null) {
									if(objects.size() > 0) {
										// User is already bound to this event.
										Toast.makeText(context,  "Already invited to " + event.getEventName(), Toast.LENGTH_LONG).show();
									} else {
										// Create a new entry in the table for this user
										EventUser eventUser = new EventUser();
										eventUser.put("event", event);
										eventUser.put("user",  ParseUser.getCurrentUser());
										eventUser.saveInBackground();
										eventUser.pinInBackground(null);
										Toast.makeText(context,  "Added " + event.getEventName(), Toast.LENGTH_LONG).show();
									}
								} else {
									e.printStackTrace();
								}
							}			     			
			     		});
			        } else {
			            Toast.makeText(context, "Invalid EventId = '" + eventId, Toast.LENGTH_LONG).show();
			        }
			    }
			});
		}
	}
}

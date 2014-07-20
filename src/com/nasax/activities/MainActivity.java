package com.nasax.activities;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nasax.models.Event;
import com.nasax.models.EventUser;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import eu.livotov.zxscan.ZXScanHelper;

public class MainActivity extends FragmentActivity {
	static final int ZXSCAN_CODE = 12345;
	Context context;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ParseAnalytics.trackAppOpened(getIntent());
		context = this;
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
		case R.id.miEventList:
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			return true;
		case R.id.miAddEvent:
			ZXScanHelper.setCustomScanLayout(R.layout.qr_scanner);
			ZXScanHelper.scan(this, ZXSCAN_CODE);
			return true;
		case R.id.miProfile:
			Intent i2 = new Intent(this, EditProfile.class);
			startActivity(i2);
			return true;
		case R.id.miLogout:
			ParseUser.logOut();
			Intent i3 = new Intent(this, LoginActivity.class);
			startActivity(i3);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (resultCode == RESULT_OK && requestCode == ZXSCAN_CODE) {
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

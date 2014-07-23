package com.nasax.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nasax.fragments.ProfileAclFragment;
import com.nasax.models.EventUser;
import com.nasax.models.MyActionBar;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class EventProfileActivity extends FragmentActivity {
	private EventUser eventUser;
	private EditText etNote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_profile);
		String eventUserId = getIntent().getStringExtra("eventUserId");

		// Get the eventUser data from local datastore
		ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUser");
		query.fromLocalDatastore();
		try {
			eventUser = (EventUser)query.get(eventUserId);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// Modify the action bar title
		ActionBar actionBar = this.getActionBar();
		actionBar.setTitle("Profile Access");
		
		// Populate textviews with user data
		TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
		tvUsername.setText(ParseUser.getCurrentUser().getUsername());
		
	   	// Load the image
		ParseImageView ivProfilePic = (ParseImageView) findViewById(R.id.ivProfilePic);
		ivProfilePic.setImageResource(android.R.color.transparent);
		// Populate views with data
		// Load the image
		// The placeholder will be used before and during the fetch, to be
		// replaced by the fetched image data.
		// imageView.setPlaceholder(getResources().getDrawable(R.ic_launcher));
		ivProfilePic.setParseFile(ParseUser.getCurrentUser().getParseFile("picture"));
		ivProfilePic.loadInBackground(new GetDataCallback() {
			@Override
			public void done(byte[] data, ParseException e) {
				if (e != null) {
					e.printStackTrace();
				}
			}
		});
		
		// Setup fragments
		setupFragment(R.id.fName, "showName", "name", "Name");
		setupFragment(R.id.fPhone, "showPhone", "phone", "Phone Number");
		setupFragment(R.id.fEmail, "showEmail", "email", "E-mail");
		setupFragment(R.id.fAddress, "showAddress", "address", "Address");
		setupFragment(R.id.fSchoolName, "showSchoolName", "schoolName", "School Name");
		setupFragment(R.id.fCompanyName, "showCompanyName", "companyName", "Company Name");
		setupFragment(R.id.fOccupation, "showOccupation", "occupation", "Occupation");
		setupFragment(R.id.fAbout, "showAbout", "about", "About Me");
		
		// Setup the Edit Text Note
		etNote = (EditText) findViewById(R.id.etNote);
		etNote.setText(eventUser.getString("note"));
	}
	
	private void setupFragment(Integer rId, String eUCol, String uCol, String swText) {
		ProfileAclFragment proAclFrag = (ProfileAclFragment) getSupportFragmentManager().findFragmentById(rId);
		proAclFrag.setupView(eventUser.getObjectId(), eUCol, uCol, swText);
	}
	
    public void onSaveButtonClick(View v) {
    	eventUser.put("note",  etNote.getText().toString());
    	eventUser.saveInBackground();
    }

    public void onClearButtonClick(View v) {
    	etNote.setText(null);
    	onSaveButtonClick(v);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_action_bar_event_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (MyActionBar.optionSelected(this, item, null) == false) {
			// MyActionBar didn't handle this.  Kick it up to the parent class.
			return super.onOptionsItemSelected(item);
		} else {
			return true;
		}
	}

	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		MyActionBar.onActivityResult(this, requestCode, resultCode, data);
	}

}

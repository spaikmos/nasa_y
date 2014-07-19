package com.nasax.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nasax.models.EventUser;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class AttendeeDetail extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendee_detail);
		EventUser eventUser = null;
		String eventUserId = getIntent().getStringExtra("eventUserId");

		// Get the eventUser data from local datastore
		ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUser");
		query.include("user");
		query.fromLocalDatastore();
		try {
			eventUser = (EventUser)query.get(eventUserId);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ParseUser attendee = eventUser.getParseUser("user");
		ParseImageView ivProfileImage = (ParseImageView) findViewById(R.id.ivProfilePic);
		TextView tvUsername = (TextView) findViewById(R.id.tvAttendeeUsername);

		// Clear out image if it's recycled
		ivProfileImage.setImageResource(android.R.color.transparent);;
		// Load the image
		// The placeholder will be used before and during the fetch, to be
		// replaced by the fetched image data.
		// imageView.setPlaceholder(getResources().getDrawable(R.ic_launcher));
		ivProfileImage.setParseFile(attendee.getParseFile("picture"));
		ivProfileImage.loadInBackground(new GetDataCallback() {
			@Override
			public void done(byte[] data, ParseException e) {
				if (e != null) {
					e.printStackTrace();
				}
			}
		});
		tvUsername.setText(attendee.getUsername());
		
		// Go through each of the ACLs to decide whether to display the item or not
		String[] fieldNames = {"name", "address", "phone", "email", "schoolName", "companyName", "occupation", "about"};
		int[] tvLabelId = {R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5, R.id.tv6, R.id.tv7, R.id.tv8};
		int[] tvFieldId = {R.id.tvName, R.id.tvAddress, R.id.tvPhone, R.id.tvEmail, R.id.tvSchoolName, R.id.tvCompanyName, R.id.tvOccupation, R.id.tvAbout};
		Boolean[] acls = {eventUser.getShowName(), eventUser.getShowAddress(), eventUser.getShowPhone(), eventUser.getShowEmail(), eventUser.getShowSchoolName(),
				eventUser.getShowCompanyName(), eventUser.getShowOccupation(), eventUser.getShowAbout()};
		
		for(int i = 0; i < 8; i++) {
			TextView tvLabel = (TextView) findViewById(tvLabelId[i]);
			TextView tvField = (TextView) findViewById(tvFieldId[i]);
			
			if(acls[i] == true) {
				tvField.setText(attendee.getString(fieldNames[i]));
			} else {
				tvLabel.setVisibility(View.GONE);
				tvField.setVisibility(View.GONE);
			}
		}

		// Check the meeting note separately since that is handled differently
		TextView tvNoteLabel = (TextView) findViewById(R.id.tv9);
		TextView tvNoteField = (TextView) findViewById(R.id.tvNote);
		String note = eventUser.getString("note");
		if(note != null) {
			tvNoteField.setText(note);
		} else {
			tvNoteLabel.setVisibility(View.GONE);
			tvNoteField.setVisibility(View.GONE);
		}
	}
}

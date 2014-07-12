package com.nasax.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.nasax.activities.R;
import com.nasax.models.EventUser;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileAclFragment extends Fragment {
	private String eventUserId;
	private String eventUserCol;
	private String switchText;
	private String userCol;
	private EventUser eventUser;
	private TextView tvItem;
	private Switch swItem;
	private ParseUser me;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profile_acl, container,
				false);

		// Get the Views
		tvItem = (TextView) v.findViewById(R.id.tvProfileItem);
		swItem = (Switch) v.findViewById(R.id.swProfileItem);
		return v;
	}

	public void setupView(String eId, String eUCol, String uCol, String swText) {
		// Get arguments for fragment
		eventUserId = eId;
		eventUserCol = eUCol;
		switchText = swText;
		userCol = uCol;

		me = ParseUser.getCurrentUser();
		String userId = me.getObjectId();

		// Get the EventUser data from local datastore
		ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUser");
		query.fromLocalDatastore();
		try {
			eventUser = (EventUser) query.get(eventUserId);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Setup the textview
		tvItem.setText(me.getString(userCol));

		// Setup the switch
		swItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isChecked = ((Switch) v).isChecked();
				eventUser.put(eventUserCol, isChecked);
				eventUser.saveInBackground();

				if (isChecked == true) {
					tvItem.setVisibility(View.VISIBLE);
				} else {
					tvItem.setVisibility(View.GONE);
				}
			}
		});
		
		// Set current switch value
		Boolean isChecked = eventUser.getBoolean(eventUserCol);
		swItem.setChecked(isChecked);
		if (isChecked == true) {
			tvItem.setVisibility(View.VISIBLE);
		} else {
			tvItem.setVisibility(View.GONE);
		}
		swItem.setText(switchText);
	}
}

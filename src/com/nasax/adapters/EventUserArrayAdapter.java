package com.nasax.adapters;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nasax.activities.R;
import com.nasax.fragments.AttendeeDetailFragment;
import com.nasax.models.EventUser;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseUser;

public class EventUserArrayAdapter extends ArrayAdapter<EventUser> {
	private Context context;
	private FragmentManager fragmentManager;
	// TODO:  Hack for demo of BLE
	private View rssiView;
	private int rssiVal;

	public EventUserArrayAdapter(Context ctext, List<EventUser> eventUsers, FragmentManager fm) {
		super(ctext, 0, eventUsers);
		context = ctext;
		fragmentManager = fm;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for position
		EventUser eventUser = getItem(position);
		ParseUser attendee = eventUser.getUser();

		// Find or inflate the template
		View v;
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			v = inflator.inflate(R.layout.event_user_item, parent, false);
		} else {
			v = convertView;
		}
		// find the views within template
		ParseImageView ivProfileImage = (ParseImageView) v.findViewById(R.id.ivProfileImage);
		TextView tvUsername = (TextView) v.findViewById(R.id.tvUsername);
		// Clear out image if it's recycled
		ivProfileImage.setImageResource(android.R.color.transparent);

		// Populate views with data
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
		drawStatus(v, eventUser.getAtEvent(), eventUser.getIsGoing());
		drawRssi(v, randRssi(), eventUser.getAtEvent(), attendee);

		// Set OnClickListener for the whole line
		v.setTag(eventUser.getObjectId());
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Show profile as a modal overlay
				AttendeeDetailFragment f = AttendeeDetailFragment.newInstance((String)v.getTag());
				f.show(fragmentManager, "activity_attendee_detail");
			}
		});

		return v;
	}

	private void drawStatus(View v, Boolean atEvent, int isGoing) {
		TextView tvStatus = (TextView) v.findViewById(R.id.tvStatus);
		String status;

		if (atEvent == true) {
			status = "I'm already here!";
		} else {
			switch (isGoing) {
			case 0:
				status = "I'll be there!";
				break;
			case 1:
				status = "I might attend...";
				break;
			case 2:
				status = "I won't make it.  Sorry.";
				break;
			default:
				status = "ERROR:  I have no idea wtf I'm doing!!!";
				break;
			}
		}

		tvStatus.setText(status);
	}

	private void drawRssi(View v, int rssi, Boolean atEvent, ParseUser attendee) {
		ImageView ivRssi = (ImageView) v.findViewById(R.id.ivRssi);
		TextView tvRssi = (TextView) v.findViewById(R.id.tvRssi);
		int color;
		int icon;
		
		if (rssi < -63) {
			color = R.color.reddark;
			icon = R.drawable.ic_launcher_wifi0;
		} else if (rssi < -56) {
			color = R.color.yellowdark;
			icon = R.drawable.ic_launcher_wifi2;
		} else {
			color = R.color.greendark;
			icon = R.drawable.ic_launcher_wifi3;
		}

		if (atEvent && (attendee.equals(ParseUser.getCurrentUser()) == false)) {
			ivRssi.setImageDrawable(context.getResources().getDrawable(icon));
			tvRssi.setText(String.valueOf(rssi));
			tvRssi.setTextColor(context.getResources().getColor(color));
			
			// TODO:  Hack for BLE demo.  Cache the last RSSI element drawn
			rssiView = v;
			rssiVal = rssi;
		} else {
			ivRssi.setImageResource(android.R.color.transparent);
			tvRssi.setText(null);
		}
	}

	private static int randRssi() {
		Random rand = new Random();
		int max = -50;
		int min = -70;

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
	
	private void randRssiDelta(int delta) {
		Random rand = new Random();

		// Pick an RSSI value between 1 and 3
		delta *= rand.nextInt(3) + 1;
		int newRssi = rssiVal + delta;
		
		if(newRssi < -70) {
			newRssi = -70;
		} else if(newRssi > -50) {
			newRssi = -50;
		}
		
		rssiVal = newRssi;
	}

	// TODO:  Hack for BLE demo.  Modify the RSSI value accordingly.
	public void updateRssi(int delta) {
		ImageView ivRssi = (ImageView) rssiView.findViewById(R.id.ivRssi);
		TextView tvRssi = (TextView) rssiView.findViewById(R.id.tvRssi);
		int color;
		int icon;
		
		// Generate new RSSI based on volume direction
		randRssiDelta(delta);
		
		if (rssiVal < -63) {
			color = R.color.reddark;
			icon = R.drawable.ic_launcher_wifi0;
		} else if (rssiVal < -56) {
			color = R.color.yellowdark;
			icon = R.drawable.ic_launcher_wifi2;
		} else {
			color = R.color.greendark;
			icon = R.drawable.ic_launcher_wifi3;
		}

		ivRssi.setImageDrawable(context.getResources().getDrawable(icon));
		tvRssi.setText(String.valueOf(rssiVal));
		tvRssi.setTextColor(context.getResources().getColor(color));
	}
}

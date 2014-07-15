package com.nasax.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nasax.activities.AttendeeDetail;
import com.nasax.activities.R;
import com.nasax.models.EventUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseUser;

public class EventUserArrayAdapter extends ArrayAdapter<EventUser> {
	private Context appContext;
	
	public EventUserArrayAdapter(Context context, List<EventUser> eventUsers) {
		super(context, 0, eventUsers);
		appContext = context;
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
		ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
		TextView tvUsername = (TextView) v.findViewById(R.id.tvUsername);
		// Clear out image if it's recycled
		ivProfileImage.setImageResource(android.R.color.transparent);;
		ImageLoader imageLoader = ImageLoader.getInstance();
		// Populate views with data
		imageLoader.displayImage(attendee.getString("imgUrl"), ivProfileImage);
		tvUsername.setText(attendee.getUsername());
		drawStatus(v, eventUser.getAtEvent(), eventUser.getIsGoing());
		
		// Set OnClickListener for the whole line
		v.setTag(eventUser.getObjectId());
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO:  Make the attendee detail a modal overlay instead of a separate activity
				Intent i = new Intent(getContext(), AttendeeDetail.class);
				i.putExtra("eventUserId", (String) v.getTag());
				appContext.startActivity(i);
			}			
		});

		return v;
	}
	
	private void drawStatus(View v, Boolean atEvent, int isGoing) {
		TextView tvStatus = (TextView) v.findViewById(R.id.tvStatus);
		String status;

		if(atEvent == true) {
			status = "I'm already here!";
		} else {
			switch(isGoing) {
			case 0:
				status = "I will attend";
				break;
			case 1:
				status = "I might attend";
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
}

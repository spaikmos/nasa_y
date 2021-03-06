package com.nasax.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nasax.activities.EventActivity;
import com.nasax.activities.R;
import com.nasax.models.Event;
import com.parse.GetDataCallback;
import com.parse.ParseImageView;

public class EventArrayAdapter extends ArrayAdapter<Event> {
	
	public EventArrayAdapter(Context context, List<Event> events) {
		super(context, 0, events);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for position
		Event event = getItem(position);
		// Find or inflate the template
		View v;
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			v = inflator.inflate(R.layout.event_item, parent, false);
		} else {
			v = convertView;
		}
		// find the views within template
		ParseImageView ivEventImage = (ParseImageView) v.findViewById(R.id.ivEventImage);
		TextView tvEventTitle = (TextView) v.findViewById(R.id.tvEventTitle);
		TextView tvEventLocation = (TextView) v.findViewById(R.id.tvEventLocation);
		TextView tvRelativeTimestamp = (TextView) v.findViewById(R.id.tvRelativeTimestamp);
		// Clear out image if it's recycled
		ivEventImage.setImageResource(android.R.color.transparent);;
		// Populate views with data
		// Load the image
		// The placeholder will be used before and during the fetch, to be
		// replaced by the fetched image data.
		// imageView.setPlaceholder(getResources().getDrawable(R.ic_launcher));
		ivEventImage.setParseFile(event.getImageFile());
		ivEventImage.loadInBackground(new GetDataCallback() {
			@Override
			public void done(byte[] data, com.parse.ParseException e) {
				if (e != null) {
					e.printStackTrace();
				}
			}
		});
		
		tvEventTitle.setText(event.getEventName());
		tvEventLocation.setText(event.getLocation());
		// TODO:  Set the timestamp based on EventStart - currentTime
		tvRelativeTimestamp.setText(getRelativeTime(event.getStartTime()));
		// Set OnClickListener for the whole line
		v.setTag(event.getObjectId());
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getContext(), EventActivity.class);
				i.putExtra("eventId", (String) v.getTag());
				v.getContext().startActivity(i);
			}			
		});
		return v;
	}
		
	public String getRelativeTime(Date startTime) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(startTime.toString()).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
}

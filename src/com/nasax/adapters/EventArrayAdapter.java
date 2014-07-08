package com.nasax.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.nasax.activities.EventActivity;
import com.nasax.activities.R;
import com.nasax.models.Event;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EventArrayAdapter extends ArrayAdapter<Event> {
	private Context appContext;
	
	public EventArrayAdapter(Context context, List<Event> events) {
		super(context, 0, events);
		appContext = context;
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
		ImageView ivEventImage = (ImageView) v.findViewById(R.id.ivEventImage);
		TextView tvEventTitle = (TextView) v.findViewById(R.id.tvEventTitle);
		TextView tvEventLocation = (TextView) v.findViewById(R.id.tvEventLocation);
		TextView tvRelativeTimestamp = (TextView) v.findViewById(R.id.tvRelativeTimestamp);
		// Clear out image if it's recycled
		ivEventImage.setImageResource(android.R.color.transparent);;
		ImageLoader imageLoader = ImageLoader.getInstance();
		// Populate views with data
		imageLoader.displayImage(event.getImageUrl(), ivEventImage);
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
				appContext.startActivity(i);
			}			
		});
		return v;
	}
		
	public String getRelativeTime(String startTime) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(startTime).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
}

package com.nasax.models;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("event")
public class Event extends ParseObject {
	// I don't recall all the member variables in event.  We'll have to make sure this matches the model.
	private String id;
	private String eventName;
	private String summary;			// Short description of event
	private String description;		// Long description
	private String location;		// Address
	private String imageUrl;
	private String startTime;
	private String endTime;

	public Event() {
	}
	
	public static Event fromParseObject(ParseObject obj) {
		Event event = new Event();
		// Extract values from obj to populate the member variables
		event.id = obj.getString("objectId");
		event.eventName = obj.getString("eventName");
		event.summary = obj.getString("summary");
		event.description = obj.getString("description");
		event.location = obj.getString("location");
		event.imageUrl = obj.getString("imageUrl");
		event.startTime = obj.getString("startTime");
		event.endTime = obj.getString("endTime");
		Log.d("debug", "fromParseObject eventName = " + event.eventName);
		return event;
	}
	
	public static ArrayList<Event> fromParseObjectsList(List<ParseObject> list) {
		ArrayList<Event> events = new ArrayList<Event>(list.size());
		Log.d("debug", "fromParseObjectsList - num objects = " + String.valueOf(list.size()));
		for (int i=0; i<list.size(); i++) {
			events.add(Event.fromParseObject(list.get(i)));
		}
		
		return events;
	}

	@Override
	public String toString() {
		return getId() + ": " + getEventName();
	}

	// Getter methods
	public String getId() {
		return id;
	}

	public String getEventName() {
		return eventName;
	}

	public String getSummary() {
		return summary;
	}

	public String getDescription() {
		return description;
	}

	public String getLocation() {
		return location;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}
}

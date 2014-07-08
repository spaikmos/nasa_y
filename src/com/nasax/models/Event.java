package com.nasax.models;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("event")
public class Event extends ParseObject {
	// I don't recall all the member variables in event.  We'll have to make sure this matches the model.
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
		return (Event)obj;
	}
	
	public static ArrayList<Event> fromParseObjectsList(List<ParseObject> list) {
		ArrayList<Event> events = new ArrayList<Event>(list.size());
		for (int i=0; i<list.size(); i++) {
			events.add(fromParseObject(list.get(i)));
		}
		
		return events;
	}

	@Override
	public String toString() {
		return getObjectId() + ": " + getEventName();
	}

	// Getter methods
	public String getEventName() {
		return this.getString("eventName");
	}

	public String getSummary() {
		return this.getString("summary");
	}

	public String getDescription() {
		return this.getString("description");
	}

	public String getLocation() {
		return this.getString("location");
	}

	public String getImageUrl() {
		return this.getString("imageUrl");
	}

	public String getStartTime() {
		return this.getString("startTime");
	}

	public String getEndTime() {
		return this.getString("endTime");
	}
}

package com.nasax.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("event")
public class Event extends ParseObject {

	public Event() {
	}
	
	public static Event fromParseObject(ParseObject obj) {
		Event event = (Event) obj;
		event.pinInBackground(null);
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

	public Date getStartTime() {
		return this.getDate("startTime");
	}

	public Date getEndTime() {
		return this.getDate("endTime");
	}
}

package com.nasax.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;



@ParseClassName("event")
public class Event extends ParseObject implements Comparable<Event> {

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
		
		Collections.sort(events);
		return events;
	}
	
	@Override
	public String toString() {
		return getObjectId() + ": " + getEventName();
	}

	public int compareTo(Event e) {
		int cmp = getStartTime().compareTo(e.getStartTime());
        return (cmp != 0 ? cmp : getEventName().compareTo(e.getEventName()));
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

	public ParseFile getImageFile() {
		return this.getParseFile("imageFile");
	}

	public Date getStartTime() {
		return this.getDate("startTime");
	}

	public Date getEndTime() {
		return this.getDate("endTime");
	}
}

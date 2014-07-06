package com.nasax.models;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;


public class Event implements Parcelable{
	// I don't recall all the member variables in event.  We'll have to make sure this matches the model.
	private String id;
	private String eventTitle;
	private String summary;			// Short description of event
	private String description;		// Long description
	private String location;		// Address
	private String imageUrl;
	private String createdAt;
	private String startTime;
	private String endTime;
	private String lastModified;

	public Event() {
	}
	
	public static Event fromParseObject(ParseObject obj) {
		Event event = new Event();
		// Extract values from obj to populate the member variables
		// event.eventTitle = obj.getString("eventTitle");
		
		return event;
	}
	
	public static ArrayList<Event> fromParseObjectsList(List<ParseObject> list) {
		ArrayList<Event> events = new ArrayList<Event>(list.size());
		
		for (int i=0; i<list.size(); i++) {
			events.add(Event.fromParseObject(list.get(i)));
		}
		
		return events;
	}

	@Override
	public String toString() {
		return getId() + ": " + getEventTitle();
	}

	// Parcelable interface
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(eventTitle);
	}
	
	public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
		@Override
		public Event createFromParcel(Parcel in) {
			return new Event(in);
		}
		
		@Override
		public Event[] newArray(int size) {
			return new Event[size];
		}
	};
	
	private Event(Parcel in) {
		id = in.readString();
	    eventTitle = in.readString();
	}

	// Getter methods
	public String getId() {
		return id;
	}

	public String getEventTitle() {
		return eventTitle;
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

	public String getCreatedAt() {
		return createdAt;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getLastModified() {
		return lastModified;
	}
}

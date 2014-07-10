package com.nasax.models;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("EventUser")
public class EventUser extends ParseObject {

	public EventUser() {
	}
	
	public static EventUser fromParseObject(ParseObject obj) {
		EventUser eventUser = (EventUser) obj;
		eventUser.pinInBackground(null);
		return (EventUser)obj;
	}
	
	public static ArrayList<EventUser> fromParseObjectsList(List<ParseObject> list) {
		ArrayList<EventUser> eventUsers = new ArrayList<EventUser>(list.size());
		for (int i=0; i<list.size(); i++) {
			eventUsers.add(fromParseObject(list.get(i)));
		}
		
		return eventUsers;
	}

	@Override
	public String toString() {
		return getObjectId() + ": " + getEventId();
	}

	// Getter methods
	public String getEventId() {
		return this.getString("eventId");
	}
	
	public String getUserId() {
		return this.getString("userId");
	}
	
	public String getDisplayFields() {
		//return this.get("displayFields");
		return null;
	}
	
	public String getAtEvent() {
		return this.getString("atEvent");
	}
	
	public String getIsGoing() {
		return this.getString("isGoing");
	}
	
	// Setter methods
	//public void setDisplayFields();
	
	public void setAtEvent(Boolean atEvent) {
		this.put("atEvent", atEvent);
		this.saveInBackground();
	}
	
	public void setIsGoing(String isGoing) {
		if(isGoing.equals("Yes") || isGoing.equals("Maybe") || isGoing.equals("No")) {
			this.put("isGoing", isGoing);
			this.saveInBackground();
		}
	}
}
package com.nasax.models;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("EventUser")
public class EventUser extends ParseObject implements Comparable<EventUser> {

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
		return getObjectId() + ": " + getEvent();
	}
	
	public int compareTo(EventUser eu) {
		int cmp = (getUser().getUsername()).compareTo(eu.getUser().getUsername());
        return cmp;
    }

	// Getter methods
	public ParseObject getEvent() {
		return this.getParseObject("event");
	}
	
	public ParseUser getUser() {
		return this.getParseUser("user");
	}
	
	public String getDisplayFields() {
		//return this.get("displayFields");
		return null;
	}
	
	public Boolean getAtEvent() {
		return this.getBoolean("atEvent");
	}
	
	public int getIsGoing() {
		return this.getInt("isGoing");
	}
	
	public Boolean getShowName() {
		return this.getBoolean("showName");
	}
	
	public Boolean getShowPhone() {
		return this.getBoolean("showPhone");
	}
	public Boolean getShowEmail() {
		return this.getBoolean("showEmail");
	}
	
	public Boolean getShowAddress() {
		return this.getBoolean("showAddress");
	}
	
	public Boolean getShowSchoolName() {
		return this.getBoolean("showSchoolName");
	}
	
	public Boolean getShowCompanyName() {
		return this.getBoolean("showCompanyName");
	}
	
	public Boolean getShowOccupation() {
		return this.getBoolean("showOccupation");
	}
	
	public Boolean getShowAbout() {
		return this.getBoolean("showAbout");
	}
	
	// Setter methods	
	public void setAtEvent(Boolean atEvent) {
		this.put("atEvent", atEvent);
		this.saveInBackground();
	}
	
	public void setIsGoing(int isGoing) {
		if((isGoing >= 0) && (isGoing <= 2)) {
			this.put("isGoing", isGoing);
			this.saveInBackground();
		}
	}
	
	public void setShow(String field, Boolean val) {
		this.put(field,  val);
		this.saveInBackground();
	}
}
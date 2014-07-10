package com.nasax.models;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseUser;


@ParseClassName("PUser")
public class PUser extends ParseUser {

	public PUser() {
	}
	
	public static PUser fromParseUser(ParseUser obj) {
		PUser user = (PUser) obj;
		user.pinInBackground(null);
		return (PUser)obj;
	}
	
	public static ArrayList<PUser> fromParseUsersList(List<ParseUser> list) {
		ArrayList<PUser> users = new ArrayList<PUser>(list.size());
		for (int i=0; i<list.size(); i++) {
			users.add(fromParseUser(list.get(i)));
		}
		
		return users;
	}

	@Override
	public String toString() {
		return getObjectId() + ": " + getUsername() + " : " + getName();
	}

	// Getter methods
	public String getName() {
		return this.getString("name");
	}

	public String getAddress() {
		return this.getString("address");
	}

	public String getCompanyName() {
		return this.getString("companyName");
	}

	public String getSchoolName() {
		return this.getString("schoolName");
	}

	public String getOccupation() {
		return this.getString("occupation");
	}

	public String getAbout() {
		return this.getString("about");
	}

}

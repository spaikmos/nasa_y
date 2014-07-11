package com.nasax.activities;

import android.app.Application;
import android.util.Log;

import com.nasax.models.Event;
import com.nasax.models.EventUser;
import com.nasax.models.PUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class NasaXApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
        		cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .defaultDisplayImageOptions(defaultOptions)
            .build();
        ImageLoader.getInstance().init(config);

		// Add your initialization code here
	    ParseObject.registerSubclass(Event.class);
	    ParseObject.registerSubclass(EventUser.class);
	    ParseUser.registerSubclass(PUser.class);
	    Parse.enableLocalDatastore(this);
		Parse.initialize(this, "ZnS9KINPID8w6NcMT25IIcYRB6xVD0ELOZsDYcH1",
				"0m2t7SvIxJijmnm312SxI7pTGcp6s0wr973CltoG");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
		
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
		
		// Print some debug info
		ParseUser me = ParseUser.getCurrentUser();
		try {
			me.fetch();
			me.pin();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d("debug", "Your username is: " + me.getUsername());
		Log.d("debug", "Your name is: " + me.getString("name"));
	}
}

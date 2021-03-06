package com.nasax.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.nasax.fragments.AttendeeListFragment;
import com.nasax.fragments.EventDetailsFragment;
import com.nasax.fragments.MeetingPicsFragment;
import com.nasax.models.Event;
import com.nasax.models.EventUser;
import com.nasax.models.MyActionBar;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class EventActivity extends FragmentActivity {
	private static String eventId;
	private static String eventUserId;
	FragmentPagerAdapter adapterViewPager;

	private OnKeyEventListener onKeyEventListener;

	public interface OnKeyEventListener {
		public void onKeyPressed(int volume);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Event event = null;

		super.onCreate(savedInstanceState);
		eventId = getIntent().getStringExtra("eventId");

		// Get the event data from local datastore
		ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
		query.fromLocalDatastore();
		try {
			event = (Event) query.get(eventId);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Get the EventUser data from local datastore
		query = ParseQuery.getQuery("EventUser");
		query.fromLocalDatastore();
		query.whereEqualTo("event", event);
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		try {
			EventUser eventUser = (EventUser) query.getFirst();
			eventUser.pinInBackground(null);
			eventUserId = eventUser.getObjectId();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		setContentView(R.layout.activity_event);
		ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
		// TODO: Set the title bar of this activity to use the eventImage and
		// eventName fields
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_action_bar_event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (MyActionBar.optionSelected(this, item, eventUserId) == false) {
			// MyActionBar didn't handle this. Kick it up to the parent class.
			return super.onOptionsItemSelected(item);
		} else {
			return true;
		}
	}

	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		MyActionBar.onActivityResult(this, requestCode, resultCode, data);
	}

	public void setOnKeyEventListener(OnKeyEventListener listener) {
		this.onKeyEventListener = listener;
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {
		private final int NUM_ITEMS = 3;

		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0: // Fragment # 0 - This will show FirstFragment
				return EventDetailsFragment.newInstance(eventUserId);
			case 1: // Fragment # 0 - This will show FirstFragment different
					// title
				AttendeeListFragment fragment = AttendeeListFragment.newInstance(eventId);
				setOnKeyEventListener(fragment);
				return fragment;
			case 2: // Fragment # 1 - This will show SecondFragment
				return MeetingPicsFragment.newInstance(eventUserId);
			default:
				return null;
			}
		}

		// Returns the page title for the top indicator
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Event Details";
			case 1:
				return "Attendee List";
			case 2:
				return "Pictures";
			}
			Log.d("debug", "getPageTitle Invalid = " + String.valueOf(position));
			return null;
		}

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		int action = event.getAction();
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			if ((onKeyEventListener != null) && (action == android.view.KeyEvent.ACTION_DOWN)) {
				onKeyEventListener.onKeyPressed(1);
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if ((onKeyEventListener != null) && (action == android.view.KeyEvent.ACTION_DOWN)) {
				onKeyEventListener.onKeyPressed(-1);
			}
			return true;
		default:
			return super.dispatchKeyEvent(event);
		}
	}
}

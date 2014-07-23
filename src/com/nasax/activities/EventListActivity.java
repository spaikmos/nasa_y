package com.nasax.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nasax.models.MyActionBar;
import com.parse.ParseAnalytics;

public class EventListActivity extends FragmentActivity {
	static final int ZXSCAN_CODE = 12345;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);
		ParseAnalytics.trackAppOpened(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_action_bar_event_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (MyActionBar.optionSelected(this, item, null) == false) {
			// MyActionBar didn't handle this.  Kick it up to the parent class.
			return super.onOptionsItemSelected(item);
		} else {
			return true;
		}
	}

	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		MyActionBar.onActivityResult(this, requestCode, resultCode, data);
	}
}

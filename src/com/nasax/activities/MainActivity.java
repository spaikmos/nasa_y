package com.nasax.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;

import eu.livotov.zxscan.ZXScanHelper;

public class MainActivity extends FragmentActivity {
	static final int ZXSCAN_CODE = 12345;
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ParseAnalytics.trackAppOpened(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_action_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.miAddEvent:
			ZXScanHelper.scan(this, ZXSCAN_CODE);
			return true;
		case R.id.miProfile:
			Intent i = new Intent(this, EditProfile.class);
			startActivity(i);
			return true;
		case R.id.miLogout:
			ParseUser.logOut();
			Intent i2 = new Intent(this, LoginActivity.class);
			startActivity(i2);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (resultCode == RESULT_OK && requestCode == ZXSCAN_CODE) {
			String scannedCode = ZXScanHelper.getScannedCode(data);
			Toast.makeText(this,  "Scan = " + scannedCode, Toast.LENGTH_LONG).show();
		}
	}

}

package com.nasax.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {
	EditText etPassword;
	EditText etUsername;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Comment this out if you want to see the login page again		
		if (ParseUser.getCurrentUser() != null) {
			//Log.d("debug", "Current user = " + String.valueOf(ParseUser.getCurrentUser()));
			// User is already logged in. Skip the login screen
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
		}
		
		setContentView(R.layout.activity_login);

		// Setup views
		etPassword = (EditText) findViewById(R.id.etPassword);
		etUsername = (EditText) findViewById(R.id.etUsername);
		context = this;
	}

	public void onClickLogin(View v) {
		ParseUser.logInInBackground(etUsername.getText().toString(), etPassword
				.getText().toString(), new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					// Hooray! The user is logged in. Go to the main activity
					// page.
					Intent i = new Intent(context, MainActivity.class);
					startActivity(i);
				} else {
					Toast.makeText(context, "Invalid credentials.  Try again!",
							Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	public void onClickSignup(View v) {
		Intent i = new Intent(context, SignupActivity.class);
		startActivity(i);
	}
}

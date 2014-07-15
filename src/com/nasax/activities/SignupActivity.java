package com.nasax.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends Activity {
	EditText etUsername;
	EditText etPassword;
	EditText etPassword2;
	EditText etName;
	EditText etEmail;
	EditText etPhone;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		// Get views
		etUsername = (EditText) findViewById(R.id.etNewUsername);
		etPassword = (EditText) findViewById(R.id.etNewPassword);
		etPassword2 = (EditText) findViewById(R.id.etNewPassword2);
		etName = (EditText) findViewById(R.id.etNewName);
		etEmail = (EditText) findViewById(R.id.etNewEmail);
		etPhone = (EditText) findViewById(R.id.etNewPhone);
		context = this;
	}

	public void onClickNewSignup(View v) {

		String pw1 = etPassword.getText().toString();
		String pw2 = etPassword2.getText().toString();

		// Verify the passwords match
		if (pw1.equals(pw2)) {
			// TODO:  Need to validate each of the strings.  I.e. valid phone, e-mail, etc.
			// Create the ParseUser
			ParseUser user = new ParseUser();
			
			// Set core properties
			user.setUsername(etUsername.getText().toString());
			user.setPassword(pw1);
			user.setEmail(etEmail.getText().toString());
			
			// Set custom properties
			user.put("name", etName.getText().toString());
			user.put("phone", etPhone.getText().toString());

			// Invoke signUpInBackground
			user.signUpInBackground(new SignUpCallback() {
				public void done(ParseException e) {
					if (e == null) {
						// Hooray! Let them use the app now.
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
					} else {
						// Sign up didn't succeed. Look at the ParseException
						// to figure out what went wrong
						Toast.makeText(context, "TODO:  Fix this error!",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		} else {
			// Passwords do not match
			Toast.makeText(context,  "Passwords must match!",  Toast.LENGTH_LONG).show();
		}
	}
}

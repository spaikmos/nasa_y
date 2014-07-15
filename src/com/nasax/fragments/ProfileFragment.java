package com.nasax.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nasax.activities.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {
	EditText etName;
	EditText etAddress;
	EditText etPhone;
	EditText etEmail;
	EditText etSchoolName;
	EditText etCompanyName;
	EditText etOccupation;
	EditText etAbout;
	ImageView ivProfilePic;
	ParseUser user;
	TextView tvUsername;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profile, container, false);
		
		// Get views
		etName = (EditText) v.findViewById(R.id.etName);
		etAddress = (EditText) v.findViewById(R.id.etAddress);
		etPhone = (EditText) v.findViewById(R.id.etPhone);
		etEmail = (EditText) v.findViewById(R.id.etEmail);
		etSchoolName = (EditText) v.findViewById(R.id.etSchoolName);
		etCompanyName = (EditText) v.findViewById(R.id.etCompanyName);
		etOccupation = (EditText) v.findViewById(R.id.etOccupation);
		etAbout = (EditText) v.findViewById(R.id.etAbout);
		ivProfilePic = (ImageView) v.findViewById(R.id.ivProfilePic);
		tvUsername = (TextView) v.findViewById(R.id.tvUsername);
		user = ParseUser.getCurrentUser();
		
		// Refresh the user object via the network
		user.fetchInBackground(new GetCallback<ParseUser>() {
			public void done(ParseUser object, ParseException e) {
				if (e == null) {
					// Success!
					user = object;
					Log.d("debug", "callback username = " + object.getString("name"));
					user.pinInBackground(null);
					// Populate the views
					onResetButtonClick(null);
				} else {
					// Failure!
					e.printStackTrace();
				}
			}
		});
		
		// Set the view with data if a current version exists
		onResetButtonClick(null);
		
		// Configure the buttons
		Button button = (Button) v.findViewById(R.id.btnReset);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	onResetButtonClick(v);
		    }
		});
		
		button = (Button) v.findViewById(R.id.btnSave);
		button.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	onSaveButtonClick(v);
		    }
		});
		
		return v;
	}
	
    public void onSaveButtonClick(View v) {
    	user.put("name", etName.getText().toString());
    	user.put("address", etAddress.getText().toString());
    	user.put("phone", etPhone.getText().toString());
    	user.put("email", etEmail.getText().toString());
    	user.put("schoolName", etSchoolName.getText().toString());
    	user.put("companyName", etCompanyName.getText().toString());
    	user.put("occupation", etOccupation.getText().toString());
    	user.put("about", etAbout.getText().toString());
    	user.saveInBackground();
    }

    public void onResetButtonClick(View v) {
    	etName.setText(user.getString("name"));
    	etAddress.setText(user.getString("address"));
    	etPhone.setText(user.getString("phone"));
    	etEmail.setText(user.getString("email"));
    	etSchoolName.setText(user.getString("schoolName"));
    	etCompanyName.setText(user.getString("companyName"));
    	etOccupation.setText(user.getString("occupation"));
    	etAbout.setText(user.getString("about"));
    	tvUsername.setText(user.getUsername());
    	
    	// Load the image
		ivProfilePic.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		// Populate views with data
		imageLoader.displayImage(user.getString("imgUrl"), ivProfilePic);
    }

}

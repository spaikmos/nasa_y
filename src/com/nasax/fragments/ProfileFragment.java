package com.nasax.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.nasax.activities.R;
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
	ParseUser user;

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
		user = ParseUser.getCurrentUser();
		
		// Populate the views
		onResetButtonClick(v.findViewById(R.id.btnReset));
		
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
    	etName.setText(user.getString("name").toString());
    	etAddress.setText(user.getString("address").toString());
    	etPhone.setText(user.getString("phone").toString());
    	etEmail.setText(user.getString("email").toString());
    	etSchoolName.setText(user.getString("schoolName").toString());
    	etCompanyName.setText(user.getString("companyName").toString());
    	etOccupation.setText(user.getString("occupation").toString());
    	etAbout.setText(user.getString("about").toString());
    }

}

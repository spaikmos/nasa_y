package com.nasax.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nasax.activities.R;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ProfileFragment extends Fragment {
	EditText etName;
	EditText etAddress;
	EditText etPhone;
	EditText etEmail;
	EditText etSchoolName;
	EditText etCompanyName;
	EditText etOccupation;
	EditText etAbout;
	ParseImageView ivProfilePic;
	ParseUser user;
	TextView tvUsername;

	public final String APP_TAG = "nasa_x";
	public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
	public final static int PICK_PHOTO_CODE = 1046;
	public String photoFileName = "profilePhoto.jpg";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profile, container, false);

		// Get views
		etName = (EditText) v.findViewById(R.id.tvName);
		etAddress = (EditText) v.findViewById(R.id.tvAddress);
		etPhone = (EditText) v.findViewById(R.id.tvPhone);
		etEmail = (EditText) v.findViewById(R.id.tvEmail);
		etSchoolName = (EditText) v.findViewById(R.id.tvSchoolName);
		etCompanyName = (EditText) v.findViewById(R.id.tvCompanyName);
		etOccupation = (EditText) v.findViewById(R.id.tvOccupation);
		etAbout = (EditText) v.findViewById(R.id.tvAbout);
		ivProfilePic = (ParseImageView) v.findViewById(R.id.ivProfilePic);
		tvUsername = (TextView) v.findViewById(R.id.tvUsername);
		user = ParseUser.getCurrentUser();

		// Refresh the user object via the network
		user.fetchInBackground(new GetCallback<ParseUser>() {
			public void done(ParseUser object, ParseException e) {
				if (e == null) {
					// Success!
					user = object;
					Log.d("debug",
							"callback username = " + object.getString("name"));
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

		button = (Button) v.findViewById(R.id.btnCamera);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onLaunchCamera(v);
			}
		});

		button = (Button) v.findViewById(R.id.btnGallery);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onPickPhoto(v);
			}
		});

		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		Log.d("debug", "called onSaveInstanceState");
		// outState.putInt(SOME_VALUE_KEY, someStateValue);
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
		// The placeholder will be used before and during the fetch, to be
		// replaced by the fetched image data.
		// imageView.setPlaceholder(getResources().getDrawable(R.ic_launcher));
		ivProfilePic.setParseFile(user.getParseFile("picture"));
		ivProfilePic.loadInBackground(new GetDataCallback() {
			@Override
			public void done(byte[] data, ParseException e) {
				if (e != null) {
					e.printStackTrace();
				}
			}
		});
	}

	// Camera handling code

	public void onLaunchCamera(View view) {
		// create Intent to take a picture and return control to the calling application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// set the image filename
		intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName));
		// Start the image capture intent to take photo
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	// Returns the Uri for a photo stored on disk given the fileName
	public Uri getPhotoFileUri(String fileName) {
		// Get safe storage directory for photos
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_TAG);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
			Log.d("debug", "failed to create directory");
		}

		// Return the file target for the photo based on filename
		Uri retVal = Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
		return retVal;
	}

	// Trigger gallery selection for a photo
	public void onPickPhoto(View view) {
		// Create intent for picking a photo from the gallery
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// Bring up gallery to select a photo
		startActivityForResult(intent, PICK_PHOTO_CODE);
	}

	public static Bitmap RotateBitmap(Bitmap source, float angle)
	{
	      Matrix matrix = new Matrix();
	      matrix.postRotate(angle);
	      return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			Log.d("debug", "processing camera data");
			if (resultCode == Activity.RESULT_OK) {
				Uri takenPhotoUri = getPhotoFileUri(photoFileName);
				Log.d("debug", "taken Uri = " + takenPhotoUri.toString());
				// by this point we have the camera photo on disk
				Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());

				// Rotate the image if necessary
				// TODO:  Need to look at Exif data - http://stackoverflow.com/questions/14066038/why-image-captured-using-camera-intent-gets-rotated-on-some-devices-in-android
				// However, seems that the camera alwasy returns orientation 0.  Need to find a better solution.
/*
				ExifInterface ei = null;
				try {
					ei = new ExifInterface(takenPhotoUri.toString());
					int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

					switch(orientation) {
					    case ExifInterface.ORIENTATION_ROTATE_90:	
					        takenImage = RotateBitmap(takenImage, 90);
					        break;
					    case ExifInterface.ORIENTATION_ROTATE_180:
					        takenImage = RotateBitmap(takenImage, 180);
					        break;
					    case ExifInterface.ORIENTATION_ROTATE_270:
					        takenImage = RotateBitmap(takenImage, 180);
					        break;
					    default:
					    	Log.d("debug", "Need to handle exif orientation = " + String.valueOf(orientation));
					    	break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
*/
				
				// get byte array here
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				takenImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);			
				byte[] array = stream.toByteArray();

				// TODO: Set filename to username + .jpg
				final ParseFile file = new ParseFile("cameraPic.jpg", array);
				file.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException arg0) {
						user.put("picture", file);
						user.saveInBackground();
					}
				});
			} else { // Result was a failure
				Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
			}
			break;
		case PICK_PHOTO_CODE:
			if ((resultCode == Activity.RESULT_OK) && (data != null)) {
				Uri photoUri = data.getData();
				Log.d("debug", "gallery Uri = " + photoUri.toString());
				// Do something with the photo based on Uri
				Bitmap selectedImage = null;
				try {
					selectedImage = MediaStore.Images.Media.getBitmap(
							getActivity().getContentResolver(), photoUri);

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					selectedImage.compress(Bitmap.CompressFormat.JPEG, 100,
							stream);
					// get byte array here
					byte[] array = stream.toByteArray();

					// TODO: Set filename to username + .jpg
					final ParseFile file = new ParseFile("galleryPic.jpg",
							array);
					file.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException arg0) {
							user.put("picture", file);
							user.saveInBackground();
						}
					});
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Load the selected image into a preview
				ivProfilePic.setImageBitmap(selectedImage);
			} else { // Result was a failure
				Toast.makeText(getActivity(), "Picture wasn't chosen!", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			// Exit early
			return;
		}
	}
}

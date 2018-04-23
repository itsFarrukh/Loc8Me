package com.loc8me.client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loc8me.client.broadcastservices.YourService;
import com.loc8me.client.datastore.AssetData;
import com.loc8me.client.datastore.DataBaseHandler;
import com.loc8me.client.webservices.AssetsDetails;
import com.loc8me.client.webservices.ClientLoginInfo;
import com.loc8me.client.webservices.StaticFields;
import com.loc8me.client.webservices.WebService;

public class LoginActivity extends Activity implements OnClickListener {
	EditText emailEt, pwdEt;
	Button Login;
	WebService obj = new WebService();
	SharedPreferences myPrefs;
    ArrayList<AssetsDetails> assetsDetails = new ArrayList<AssetsDetails>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Initializers();
		if (checkPreferences()) {
//
//			Toast.makeText(this, "You are Logged IN", Toast.LENGTH_SHORT)
//			.show();
//
//			Intent i = new Intent(LoginActivity.this, YourService.class);
//			startService(i);
			Intent intent=new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
			
		}

	}

	public void Initializers() {
		emailEt = (EditText) findViewById(R.id.emailIdEt);
		pwdEt = (EditText) findViewById(R.id.passwordEt);
		Login = (Button) findViewById(R.id.submitButton);
		Login.setOnClickListener(this);

	}

	private class AsyncLoginWS extends AsyncTask<String, Void, Void> {
		ClientLoginInfo clientLoginInfo;

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			clientLoginInfo = obj.ClientLogin(params[0], params[1]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(StaticFields.serviceStatus=="sucess"&&clientLoginInfo.Uid!=-1){
				myPrefs = PreferenceManager
						.getDefaultSharedPreferences(LoginActivity.this);
				Editor edit = myPrefs.edit();

				edit.putString("UserId", ""+clientLoginInfo.Uid);
				Toast.makeText(LoginActivity.this, "hghgh", Toast.LENGTH_SHORT);
				if(clientLoginInfo.Type.equals("static")){
				edit.putBoolean("isStatic", true);
				edit.putString("Latitude", clientLoginInfo.Latitude);
				edit.putString("Longitude", clientLoginInfo.Longitude);
				}
				else{
					edit.putBoolean("isStatic", false);
				}
				edit.commit();
				AsyncGetAssetsDetailsWS task = new AsyncGetAssetsDetailsWS();
				task.execute();
				Toast.makeText(LoginActivity.this, "You are Logged IN \n Now setting up  your A/c", Toast.LENGTH_SHORT)
				
				.show();
			

			}

			else {
				Toast.makeText(LoginActivity.this, "unable to register", Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		protected void onPreExecute() {
			Toast.makeText(LoginActivity.this, "Validating..",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

	}

	private class AsyncGetAssetsDetailsWS extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			myPrefs = PreferenceManager
					.getDefaultSharedPreferences(LoginActivity.this);
			String id = myPrefs.getString("UserId", "-1");
			int Uid = Integer.parseInt(id);
          if(Uid!=-1)
        	  assetsDetails = obj.getAssetsDetails(Uid);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (StaticFields.serviceStatus=="sucess") {
				DataBaseHandler dbh = new DataBaseHandler(LoginActivity.this);
				for(AssetsDetails ad : assetsDetails){
					AssetData assetData = new AssetData();
					assetData.setId(ad.Aid);
					assetData.setName(ad.Name);
					assetData.setStartTime(ad.StartTime);
					assetData.setStopTime(ad.StopTime);
					assetData.setSpeedLimit(ad.SpeedLimit);
					assetData.setTimeSpan(ad.RefreshRate);
					assetData.setPNumber(ad.PNumber);
					long l=dbh.addData(assetData);
				}
				Intent i = new Intent(LoginActivity.this, YourService.class);
				startService(i);
				Intent intent=new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
			}

			else {
				Intent i = new Intent(LoginActivity.this, YourService.class);
				startService(i);
				Intent intent=new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				Toast.makeText(LoginActivity.this,StaticFields.serviceStatus, Toast.LENGTH_LONG).show();
				//finish();
			}
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

	}

	private boolean checkPreferences() {
		myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String email = myPrefs.getString("UserId", null);
		if (email != null) {
			return true;
		} else
			return false;

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.submitButton:
			if (emailEt.getText().toString() != null
					&& pwdEt.getText().toString() != null) {
				myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
				Editor editor = myPrefs.edit();
				editor.putString("EmailPreference", emailEt.getText()
						.toString());
				editor.putString("PasswordPreference", pwdEt.getText()
						.toString());
				editor.commit();

				AsyncLoginWS task = new AsyncLoginWS();
				task.execute(emailEt.getText().toString(), pwdEt.getText()
						.toString());
			} else {
				Toast.makeText(this, "Fields cannot be empty",
						Toast.LENGTH_SHORT).show();
			}

			break;
		}

	}

}

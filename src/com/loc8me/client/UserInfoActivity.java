package com.loc8me.client;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loc8me.client.datastore.AssetData;
import com.loc8me.client.datastore.DataBaseHandler;
import com.loc8me.client.webservices.StaticFields;

public class UserInfoActivity extends Activity implements OnClickListener {

	private TextView Name, StartTime, StopTime, TimeSpan, SpeedLimit,
			TrackingDays, PNumber;
	private Button submit, DateBtn, StartTimeBtn, StopTimeBtn, submitValueBtn;
	private int mYear, mMonth, mDay, mHour, mMinute;
	EditText dateEt, startEt, stopEt;
	LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		Initializers();
		int id = StaticFields.getAid();
		DataBaseHandler db = new DataBaseHandler(this);
		AssetData data = db.getAssetDetails(id);
		Name.setText(data.getName());
		StartTime.setText(data.getStartTime());
		StopTime.setText(data.getStopTime());
		TimeSpan.setText(data.getTimeSpan());
		SpeedLimit.setText(data.getSpeedLimit());
		TrackingDays.setText(data.getTrackingDays());
		PNumber.setText(data.getPNumber());
		submit.setOnClickListener(this);

	}

	public void Initializers() {
		Name = (TextView) findViewById(R.id.NameTv);
		StartTime = (TextView) findViewById(R.id.startTimeTv);
		StopTime = (TextView) findViewById(R.id.stopTimeTv);
		TimeSpan = (TextView) findViewById(R.id.timeSpanTv);
		SpeedLimit = (TextView) findViewById(R.id.speedLimitTv);
		TrackingDays = (TextView) findViewById(R.id.trackingDaysTv);
		PNumber = (TextView) findViewById(R.id.phoneNumberTv);
		submit = (Button) findViewById(R.id.submitBtn);

		mInflater = (LayoutInflater) this
				.getSystemService(this.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submitBtn:
			final Dialog dialog = new Dialog(this);
			View view = mInflater.inflate(R.layout.custom_dialog, null, false);
			dialog.setContentView(view);
			dialog.setTitle("Title...");
			dateEt = (EditText) view.findViewById(R.id.DateEt);
			startEt = (EditText) view.findViewById(R.id.startTimeEt);
			stopEt = (EditText) view.findViewById(R.id.stopTimeEt);

			// final Calendar c = Calendar.getInstance();
			// mYear = c.get(Calendar.YEAR);
			// mMonth = c.get(Calendar.MONTH);
			// mDay = c.get(Calendar.DAY_OF_MONTH);
			DateBtn = (Button) view.findViewById(R.id.setDateBtn);
			StartTimeBtn = (Button) view.findViewById(R.id.setStartTimeBtn);
			StopTimeBtn = (Button) view.findViewById(R.id.setStopTimeBtn);
			submitValueBtn = (Button) view.findViewById(R.id.setValueBtn);
			submitValueBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String _dateEt=""+dateEt.getText().toString();
					String _startEt=""+startEt.getText().toString();
					String _stopEt=""+stopEt.getText().toString();
					 if( _dateEt!=""&&_startEt!=""&&_stopEt!="")
					 {
						 StaticFields.FromTrack=dateEt.getText().toString()+" "+startEt.getText().toString();
						 StaticFields.ToTrack=dateEt.getText().toString()+" "+stopEt.getText().toString();
						 Intent intent = new Intent(UserInfoActivity.this,RouteActivity.class);
						 startActivity(intent);
					 }
					 else{
						 Toast.makeText(UserInfoActivity.this, "Select something", Toast.LENGTH_LONG).show();
					 }

				}
			});
			StartTimeBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					TimePickerDialog tpd1 = new TimePickerDialog(
							UserInfoActivity.this,
							new TimePickerDialog.OnTimeSetListener() {

								@Override
								public void onTimeSet(TimePicker view,
										int hourOfDay, int minute) {
									// TODO Auto-generated method stub
									startEt.setText(hourOfDay + ":" + minute
											+ ":" + "00");

								}
							}, mHour, mMinute, true);
					tpd1.show();

				}
			});
			StopTimeBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TimePickerDialog tpd = new TimePickerDialog(
							UserInfoActivity.this,
							new TimePickerDialog.OnTimeSetListener() {

								@Override
								public void onTimeSet(TimePicker view,
										int hourOfDay, int minute) {
									// TODO Auto-generated method stub
									stopEt.setText(hourOfDay + ":" + minute
											+ ":" + "00");

								}
							}, mHour, mMinute, true);
					tpd.show();

				}
			});

			DateBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final Calendar c = Calendar.getInstance();
					mYear = c.get(Calendar.YEAR);
					mMonth = c.get(Calendar.MONTH);
					mDay = c.get(Calendar.DAY_OF_MONTH);

					DatePickerDialog dpd = new DatePickerDialog(
							UserInfoActivity.this,
							new DatePickerDialog.OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									Toast.makeText(UserInfoActivity.this,
											" " + dayOfMonth, Toast.LENGTH_LONG)
											.show();
									dateEt.setText( (monthOfYear + 1) +"/"
											+ dayOfMonth + "/" + year);

								}
							}, mYear, mMonth, mDay);
					dpd.show();

					// DatePickerDialog.OnDateSetListener dpd=new
					// DatePickerDialog.OnDateSetListener() {
					//
					// @Override
					// public void onDateSet(DatePicker view, int year, int
					// monthOfYear,
					// int dayOfMonth) {
					// // TODO Auto-generated method stub
					// mYear=year;
					// mMonth=monthOfYear;
					// mDay=dayOfMonth;
					//
					//
					// }
					//
					// };

				}

			});
			dialog.show();

			break;
		// case R.id.setDateBtn:
		// final Calendar c = Calendar.getInstance();
		// mYear = c.get(Calendar.YEAR);
		// mMonth = c.get(Calendar.MONTH);
		// mDay = c.get(Calendar.DAY_OF_MONTH);
		// DatePickerDialog dpd = new DatePickerDialog(this,
		// new DatePickerDialog.OnDateSetListener() {
		//
		// @Override
		// public void onDateSet(DatePicker view, int year,
		// int monthOfYear, int dayOfMonth) {
		// // Display Selected date in textbox
		// dateEt.setText(dayOfMonth + "-" + (monthOfYear + 1)
		// + "-" + year);
		//
		// }
		//
		// }, mYear, mMonth, mDay);
		// dpd.show();
		// break;
		case R.id.setDateBtn:

		}

	}

}

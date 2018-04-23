package com.loc8me.client;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loc8me.client.webservices.StaticFields;
import com.loc8me.client.webservices.*;

public class LiveTracking extends Fragment {
	ArrayList<CoordinateDetails> CoordinatesDetails = new ArrayList<CoordinateDetails>();
	WebService services = new WebService();
	private GoogleMap googleMap;
	MapView mapView;
	final HandlerTask handlerTask = new HandlerTask();
	private static final float DEFAULT_ZOOM = 19;
	public static final String KEY_ID = "id";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_DateTime = "dateTime";
	ArrayList<HashMap<String, String>> locInfo = new ArrayList<HashMap<String, String>>();
	HashMap<String, Marker> markerHashMap = new HashMap<String, Marker>();
	final Handler handler = new Handler();
	private static double Karachi_Latitude = 24.8600;
	private static double Karachi_Longitude = 67.0100;
	private SharedPreferences myPrefs;
	View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_track_me, container,
				false);

		this.v = rootView;
		try {
			// inflater = (LayoutInflater) this.getSystemService(
			// this.LAYOUT_INFLATER_SERVICE );

			try {
				MapsInitializer.initialize(this.getActivity());
			} catch (Exception e) {
				Log.e("Address Map", "Could not initialize google play", e);
			}
			switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this
					.getActivity())) {
			case ConnectionResult.SUCCESS:
				mapView = (MapView) v.findViewById(R.id.map);
				mapView.onCreate(savedInstanceState);
				// Gets to GoogleMap from the MapView and does initialization
				// stuff
				if (mapView != null) {
					googleMap = mapView.getMap();
					Toast.makeText(this.getActivity(), "Map inti",
							Toast.LENGTH_SHORT).show();
					// setFocusOnMap(Karachi_Latitude, Karachi_Longitude);
					AsyncTrackMe task = new AsyncTrackMe();
					task.execute();

				}
				break;
			case ConnectionResult.SERVICE_MISSING:
				Toast.makeText(this.getActivity(), "SERVICE MISSING",
						Toast.LENGTH_SHORT).show();
				break;
			case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
				Toast.makeText(this.getActivity(), "UPDATE REQUIRED",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(
						this.getActivity(),
						GooglePlayServicesUtil
								.isGooglePlayServicesAvailable(this
										.getActivity()), Toast.LENGTH_SHORT)
						.show();
			}

		} catch (Exception ex) {
			Toast.makeText(this.getActivity(), ex.getMessage(),
					Toast.LENGTH_LONG).show();
		}
		return rootView;
	}

	class HandlerTask implements Runnable {
		public void run() {

			AsyncTrackMe task = new AsyncTrackMe();
			task.execute();
			// Toast.makeText(activity, "timer", Toast.LENGTH_LONG).show();
			handler.postDelayed(this, 5000); // now is every 2 minutes
		}
	}

	private class AsyncTrackMe extends AsyncTask<String, Void, Void> {
		String Response;

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			myPrefs = PreferenceManager
					.getDefaultSharedPreferences(LiveTracking.this
							.getActivity());
			String id = myPrefs.getString("UserId", "-1");
			int Uid = Integer.parseInt(id);

			CoordinatesDetails = services.getAssetsLocation(Uid);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				if (StaticFields.serviceStatus.equals("sucess")) {
					for (CoordinateDetails cd : CoordinatesDetails) {
						HashMap<String, String> map = new HashMap<String, String>();

						map.put(KEY_ID, "" + cd.Aid);
						map.put(KEY_LATITUDE, cd.Latitude);
						map.put(KEY_LONGITUDE, cd.Longitude);
						map.put(KEY_DateTime, cd.DateTime);
						locInfo.add(map);

					}
					if (markerHashMap.size() <= 0) {
						for (HashMap<String, String> map : locInfo) {
							addMarker(Integer.parseInt(map.get(KEY_ID)),
									Double.parseDouble(map.get(KEY_LATITUDE)),
									Double.parseDouble(map.get(KEY_LONGITUDE)),
									map.get(KEY_DateTime));
							Karachi_Latitude = Double.parseDouble(map
									.get(KEY_LATITUDE));
							Karachi_Longitude = Double.parseDouble(map
									.get(KEY_LONGITUDE));
						}
						setFocusOnMap(Karachi_Latitude, Karachi_Longitude);

					} else
						for (HashMap<String, String> map : locInfo) {
							if (markerHashMap.containsKey("" + map.get(KEY_ID))) {
								Marker marker = markerHashMap.get(""
										+ map.get(KEY_ID));
								marker.setPosition(new LatLng(Double
										.parseDouble(map.get(KEY_LATITUDE)),
										Double.parseDouble(map
												.get(KEY_LONGITUDE))));
								// Toast.makeText(activity,
								// map.get(KEY_LATITUDE)+"\n"+map.get(KEY_LONGITUDE),
								// Toast.LENGTH_SHORT).show();

							} else {
								addMarker(Integer.parseInt(map.get(KEY_ID)),
										Double.parseDouble(map
												.get(KEY_LATITUDE)),
										Double.parseDouble(map
												.get(KEY_LONGITUDE)),
										map.get(KEY_DateTime));
							}
						}
				} else
					Toast.makeText(LiveTracking.this.getActivity(),
							StaticFields.serviceStatus, Toast.LENGTH_SHORT)
							.show();
			} catch (Exception ex) {
				Toast.makeText(getActivity(), ex.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

	}

	private void addMarker(int Uid, double Latitude, double Longitude,
			final String DateTime) {
		// TODO Auto-generated method stub
		MarkerOptions option;

		option = new MarkerOptions().title("" + Uid)
				.position(new LatLng(Latitude, Longitude)).flat(true);

		Marker m = googleMap.addMarker(option);

		m.showInfoWindow();
		markerHashMap.put("" + Uid, m);

	}

	private void setFocusOnMap(double latitude, double longitude) {
		LatLng LL = new LatLng(latitude, longitude);
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(LL,
				DEFAULT_ZOOM);
		googleMap.animateCamera(cameraUpdate);

	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
		handler.postDelayed(handlerTask, 9000);

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		handler.removeCallbacks(handlerTask);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		handler.removeCallbacks(handlerTask);

	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
}

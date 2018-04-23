package com.loc8me.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
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
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loc8me.client.datastore.AssetData;
import com.loc8me.client.datastore.DataBaseHandler;
import com.loc8me.client.webservices.Coordinate;
import com.loc8me.client.webservices.StaticFields;
import com.loc8me.client.webservices.WebService;

public class RouteActivity  extends Activity{
	ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
	WebService services = new WebService();
	private GoogleMap googleMap;
	MapView mapView;
	// final HandlerTask handlerTask = new HandlerTask();
	private static final float DEFAULT_ZOOM = 19;
	public static final String KEY_AID = "aid";
	public static final String KEY_ALOCID = "ALocId";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_DateTime = "dateTime";
	ArrayList<HashMap<String, String>> locInfo = new ArrayList<HashMap<String, String>>();
	HashMap<String, Marker> markerHashMap = new HashMap<String, Marker>();
	final Handler handler = new Handler();
	private static double Karachi_Latitude = 24.8600;
	private static double Karachi_Longitude = 67.0100;
	private SharedPreferences myPrefs;
	boolean isOverSpeeding=false;
	boolean isNotOverSpeeding=false;
	boolean isfirstHistory = true;
	int assetId;
	private Handler mHandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track_me);
		try {
			// inflater = (LayoutInflater) this.getSystemService(
			// this.LAYOUT_INFLATER_SERVICE );

			try {
				MapsInitializer.initialize(this);
			} catch (Exception e) {
				Log.e("Address Map", "Could not initialize google play", e);
			}
			switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this
					)) {
			case ConnectionResult.SUCCESS:
				mapView = (MapView) findViewById(R.id.map);
				mapView.onCreate(savedInstanceState);
				// Gets to GoogleMap from the MapView and does initialization
				// stuff
				if (mapView != null) {
					googleMap = mapView.getMap();
					Toast.makeText(this, "Map inti",
							Toast.LENGTH_SHORT).show();
					// setFocusOnMap(Karachi_Latitude, Karachi_Longitude);
					if (isfirstHistory) {
						try {

							AsyncTrackHistory task = new AsyncTrackHistory();
							task.execute();
						} catch (Exception ex) {
							Toast.makeText(this, ex.toString(),
									Toast.LENGTH_LONG).show();
						}

						/*
						 * AsyncCallDrawPolygon secondTask = new
						 * AsyncCallDrawPolygon(); secondTask.execute();
						 */
					}
				}
				break;
			case ConnectionResult.SERVICE_MISSING:
				Toast.makeText(this, "SERVICE MISSING",
						Toast.LENGTH_SHORT).show();
				break;
			case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
				Toast.makeText(this, "UPDATE REQUIRED",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(
						this,
						GooglePlayServicesUtil
								.isGooglePlayServicesAvailable(this
										), Toast.LENGTH_SHORT)
						.show();
			}

		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(),
					Toast.LENGTH_LONG).show();
		}
		
		
		
	}
	private class AsyncTrackHistory extends
	AsyncTask<String, Void, List<Coordinate>> {

@Override
protected List<Coordinate> doInBackground(String... params) {
	// TODO Auto-generated method stub
	
	myPrefs = PreferenceManager
			.getDefaultSharedPreferences(RouteActivity.this);
	String id = myPrefs.getString("UserId", "-1");
	int Uid = Integer.parseInt(id);
     
	coordinates = services.getAssetsHistoryLocation(Uid,StaticFields.getAid(),
			StaticFields.FromTrack,StaticFields.ToTrack);	
	mHandler.post(new Runnable() {
        public void run() {
        	
        	List<Coordinate> result=coordinates;
        	if( result.size()!=0){
        		int id = StaticFields.getAid();
        		DataBaseHandler db = new DataBaseHandler(RouteActivity.this);
        		AssetData data = db.getAssetDetails(id);
        		double speedLimit=Double.parseDouble(data.getSpeedLimit());
        	ArrayList<LatLng> latlngList = new ArrayList<LatLng>();
        
        	for (int a = 0; a < result.size(); a++) {
        
        		LatLng latlng = new LatLng(Double.parseDouble(result.get(a).Latitude),
						 Double.parseDouble(result.get(a).Longitude));
        		latlngList.add(latlng);
        		double speed=Double.parseDouble(result.get(a).Speed);
        		
        		if(speed>=speedLimit){
        			//overspeeding
        			if(isNotOverSpeeding){
        				LatLng temp = latlngList.get(latlngList.size()-1);
        				isNotOverSpeeding=false;
        				googleMap.addPolyline(new PolylineOptions()
        	            .addAll(latlngList)
        	            .width(3)
        	            .color(Color.RED));
        				latlngList.clear();
        				latlngList.add(temp);
        			}
        			isOverSpeeding=true;
        			
        		}
        		else{
        			if(isOverSpeeding){
        				LatLng temp = latlngList.get(latlngList.size()-1);
        				isOverSpeeding=false;
        				googleMap.addPolyline(new PolylineOptions()
        	            .addAll(latlngList)
        	            .width(3)
        	            .color(Color.BLUE));
        				latlngList.clear();
        				latlngList.add(temp);
        			}
        				isNotOverSpeeding=true;
        		}

        	}
        	if(isNotOverSpeeding){
        	googleMap.addMarker(new MarkerOptions()
            .position(latlngList.get(0))
            .draggable(false));
        	googleMap.addPolyline(new PolylineOptions()
            .addAll(latlngList)
            .width(3)
            .color(Color.RED));}
        	else if(isOverSpeeding){
        		googleMap.addMarker(new MarkerOptions()
                .position(latlngList.get(0))
                .draggable(false));
            	googleMap.addPolyline(new PolylineOptions()
                .addAll(latlngList)
                .width(3)
                .color(Color.BLUE));	
        	}
        	googleMap.addMarker(new MarkerOptions()
            .position(latlngList.get(latlngList.size()-1))
            .draggable(false));
        	}
        }});
	
	return coordinates;
}
protected void onPostExecute(List<Coordinate> result) {

	if (StaticFields.serviceStatus.equals("sucess")) {
	
	//	RenderTrack(result);
			

	

}



	

}

private void RenderTrack(List<Coordinate> result){
	for (int a = 0; a < result.size(); a++) {
		
		Coordinate coordmap1 = result.get(a);
		if (a == 0||result.size() -1 == a) {
				
//					addMarker(16,
//							Double.parseDouble(coordmap1.Latitude),
//									Double.parseDouble(coordmap1.Longitude)
							//);

				} else {
					Coordinate coordmap2 = result
							.get(a+1);

					 googleMap
							.addPolygon(new PolygonOptions()
									.add(new LatLng(
										 Double.parseDouble(coordmap1.Latitude),
										 Double.parseDouble(coordmap1.Longitude)),
											new LatLng(
													Double.parseDouble(coordmap2.Latitude),
													Double.parseDouble(coordmap2.Longitude)))
									.strokeColor(Color.RED)
									.fillColor(Color.RED));
				}

			}
}
@Override
protected void onPreExecute() {

	Toast.makeText(RouteActivity.this,
			"get history location", Toast.LENGTH_LONG).show();

}

@Override
protected void onProgressUpdate(Void... values) {

}

}

private Marker addMarker(int Uid, double Latitude, double Longitude
) {
// TODO Auto-generated method stub
MarkerOptions option;

option = new MarkerOptions().title("" + Uid)
		.position(new LatLng(Latitude, Longitude)).flat(true);

Marker m = googleMap.addMarker(option);

m.showInfoWindow();
return m;

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
// handler.postDelayed(handlerTask, 9000);

}

@Override
public void onDestroy() {
super.onDestroy();
mapView.onDestroy();
// handler.removeCallbacks(handlerTask);

}

@Override
public void onLowMemory() {
super.onLowMemory();
mapView.onLowMemory();
}


}

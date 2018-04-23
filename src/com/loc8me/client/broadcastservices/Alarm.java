package com.loc8me.client.broadcastservices;

import java.io.InputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.loc8me.client.R;
import com.loc8me.client.datastore.AssetData;
import com.loc8me.client.datastore.DataBaseHandler;
import com.loc8me.client.webservices.CoordinateDetails;
import com.loc8me.client.webservices.StaticFields;
import com.loc8me.client.webservices.WebService;

public class Alarm extends BroadcastReceiver
		implements
		ConnectionCallbacks,
		OnConnectionFailedListener,
		LocationListener,
		com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks,
		com.google.android.gms.location.LocationListener {
	String status;
	String Response;
	Context context;
	long minutes;
	double Lat, Lng;
	LocationClient lcClient;
	ArrayList<CoordinateDetails> coordinateDetails = new ArrayList<CoordinateDetails>();
	int level = 0;
	String BatteryLevel;
	static String file = "Buffer";
	WebService obj = new WebService();
	ArrayList<Buffer> bufferObj = new ArrayList<Buffer>();
	SharedPreferences myPrefs;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			this.context = context;

			if (haveNetworkConnection()) {

				if (isStaticPosition()) {
					myPrefs = PreferenceManager
							.getDefaultSharedPreferences(context);
					String Latitude = myPrefs.getString("Latitude", null);
					String Longitude = myPrefs.getString("Longitude", null);
					Lat = Double.parseDouble(Latitude);
					Lng = Double.parseDouble(Longitude);

					if (Latitude != null && Longitude != null) {
						AsyncCallGeoFencingCoordinatesWS task = new AsyncCallGeoFencingCoordinatesWS();
						task.execute(Latitude, Longitude);
					}
				} else {
					lcClient = new LocationClient(
							context.getApplicationContext(), this, this);
					lcClient.connect();

				}
			}
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			PowerManager.WakeLock wl = pm.newWakeLock(
					PowerManager.PARTIAL_WAKE_LOCK, "");
			wl.acquire();
			wl.release();
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	  private static void generateNotification(Context context,String name, String message,Bitmap bitmap) {
//	    	PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//		            new Intent(context, Chat.class), 0);

		    NotificationCompat.Builder mBuilder =
		            new NotificationCompat.Builder(context)
		            .setSmallIcon(R.drawable.ic_launcher)
		          //  .setLargeIcon(bitmap)
		            .setContentTitle(name)
		            .setContentText(message);
		  //  mBuilder.setContentIntent(contentIntent);
		    mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		    mBuilder.setAutoCancel(true);
		    NotificationManager mNotificationManager =
		        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		    mNotificationManager.notify(1, mBuilder.build());
   

	    }
	 
	  public String getDistanceByDirectionService(double lat1, double lon1, double lat2, double lon2) {
	        String result_in_kms = "";
	        String url = "http://maps.google.com/maps/api/directions/xml?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric";
	        String tag[] = {"text"};
	        HttpResponse response = null;
	        try {
	            HttpClient httpClient = new DefaultHttpClient();
	            HttpContext localContext = new BasicHttpContext();
	            HttpPost httpPost = new HttpPost(url);
	            response = httpClient.execute(httpPost, localContext);
	            InputStream is = response.getEntity().getContent();
	            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	            Document doc = builder.parse(is);
	            if (doc != null) {
	                NodeList nl;
	                ArrayList args = new ArrayList();
	                for (String s : tag) {
	                    nl = doc.getElementsByTagName(s);
	                    if (nl.getLength() > 0) {
	                        Node distance = nl.item(nl.getLength() - 1);
	                        Node time = nl.item(nl.getLength() - 2);
	                        args.add(distance.getTextContent());
	                        args.add(time.getTextContent());
	                    } else {
	                        args.add(" - ");
	                    }
	                }
	                result_in_kms =String.valueOf(args.get(0)+" reach in "+ args.get(1));
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result_in_kms;
	      //  Float f=Float.valueOf(result_in_kms);
	       // return f*1000;
	    }
	public void SetAlarm(Context context) {

		int time = 10;
		time *= 10;
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, Alarm.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				100 * time * 1, pi); // Millisec * Second * Minute

	}

	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

	private class AsyncCallGeoFencingCoordinatesWS extends
			AsyncTask<String, Void, Void> {
		String output="";
		@Override
		protected Void doInBackground(String... params) {

			myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			String id = myPrefs.getString("UserId", "-1");

			if (id != "-1") {
				coordinateDetails = obj.GeoFencingCoordinates(id, params[0],
						params[1]);
				if (StaticFields.serviceStatus == "sucess") {
					for (CoordinateDetails cd : coordinateDetails) {
						float[] results = new float[1];
						// get distance in meter
					String r=	getDistanceByDirectionService(Lat, Lng,
								Double.parseDouble(cd.Latitude),
								Double.parseDouble(cd.Longitude));
						
//						Location.distanceBetween(Lat, Lng,
//								Double.parseDouble(cd.Latitude),
//								Double.parseDouble(cd.Longitude), results);
//						
					DataBaseHandler dbh = new DataBaseHandler(context);

					String name = dbh.getAssetName(cd.Aid);
					output+=name+" at distance "  + r;
						
//						Toast.makeText(context,
//								"distance of " + cd.Aid + " is " + results[0],
//								Toast.LENGTH_LONG).show();
					}
				

				}
			}

			else {
				Response = "Cache Clear";

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			
			try {
				generateNotification(context ,"Loc8me", ""+ output ,null);

			} catch (Exception ex) {

				Log.e("TAG", ex.toString());
			}
		}

		@Override
		protected void onPreExecute() {

			// Toast.makeText(context, "Sending...", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

	}

	public double getDistance(LatLng LatLng1, LatLng LatLng2) {
		double distance = 0;
		Location locationA = new Location("A");
		locationA.setLatitude(LatLng1.latitude);
		locationA.setLongitude(LatLng1.longitude);
		Location locationB = new Location("B");
		locationB.setLatitude(LatLng2.latitude);
		locationB.setLongitude(LatLng2.longitude);
		distance = locationA.distanceTo(locationB);
		return distance;

	}

	private boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

		currentLocation();

		LocationRequest cLocationRequest = LocationRequest.create();
		cLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		cLocationRequest.setInterval(3000);
		cLocationRequest.setFastestInterval(1000);
		lcClient.requestLocationUpdates(cLocationRequest, this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	public void currentLocation() {
		Location CurrentLocation = lcClient.getLastLocation();
		if (!haveNetworkConnection()) {
			Toast.makeText(context, "Unable to trace current location",
					Toast.LENGTH_SHORT).show();
		} else {
			String Latitude = "" + CurrentLocation.getLatitude();
			String Longitude = "" + CurrentLocation.getLongitude();
			Lat = Double.parseDouble(Latitude);
			Lng = Double.parseDouble(Longitude);
			AsyncCallGeoFencingCoordinatesWS task = new AsyncCallGeoFencingCoordinatesWS();
			task.execute(Latitude, Longitude);
		}
	}

	private boolean isStaticPosition() {
		myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		return myPrefs.getBoolean("isStatic", false);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}

}
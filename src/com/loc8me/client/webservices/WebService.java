package com.loc8me.client.webservices;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



public class WebService {

	private final String NAMESPACE = "http://tempuri.org/";
	private final String URL = "http://loc8app.azurewebsites.net/WebServices/WebService1.asmx";

	private final String ClientLoginSOAP_ACTION = "http://tempuri.org/ClientLogin";
	private final String ClientLoginMETHOD_NAME = "ClientLogin";
	private final String getAssetsLocationSOAP_ACTION = "http://tempuri.org/getAssetsLocation";
	private final String getAssetsLocationMETHOD_NAME = "getAssetsLocation";

	private final String getAssetsHistoryLocationSOAP_ACTION = "http://tempuri.org/getAssetsHistoryLocation";
	private final String getAssetsHistoryLocationMETHOD_NAME = "getAssetsHistoryLocation";

	private final String getAssetsDetailsSOAP_ACTION = "http://tempuri.org/getAssetsDetails";
	private final String getAssetsDetailsMETHOD_NAME = "getAssetsDetails";

	private final String GeoFencingCoordinatesSOAP_ACTION = "http://tempuri.org/GeoFencingCoordinates";
	private final String GeoFencingCoordinatesMETHOD_NAME = "GeoFencingCoordinates";

	public String Result;
	public String timeOutResult;
	public ArrayList<CoordinateDetails> GeoFencingCoordinates(String id,String Latitude,String Longitude) {
		ArrayList<CoordinateDetails> CoordinatesDetails = new ArrayList<CoordinateDetails>();
		try {
			SoapObject request = new SoapObject(NAMESPACE,
					GeoFencingCoordinatesMETHOD_NAME);

			PropertyInfo addnumber = new PropertyInfo();
			addnumber.setName("Uid");
			addnumber.setValue(id);
			addnumber.setType(String.class);
			request.addProperty(addnumber);

			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;

			envelope.setOutputSoapObject(request);
			envelope.addMapping(NAMESPACE, "CoordinateDetails",
					new CoordinateDetails().getClass());

			HttpTransportSE httpTransport = new HttpTransportSE(URL);
			httpTransport.call(GeoFencingCoordinatesSOAP_ACTION, envelope);
			SoapObject response = (SoapObject) envelope.getResponse();

			int count = response.getPropertyCount();

			for (int i = 0; i < count; i++) {
				SoapObject responseChild = (SoapObject) response.getProperty(i);
				CoordinateDetails coordinateDetails = new CoordinateDetails();
				coordinateDetails.Aid = Integer.parseInt(responseChild
						.getProperty(0).toString());
				coordinateDetails.Latitude = responseChild.getProperty(1)
						.toString();
				coordinateDetails.Longitude = responseChild.getProperty(2)
						.toString();
				coordinateDetails.DateTime = responseChild.getProperty(3)
						.toString();
				CoordinatesDetails.add(coordinateDetails);
			}

			StaticFields.serviceStatus = "sucess";
			return CoordinatesDetails;

		} catch (Exception e) {
			StaticFields.serviceStatus = e.getMessage();
			return null;
		}

	}

	
	public ClientLoginInfo ClientLogin(String Email, String Password) {
		ClientLoginInfo clientLoginInfo = new ClientLoginInfo();
		try {
			
		SoapObject request = new SoapObject(NAMESPACE, ClientLoginMETHOD_NAME);
		PropertyInfo EmailId = new PropertyInfo();
		EmailId.setName("Email");
		EmailId.setValue(Email);
		EmailId.setType(String.class);
		request.addProperty(EmailId);
		PropertyInfo password = new PropertyInfo();
		password.setName("Password");
		password.setValue(Password);
		password.setType(String.class);
		request.addProperty(password);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		envelope.addMapping(NAMESPACE, "ClientLoginInfo",
				new ClientLoginInfo().getClass());
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		
			androidHttpTransport.call(ClientLoginSOAP_ACTION, envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			clientLoginInfo.Uid= Integer.parseInt(response.getProperty(0).toString());
			clientLoginInfo.Type=response.getPropertyAsString(1);
			clientLoginInfo.Latitude=response.getPropertyAsString(2);
			clientLoginInfo.Longitude=response.getPropertyAsString(3);
            StaticFields.serviceStatus="sucess";
		} catch (Exception e) {
            StaticFields.serviceStatus="failed";
		}
		return clientLoginInfo;
	}

	public ArrayList<CoordinateDetails> getAssetsLocation(int Uid) {
		ArrayList<CoordinateDetails> CoordinatesDetails = new ArrayList<CoordinateDetails>();
		try {
			SoapObject request = new SoapObject(NAMESPACE,
					getAssetsLocationMETHOD_NAME);

			PropertyInfo number = new PropertyInfo();
			number.setName("Uid");
			number.setValue(Uid);
			number.setType(Integer.class);
			request.addProperty(number);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;

			envelope.setOutputSoapObject(request);
			envelope.addMapping(NAMESPACE, "CoordinateDetails",
					new CoordinateDetails().getClass());

			HttpTransportSE httpTransport = new HttpTransportSE(URL);
			httpTransport.call(getAssetsLocationSOAP_ACTION, envelope);
			SoapObject response = (SoapObject) envelope.getResponse();

			int count = response.getPropertyCount();

			for (int i = 0; i < count; i++) {
				SoapObject responseChild = (SoapObject) response.getProperty(i);
				CoordinateDetails coordinateDetails = new CoordinateDetails();
				coordinateDetails.Aid = Integer.parseInt(responseChild
						.getProperty(0).toString());
				coordinateDetails.Latitude = responseChild.getProperty(1)
						.toString();
				coordinateDetails.Longitude = responseChild.getProperty(2)
						.toString();
				coordinateDetails.DateTime = responseChild.getProperty(3)
						.toString();
				CoordinatesDetails.add(coordinateDetails);
			}

			StaticFields.serviceStatus = "sucess";
			return CoordinatesDetails;

		} catch (Exception e) {
			StaticFields.serviceStatus = e.getMessage();
			return null;
		}

	}

	public ArrayList<AssetsDetails> getAssetsDetails(int Uid) {
		ArrayList<AssetsDetails> assetsDetails = new ArrayList<AssetsDetails>();
		try {
			SoapObject request = new SoapObject(NAMESPACE,
					getAssetsDetailsMETHOD_NAME);

			PropertyInfo number = new PropertyInfo();
			number.setName("Uid");
			number.setValue(Uid);
			number.setType(String.class);
			request.addProperty(number);

			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;

			envelope.setOutputSoapObject(request);
			envelope.addMapping(NAMESPACE, "AssetsDetails",
					new AssetsDetails().getClass());
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
			
				androidHttpTransport.call(getAssetsDetailsSOAP_ACTION, envelope);
				SoapObject response = (SoapObject) envelope.getResponse();
				
		


			
			
			
			
			StaticFields.serviceStatus=""+response;
		int count = response.getPropertyCount();

			for (int i = 0; i < count; i++) {
				SoapObject responseChild = (SoapObject) response.getProperty(i);
				AssetsDetails assetDetails = new AssetsDetails();
				assetDetails.Aid = Integer.parseInt(responseChild
						.getProperty(0).toString());
			assetDetails.Name = responseChild.getProperty(1).toString();
				assetDetails.StartTime = responseChild.getProperty(2)
						.toString();
				assetDetails.StopTime = responseChild.getProperty(3).toString();
				assetDetails.RefreshRate = responseChild.getProperty(4)
						.toString();
				assetDetails.SpeedLimit = responseChild.getProperty(5)
						.toString();
				assetDetails.TrackingDays = responseChild.getProperty(6)
						.toString();
				assetDetails.PNumber = responseChild.getProperty(7).toString();
				assetsDetails.add(assetDetails);
			}

			StaticFields.serviceStatus = "sucess";
			return assetsDetails;

		} catch (Exception e) {
			StaticFields.serviceStatus = e.toString();
			return null;
		}
	}

	public ArrayList<Coordinate> getAssetsHistoryLocation(int Uid, int Aid,
			String FromTime, String ToTime) {
		ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
		try {
			SoapObject request = new SoapObject(NAMESPACE,
					getAssetsHistoryLocationMETHOD_NAME);

			PropertyInfo numberprop = new PropertyInfo();
			numberprop.setName("Uid");
			numberprop.setValue(Uid);
			numberprop.setType(Integer.class);
			request.addProperty(numberprop);

			PropertyInfo aidprop = new PropertyInfo();
			aidprop.setName("Aid");
			aidprop.setValue(Aid);
			aidprop.setType(Integer.class);
			request.addProperty(aidprop);

			PropertyInfo FromTimeprop = new PropertyInfo();
			FromTimeprop.setName("FromTime");
			FromTimeprop.setValue(FromTime);
			FromTimeprop.setType(String.class);
			request.addProperty(FromTimeprop);

			PropertyInfo ToTimeprop = new PropertyInfo();
			ToTimeprop.setName("ToTime");
			ToTimeprop.setValue(ToTime);
			ToTimeprop.setType(String.class);
			request.addProperty(ToTimeprop);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;

			envelope.setOutputSoapObject(request);
			envelope.addMapping(NAMESPACE, "Coordinate",
					new Coordinate().getClass());

			HttpTransportSE httpTransport = new HttpTransportSE(URL);
			httpTransport.call(getAssetsHistoryLocationSOAP_ACTION, envelope);
			SoapObject response = (SoapObject) envelope.getResponse();

			int count = response.getPropertyCount();

			for (int i = 0; i < count; i++) {
				SoapObject responseChild = (SoapObject) response.getProperty(i);
				Coordinate coordinate = new Coordinate();
				coordinate.Latitude = responseChild.getProperty(0).toString();
				coordinate.Longitude = responseChild.getProperty(1).toString();
				coordinate.Speed = responseChild.getProperty(2).toString();
				coordinates.add(coordinate);
			}

			StaticFields.serviceStatus = "sucess";
			return coordinates;

		} catch (Exception e) {
			StaticFields.serviceStatus = e.getMessage();
			return null;
		}

	}

}

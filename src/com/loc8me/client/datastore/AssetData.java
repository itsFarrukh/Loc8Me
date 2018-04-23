package com.loc8me.client.datastore;

public class AssetData {
	private int id;
	private String Name;
	private String StartTime;
	private String StopTime;
	private String TimeSpan;
	private String SpeedLimit;
	private String TrackingDays;
	private String PNumber;
	

	public AssetData() {

	}

	public AssetData(int  id,String name,String startTime,String stopTime,String timeSpan,String speedLimit,String trackingDays,String pNumber )
    {
    	this.id=id;
    	this.Name=name;
    	this.StartTime=startTime;
    	this.StopTime=stopTime;
    	this.TimeSpan=timeSpan;
    	this.SpeedLimit=speedLimit;
    	this.TrackingDays=trackingDays;
    	this.PNumber=pNumber;

    }


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getStopTime() {
		return StopTime;
	}

	public void setStopTime(String stopTime) {
		StopTime = stopTime;
	}

	public String getTimeSpan() {
		return TimeSpan;
	}

	public void setTimeSpan(String timeSpan) {
		TimeSpan = timeSpan;
	}

	public String getSpeedLimit() {
		return SpeedLimit;
	}

	public void setSpeedLimit(String speedLimit) {
		SpeedLimit = speedLimit;
	}

	public String getTrackingDays() {
		return TrackingDays;
	}

	public void setTrackingDays(String trackingDays) {
		TrackingDays = trackingDays;
	}

	public String getPNumber() {
		return PNumber;
	}

	public void setPNumber(String pNumber) {
		PNumber = pNumber;
	}
}

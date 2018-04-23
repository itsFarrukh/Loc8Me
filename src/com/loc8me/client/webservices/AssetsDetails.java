package com.loc8me.client.webservices;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class AssetsDetails implements KvmSerializable
{
    public int Aid;
    public String Name;
    public String StartTime;
    public String StopTime;
    public String RefreshRate;
    public String SpeedLimit;
    public String TrackingDays;
    public String PNumber;
	

    public AssetsDetails() { }
    public AssetsDetails(int  aid,String name,String startTime,String stopTime,String refreshRate,String speedLimit,String trackingDays,String pNumber )
    {
    	this.Aid=aid;
    	this.Name=name;
    	this.StartTime=startTime;
    	this.StopTime=stopTime;
    	this.RefreshRate=refreshRate;
    	this.SpeedLimit=speedLimit;
    	this.TrackingDays=trackingDays;
    	this.PNumber=pNumber;

    }
        @Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
	    switch(arg0)
        {
        case 0:
        	return this.Aid;
        case 1:
        	return this.Name;
        case 2:
        	return this.StartTime;
        case 3:
        	return this.StopTime;
        case 4:
        	return this.RefreshRate;
        case 5:
        	return this.SpeedLimit;
        case 6:
        	return this.TrackingDays;
        case 7:
        	return this.PNumber;
        }
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 8;
	}


	@Override
	  public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
			// TODO Auto-generated method stub
		switch(index)
      {
      case 0:
          info.type = PropertyInfo.INTEGER_CLASS;
          info.name = "Aid";
          break;
      case 1:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "Name";
      case 2:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "StartTime";
          break;
      case 3:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "StopTime";
          break;
      case 4:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "RefreshRate";
          break;
      case 5:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "SpeedLimit";
          break;
      case 6:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "TrackingDays";
          break;
      case 7:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "PNumber";
          break;

      default:break;
      
      }
		
	}

	@Override
	   public void setProperty(int index, Object value) {
      switch(index)
      {
      case 0:
      	this.Aid=Integer.parseInt(value.toString());
      case 1:
        this.Name=value.toString();
      case 2:
      	this.StartTime=value.toString();
      case 3:
      	this.StopTime=value.toString();
      case 4:
      	this.RefreshRate=value.toString();
      case 5:
      	this.SpeedLimit=value.toString();
      case 6:
        this.TrackingDays=value.toString();
      case 7:
        this.PNumber=value.toString();
      }	
	}

}


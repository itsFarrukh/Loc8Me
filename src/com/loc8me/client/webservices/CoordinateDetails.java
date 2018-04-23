package com.loc8me.client.webservices;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class CoordinateDetails implements KvmSerializable
{
	public int Aid;
    public String Latitude;
    public String Longitude;
    public String DateTime; 
    public CoordinateDetails() { }
    public CoordinateDetails(int aid,String latitude, String longitude, String dateTime)
    {
        this.Aid = aid;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.DateTime = dateTime;

    }
        @Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
	    switch(arg0)
        {
        case 0:
        	return this.Aid;
        case 1:
        	return this.Latitude;
        case 2:
        	return this.Longitude;
        case 3:
        	return this.DateTime;
        }
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 4;
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
          info.name = "Latitude";
          break;
      case 2:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "Longitude";
          break;
      case 3:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "DateTime";
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
      	this.Latitude=value.toString();
      case 2:
        	this.Longitude=value.toString();
      case 3:
        	this.DateTime=value.toString();
      }	
	}

}

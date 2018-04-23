package com.loc8me.client.webservices;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class Coordinate implements KvmSerializable
{
    public String Latitude;
    public String Longitude;
    public String Speed;
    public Coordinate() { }
    public Coordinate(String latitude, String longitude,String speed)
    {
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Speed=speed;

    }
        @Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
	    switch(arg0)
        {
        case 0:
        	return this.Latitude;
        case 1:
        	return this.Longitude;
        case 2:
        	return this.Speed;
        }
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 3;
	}


	@Override
	  public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
			// TODO Auto-generated method stub
		switch(index)
      {
      case 0:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "Latitude";
          break;
      case 1:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "Longitude";
          break;
      case 2:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "Speed";
          break;
         
      default:break;
      
      }
		
	}

	@Override
	   public void setProperty(int index, Object value) {
      switch(index)
      {
      case 0:
      	this.Latitude=value.toString();
      case 1:
        	this.Longitude=value.toString();
      case 2:
      	this.Speed=value.toString();
      }	
	}

}


package com.loc8me.client.webservices;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class ClientLoginInfo implements KvmSerializable
{
	public int Uid;
	public String Type;
    public String Latitude;
    public String Longitude;
    public ClientLoginInfo() { }
    public ClientLoginInfo(int uid,String type,String latitude, String longitude)
    {
    	this.Uid=uid;
    	this.Type=type;
        this.Latitude = latitude;
        this.Longitude = longitude;

    }
        @Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
	    switch(arg0)
        {
        case 0:
        	return this.Uid;
        case 1:
        	return this.Type;
        case 2:
        	return this.Latitude;
        case 3:
        	return this.Longitude;
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
          info.name = "Uid";
          break;
      case 1:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "Type";
          break;
      case 2:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "Latitude";
          break;
      case 3:
          info.type = PropertyInfo.STRING_CLASS;
          info.name = "Longitude";
          break;
         
      default:break;
      
      }
		
	}

	@Override
	   public void setProperty(int index, Object value) {
      switch(index)
      {
      case 0:
        	this.Uid=Integer.parseInt(value.toString());
        case 1:
          	this.Type=value.toString();
      case 2:
      	this.Latitude=value.toString();
      case 3:
        	this.Longitude=value.toString();
      }	
	}

}


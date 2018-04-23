package com.loc8me.client.webservices;

import java.util.Hashtable;
import java.util.Vector;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class CoordinateDetailsList extends Vector<CoordinateDetails> implements
		KvmSerializable {

	String n1 = "http://tempuri.org/";

	@Override
	public Object getProperty(int arg0) {
		return this.get(arg0);
	}

	@Override
	public int getPropertyCount() {
		return this.size();
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub
		arg2.setType(CoordinateDetails.class);
		arg2.setName("CoordinateDetails");

		arg2.setNamespace(n1);

	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		this.add((CoordinateDetails) arg1);
	}

}

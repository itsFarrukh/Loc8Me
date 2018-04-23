package com.loc8me.client.webservices;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class CoordinateList extends Vector<Coordinate> implements
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
arg2.setType(Coordinate.class);
arg2.setName("Coordinate");

arg2.setNamespace(n1);

}

@Override
public void setProperty(int arg0, Object arg1) {
this.add((Coordinate) arg1);
}

}

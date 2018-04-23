package com.loc8me.client.webservices;

public class StaticFields {
public static String serviceStatus;
private static int Aid;
public static String FromTrack;
public static String ToTrack;

public  static void setData(int aid) {

	Aid = aid;
	

}

public static int getAid() {
	return Aid;
}
}

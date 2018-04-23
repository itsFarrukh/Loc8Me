package com.loc8me.client.datastore;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
 
	private static final String DATABASE_NAME = "AssetData";
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "NAME";
	private static final String KEY_START_TIME = "StartTime";
	private static final String KEY_STOP_TIME = "StopTime";
	private static final String KEY_TIMESPAN = "TimeSpan";
	private static final String KEY_SPEEDLIMIT = "SpeedLimit";
	private static final String KEY_TRACKINGDAYS = "TrackingDays";
	private static final String KEY_PNUMBER = "PNumber";
	private static final String Table_Data = "AssetDetails";

	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_ASSET_DATA_TABLE = "CREATE TABLE " + Table_Data + "("
				+ KEY_ID + " integer ," + KEY_NAME
				+ " TEXT," + KEY_START_TIME + " TEXT," + KEY_STOP_TIME + " TEXT,"
				+ KEY_TIMESPAN + " TEXT," +KEY_SPEEDLIMIT + " TEXT," +KEY_TRACKINGDAYS + " TEXT," +KEY_PNUMBER + " TEXT" +
				")";
		db.execSQL(CREATE_ASSET_DATA_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL("DROP TABLE IF EXISTS " + Table_Data);
		onCreate(db);
	}

	public long addData(AssetData data) {
	
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ID, data.getId());
		values.put(KEY_NAME, data.getName());
		values.put(KEY_START_TIME, data.getStartTime());
		values.put(KEY_STOP_TIME, data.getStopTime());
		values.put(KEY_TIMESPAN, data.getTimeSpan());
		values.put(KEY_SPEEDLIMIT, data.getSpeedLimit());
		values.put(KEY_TRACKINGDAYS, data.getTrackingDays());
		values.put(KEY_PNUMBER, data.getPNumber());

	 long i=	db.insert(Table_Data, null, values);
		db.close();
		return i;
		
	}
	public String getAssetName(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(Table_Data, new String[] {KEY_NAME }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		
		return	cursor.getString(0);
		
	}

	public AssetData getAssetDetails(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(Table_Data, new String[] { KEY_ID, KEY_NAME,
				KEY_START_TIME, KEY_STOP_TIME, KEY_TIMESPAN,KEY_SPEEDLIMIT,KEY_TRACKINGDAYS,KEY_PNUMBER }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		AssetData data = new AssetData(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
		// return contact
		return data;
	}

	public List<AssetData> getAssetList() {
		List<AssetData> AssetDataList = new ArrayList<AssetData>();
		// Select All Query
//		String selectQuery = "SELECT  * FROM " + Table_Data;

		SQLiteDatabase db = this.getWritableDatabase();

		
	//	Cursor cursor = db.rawQuery(selectQuery, null);

		Cursor cursor = db.query(Table_Data, new String[] { KEY_ID, KEY_NAME }, null,
				 null, null, null, null);
	
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				AssetData data = new AssetData();
				data.setId(Integer.parseInt(cursor.getString(0)));
				data.setName(cursor.getString(1));
				// Adding contact to list
				AssetDataList.add(data);
			} while (cursor.moveToNext());
		}

		// return contact list
		return AssetDataList;
	}
}
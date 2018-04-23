package com.loc8me.client;

import com.loc8me.client.datastore.DataBaseHandler;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		try {
			DataBaseHandler db = new DataBaseHandler(this);
			Toast.makeText(this, "DB created", Toast.LENGTH_SHORT).show();
		} catch (Exception ex) {

			Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

}

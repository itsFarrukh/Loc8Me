package com.loc8me.client;

import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loc8me.client.datastore.AssetData;
import com.loc8me.client.datastore.DataBaseHandler;
import com.loc8me.client.webservices.StaticFields;

public class Fragment_AssetList extends Fragment {

	ListView listView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_assetlist,
				container, false);
		DataBaseHandler dbh = new DataBaseHandler(this.getActivity());

		final List<AssetData> assetslist = dbh.getAssetList();
		String[] listitems = new String[assetslist.size()];
		int count = 0;
		for (AssetData ad : assetslist)
			listitems[count++] = ad.getName();
	
		listView = (ListView) rootView.findViewById(R.id.listView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, listitems);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int Aid = assetslist.get(position).getId();
				Toast.makeText(getActivity(), "" + Aid, Toast.LENGTH_SHORT)
						.show();
				StaticFields.setData(Aid);
				Intent i = new Intent(getActivity(), UserInfoActivity.class);
				startActivity(i);

			}

		});
		return rootView;
	}

}

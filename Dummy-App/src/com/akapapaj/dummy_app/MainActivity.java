package com.akapapaj.dummy_app;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	static final String[] myUrls = new String[] { "http://www.yahoo.com",
			"http://www.google.com", "http://www.youtube.com" };
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// setContentView(R.layout.activity_main);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_main,
				myUrls));

		ListView j_lv = getListView();
		j_lv.setTextFilterEnabled(true);

		j_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// what to do when clicked //
				url = ((TextView) view).getText().toString();

				Intent intent = new Intent(
						"com.akapapaj.j_browser.LAUNCH_TEST", Uri.parse(url));
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}

		});
	}
}

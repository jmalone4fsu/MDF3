package com.akapapaj.java2_proj2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.akapapaj.java2_proj2.service.ItemsDownloaderService;

public class MainActivity extends FragmentActivity implements
		ProductsFragment.OnItemSelectedListener {
	Button button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	public void onClick(View v) {
		Intent intent = new Intent(this, ItemsDownloaderService.class);
		intent.setData(Uri
				.parse("http://www.geek.com/articles/apple-picks/feed?format=xml"));
		startService(intent);
	}

	@Override
	public void onItemSelected(String itemUrl) {
		DetailsFragment viewer = (DetailsFragment) getSupportFragmentManager()
				.findFragmentById(R.id.details_fragment);

		if (viewer == null || !viewer.isInLayout()) {
			Intent showContent = new Intent(getApplicationContext(),
					DetailsActivity.class);
			showContent.setData(Uri.parse(itemUrl));
			Log.w("URL", itemUrl);
			startActivity(showContent);
		} else {
			viewer.updateUrl(itemUrl);
		}

	}

}

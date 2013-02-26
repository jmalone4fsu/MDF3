package com.akapapaj.java2_proj2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class DetailsActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_fragment);

		Intent launchingIntent = getIntent();
		String content = launchingIntent.getData().toString();

		DetailsFragment viewer = (DetailsFragment) getSupportFragmentManager()
				.findFragmentById(R.id.details_fragment);

		viewer.updateUrl(content);

	}
}

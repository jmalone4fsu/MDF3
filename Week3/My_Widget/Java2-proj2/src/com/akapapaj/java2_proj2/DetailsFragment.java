package com.akapapaj.java2_proj2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class DetailsFragment extends Fragment {
	private WebView viewer = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewer = (WebView) inflater.inflate(R.layout.item_view, container,
				false);
		WebSettings settings = viewer.getSettings();
		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);
		return viewer;
	}

	public void updateUrl(String newUrl) {
		if (viewer != null) {
			viewer.loadUrl(newUrl);
		}
	}
}
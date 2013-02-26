package com.akapapaj.java2_proj2.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.akapapaj.java2_proj2.data.PapaProvider;
import com.akapapaj.java2_proj2.data.PapasDatabase;
import com.akapapaj.java2_proj2.my_widget.MyWidgetProvider;

public class ItemsDownloaderService extends Service {
	private static final String DEBUG_TAG = "ItemsDownloaderService";
	private DownloaderTask itemsDownloader;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		URL itemsPath;
		try {
			itemsPath = new URL(intent.getDataString());
			itemsDownloader = new DownloaderTask();
			itemsDownloader.execute(itemsPath);
		} catch (MalformedURLException e) {
			Log.e(DEBUG_TAG, "Bad Url", e);
		}
		return Service.START_FLAG_REDELIVERY;
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	private class DownloaderTask extends AsyncTask<URL, Void, Boolean> {
		private static final String DEBUG_TAG = "ItemsDownloaderService$DownloaderTask";

		@Override
		protected Boolean doInBackground(URL... params) {
			boolean succeeded = false;
			URL downloadPath = params[0];
			if (downloadPath != null) {
				succeeded = xmlParse(downloadPath);
			}
			return succeeded;
		}

		@SuppressLint("SimpleDateFormat")
		private boolean xmlParse(URL downloadPath) {
			boolean succeeded = false;
			XmlPullParser myItems;
			try {
				myItems = XmlPullParserFactory.newInstance().newPullParser();
				myItems.setInput(downloadPath.openStream(), null);
				int eventType = -1;

				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {
						String tagName = myItems.getName();
						if (tagName.equals("item")) {

							ContentValues itemsData = new ContentValues();
							// loop to find title and link
							while (eventType != XmlPullParser.END_DOCUMENT) {
								if (eventType == XmlPullParser.START_TAG) {
									if (myItems.getName().equals("link")) {
										myItems.next();
										Log.d(DEBUG_TAG,
												"Link:" + myItems.getText());
										itemsData.put(PapasDatabase.COL_URL,
												myItems.getText());
									} else if (myItems.getName()
											.equals("title")) {
										myItems.next();
										itemsData.put(
												PapasDatabase.COL_ITEMNAME,
												myItems.getText());

									} else if (myItems.getName().equals(
											"pubDate")) {
										myItems.next();
										DateFormat parser = new SimpleDateFormat(
												"E, dd MMM yyyy");
										try {
											Date date = parser.parse(myItems
													.getText());
											itemsData.put(
													PapasDatabase.COL_DATE,
													date.getTime() / 1000);
										} catch (ParseException e) {
											Log.e(DEBUG_TAG,
													"Error parsing date: "
															+ myItems.getText());
										}
									}
								} else if (eventType == XmlPullParser.END_TAG) {
									if (myItems.getName().equals("item")) {
										// save data and continue //
										getContentResolver().insert(
												PapaProvider.CONTENT_URI,
												itemsData);
										break;
									}
								}
								eventType = myItems.next();
							}
						}
					}
					eventType = myItems.next();
				}
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return succeeded;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			Context context = ItemsDownloaderService.this
					.getApplicationContext();
			MyWidgetProvider.updateWidgetContent(context,
					AppWidgetManager.getInstance(context));
			stopSelf();
		}
	}

}

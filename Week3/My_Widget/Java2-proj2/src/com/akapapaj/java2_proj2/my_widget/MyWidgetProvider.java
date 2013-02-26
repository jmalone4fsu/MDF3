package com.akapapaj.java2_proj2.my_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.akapapaj.java2_proj2.MainActivity;
import com.akapapaj.java2_proj2.R;
import com.akapapaj.java2_proj2.data.PapaProvider;
import com.akapapaj.java2_proj2.data.PapasDatabase;

public class MyWidgetProvider extends AppWidgetProvider {

	public static final String DEBUG_TAG = "MyWidgetProvider";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// Get newest item from the database
		try {
			updateWidgetContent(context, appWidgetManager);
		} catch (Exception e) {
			Log.e(DEBUG_TAG, "Failed", e);
		}
	}

	public static void updateWidgetContent(Context context,
			AppWidgetManager appWidgetManager) {
		String newestTitle = "Not Available";
		String[] projection = { PapasDatabase.COL_ITEMNAME };
		Uri content = PapaProvider.CONTENT_URI;
		Cursor cursor = context.getContentResolver().query(content, projection,
				null, null, PapasDatabase.COL_DATE + " desc LIMIT 1");
		if (cursor.moveToFirst()) {
			newestTitle = cursor.getString(0);
			Log.d("NewestItem", newestTitle);
		}
		cursor.close();
		// Update the widget
		RemoteViews remoteView = new RemoteViews(context.getPackageName(),
				R.layout.mywidget_layout);
		remoteView.setTextViewText(R.id.title, newestTitle);

		// handle click
		Intent handleClick = new Intent(context, MainActivity.class);
		PendingIntent pending = PendingIntent.getActivity(context, 0,
				handleClick, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteView.setOnClickPendingIntent(R.id.full_widget, pending);

		// get Component name
		ComponentName myWidget = new ComponentName(context,
				MyWidgetProvider.class);
		appWidgetManager.updateAppWidget(myWidget, remoteView);
	}
}

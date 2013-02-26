package com.akapapaj.java2_proj2;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.akapapaj.java2_proj2.data.PapaProvider;
import com.akapapaj.java2_proj2.data.PapasDatabase;
import com.akapapaj.java2_proj2.service.ItemsDownloaderService;

public class ProductsFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private OnItemSelectedListener itemSelectedListener;
	private SimpleCursorAdapter adapter;
	private static final int ITEMS_LIST_LOADER = 0x01;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] uiBindFrom = { PapasDatabase.COL_ITEMNAME,
				PapasDatabase.COL_DATE };
		int[] uiBindTo = { R.id.title, R.id.date };
		getLoaderManager().initLoader(ITEMS_LIST_LOADER, null, this);
		adapter = new SimpleCursorAdapter(
				getActivity().getApplicationContext(), R.layout.list_item,
				null, uiBindFrom, uiBindTo,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		adapter.setViewBinder(new ItemsViewBinder());
		setListAdapter(adapter);

	}

	public void onClick(View v) {
		Intent intent = new Intent(getActivity().getApplicationContext(),
				ItemsDownloaderService.class);
		intent.setData(Uri
				.parse("http://www.geek.com/articles/apple-picks/feed?format=xml"));
		getActivity().startService(intent);
	}

	public interface OnItemSelectedListener {
		public void onItemSelected(String itemUrl);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String projection[] = { PapasDatabase.COL_URL };
		Cursor itemCursor = getActivity().getContentResolver().query(
				Uri.withAppendedPath(PapaProvider.CONTENT_URI,
						String.valueOf(id)), projection, null, null, null);
		if (itemCursor.moveToFirst()) {
			String itemUrl = itemCursor.getString(0);
			itemSelectedListener.onItemSelected(itemUrl);
		}
		itemCursor.close();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			itemSelectedListener = (OnItemSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ "must implement OnItemSelectedListener");
		}
	}

	// Methods for LoaderManager.LoaderCallbacks //

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { PapasDatabase.ID, PapasDatabase.COL_ITEMNAME,
				PapasDatabase.COL_DATE };
		Uri content = PapaProvider.CONTENT_URI;
		CursorLoader cursorLoader = new CursorLoader(getActivity(), content,
				projection, null, null, PapasDatabase.COL_DATE + " desc");

		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);

	}

	private class ItemsViewBinder implements SimpleCursorAdapter.ViewBinder {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int index) {
			if (index == cursor.getColumnIndex(PapasDatabase.COL_DATE)) {
				DateFormat formatter = android.text.format.DateFormat
						.getDateFormat(getActivity().getApplicationContext());
				long date = cursor.getLong(index);
				Date dateObj = new Date(date * 1000);
				((TextView) view).setText(formatter.format(dateObj));
				return true;
			} else {
				return false;
			}
		}

	}
}

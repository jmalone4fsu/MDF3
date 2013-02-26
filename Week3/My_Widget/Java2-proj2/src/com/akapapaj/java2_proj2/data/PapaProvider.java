package com.akapapaj.java2_proj2.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class PapaProvider extends ContentProvider {
	private PapasDatabase pDB;
	private static final String DEBUG_TAG = "PapasProvider";

	// Helper Constants //
	private static final String AUTHORITY = "com.akapapaj.java2_proj2.data.PapaProvider";
	public static final int ITEMS = 100;
	public static final int ITEM_ID = 110;
	private static final String BASE_PATH = "items";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/mt-tutorial";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/mt-tutorial";

	private static final UriMatcher pURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		pURIMatcher.addURI(AUTHORITY, BASE_PATH, ITEMS);
		pURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ITEM_ID);
	}

	@Override
	public boolean onCreate() {
		pDB = new PapasDatabase(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(PapasDatabase.TABLE_ITEMS);
		int uriType = pURIMatcher.match(uri);
		switch (uriType) {
		case ITEM_ID:
			queryBuilder.appendWhere(PapasDatabase.ID + "="
					+ uri.getLastPathSegment());
			break;
		case ITEMS:
			// no filter show all //
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}
		Cursor cursor = queryBuilder.query(pDB.getReadableDatabase(),
				projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = pURIMatcher.match(uri);
		SQLiteDatabase sqlDB = pDB.getWritableDatabase();
		int rowsAffected = 0;
		switch (uriType) {
		case ITEMS:
			rowsAffected = sqlDB.delete(PapasDatabase.TABLE_ITEMS, selection,
					selectionArgs);
			break;
		case ITEM_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsAffected = sqlDB.delete(PapasDatabase.TABLE_ITEMS,
						PapasDatabase.ID + "=" + id, null);
			} else {
				rowsAffected = sqlDB.delete(PapasDatabase.TABLE_ITEMS,
						selection + " and " + PapasDatabase.ID + "=" + id,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = pURIMatcher.match(uri);
		switch (uriType) {
		case ITEMS:
			return CONTENT_TYPE;
		case ITEM_ID:
			return CONTENT_ITEM_TYPE;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = pURIMatcher.match(uri);
		if (uriType != ITEMS) {
			throw new IllegalArgumentException("Invalid URI for insert");
		}
		SQLiteDatabase sqlDB = pDB.getWritableDatabase();
		try {
			long newID = sqlDB.insertOrThrow(PapasDatabase.TABLE_ITEMS, null,
					values);
			if (newID > 0) {
				Uri newUri = ContentUris.withAppendedId(uri, newID);
				getContext().getContentResolver().notifyChange(uri, null);
				return newUri;
			} else {
				throw new SQLException("Failed to insert row into " + uri);
			}
		} catch (SQLiteConstraintException e) {
			Log.i(DEBUG_TAG, "Ignorning constraints failure.");
		}
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = pURIMatcher.match(uri);
		SQLiteDatabase sqlDB = pDB.getWritableDatabase();

		int rowsAffected;
		switch (uriType) {
		case ITEM_ID:
			String id = uri.getLastPathSegment();
			StringBuilder modSelection = new StringBuilder(PapasDatabase.ID
					+ "=" + id);
			if (!TextUtils.isEmpty(selection)) {
				modSelection.append(" AND " + selection);
			}
			rowsAffected = sqlDB.update(PapasDatabase.TABLE_ITEMS, values,
					modSelection.toString(), null);
			break;
		case ITEMS:
			rowsAffected = sqlDB.update(PapasDatabase.TABLE_ITEMS, values,
					selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}

}

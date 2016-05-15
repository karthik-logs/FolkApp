package com.karthyk.folkit.providers;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.karthyk.folkit.localstore.LocalStoreContract;
import com.karthyk.folkit.localstore.StoreContentHelper;

public class DefaultContentHelper extends StoreContentHelper {

  public DefaultContentHelper(UriMatcher uriMatcher, String authority) {
    super(uriMatcher, authority);
    uriMatcher.addURI(authority, "default" + "/*", 1);
  }

  @Override public int[] getSupportedRoutes() {
    return new int[]{1};
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                      String sortOrder) {
    int uriMatch = mUriMatcher.match(uri);
    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    String table;
    String id;
    Context ctx;
    Cursor result;
    switch (uriMatch) {
      case 1:
        id = uri.getLastPathSegment();
        selection = LocalStoreContract.Default.COLUMN_NAME_DATE + "=?";
        selectionArgs = new String[]{id};
        table = LocalStoreContract.Default.TABLE_NAME;
        result = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        ctx = getContext();
        assert ctx != null;
        result.setNotificationUri(ctx.getContentResolver(), uri);
        return result;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
  }

  @Override public String getType(Uri uri) {
    final int match = mUriMatcher.match(uri);
    switch (match) {
      case 1:
        return LocalStoreContract.Default.CONTENT_ITEM_TYPE;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
  }

  @Override public Uri insert(Uri uri, ContentValues contentValues) {
    SQLiteDatabase db = databaseHelper.getWritableDatabase();
    Uri result;
    assert db != null;
    final int match = mUriMatcher.match(uri);
    switch (match) {
      case 1:
        db.insertOrThrow(LocalStoreContract.Default.TABLE_NAME, null, contentValues);
        result = Uri.parse(LocalStoreContract.Default.CONTENT_URI + "/"
            + contentValues.get(LocalStoreContract.Default.COLUMN_NAME_DATE));
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    // sending broadcast to the registered content observers.
    Context ctx = getContext();
    assert ctx != null;
    ctx.getContentResolver().notifyChange(uri, null, false);
    return result;
  }

  @Override public int delete(Uri uri, String selection, String[] selectionArgs) {
    SQLiteDatabase db = databaseHelper.getWritableDatabase();
    int result;
    int match = mUriMatcher.match(uri);
    String table;
    switch (match) {
      case 1:
        table = LocalStoreContract.Default.TABLE_NAME;
        result = db.delete(table, selection, selectionArgs);
        break;
      default:
        throw new UnsupportedOperationException("Unsupported uri: " + uri);
    }
    // sending broadcast to the registered content observers.
    Context ctx = getContext();
    assert ctx != null;
    ctx.getContentResolver().notifyChange(uri, null, false);
    return result;
  }

  @Override public int update(Uri uri, ContentValues contentValues, String selection,
                              String[] selectionArgs) {
    SQLiteDatabase db = databaseHelper.getWritableDatabase();
    int match = mUriMatcher.match(uri);
    String table;
    int result;
    switch (match) {
      case 1:
        table = LocalStoreContract.Default.TABLE_NAME;
        result = db.update(table, contentValues, selection, selectionArgs);
        break;
      default:
        throw new UnsupportedOperationException("Unsupported uri: " + uri);
    }
    // sending broadcast to the registered content observers.
    Context ctx = getContext();
    assert ctx != null;
    ctx.getContentResolver().notifyChange(uri, null, false);
    return result;
  }
}

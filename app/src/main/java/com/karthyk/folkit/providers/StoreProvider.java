package com.karthyk.folkit.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.SparseArray;

import com.karthyk.folkit.localstore.LocalStoreContract;
import com.karthyk.folkit.localstore.StoreContentHelper;

import java.util.HashSet;
import java.util.Set;

public class StoreProvider extends ContentProvider {
  private static final String AUTHORITY = LocalStoreContract.CONTENT_AUTHORITY;
  private FolkDatabaseHelper mDatabaseHelper;
  private static final UriMatcher S_URIMATCHER = new UriMatcher(UriMatcher.NO_MATCH);

  private static final StoreContentHelper[] STORE_HELPERS = new StoreContentHelper[]{
      new DefaultContentHelper(S_URIMATCHER, AUTHORITY)
  };

  private static final SparseArray<StoreContentHelper> S_CONTENT_HELPERS = new SparseArray<>();

  static {
    Set<Integer> routeCheck = new HashSet<Integer>();

    for (StoreContentHelper contentHelper : STORE_HELPERS) {
      for (int route : contentHelper.getSupportedRoutes()) {
        if (!routeCheck.add(route)) {
          throw new IllegalStateException("Found Content helper with duplicate routes "
              + route);
        }
        S_CONTENT_HELPERS.put(route, contentHelper);
      }
    }
  }

  @Override
  public boolean onCreate() {
    mDatabaseHelper = new FolkDatabaseHelper(getContext());
    return true;
  }

  public void close() {
    if (mDatabaseHelper != null) {
      mDatabaseHelper.close();
    }
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                      String sortOrder) {
    int match = S_URIMATCHER.match(uri);
    StoreContentHelper helper = S_CONTENT_HELPERS.get(match);
    helper.openConnection(getContext(), mDatabaseHelper);
    return helper.query(uri, projection, selection, selectionArgs, sortOrder);
  }

  @Override
  public String getType(Uri uri) {
    final int match = S_URIMATCHER.match(uri);
    StoreContentHelper helper = S_CONTENT_HELPERS.get(match);
    helper.openConnection(getContext(), mDatabaseHelper);
    return helper.getType(uri);
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    final int match = S_URIMATCHER.match(uri);
    StoreContentHelper helper = S_CONTENT_HELPERS.get(match);
    helper.openConnection(getContext(), mDatabaseHelper);
    return helper.insert(uri, values);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int match = S_URIMATCHER.match(uri);
    StoreContentHelper helper = S_CONTENT_HELPERS.get(match);
    helper.openConnection(getContext(), mDatabaseHelper);
    return helper.delete(uri, selection, selectionArgs);
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    int match = S_URIMATCHER.match(uri);
    StoreContentHelper contentHelper = S_CONTENT_HELPERS.get(match);
    contentHelper.openConnection(getContext(), mDatabaseHelper);
    return contentHelper.update(uri, values, selection, selectionArgs);
  }

  static class FolkDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "folkitstore.db";

    public FolkDatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      for (String createQuery : new String[]{}) {
        db.execSQL(createQuery);
      }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      for (String deleteQuery : new String[]{}) {
        db.execSQL(deleteQuery);
      }
      onCreate(db);
    }
  }
}

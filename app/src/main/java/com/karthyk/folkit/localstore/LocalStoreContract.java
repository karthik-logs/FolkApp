package com.karthyk.folkit.localstore;

import android.net.Uri;

public class LocalStoreContract {
  public static final String CONTENT_AUTHORITY = "com.karthyk.folkit.localstore";
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  public static class Default {
    public static final String TABLE_NAME = "default";
    public static final String COLUMN_NAME_DATE = "column_date";
    public static final String CONTENT_ITEM_TYPE = "item_type";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
        .appendPath("default").build();
  }
}

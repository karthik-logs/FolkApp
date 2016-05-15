package com.karthyk.folkit.utils;

import android.content.SharedPreferences;

import com.karthyk.folkit.FolkGlobal;

public class FolkUtils {

  public static final String USER_PREF_FILE = "User_Prefs_File";

  public static boolean putInSharedPrefs(String key, Object value) {
    SharedPreferences userProfile = FolkGlobal.getAppContext().getSharedPreferences(
        USER_PREF_FILE, 0);
    SharedPreferences.Editor editor = userProfile.edit();
    if (value instanceof Boolean) {
      editor.putBoolean(key, (Boolean) value);
    } else if (value instanceof String) {
      editor.putString(key, (String) value);
    } else if (value instanceof Integer) {
      editor.putInt(key, (Integer) value);
    } else if (value instanceof Long) {
      editor.putLong(key, (Long) value);
    } else {
      return false;
    }
    editor.commit();
    return true;
  }

  /**
   * To get the value from the UserProfile sharedpreferences
   *
   * @param key  The key for which the value to be returned from shared
   *             preferences
   * @param type The return type of the result and the value will be taken as
   *             the default if no key or value is found in the shared prefs
   *             for the passed key
   * @return Return type is generic based on the @param type
   */
  @SuppressWarnings("unchecked")
  public static <Result> Result getFromSharedPrefs(String key, Result type) {
    Result result = type;
    SharedPreferences userProfile = FolkGlobal.getAppContext().getSharedPreferences(
        USER_PREF_FILE, 0);
    if (type instanceof Boolean) {
      result = (Result) (Object) userProfile.getBoolean(key, (Boolean) type);
    } else if (type instanceof String) {
      result = (Result) userProfile.getString(key, (String) type);
    } else if (type instanceof Integer) {
      result = (Result) (Integer) userProfile.getInt(key, (Integer) type);
    } else if (type instanceof Long) {
      result = (Result) (Long) userProfile.getLong(key, (Long) type);
    }
    return result;
  }
}

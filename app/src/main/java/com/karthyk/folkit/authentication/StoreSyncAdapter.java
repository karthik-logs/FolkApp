package com.karthyk.folkit.authentication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.karthyk.folkit.FolkGlobal;
import com.karthyk.folkit.transaction.CredentialTransaction;
import com.karthyk.folkit.utils.SessionUtils;

import java.util.concurrent.ExecutionException;

public class StoreSyncAdapter extends AbstractThreadedSyncAdapter {
  private final ContentResolver mContentResolver;
  private static final String TAG = StoreSyncAdapter.class.getSimpleName();

  /**
   * Constructor. Obtains handle to content resolver for later use.
   */
  public StoreSyncAdapter(Context context, boolean autoInitialize) {
    super(context, autoInitialize);
    mContentResolver = context.getContentResolver();
  }

  /**
   * Constructor. Obtains handle to content resolver for later use.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public StoreSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
    super(context, autoInitialize, allowParallelSyncs);
    mContentResolver = context.getContentResolver();
  }

  @Override public void onPerformSync(Account account, Bundle extras, String authority,
                                      ContentProviderClient provider, SyncResult syncResult) {
    SessionUtils.updateUserBundle(SessionUtils.getAuthenticatedUser());
    Log.d(TAG, "onPerformSync: with authToken"
        + SessionUtils.getAuthenticatedUser().getAuthToken());
    boolean success = false;
    try {
      success = new CredentialTransaction.CheckAuthTokenTask(
          SessionUtils.getAuthenticatedUser().getAuthToken()).execute().get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    if(!success) {
      Log.d(TAG, "onPerformSync: OnRemove");
      AccountManager accountManager = AccountManager.get(FolkGlobal.getAppContext());
      accountManager.removeAccount(account, null, null);
      SessionUtils.setUserSessionExpired(true);
    }
  }
}

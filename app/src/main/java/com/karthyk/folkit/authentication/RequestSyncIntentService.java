package com.karthyk.folkit.authentication;

import android.accounts.Account;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.karthyk.folkit.localstore.LocalStoreContract;
import com.karthyk.folkit.utils.RestUtils;

public class RequestSyncIntentService extends IntentService {
  public static final int IN_PROGRESS = 0;
  public static final int NETWORK_UNREACHABLE = 1;
  public static final String IS_MANUAL_REQUEST = "is_manual_request";
  private static final String TAG = RequestSyncIntentService.class.getSimpleName();

  public RequestSyncIntentService() {
    super("RequestSyncIntentService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Log.d(TAG, "onHandleIntent: Syncing");
    boolean isServerAvailable = RestUtils.isServerActive();
    ResultReceiver mReceiver = intent.getParcelableExtra("receiver");
    boolean isManualRequest = intent.getBooleanExtra(IS_MANUAL_REQUEST, false);
    if (isServerAvailable || !isManualRequest) {
      // check if the sync is already pending.
      Account account = AuthenticationService.getActiveAccount();
      boolean syncActive = ContentResolver.isSyncActive(account,
          LocalStoreContract.CONTENT_AUTHORITY);
      boolean syncPending = ContentResolver.isSyncPending(account,
          LocalStoreContract.CONTENT_AUTHORITY);
      if (!syncActive && !syncPending) {
        requestSync();
      } else if (isManualRequest) {
        ContentResolver.cancelSync(AuthenticationService.getActiveAccount(),
            LocalStoreContract.CONTENT_AUTHORITY);
        requestSync();
      } else {
        // send message as sync already in progress.
        if (mReceiver != null)
          mReceiver.send(IN_PROGRESS, null);
      }
    } else {
      /// send no data connection.
      if (mReceiver != null)
        mReceiver.send(NETWORK_UNREACHABLE, null);
    }
  }

  public static void requestSync() {
    Bundle b = new Bundle();
    // Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
    b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
    b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

    ContentResolver.requestSync(
        AuthenticationService.getActiveAccount(),
        LocalStoreContract.CONTENT_AUTHORITY,
        b);
  }
}

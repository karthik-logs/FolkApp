package com.karthyk.folkit.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class StoreSyncService extends Service {
  private static final String TAG = "SyncService";

  private static final Object S_SYNC_ADAPTER_LOCK = new Object();
  private static StoreSyncAdapter sSyncAdapter = null;

  /**
   * Thread-safe constructor, creates static {@link StoreSyncAdapter} instance.
   */
  @Override
  public void onCreate() {
    super.onCreate();
    Log.i(TAG, "Service created");
    synchronized (S_SYNC_ADAPTER_LOCK) {
      if (sSyncAdapter == null) {
        sSyncAdapter = new StoreSyncAdapter(getApplicationContext(), true);
      }
    }
  }

  @Override
  /**
   * Logging-only destructor.
   */
  public void onDestroy() {
    Log.i(TAG, "Service destroyed");
    super.onDestroy();
  }

  /**
   * Return Binder handle for IPC communication with {@link StoreSyncAdapter}.
   * <p/>
   * <p>New sync requests will be sent directly to the SyncAdapter using this channel.
   *
   * @param intent Calling intent
   * @return Binder handle for {@link StoreSyncAdapter}
   */
  @Override
  public IBinder onBind(Intent intent) {
    return sSyncAdapter.getSyncAdapterBinder();
  }
}

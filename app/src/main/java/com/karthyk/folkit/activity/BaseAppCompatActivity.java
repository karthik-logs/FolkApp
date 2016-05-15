package com.karthyk.folkit.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.karthyk.folkit.utils.SessionUtils;

public abstract class BaseAppCompatActivity extends AppCompatActivity {

  public SessionExpiredReceiver sessionExpiredReceiver = new SessionExpiredReceiver();

  public class SessionExpiredReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      checkForSessionExpiration();
    }
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    IntentFilter expFilter = new IntentFilter();
    expFilter.addAction(SessionUtils.SESSION_EXPIRED_ACTION);
    registerReceiver(sessionExpiredReceiver, expFilter);
    // on view recreation check session expire.
    if (savedInstanceState != null) {
      checkForSessionExpiration();
    }
  }

  @Override
  protected void onDestroy() {
    if (sessionExpiredReceiver != null)
      unregisterReceiver(sessionExpiredReceiver);
    super.onDestroy();
  }

  public void checkForSessionExpiration() {
    if (!SessionUtils.isUserAuthenticated()) {
      Intent restartLogin = new Intent(this, SplashActivity.class);
      startActivity(restartLogin);
      finish();
    }
  }
}

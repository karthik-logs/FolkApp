package com.karthyk.folkit.activity;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.karthyk.folkit.R;
import com.karthyk.folkit.authentication.AuthenticationService;
import com.karthyk.folkit.utils.SessionUtils;

public class SplashActivity extends AppCompatActivity implements AccountManagerCallback<Bundle> {

  private static final String TAG = SplashActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    final AccountManager accountManager = AccountManager.get(this);
    if (!SessionUtils.isUserAuthenticated()) {
      if (SessionUtils.isUserSessionExpired()) {
        accountManager.updateCredentials(AuthenticationService.getActiveAccount(), "", null, null,
            this, null);
      } else {
        accountManager.addAccount(AuthenticationService.ACCOUNT_TYPE, null, null, null,
            SplashActivity.this, SplashActivity.this, null);
      }
    } else {
      onLoginSuccess();
    }
  }

  @Override
  public void run(AccountManagerFuture<Bundle> future) {
    try {
      Bundle bundle = future.getResult();
      Log.i(TAG, "Added account bundle is " + bundle);
      onLoginSuccess();
    } catch (OperationCanceledException e) {
      Log.i(TAG, "authentication operation was cancelled");
      finish();
    } catch (Exception ex) {
      showErrorDialog(ex.getMessage());
    }
  }

  private void onLoginSuccess() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();
  }

  private void showErrorDialog(String errors) {
    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setMessage(
        "Error(s) occurred. Look into DDMS log for details, " + "please. Errors: " + errors)
        .create().show();
  }
}

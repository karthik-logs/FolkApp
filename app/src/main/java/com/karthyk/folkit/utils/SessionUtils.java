package com.karthyk.folkit.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.karthyk.folkit.FolkGlobal;
import com.karthyk.folkit.authentication.AuthenticationService;
import com.karthyk.folkit.authentication.RequestSyncIntentService;
import com.karthyk.folkit.localstore.LocalStoreContract;
import com.karthyk.folkit.model.Credential;

public class SessionUtils {
  private static final String TAG = SessionUtils.class.getSimpleName();
  private static final String PREF_USER_NAME = "pref_username";
  private static final String PREF_SESSION_EXPIRED = "pref_session_expired";
  public static final String SESSION_EXPIRED_ACTION = "session_expired_action";
  private static Credential signedInUser;
  private static final long SYNC_FREQUENCY =  10;  // 10 seconds (in seconds)

  private SessionUtils() {
    // No Initialization.
  }

  public static Credential getAuthenticatedUser() {
    if (signedInUser == null) {
      AccountManager accountManager = AccountManager.get(FolkGlobal.getAppContext());
      Account account = AuthenticationService.getActiveAccount();
      if (account == null) return null;
      signedInUser = Credential.fromAccount(account, accountManager);
      if (TextUtils.isEmpty(signedInUser.getUsername())
          || TextUtils.isEmpty(signedInUser.getPassword())) {
        return null;
      }
    }
    return signedInUser;
  }

  public static void onRemoveAuthenticatedUser(Account account) {
    // to be sure remove the cached Credential if any.
    // Account Manager definition
    FolkUtils.putInSharedPrefs(PREF_SESSION_EXPIRED, true);
    signedInUser = null;
  }

  public static void onAddNewAccount() {
    FolkUtils.putInSharedPrefs(PREF_USER_NAME, signedInUser.getUsername());
    FolkUtils.putInSharedPrefs(PREF_SESSION_EXPIRED, false);
  }

  public static boolean isUserSessionExpired() {
    return (getAuthenticatedUser() != null && getAuthenticatedUser().isSessionExpired());
  }

  public static boolean isUserAuthenticated() {
    return (getAuthenticatedUser() != null && !getAuthenticatedUser().isSessionExpired());
  }

  public static void setUserSessionExpired(boolean isExpired) {
    Account account = AuthenticationService.getActiveAccount();
    if (account != null) {
      AccountManager accountManager = AccountManager.get(FolkGlobal.getAppContext());
      accountManager.setUserData(account, Credential.PREF_SESSION_STATUS, String.valueOf(isExpired));
    }
    if (signedInUser != null) {
      signedInUser.setSessionExpired(isExpired);
    }

    if(isExpired) {
      broadcastSessionExpire();
    }
  }

  public static void updateUserCredentials(Credential updatedCredential) {
    Account account = AuthenticationService.getActiveAccount();
    if (account != null && account.name.equals(updatedCredential.getUsername())) {
      // valid to update the user credentials.
      AccountManager accountManager = AccountManager.get(FolkGlobal.getAppContext());
      accountManager.setPassword(account, updatedCredential.getPassword());
      accountManager.setUserData(account, Credential.PREF_SESSION_STATUS,
          String.valueOf(updatedCredential.isSessionExpired()));
      signedInUser = updatedCredential;
    }
  }

  public static void updateUserBundle(Credential credential) {
    Account account = AuthenticationService.getActiveAccount();
    if (account != null && account.name.equals(credential.getUsername())) {
      Bundle bundle = credential.toUserBundle();
      AccountManager accountManager = AccountManager.get(FolkGlobal.getAppContext());
      for (String key : bundle.keySet()) {
        accountManager.setUserData(account, key, bundle.getString(key));
      }
      signedInUser = credential;
    }
  }

  public static void broadcastSessionExpire() {
    Intent intent = new Intent();
    intent.setAction(SESSION_EXPIRED_ACTION);
    FolkGlobal.getAppContext().sendBroadcast(intent);
  }
  
  public static void setSignedInUser(Credential credential) {
    Account account = AuthenticationService.getActiveAccount();
    if (account != null) {
      if (account.name.equals(credential.getUsername())) {
        Log.i(TAG, "User already signed in");
      } else {
        throw new UnsupportedOperationException("Not more than one user can signin");
      }
      return;
    }

    AccountManager accountManager = AccountManager.get(FolkGlobal.getAppContext());
    account = new Account(credential.getUsername(), AuthenticationService.ACCOUNT_TYPE);
    if (accountManager.addAccountExplicitly(account, credential.getPassword(),
        credential.toUserBundle())) {
      // Inform the system that this account supports sync
      ContentResolver.setIsSyncable(account, LocalStoreContract.CONTENT_AUTHORITY, 1);
      // Inform the system that this account is eligible for auto sync when the network is up
      ContentResolver.setSyncAutomatically(account, LocalStoreContract.CONTENT_AUTHORITY, true);
      // Recommend a schedule for automatic synchronization. The system may modify this based
      // on other scheduled syncs and network utilization.
      ContentResolver.addPeriodicSync(
          account, LocalStoreContract.CONTENT_AUTHORITY, new Bundle(), SYNC_FREQUENCY);
      // reset the cloud value of gcmid to re upload the id to the cloud.
      RequestSyncIntentService.requestSync();
    }
    signedInUser = credential;
  }
}

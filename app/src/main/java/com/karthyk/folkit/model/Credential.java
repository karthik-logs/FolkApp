package com.karthyk.folkit.model;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;

public class Credential {

  public static final String PREF_USERNAME = "pref_username";
  public static final String PREF_EMAIL = "pref_email";
  public static final String PREF_ID = "pref_id";
  public static final String PREF_AUTH_TOKEN = "pref_auth_token";
  public static final String PREF_PASSWORD = "pref_password";
  public static final String PREF_SESSION_STATUS = "pref_session_status";
  private String id;
  private String username;
  private String password;
  private String email;
  private String authToken;
  private boolean sessionExpired;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  public static Credential fromAccount(Account account, AccountManager accountManager) {
    Credential credential = new Credential();
    credential.setUsername(accountManager.getUserData(account, PREF_USERNAME));
    credential.setEmail(accountManager.getUserData(account, PREF_EMAIL));
    credential.setId(accountManager.getUserData(account, PREF_ID));
    credential.setAuthToken(accountManager.getUserData(account, PREF_AUTH_TOKEN));
    credential.setPassword(accountManager.getUserData(account, PREF_PASSWORD));
    return credential;
  }

  public Bundle toUserBundle() {
    Bundle bundle = new Bundle();
    bundle.putString(PREF_ID, id);
    bundle.putString(PREF_USERNAME, username);
    bundle.putString(PREF_EMAIL, email);
    bundle.putString(PREF_PASSWORD, password);
    bundle.putString(PREF_AUTH_TOKEN, authToken);
    return bundle;
  }

  public boolean isSessionExpired() {
    return sessionExpired;
  }

  public void setSessionExpired(boolean sessionExpired) {
    this.sessionExpired = sessionExpired;
  }
}

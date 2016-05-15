package com.karthyk.folkit.authentication;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.karthyk.folkit.FolkGlobal;
import com.karthyk.folkit.activity.LoginActivity;

public class Authenticator extends AbstractAccountAuthenticator {

  private static final String TAG = Authenticator.class.getSimpleName();
  public Handler handler = new Handler();
  private final Context mContext;

  public Authenticator(Context context) {
    super(context);
    this.mContext = context;
  }

  @Override
  public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response,
                                                   Account account) throws NetworkErrorException {
    Bundle result = new Bundle();

    // to clear user data from application context.
    Intent intentRemove = new Intent(mContext, AccountRemoveIntentService.class);
    intentRemove.putExtra(AccountRemoveIntentService.ACCOUNT_TO_REMOVE, account);
    mContext.startService(intentRemove);

    result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);
    return result;
  }

  @Override public Bundle editProperties(AccountAuthenticatorResponse response,
                                         String accountType) {
    return null;
  }

  @Override
  public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
                           String authTokenType, String[] requiredFeatures,
                           Bundle options) throws NetworkErrorException {
    AccountManager accountManager = AccountManager.get(FolkGlobal.getAppContext());
    Account[] accounts = accountManager.getAccountsByType(AuthenticationService.ACCOUNT_TYPE);
    if (accounts.length > 0) {
      final Bundle bundle = new Bundle();
      bundle.putInt(AccountManager.KEY_ERROR_CODE, 1);
      bundle.putString(AccountManager.KEY_ERROR_MESSAGE,
          mContext.getString(com.karthyk.folkit.R.string.one_account_allowed));
      handler.post(new Runnable() {

        @Override
        public void run() {
          Toast.makeText(mContext, com.karthyk.folkit.R.string.one_account_allowed,
              Toast.LENGTH_SHORT).show();
        }
      });
      return bundle;
    }
    final Intent intent = new Intent(mContext, LoginActivity.class);
    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
    final Bundle bundle = new Bundle();
    bundle.putParcelable(AccountManager.KEY_INTENT, intent);
    return bundle;
  }

  @Override
  public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account,
                                             Bundle options) throws NetworkErrorException {
    return null;
  }

  @Override
  public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
                                       String authTokenType,
                                       Bundle options) throws NetworkErrorException {
    return null;
  }

  @Override
  public String getAuthTokenLabel(String authTokenType) {
    return null;
  }

  @Override
  public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
                                            String authTokenType,
                                            Bundle options) throws NetworkErrorException {
    final Intent intent = new Intent(mContext, LoginActivity.class);
    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
    final Bundle bundle = new Bundle();
    bundle.putParcelable(AccountManager.KEY_INTENT, intent);
    return bundle;
  }

  @Override public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
                                      String[] features) throws NetworkErrorException {
    return null;
  }
}

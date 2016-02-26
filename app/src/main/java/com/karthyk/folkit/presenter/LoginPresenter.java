package com.karthyk.folkit.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.karthyk.folkit.callbacks.ILoginCallback;
import com.karthyk.folkit.utils.RestUtils;
import com.karthyk.folkit.view.ILoginView;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by karthik on 20/2/16.
 */
public class LoginPresenter implements ILoginPresenter {

  private static final String TAG = LoginPresenter.class.getSimpleName();
  public ILoginView loginView;
  public ILoginCallback loginCallback;

  public LoginPresenter(ILoginView loginView, ILoginCallback loginCallback) {
    this.loginView = loginView;
    this.loginCallback = loginCallback;
  }

  @Override
  public void onViewReady() {
  }

  @Override
  public void onSignInClicked(String username, String password) {

  }

  @Override
  public void onSignUpClicked(String username, String password, String email) {

  }

  @Override
  public void onNewUsername(String username) {
    new SignUpValidationTask(username, "checkUsername/").execute();
  }

  @Override
  public void onNewEmail(String email) {
    new SignUpValidationTask(email, "checkEmail/").execute();
  }

  public void onUsernameExist() {
    loginCallback.onValidationError(ILoginCallback.ERROR_USERNAME_EXIST);
    Log.d(TAG, "onUsernameExist: ");
  }

  public void onEmailExist() {
    loginCallback.onValidationError(ILoginCallback.ERROR_EMAIL_EXIST);
    Log.d(TAG, "onEmailExist: ");
  }

  public void onUsernameAvailable() {
    loginCallback.onSuccessfulValidation(ILoginCallback.SUCCESS_NEW_USERNAME);
  }

  public void onEmailAvailable() {
    loginCallback.onSuccessfulValidation(ILoginCallback.SUCCESS_NEW_EMAIL);
  }

  public class SignUpValidationTask extends AsyncTask<Void, Void, String> {
    String url = RestUtils.getRootURL();
    String checkUrl;

    public SignUpValidationTask(String param, String checkUrl) {
      this.checkUrl = checkUrl;
      url = url + checkUrl + param;
      Log.d(TAG, "SignUpValidationTask: " + url);
    }

    @Override protected String doInBackground(Void... params) {
      try {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String result = restTemplate.getForObject(url, String.class);
        Log.d(TAG, "doInBackground:Result:  " + result);
        return result;
      } catch (Exception e) {
        Log.i(TAG, "doInBackground: " + e.toString());
      }
      return null;
    }

    @Override protected void onPostExecute(String result) {
      super.onPostExecute(result);
      if (result.contains("Username already in use")) {
        onUsernameExist();
      } else if (checkUrl.contains("checkUsername")) {
        onUsernameAvailable();
      }
      if (result.contains("Email already in use")) {
        onEmailExist();
      } else if (checkUrl.contains("checkEmail")) {
        onEmailAvailable();
      }
      Log.i(TAG, "doInBackground: got" + result);
    }
  }
}

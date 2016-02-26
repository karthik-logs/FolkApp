package com.karthyk.folkit.transaction;

import android.os.AsyncTask;
import android.util.Log;

import com.karthyk.folkit.callbacks.ILoginCallback;
import com.karthyk.folkit.model.Credential;
import com.karthyk.folkit.utils.RestUtils;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class CredentialTransaction {

  private static final String TAG = CredentialTransaction.class.getSimpleName();

  public static class HTTPGetRequestTask extends AsyncTask<Void, Void, List<Credential>> {

    final String url = "http://10.21.11.72:8080/user";

    @Override protected List<Credential> doInBackground(Void... params) {
      try {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        Credential[] credential = restTemplate.getForObject(url, Credential[].class);
        Log.i(TAG, "doInBackground: " + credential[0].getUsername());
        return Arrays.asList(credential);
      } catch (Exception e) {
        Log.e(TAG, "doInBackground: " + e.toString());
      }
      return null;
    }
  }

  public static class HTTPPostRequestTask extends AsyncTask<Void, Void, Void> {
    final String url = "http://10.21.11.72:8080/credential";

    @Override protected Void doInBackground(Void... params) {
      try {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        Credential credential = new Credential();
        credential.setUsername("a");
        credential.setPassword("password");
        credential.setEmail("b");
        restTemplate.postForObject(url, credential, Credential.class);
        Log.i(TAG, "doInBackground: + posted");
      } catch (Exception e) {
        Log.e(TAG, "doInBackground: " + e.toString());
      }
      return null;
    }
  }

  public static class SignUpUser extends AsyncTask<ILoginCallback, Void, Void> {
    String url = RestUtils.getRootURL();

    final String username;
    final String password;
    final String email;

    ILoginCallback loginCallback;

    public SignUpUser(String username, String password, String email) {
      this.username = username;
      this.password = password;
      this.email = email;
    }

    @Override protected Void doInBackground(ILoginCallback... params) {
      try {
        loginCallback = params[0];
        url = url + "signUp/";
        Log.d(TAG, "SignUpURL: " + url);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        Credential credential = new Credential();
        credential.setUsername(this.username);
        credential.setPassword(this.password);
        credential.setEmail(this.email);
        Credential newCredential = restTemplate.postForObject(url, credential, Credential.class);
        Log.d(TAG, newCredential.getAuthToken());
        Log.i(TAG, "doInBackground: + SignedUp");
      } catch (Exception e) {
        Log.e(TAG, "doInBackground: " + e.toString());
      }
      return null;
    }

    @Override protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      loginCallback.onSignUpSuccessful();
    }
  }
}

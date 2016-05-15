package com.karthyk.folkit.transaction;

import android.os.AsyncTask;
import android.util.Log;

import com.karthyk.folkit.callbacks.ILoginCallback;
import com.karthyk.folkit.model.Credential;
import com.karthyk.folkit.utils.RestUtils;
import com.karthyk.folkit.utils.SessionUtils;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
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
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        Credential credential = new Credential();
        credential.setUsername(this.username);
        credential.setPassword(this.password);
        credential.setEmail(this.email);
        Credential newCredential = restTemplate.postForObject(url, credential, Credential.class);
        Log.d(TAG, newCredential.getAuthToken());

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("password", password);
        request.put("authToken", newCredential.getAuthToken());

// set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);

// send request and parse result
        try {
          String loginResponse = restTemplate.postForObject(RestUtils.getRootURL() +
              "auth/login/", entity, String.class);
          Log.d(TAG, "doInBackground: OK ... : " + loginResponse);
        } catch (Exception e) {
          Log.d(TAG, "doInBackground: Exc" + e.toString());
        }
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

  public static class SignInTask extends AsyncTask<ILoginCallback, Void, Boolean> {
    public static final String URL = RestUtils.getRootURL();
    private String username;
    private String password;
    private Credential credential;
    private ILoginCallback loginCallback;

    public SignInTask(String username, String password) {
      this.username = username;
      this.password = password;
    }

    @Override protected Boolean doInBackground(ILoginCallback... params) {
      try{
        this.loginCallback = params[0];
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        JSONObject request = new JSONObject();
        request.put("username", this.username);
        request.put("password", this.password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
        Credential response = restTemplate.postForObject(URL + "signIn/", entity, Credential.class);
        if(response != null) {
          this.credential = response;
          return true;
        } else {
          return false;
        }
      } catch (Exception e) {
        Log.d(TAG, "doInBackground: " + e.toString());
      }
      return false;
    }

    @Override protected void onPostExecute(Boolean aBoolean) {
      super.onPostExecute(aBoolean);
      if(aBoolean) {
        this.loginCallback.onSignInSuccessful(this.credential);
      } else {
        this.loginCallback.onSignInError();
      }
    }
  }

  public static class CheckAuthTokenTask extends AsyncTask<Void, Void, Boolean> {
    public static final String URL = RestUtils.getRootURL();
    private String authToken;

    public CheckAuthTokenTask(String authToken) {
      this.authToken = authToken;
    }

    @Override protected Boolean doInBackground(Void... params) {
      try{
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        JSONObject request = new JSONObject();
        request.put("authToken", this.authToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
        Credential response = restTemplate.postForObject(URL + "checkAuthToken/", entity,
            Credential.class);
        return response != null;
      } catch (Exception e) {
        Log.d(TAG, "doInBackground: " + e.toString());
      }
      return false;
    }
  }
}

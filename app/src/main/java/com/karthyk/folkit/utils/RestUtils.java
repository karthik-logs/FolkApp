package com.karthyk.folkit.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

public class RestUtils {
  public static final String HTTP_PROTOCOL = "http";
  public static final String HTTP_ADDRESS = "10.21.11.43";
  public static final int HTTP_PORT = 8080;

  public static String getRootURL() {
    return HTTP_PROTOCOL + "://" + HTTP_ADDRESS + ":" + HTTP_PORT + "/";
  }

  public static String getCustomRootURL(String protocol, String address, int port) {
    return protocol + "://" + address + ":" + port + "/";
  }

  public static boolean isServerActive() {
    try {
      return new ServerConnection().execute().get();
    } catch (InterruptedException | ExecutionException e) {
      Log.e(ServerConnection.TAG, "isServerActive: error" + e.toString());
      return false;
    }
  }

  private static class ServerConnection extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = RestUtils.class.getSimpleName();

    @Override protected Boolean doInBackground(Void... params) {
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
      try {
        String response = restTemplate.getForObject(getRootURL() + "serverResponse", String.class);
        if(response.contains("Connected")) {
          return true;
        }
      } catch (Exception e) {
        Log.e(TAG, "doInBackground: Cannot connect to server");
      }
      return false;
    }
  }
}

package com.karthyk.folkit.utils;

public class RestUtils {
  public static final String HTTP_PROTOCOL = "http";
  public static final String HTTP_ADDRESS = "10.21.11.72";
  public static final int HTTP_PORT = 8080;

  public static String getRootURL() {
    return HTTP_PROTOCOL + "://" + HTTP_ADDRESS + ":" + HTTP_PORT + "/";
  }

  public static String getCustomRootURL(String protocol, String address, int port) {
    return protocol + "://" + address + ":" + port + "/";
  }
}

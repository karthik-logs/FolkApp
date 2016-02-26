package com.karthyk.folkit.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class EncodeUtils {
  private static final String TAG = EncodeUtils.class.getSimpleName();

  public static String encodePassword(String password) throws NoSuchAlgorithmException {
    String salt = getSalt();
    String securePassword = get_SHA_512_SecurePassword(password, salt);
    Log.d(TAG, "main: " + securePassword);
    return securePassword;
  }

  private static String get_SHA_512_SecurePassword(String passwordToHash, String salt) {
    String generatedPassword = null;
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-512");
      md.update(salt.getBytes());
      byte[] bytes = md.digest(passwordToHash.getBytes());
      StringBuilder sb = new StringBuilder();
      for (byte aByte : bytes) {
        sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
      }
      generatedPassword = sb.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return generatedPassword;
  }

  private static String getSalt() throws NoSuchAlgorithmException {
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    return Arrays.toString(salt);
  }
}

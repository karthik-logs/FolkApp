package com.karthyk.folkit.view;

import android.widget.EditText;

public interface ILoginView {
  void injectViews();

  void clearError(EditText... params);

  void setErrorText(EditText editText, String errorMsg);

  void setAvailable(EditText editText, String msg);

  void clearOk(EditText... params);

  boolean checkForNullValues(EditText... params);

  void onSignInClicked();

  void onSignUpClicked();

  void renderButtonsAfterError();

  void showSnackBar(String message);
}

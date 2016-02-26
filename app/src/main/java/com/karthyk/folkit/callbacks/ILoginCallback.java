package com.karthyk.folkit.callbacks;

public interface ILoginCallback {
  int ERROR_USERNAME_EXIST = 0;
  int ERROR_EMAIL_EXIST = 1;

  int SUCCESS_NEW_USERNAME = 2;
  int SUCCESS_NEW_EMAIL = 3;

  void onValidationError(int error);

  void onSuccessfulValidation(int success);
}

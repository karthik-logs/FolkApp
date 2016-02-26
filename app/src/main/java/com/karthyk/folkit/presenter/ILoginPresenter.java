package com.karthyk.folkit.presenter;

/**
 * Created by karthik on 20/2/16.
 */
public interface ILoginPresenter {
  void onViewReady();
  void onSignInClicked(String username, String password);
  void onSignUpClicked(String username, String password, String email);
  void onNewUsername(String username);
  void onNewEmail(String email);
}

package com.karthyk.folkit.activity;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.androidadvance.topsnackbar.TSnackbar;
import com.karthyk.folkit.R;
import com.karthyk.folkit.authentication.AuthenticationService;
import com.karthyk.folkit.callbacks.ILoginCallback;
import com.karthyk.folkit.model.Credential;
import com.karthyk.folkit.presenter.ILoginPresenter;
import com.karthyk.folkit.presenter.LoginPresenter;
import com.karthyk.folkit.utils.RestUtils;
import com.karthyk.folkit.utils.SessionUtils;
import com.karthyk.folkit.view.ILoginView;

public class LoginActivity extends AccountAuthenticatorActivity implements ILoginView,
    View.OnClickListener, ILoginCallback {

  private static final String TAG = LoginActivity.class.getSimpleName();
  private ViewSwitcher viewSwitcher;
  private EditText etUsername;
  private EditText etPassword;
  private EditText etUsernameNew;
  private EditText etPasswordNew;
  private EditText etEmail;
  private EditText etConfirmPassword;

  private Button btnSignIn;
  private Button btnSignUp;

  private ProgressBar progressBar;
  private ProgressBar progressBarNew;
  private ProgressBar progressBarUsername;
  private ProgressBar progressBarEmail;

  private ILoginPresenter loginPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    loginPresenter = new LoginPresenter(this, this);
    injectViews();
  }

  @Override
  public void injectViews() {
    viewSwitcher = (ViewSwitcher) findViewById(R.id.view_switcher);
    ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggle_sign_up_sign_in);
    toggleButton.setOnClickListener(this);
    btnSignIn = (Button) findViewById(R.id.btn_sign_in);
    btnSignIn.setOnClickListener(this);
    btnSignUp = (Button) findViewById(R.id.btn_sign_up);
    btnSignUp.setOnClickListener(this);
    btnSignUp.setEnabled(false);
    etUsername = (EditText) findViewById(R.id.edit_text_username);
    etPassword = (EditText) findViewById(R.id.edit_text_password);
    etUsernameNew = (EditText) findViewById(R.id.edit_text_username_new);
    etPasswordNew = (EditText) findViewById(R.id.edit_text_password_new);
    etEmail = (EditText) findViewById(R.id.edit_text_email_new);
    clearOk(etUsernameNew, etEmail);
    etConfirmPassword = (EditText) findViewById(R.id.edit_text_confirm_password);
    clearError(etUsername, etPassword, etPasswordNew, etConfirmPassword, etUsernameNew, etEmail);
    progressBar = (ProgressBar) findViewById(R.id.progressBar);
    progressBarNew = (ProgressBar) findViewById(R.id.progressBar_new);
    progressBarUsername = (ProgressBar) findViewById(R.id.progressBar_username);
    progressBarEmail = (ProgressBar) findViewById(R.id.progressBar_email);
    loginPresenter.onViewReady();
  }

  private Drawable getErrorIcon() {
    Drawable errorIcon;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      errorIcon = getDrawable(R.drawable.ic_error_outline);
    } else {
      errorIcon = getResources().getDrawable(R.drawable.ic_error_outline);
    }
    assert errorIcon != null;
    errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());
    return errorIcon;
  }

  private Drawable getOkIcon() {
    Drawable errorIcon;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      errorIcon = getDrawable(R.drawable.ic_check_circle);
    } else {
      errorIcon = getResources().getDrawable(R.drawable.ic_check_circle);
    }
    assert errorIcon != null;
    errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());
    return errorIcon;
  }

  @Override
  public void setErrorText(EditText editText, String errorMsg) {
    editText.setError(errorMsg, getErrorIcon());
  }

  @Override
  public void setAvailable(EditText editText, String msg) {
    showProgressBar(editText, View.INVISIBLE);
    editText.setError(msg, getOkIcon());
    if (!TextUtils.isEmpty(etUsernameNew.getError()) && !TextUtils.isEmpty(etEmail.getError())) {
      if (TextUtils.equals(etUsernameNew.getError().toString(),
          getString(R.string.available_username))
          && TextUtils.equals(etEmail.getError().toString(),
          getString(R.string.available_email))) {
        btnSignUp.setEnabled(true);
      }
    }
  }

  private void showProgressBar(EditText editText, int visibility) {
    switch (editText.getId()) {
      case R.id.edit_text_username_new:
        progressBarUsername.setVisibility(visibility);
        break;
      case R.id.edit_text_email_new:
        progressBarEmail.setVisibility(visibility);
        break;
      default:
    }
  }

  @Override
  public void clearError(EditText... params) {
    for (final EditText editText : params) {
      editText.addTextChangedListener(new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
          editText.setError(null);
        }

        @Override public void afterTextChanged(Editable s) {

        }
      });
    }
  }

  @Override
  public void clearOk(EditText... params) {
    for (EditText editText : params) {
      editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override public void onFocusChange(View v, boolean hasFocus) {
          if (!hasFocus) {
            switch (v.getId()) {
              case R.id.edit_text_username_new:
                if (!TextUtils.isEmpty(etUsernameNew.getText().toString())) {
                  btnSignUp.setEnabled(false);
                  progressBarUsername.setVisibility(View.VISIBLE);
                  loginPresenter.onNewUsername(etUsernameNew.getText().toString());
                }
                break;
              case R.id.edit_text_email_new:
                if (!TextUtils.isEmpty(etEmail.getText().toString())) {
                  if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {
                    setErrorText(etEmail, "Not a valid email");
                    return;
                  }
                  btnSignUp.setEnabled(false);
                  progressBarEmail.setVisibility(View.VISIBLE);
                  loginPresenter.onNewEmail(etEmail.getText().toString());
                }
                break;
              default:
            }
          }
        }
      });
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_sign_in:
        if (!checkForNullValues(etUsername, etPassword)) {
          onSignInClicked();
        } else {
          renderButtonsAfterError();
        }
        break;
      case R.id.btn_sign_up:
        if (!checkForNullValues(etUsernameNew, etPasswordNew, etEmail, etConfirmPassword)) {
          onSignUpClicked();
        } else {
          renderButtonsAfterError();
        }
        break;
      case R.id.toggle_sign_up_sign_in:
        viewSwitcher.showNext();
        break;
      default:
    }
  }

  @Override
  public boolean checkForNullValues(EditText... params) {
    boolean result = false;
    for (EditText param : params) {
      switch (param.getId()) {
        case R.id.edit_text_username:
          if (TextUtils.isEmpty(etUsername.getText().toString())) {
            setErrorText(etUsername, getString(R.string.error_empty_username));
            result = true;
          }
          break;
        case R.id.edit_text_username_new:
          if (TextUtils.isEmpty(etUsernameNew.getText().toString())) {
            setErrorText(etUsernameNew, getString(R.string.error_empty_username));
            result = true;
          }
          break;
        case R.id.edit_text_password:
          if (TextUtils.isEmpty(etPassword.getText().toString())) {
            setErrorText(etPassword, getString(R.string.error_empty_password));
            result = true;
          }
          break;
        case R.id.edit_text_password_new:
          if (TextUtils.isEmpty(etPasswordNew.getText().toString())
              || etPasswordNew.getText().toString().trim().length() < 6) {
            setErrorText(etPasswordNew, getString(R.string.error_empty_password));
            result = true;
          }
          break;
        case R.id.edit_text_confirm_password:
          if (TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
            setErrorText(etConfirmPassword, getString(R.string.error_password_mismatch));
            result = true;
          }
          break;
        case R.id.edit_text_email_new:
          if (TextUtils.isEmpty(etEmail.getText().toString())) {
            setErrorText(etEmail, getString(R.string.error_empty_email));
            result = true;
          }
          break;
        default:
      }
    }
    return result;
  }

  @Override
  public void onSignInClicked() {
    if (!RestUtils.isServerActive()) {
      showSnackBar("No Internet Connection");
      return;
    }
    //new CredentialTransaction.HTTPPostRequestTask().execute();
//    List<Credential> userModelList = null;
//    try {
//      userModelList = new CredentialTransaction.HTTPGetRequestTask().execute().get();
//    } catch (InterruptedException | ExecutionException e) {
//      e.printStackTrace();
//    }
//    Log.i(TAG, "onSignInClicked: " + userModelList.get(1).getUsername());
    btnSignIn.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
    closeKeyboard();
    loginPresenter.onSignInClicked(etUsername.getText().toString(),
        etPassword.getText().toString());
  }

  @Override
  public void onSignUpClicked() {
    if (!RestUtils.isServerActive()) {
      showSnackBar("Cannot connect to the server!");
      return;
    }
    if (!TextUtils.equals(etPasswordNew.getText().toString(),
        etConfirmPassword.getText().toString())) {
      etConfirmPassword.setError(getString(R.string.error_password_mismatch), getErrorIcon());
      return;
    }
    loginPresenter.onSignUpClicked(etUsernameNew.getText().toString(),
        etPasswordNew.getText().toString(), etEmail.getText().toString());
    progressBarNew.setVisibility(View.VISIBLE);
    btnSignUp.setVisibility(View.GONE);
  }

  @Override
  public void renderButtonsAfterError() {
    progressBar.setVisibility(View.INVISIBLE);
    btnSignUp.setVisibility(View.VISIBLE);
    btnSignIn.setVisibility(View.VISIBLE);
    progressBarNew.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onValidationError(int error) {
    switch (error) {
      case ILoginCallback.ERROR_USERNAME_EXIST:
        setErrorText(etUsernameNew, "Username not available!");
        progressBarUsername.setVisibility(View.INVISIBLE);
        break;
      case ILoginCallback.ERROR_EMAIL_EXIST:
        progressBarEmail.setVisibility(View.INVISIBLE);
        setErrorText(etEmail, "Try signing in with this Email Address!");
        break;
    }
  }

  @Override
  public void onSuccessfulValidation(int success) {
    switch (success) {
      case ILoginCallback.SUCCESS_NEW_USERNAME:
        progressBarUsername.setVisibility(View.INVISIBLE);
        setAvailable(etUsernameNew, getString(R.string.available_username));
        break;
      case ILoginCallback.SUCCESS_NEW_EMAIL:
        progressBarEmail.setVisibility(View.INVISIBLE);
        setAvailable(etEmail, getString(R.string.available_email));
        break;
    }
  }

  @Override
  public void onSignUpSuccessful() {
    progressBarNew.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onSignInSuccessful(Credential credential) {
    if (SessionUtils.isUserSessionExpired()) {
      SessionUtils.updateUserCredentials(credential);
    } else {
      SessionUtils.setSignedInUser(credential);
    }
    SessionUtils.onAddNewAccount();
    final Intent authResultIntent = new Intent();
    authResultIntent.putExtra(AccountManager.KEY_ACCOUNT_NAME,
        String.valueOf(credential.getUsername()));
    authResultIntent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AuthenticationService.ACCOUNT_TYPE);
    setAccountAuthenticatorResult(authResultIntent.getExtras());
    setResult(RESULT_OK, authResultIntent);
    progressBar.setVisibility(View.INVISIBLE);
    finish();
  }

  @Override
  public void onSignInError() {
    showSnackBar("Username or password is incorrect");
    Log.e(TAG, "onSignInError: ");
    renderButtonsAfterError();
  }

  @Override
  public void showSnackBar(String message) {
    TSnackbar snackbar = TSnackbar.make(findViewById(android.R.id.content), message,
        TSnackbar.LENGTH_LONG);
    snackbar.setActionTextColor(Color.WHITE);
    View snackbarView = snackbar.getView();
    snackbarView.setBackgroundColor(Color.parseColor("#ec103c"));
    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.
        R.id.snackbar_text);
    textView.setTextColor(Color.WHITE);
    snackbar.show();
  }

  private void closeKeyboard() {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
  }
}
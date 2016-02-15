package com.karthyk.folkit.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.karthyk.folkit.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    injectViews();
  }

  private void injectViews() {
    viewSwitcher = (ViewSwitcher) findViewById(R.id.view_switcher);
    ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggle_sign_up_sign_in);
    toggleButton.setOnClickListener(this);
    btnSignIn = (Button) findViewById(R.id.btn_sign_in);
    btnSignIn.setOnClickListener(this);
    btnSignUp = (Button) findViewById(R.id.btn_sign_up);
    btnSignUp.setOnClickListener(this);
    etUsername = (EditText) findViewById(R.id.edit_text_username);
    clearError(etUsername);
    etPassword = (EditText) findViewById(R.id.edit_text_password);
    clearError(etPassword);
    etUsernameNew = (EditText) findViewById(R.id.edit_text_username_new);
    clearError(etUsernameNew);
    etPasswordNew = (EditText) findViewById(R.id.edit_text_password_new);
    clearError(etPasswordNew);
    etEmail = (EditText) findViewById(R.id.edit_text_email_new);
    clearError(etEmail);
    etConfirmPassword = (EditText) findViewById(R.id.edit_text_confirm_password);
    clearError(etConfirmPassword);
    progressBar = (ProgressBar) findViewById(R.id.progressBar);
    progressBarNew = (ProgressBar) findViewById(R.id.progressBar_new);
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
  private void setErrorText(EditText editText, String errorMsg) {

    editText.setError(errorMsg, getErrorIcon());
  }

  private void clearError(final EditText editText) {
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

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_sign_in:
        if (!checkForNullValues(etUsername, etPassword)) {
          signIn();
        } else {
          renderButtonsAfterError();
        }
        break;
      case R.id.btn_sign_up:
        if (!checkForNullValues(etUsernameNew, etPasswordNew, etEmail, etConfirmPassword)) {
          signUp();
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

  private boolean checkForNullValues(EditText... params) {
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
          if (TextUtils.isEmpty(etPasswordNew.getText().toString())) {
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

  private void signIn() {
    btnSignIn.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  private void signUp() {
    if(!TextUtils.equals(etPasswordNew.getText().toString(),
        etConfirmPassword.getText().toString())) {
      etConfirmPassword.setError(getString(R.string.error_password_mismatch), getErrorIcon());
      return;
    }
    progressBarNew.setVisibility(View.VISIBLE);
    btnSignUp.setVisibility(View.GONE);
  }

  private void renderButtonsAfterError() {
    progressBarNew.setVisibility(View.INVISIBLE);
    btnSignUp.setVisibility(View.VISIBLE);
    btnSignIn.setVisibility(View.VISIBLE);
    progressBarNew.setVisibility(View.INVISIBLE);
  }
}

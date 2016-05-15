package com.karthyk.folkit.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.karthyk.folkit.R;
import com.karthyk.folkit.utils.SessionUtils;

public class MainActivity extends BaseAppCompatActivity {

  private TextView textViewUsername;
  private TextView textViewEmail;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    injectViews();
  }

  private void injectViews() {
    textViewEmail = (TextView) findViewById(R.id.tv_email);
    textViewUsername = (TextView) findViewById(R.id.tv_username);
    textViewUsername.setText(SessionUtils.getAuthenticatedUser().getUsername());
    textViewEmail.setText(SessionUtils.getAuthenticatedUser().getEmail());
  }
}

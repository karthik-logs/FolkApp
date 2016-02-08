package com.karthyk.folkit.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.karthyk.folkit.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

  public String mStringToBeTyped;

  public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
      LoginActivity.class);


  @Before
  public void initValidString() {
    mStringToBeTyped = "default";
  }


  @Test
  public void InputFields_Should_be_Visible() {
    onView(withId(R.id.edit_text_username)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_password)).check(matches(isDisplayed()));
  }

  @Test
  public void changeUsernameText() {
    onView(withId(R.id.edit_text_username)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_username)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_username)).check(matches(withText(mStringToBeTyped)));
  }

  @Test
  public void changePasswordText() {
    onView(withId(R.id.edit_text_password)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_password)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_password)).check(matches(withText(mStringToBeTyped)));
  }

  @Test
  public void Buttons_Should_be_Visible() {
    onView(withId(R.id.btn_sign_in)).check(matches(isDisplayed()));
    onView(withId(R.id.btn_sign_up)).check(matches(isDisplayed()));
  }

  @Test
  public void Sign_In_Should_Display_Error_On_Empty_Username() {
    onView(withId(R.id.btn_sign_in)).perform(click());
    onView(withText(R.string.error_empty_username)).check(matches(isDisplayed()));
  }

  @Test
  public void Sign_In_Should_Display_Error_On_Empty_Password() {
    onView(withId(R.id.btn_sign_in)).perform(click());
    onView(withText(R.string.error_empty_username)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_ProgressBar_On_SignIn_Pressed() {
    onView(withId(R.id.edit_text_username)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_password)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.btn_sign_in)).perform(click());
    onView(withId(R.id.progressBar)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_Sign_Up_Page_On_SignUp_Pressed() {
    onView(withId(R.id.btn_sign_in)).perform(click());
    onView(withId(R.id.edit_text_email)).check(matches(isDisplayed()));
  }
}
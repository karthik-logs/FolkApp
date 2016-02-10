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
  public void initDefaultStrings() {
    mStringToBeTyped = "default";
  }


  @Test
  public void InputFields_Should_be_Visible_SignInLayout() {
    onView(withId(R.id.edit_text_username)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_password)).check(matches(isDisplayed()));
  }

  @Test
  public void changeUsernameText_SignInLayout() {
    onView(withId(R.id.edit_text_username)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_username)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_username)).check(matches(withText(mStringToBeTyped)));
  }

  @Test
  public void changePasswordText_SignInLayout() {
    onView(withId(R.id.edit_text_password)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_password)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_password)).check(matches(withText(mStringToBeTyped)));
  }

  @Test
  public void Buttons_Should_be_Visible_SignInLayout() {
    onView(withId(R.id.btn_sign_in)).check(matches(isDisplayed()));
    onView(withId(R.id.toggle_sign_up_sign_in)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_Error_OnEmptyUsername_When_SignIn_Pressed_SignInLayout() {
    onView(withId(R.id.btn_sign_in)).perform(click());
    onView(withText(R.string.error_empty_username)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_Error_OnEmptyPassword_When_SignIn_Pressed_SignInLayout() {
    onView(withId(R.id.btn_sign_in)).perform(click());
    onView(withText(R.string.error_empty_username)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_ProgressBar_On_SignIn_Pressed_SignInLayout() {
    onView(withId(R.id.edit_text_username)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_password)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.btn_sign_in)).perform(click());
    onView(withId(R.id.progressBar)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_SignUpLayout_When_Toggle_Pressed_SignInLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.layout_sign_up)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_SignInLayout_When_Toggle_Released_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.layout_sign_in)).check(matches(isDisplayed()));
  }

  @Test
  public void InputFields_Should_be_Visible_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_username_new)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_password_new)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_email_new)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_confirm_password)).check(matches(isDisplayed()));
  }

  @Test
  public void changeUsernameText_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_username_new)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_username_new)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_username_new)).check(matches(withText(mStringToBeTyped)));
  }

  @Test
  public void changeEmailText_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_email_new)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_email_new)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_email_new)).check(matches(withText(mStringToBeTyped)));
  }

  @Test
  public void changePasswordText_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_password_new)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_password_new)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_password_new)).check(matches(withText(mStringToBeTyped)));
  }

  @Test
  public void changeConfirmPasswordText_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_confirm_password)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_confirm_password)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_confirm_password)).check(matches(withText(mStringToBeTyped)));
  }

  @Test
  public void Buttons_Should_be_Visible_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.btn_sign_up)).check(matches(isDisplayed()));
    onView(withId(R.id.toggle_sign_up_sign_in)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_Error_OnEmptyUsername_When_SignUp_Pressed_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.btn_sign_up)).perform(click());
    onView(withText(R.string.error_empty_username)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_Error_OnEmptyEmail_When_SignUp_Pressed_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.btn_sign_up)).perform(click());
    onView(withText(R.string.error_empty_email)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_Error_OnEmptyPassword_When_SignUp_Pressed_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.btn_sign_up)).perform(click());
    onView(withText(R.string.error_empty_password)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_Error_OnPasswordMismatch_When_SignUp_Pressed_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.btn_sign_up)).perform(click());
    onView(withId(R.id.edit_text_password_new)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withText(R.string.error_password_mismatch)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_ProgressBar_On_SignUp_Pressed_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_username_new)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_password)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.btn_sign_in)).perform(click());
    onView(withId(R.id.progressBar)).check(matches(isDisplayed()));
  }
}
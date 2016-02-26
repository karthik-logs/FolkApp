package com.karthyk.folkit.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.EditText;

import com.karthyk.folkit.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
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

  public final String mStringToBeTyped = "default";

  @Rule
  public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
      LoginActivity.class);

  @Test
  public void InputFields_Should_be_Visible_SignInLayout() {
    onView(withId(R.id.edit_text_username)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_password)).check(matches(isDisplayed()));
  }

  @Test
  public void Input_Fields_Should_be_Visible_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_username_new)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_email_new)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_password_new)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_confirm_password)).check(matches(isDisplayed()));
  }

  @Test
  public void Input_Fields_Should_be_Editable_SignInLayout() {
    onView(withId(R.id.edit_text_username)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_username)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_username)).check(matches(withText(mStringToBeTyped)));
    onView(withId(R.id.edit_text_password)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_password)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_password)).check(matches(withText(mStringToBeTyped)));
  }

  @Test
  public void Input_Fields_Should_be_Editable_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_username_new)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_username_new)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_username_new)).check(matches(withText(mStringToBeTyped)));

    onView(withId(R.id.edit_text_email_new)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_email_new)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_email_new)).check(matches(withText(mStringToBeTyped)));

    onView(withId(R.id.edit_text_password_new)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_password_new)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_password_new)).check(matches(withText(mStringToBeTyped)));

    onView(withId(R.id.edit_text_confirm_password)).check(matches(isDisplayed()));
    onView(withId(R.id.edit_text_confirm_password)).perform(typeText(mStringToBeTyped),
        closeSoftKeyboard());
    onView(withId(R.id.edit_text_confirm_password)).check(matches(withText(mStringToBeTyped)));
  }

  @Test
  public void Buttons_Should_be_Visible_SignInLayout() {
    onView(withId(R.id.btn_sign_in)).check(matches(isDisplayed()));
    onView(withId(R.id.toggle_sign_up_sign_in)).check(matches(isDisplayed()));
  }

  @Test
  public void Buttons_Should_be_Visible_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.btn_sign_up)).check(matches(isDisplayed()));
    onView(withId(R.id.toggle_sign_up_sign_in)).check(matches(isDisplayed()));
  }

  @Test
  public void Should_Display_Error_OnEmptyInputFields_SignInLayout() {
    onView(withId(R.id.btn_sign_in)).perform(click());
    onView(withId(R.id.edit_text_username)).check(matches(withHint(
        mActivityRule.getActivity().getString(R.string.error_empty_username))));
    onView(withId(R.id.edit_text_password)).check(matches(withHint(
        mActivityRule.getActivity().getString(R.string.error_empty_password))));
  }

  @Test
  public void Should_Display_Error_On_Invalid_Email_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_email_new)).perform(typeText(mStringToBeTyped))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.edit_text_password_new)).perform(typeText(mStringToBeTyped))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.edit_text_email_new)).check(matches(withHint(mActivityRule.getActivity()
        .getString(R.string.error_empty_email))));
  }

  @Test
  public void Should_Display_Error_On_Empty_Password_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_username_new)).perform(typeText(mStringToBeTyped))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.edit_text_email_new)).perform(typeText(mStringToBeTyped + "@default.com"))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.edit_text_confirm_password)).perform(typeText(mStringToBeTyped))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.btn_sign_up)).perform(click());
    onView(withId(R.id.edit_text_password_new)).check(matches(withHint(
        mActivityRule.getActivity().getString(R.string.error_empty_password))));
  }

  @Test
  public void Should_Display_Error_On_PasswordMismatch_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_username_new)).perform(typeText(mStringToBeTyped))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.edit_text_email_new)).perform(typeText(mStringToBeTyped + "@default.com"))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.edit_text_password_new)).perform(typeText(mStringToBeTyped))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.btn_sign_up)).perform(click());
    onView(withId(R.id.edit_text_confirm_password)).check(matches(withHint(
        mActivityRule.getActivity().getString(R.string.error_password_mismatch))));
  }

  @Test
  public void Should_Display_UsernameAvailable_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_username_new)).perform(typeText(mStringToBeTyped))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.edit_text_email_new)).perform(typeText(mStringToBeTyped))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.edit_text_username_new)).check(matches(withHint(
        mActivityRule.getActivity().getString(R.string.available_username))));
  }

  @Test
  public void Should_Display_EmailAvailable_SignUpLayout() {
    onView(withId(R.id.toggle_sign_up_sign_in)).perform(click());
    onView(withId(R.id.edit_text_email_new)).perform(typeText(mStringToBeTyped + "@default.com"))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.edit_text_username_new)).perform(typeText(mStringToBeTyped))
        .perform(closeSoftKeyboard());
    onView(withId(R.id.edit_text_email_new)).check(matches(withHint(
        mActivityRule.getActivity().getString(R.string.available_email))));
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

  public static Matcher<View> withHint(final String expectedHint) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        if (!(view instanceof EditText)) {
          return false;
        }

        String errorMsg = ((EditText) view).getError().toString();

        return expectedHint.equals(errorMsg);
      }

      @Override
      public void describeTo(Description description) {
      }
    };
  }
}
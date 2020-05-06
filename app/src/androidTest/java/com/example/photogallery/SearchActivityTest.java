package com.example.photogallery;

import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

    public static final String STRING_TO_BE_TYPED = "Espresso";

    @Rule public ActivityScenarioRule<SearchActivity> activityScenarioRule
            = new ActivityScenarioRule<>(SearchActivity.class);

    @Test
    public void test_isActivityDisplayedCorrectly() {

        onView(withId(R.id.etSearch)).check(matches(isDisplayed()));
        onView(withId(R.id.etSearch)).check(matches(withHint(R.string.search)));
        onView(withId(R.id.btnStartDate)).check(matches(isDisplayed()));
        onView(withId(R.id.btnStartDate)).check(matches(withText(R.string.start_date)));
        onView(withId(R.id.tvStartDate)).check(matches(isDisplayed()));
        onView(withId(R.id.btnEndDate)).check(matches(isDisplayed()));
        onView(withId(R.id.btnEndDate)).check(matches(withText(R.string.end_date)));
        onView(withId(R.id.tvEndDate)).check(matches(isDisplayed()));
        onView(withId(R.id.btnSearch)).check(matches(isDisplayed()));
        onView(withId(R.id.btnSearch)).check(matches(withText(R.string.search)));

    }

    @Test
    public void changeText_newTextDisplayed() {
        onView(withId(R.id.etSearch))
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

        onView(withId(R.id.etSearch)).check(matches(withText(STRING_TO_BE_TYPED)));
    }

    @Test
    public void changeDate_newDateUpdated() {
        setDate(R.id.btnStartDate,2020, 6, 6);
        onView(withId(R.id.tvStartDate)).check(matches(withText("6 Jun, 2020")));

        setDate(R.id.btnEndDate, 2020, 6, 16);
        onView(withId(R.id.tvEndDate)).check(matches(withText("16 Jun, 2020")));
    }

    public static void setDate(int datePickerId, int year, int month, int dayOfMonth) {
        onView(withId(datePickerId))
                .perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month, dayOfMonth));

        onView(withText("OK")).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
    }
}
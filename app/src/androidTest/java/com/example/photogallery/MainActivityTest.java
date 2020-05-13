package com.example.photogallery;

import org.hamcrest.core.StringStartsWith;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void searchPhoto_userSelectDate_matchedPhotoDisplayed() {
        onView(withId(R.id.buttonSearch)).perform(click());

        SearchActivityTest.setDate(R.id.btnStartDate,2020, 5, 6);
        onView(withId(R.id.tvStartDate)).check(matches(withText("6 May, 2020")));

        SearchActivityTest.setDate(R.id.btnEndDate, 2020, 5, 23);
        onView(withId(R.id.tvEndDate)).check(matches(withText("23 May, 2020")));

        onView(withId(R.id.btnSearch)).perform(click());

        onView(withId(R.id.textViewTimeStamp)).check(matches(withText(startsWith("6 May, 2020"))));
    }

    @Test
    public void searchPhoto_userInputCaption_matchedPhotoDisplayed() {
        onView(withId(R.id.buttonSearch)).perform(click());

        onView(withId(R.id.etSearch))
                .perform(typeText(SearchActivityTest.STRING_TO_BE_TYPED), closeSoftKeyboard());

        onView(withId(R.id.btnSearch)).perform(click());

        onView(withId(R.id.editTextCaptions)).check(matches(withText(SearchActivityTest.STRING_TO_BE_TYPED)));
    }
}
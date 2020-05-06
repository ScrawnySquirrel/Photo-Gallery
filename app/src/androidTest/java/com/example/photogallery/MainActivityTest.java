package com.example.photogallery;

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

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void searchPhoto_userInputDate_matchedPhotoDisplayed() {
        onView(withId(R.id.buttonSearch)).perform(click());

        SearchActivityTest.setDate(R.id.btnStartDate,2020, 6, 6);
        onView(withId(R.id.tvStartDate)).check(matches(withText("6 Jun, 2020")));

        SearchActivityTest.setDate(R.id.btnEndDate, 2020, 6, 16);
        onView(withId(R.id.tvEndDate)).check(matches(withText("16 Jun, 2020")));

        onView(withId(R.id.btnSearch)).perform(click());

        // TODO: implement method to compare photos
        //ViewInteraction img = Espresso.onView(ViewMatchers.withId(R.id.btnStartDate));
    }

    @Test
    public void searchPhoto_userInputCaption_matchedPhotoDisplayed() {
        onView(withId(R.id.buttonSearch)).perform(click());

        onView(withId(R.id.etSearch))
                .perform(typeText(SearchActivityTest.STRING_TO_BE_TYPED), closeSoftKeyboard());

        onView(withId(R.id.btnSearch)).perform(click());

        // TODO: implement method to compare photos
        //ViewInteraction img = Espresso.onView(ViewMatchers.withId(R.id.btnStartDate));
    }
}
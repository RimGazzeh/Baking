package com.geekgirl.android.baking;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.geekgirl.android.baking.ui.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Rim Gazzah on 22/10/18
 */
@RunWith(AndroidJUnit4.class)
public class ScreensTesting {

    private static final String NUTELLA_PIE = "Nutella Pie";
    private static final String CHEESECAKE = "Cheesecake";

    private static final String BROWNIES_STEP_1 = "Starting prep";
    private static final String YELLOW_CAKE_INGREDIENT_2 = "baking powder  4.0 TSP";


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }


    @Test
    public void checkDessertNames()  {
        onView(withId(R.id.recyclerview_desserts)).perform(scrollToPosition(0));
        onCheckText(NUTELLA_PIE);
        onView(withId(R.id.recyclerview_desserts)).perform(scrollToPosition(3));
        onCheckText(CHEESECAKE);
    }

    public void onCheckText(String data) {
        try {
            Thread.sleep(1000); // this to wait until databinding update the view
            onView(withText(data)).check(matches(isDisplayed()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void clickDessert_OpensDetailActivity() throws InterruptedException {
        onView(withId(R.id.recyclerview_desserts)).perform(scrollToPosition(2));
        Thread.sleep(1000);
        onView(withId(R.id.recyclerview_desserts)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.recyclerview_ingredients)).perform(scrollToPosition(2));
        Thread.sleep(1000);
        onCheckText(YELLOW_CAKE_INGREDIENT_2);
    }

    @Test
    public void clickStep_OpenStepDetail() throws InterruptedException {
        onView(withId(R.id.recyclerview_desserts)).perform(scrollToPosition(1));
        Thread.sleep(1000);
        onView(withId(R.id.recyclerview_desserts)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        Thread.sleep(1000);
        onView(withId(R.id.recyclerview_steps)).perform(scrollToPosition(0));
        onView(withId(R.id.recyclerview_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.pager)).perform(swipeLeft());
        onCheckText(BROWNIES_STEP_1);
    }


}

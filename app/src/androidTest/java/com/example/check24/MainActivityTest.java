package com.example.check24;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.check24.calculation.Calculator;
import com.example.check24.graph.ActivityComponent;
import com.example.check24.testUtils.RxSchedulersOverrideRule;
import com.example.check24.testUtils.graps.DaggerFakeActivityComponent;
import com.example.check24.testUtils.graps.FakeCalculatorModule;
import com.example.check24.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by marcingawel on 09.06.2016.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private Calculator calculator;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);
    @Rule
    public RxSchedulersOverrideRule rxRule = new RxSchedulersOverrideRule();

    @Before
    public void setup() {
        calculator = mock(Calculator.class);
        ActivityComponent component = DaggerFakeActivityComponent.builder()
                .fakeCalculatorModule(new FakeCalculatorModule(calculator))
                .build();
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        CheckApplication app = (CheckApplication) instrumentation.getTargetContext().getApplicationContext();
        app.setComponent(component);
        activityTestRule.launchActivity(new Intent());
    }

    @Test
    public void shouldNotAddNonNumbers() {
        onView(withId(R.id.item_entry)).perform(typeText("asd"));
        onView(withText(R.string.add)).perform(click());

        onView(withText("asd")).check(doesNotExist());
        onView(withText(R.string.wrong_format)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldAddItemToListAndClearList() throws Exception {
        onView(withId(R.id.item_entry)).perform(typeText("1"));
        onView(withText("Add")).perform(click());
        onView(withId(R.id.item_entry)).perform(typeText("10"));
        onView(withText("Add")).perform(click());
        onView(withText("1.0")).check(matches(isDisplayed()));
        onView(withText("10.0")).check(matches(isDisplayed()));

        onView(withText(R.string.clear)).perform(click());

        onView(withText("1.0")).check(doesNotExist());
        onView(withText("10.0")).check(doesNotExist());
    }

    @Test
    public void shouldRemoveSingleItemFromList() throws Exception {
        onView(withId(R.id.item_entry)).perform(typeText("1234"));
        onView(withText(R.string.add)).perform(click());
        onView(withText("1234.0")).check(matches(isDisplayed()));

        onView(withText(R.string.remove)).perform(click());

        onView(withText("1234")).check(doesNotExist());
    }

    @Test
    public void shouldChangeSingleItem() {
        onView(withId(R.id.item_entry)).perform(typeText("1234"));
        onView(withText(R.string.add)).perform(click());
        onView(withText("1234.0")).check(matches(isDisplayed()));

        onView(withId(R.id.item_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(allOf(withClassName(endsWith("EditText")), withText(is("1234.0")))).perform(replaceText("1"));

        onView(withId(android.R.id.button1)).perform(click());

        onView(withText("1.0")).check(matches(isDisplayed()));
    }

    @Test
    public void shouldStartComputationAndShowSuccessInfo() {
        when(calculator.getSum(any(List.class))).thenReturn(Observable.just(new BigDecimal(1)));
        onView(withId(R.id.shopComputation)).perform(click());
        onView(withText(R.string.compute_sum)).perform(click());

        verify(calculator).getSum(any(List.class));
        onView(withText("1.0")).check(matches(isDisplayed()));
    }
}

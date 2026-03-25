package com.example.calculator

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ActivityLifecycleTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    @After
    fun clearSharedPrefs() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.getSharedPreferences("calc_prefs", Context.MODE_PRIVATE).edit().clear().commit()
    }

    @Test
    fun testHistoryPersistsAfterRecreate() {
        onView(withId(R.id.inputA)).perform(typeText("10"), closeSoftKeyboard())
        onView(withId(R.id.inputB)).perform(typeText("5"), closeSoftKeyboard())
        onView(withId(R.id.btnAdd)).perform(click())
        onView(withId(R.id.inputA)).perform(clearText(), typeText("2"), closeSoftKeyboard())
        onView(withId(R.id.inputB)).perform(clearText(), typeText("3"), closeSoftKeyboard())
        onView(withId(R.id.btnMul)).perform(click())

        activityRule.scenario.recreate()

        onView(withId(R.id.tvResult)).check(matches(withText(org.hamcrest.Matchers.containsString("10 + 5 = 15"))))
        onView(withId(R.id.tvResult)).check(matches(withText(org.hamcrest.Matchers.containsString("2 * 3 = 6"))))
    }
}

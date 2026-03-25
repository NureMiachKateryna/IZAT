package com.example.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ParameterizedCalculatorTest(
    private val valA: String,
    private val valB: String,
    private val expected: String,
    private val btnId: Int
) {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf("10", "5", "50", R.id.btnMul),
            arrayOf("20", "4", "5", R.id.btnDiv),
            arrayOf("9", "0", "Error", R.id.btnDiv)
        )
    }

    @Test
    fun testOperations() {
        onView(withId(R.id.inputA)).perform(clearText(), typeText(valA), closeSoftKeyboard())
        Thread.sleep(1000)

        onView(withId(R.id.inputB)).perform(clearText(), typeText(valB), closeSoftKeyboard())
        Thread.sleep(1000)

        onView(withId(btnId)).perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.tvResult)).check(matches(withText(expected)))
        Thread.sleep(1000)
    }
}
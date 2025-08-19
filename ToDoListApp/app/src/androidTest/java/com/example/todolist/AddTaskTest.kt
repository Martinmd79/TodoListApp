package com.example.todolist


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddTaskTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addNewTaskTest() {
        //Click the "New Task" button
        onView(allOf(withId(R.id.newTaskBtn))).perform(click())
        //Enter task name
        onView(withId(R.id.name)).perform(typeText("Test Task"), closeSoftKeyboard())

        //Enter task description
        onView(withId(R.id.desc)).perform(typeText("Test Description"), closeSoftKeyboard())

        //Click the save button
        onView(withId(R.id.saveBtn)).perform(click())

        //Verify the new task is displayed in the RecyclerView
        onView(withId(R.id.recyclerViewTasks))
            .perform(RecyclerViewActions.scrollToPosition<TaskViewHolder>(0))

        onView(withText("Test Task")).check(matches(isDisplayed()))
        onView(withText("Test Description")).check(matches(isDisplayed()))
    }

}

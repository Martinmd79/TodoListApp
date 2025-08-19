package com.example.todolist


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
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
class FragmentNavigationTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testNewTaskButtonShowsNewTaskFragment() {
        // Click the "New Task" button
        onView(withId(R.id.newTaskBtn)).perform(click())

        // Check if the NewTaskFragment is displayed by verifying the presence of a unique view in that fragment
        onView(withId(R.id.designNewTask)).check(matches(isDisplayed()))
    }

    @Test
    fun testSortButtonShowsSortDialog() {
        // Click the "Sort" button
        onView(withId(R.id.sortButton)).perform(click())

        // Check if the SortDialogFragment is displayed by verifying the presence of its title
        onView(withText("Sort Tasks")).check(matches(isDisplayed()))
    }

    @Test
    fun testInfoButtonShowsTaskEditFragment() {
        // Click the "New Task" button to add a new task
        onView(withId(R.id.newTaskBtn)).perform(click())

        //Enter task name
        onView(withId(R.id.name)).perform(typeText("Test Task"), closeSoftKeyboard())

        //Enter task description
        onView(withId(R.id.desc)).perform(typeText("Test Description"), closeSoftKeyboard())

        //Click the save button to add the task
        onView(withId(R.id.saveBtn)).perform(click())

        //Perform a click on the first task's info button in the RecyclerView
        onView(withId(R.id.recyclerViewTasks))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<TaskViewHolder>(
                    0,
                    clickChildViewWithId(R.id.infoButton)
                )
            )

        //Check if TaskEditFragment is displayed by verifying the presence of a unique view in that fragment
        onView(withId(R.id.taskEditFragment)).check(matches(isDisplayed()))
    }

    // Helper function to click on a child view in a RecyclerView item (infoButton)
    private fun clickChildViewWithId(id: Int) = ViewActions.actionWithAssertions(object :
        ViewAction {
        override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)
        override fun getDescription() = "Click on a child view with specified id."
        override fun perform(uiController: UiController?, view: View) {
            val v = view.findViewById<View>(id)
            v.performClick()
        }
    })

}

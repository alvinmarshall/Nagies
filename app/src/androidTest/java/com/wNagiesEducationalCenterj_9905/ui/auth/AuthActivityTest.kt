package com.wNagiesEducationalCenterj_9905.ui.auth

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import com.wNagiesEducationalCenterj_9905.ui.teacher.TeacherNavigationActivity
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AuthActivityTest {
    @get:Rule
    val activityRule =
        ActivityTestRule(AuthActivity::class.java,false,false)

    @Before
    fun setup() {
        ActivityScenario.launch(AuthActivity::class.java)
        Intents.init()
    }

    @Test
    fun showParentNavigationWhenParentRoleIsSelectedForLogin() {
        onView(withId(R.id.et_username)).perform(typeText(STRING_PARENT_TEXT), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.et_password)).perform(typeText(STRING_PARENT_TEXT), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.sp_login_option))
            .perform(click())
        onData(anything()).atPosition(1).perform(click())
        onView(withId(R.id.sp_login_option)).check(matches(withSpinnerText(containsString(STRING_PARENT_LABEL))))
        onView(withId(R.id.btn_login)).perform(click())
        intended(hasComponent(ParentNavigationActivity::class.java.name))
    }

    @Test
    fun showTeacherNavigationDashboardWhenSelectRoleSelectedForLogin() {
        onView(withId(R.id.et_username)).perform(typeText(STRING_TEACHER_TEXT), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.et_password)).perform(typeText(STRING_TEACHER_TEXT), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.sp_login_option)).perform(click())
        onData(anything()).atPosition(2).perform(click())
        onView(withId(R.id.sp_login_option)).check(matches(withSpinnerText(containsString(STRING_TEACHER_LABEL))))
        onView(withId(R.id.btn_login)).perform(click())
        intended(hasComponent(TeacherNavigationActivity::class.java.name))
    }

    @Test
    fun showSnackBarErrorForRoleSelecting() {
        onView(withId(R.id.et_username)).perform(typeText(STRING_PARENT_TEXT), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.et_password)).perform(typeText(STRING_PARENT_TEXT), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.sp_login_option)).check(matches(withSpinnerText(STRING_SELECT_ROLE_LABEL)))
        onView(withId(R.id.btn_login)).perform(click())
        onView(allOf(withId(com.google.android.material.R.id.snackbar_text), withText("Please select a role")))
            .check(
                matches(withText(R.string.error_role_select_message))
            )
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    companion object {
        const val STRING_PARENT_TEXT = "andy1"
        const val STRING_TEACHER_TEXT = "prince1"
        const val STRING_TEACHER_LABEL = "Teacher"
        const val STRING_PARENT_LABEL = "Parent"
        const val STRING_SELECT_ROLE_LABEL = "Select a role"
    }
}

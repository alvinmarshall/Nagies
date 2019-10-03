package com.wNagiesEducationalCenterj_9905.ui.auth

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import com.wNagiesEducationalCenterj_9905.ui.teacher.TeacherNavigationActivity
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
        ActivityTestRule(RoleActivity::class.java,false,false)

    @Before
    fun setup() {
        ActivityScenario.launch(RoleActivity::class.java)
        Intents.init()
    }

    @Test
    fun showParentNavigationWhenParentRoleIsSelectedForLogin() {
        onView(withId(R.id.btn_parent_role)).perform(click())
        intended(hasComponent(AuthActivity::class.java.name))
        onView(withId(R.id.et_username)).perform(typeText(STRING_PARENT_TEXT), closeSoftKeyboard())
        onView(withId(R.id.et_password)).perform(typeText(STRING_PARENT_TEXT), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())
        intended(hasComponent(ParentNavigationActivity::class.java.name))
    }

    @Test
    fun showTeacherNavigationDashboardWhenSelectRoleSelectedForLogin() {
        onView(withId(R.id.btn_teacher_role)).perform(click())
        intended(hasComponent(AuthActivity::class.java.name))
        onView(withId(R.id.et_username)).perform(typeText(STRING_TEACHER_TEXT), closeSoftKeyboard())
        onView(withId(R.id.et_password)).perform(typeText(STRING_TEACHER_TEXT), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())
        intended(hasComponent(TeacherNavigationActivity::class.java.name))
    }

    @Test
    fun showErrorWithNoCredentials(){
        onView(withId(R.id.btn_parent_role)).perform(click())
        intended(hasComponent(AuthActivity::class.java.name))
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.et_username)).check(matches(hasErrorText(STRING_ERROR_USER_NAME)))
        onView(withId(R.id.et_username)).perform(typeText(STRING_PARENT_TEXT), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())
        onView(withId(R.id.et_password)).check(matches(hasErrorText(STRING_ERROR_PASSWORD)))
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    companion object {
        const val STRING_PARENT_TEXT = "andy1"
        const val STRING_TEACHER_TEXT = "prince1"
        const val STRING_ERROR_USER_NAME = "invalid username"
        const val STRING_ERROR_PASSWORD = "invalid password"
    }
}

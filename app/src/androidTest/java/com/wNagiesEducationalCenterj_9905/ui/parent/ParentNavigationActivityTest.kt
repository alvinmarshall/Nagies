package com.wNagiesEducationalCenterj_9905.ui.parent

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.ui.auth.RoleActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ParentNavigationActivityTest {
    @get:Rule
    val activityRule =
        ActivityTestRule(ParentNavigationActivity::class.java, false, false)

    @Before
    fun setUp() {
        ActivityScenario.launch(ParentNavigationActivity::class.java)
        Intents.init()
    }

    @Test
    fun logoutUserReturnToRolePage(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.action_logout)).perform(click())
        intended(hasComponent(RoleActivity::class.java.name))
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}
package com.wNagiesEducationalCenterj_9905

import com.wNagiesEducationalCenterj_9905.base.BaseActivity
import com.wNagiesEducationalCenterj_9905.common.LOGIN_ROLE_OPTIONS
import com.wNagiesEducationalCenterj_9905.ui.auth.RoleActivity
import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import com.wNagiesEducationalCenterj_9905.ui.teacher.TeacherNavigationActivity
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SplashActivity : BaseActivity() {

    private fun startApp() = launch {
        val role = preferenceProvider.getUserSessionData().userRole
        val loginStatus = preferenceProvider.getUserSessionData().loginStatus
        if (!loginStatus) {
            startActivity<RoleActivity>()
            finish()
        } else {
            when (role) {
                //parent
                LOGIN_ROLE_OPTIONS[0] -> {
                    startActivity<ParentNavigationActivity>()
                    finish()
                }
                //teacher
                LOGIN_ROLE_OPTIONS[1] -> {
                    startActivity<TeacherNavigationActivity>()
                    finish()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startApp()
    }

}

package com.wNagiesEducationalCenterj_9905.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.LOGIN_ROLE_OPTIONS
import com.wNagiesEducationalCenterj_9905.common.SELECTED_ROLE
import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import com.wNagiesEducationalCenterj_9905.ui.teacher.TeacherNavigationActivity
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.content_auth.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setSupportActionBar(toolbar)
        val bundle = intent.extras
        val role = bundle?.getString(SELECTED_ROLE)

        btn_login.onClick {
            setLoginRoleNavigation(role)
        }
    }

    private fun setLoginRoleNavigation(login_role: String?) {
        login_role?.let {
            when (it) {
                LOGIN_ROLE_OPTIONS[0] -> {
                    startActivity(
                        intentFor<ParentNavigationActivity>()
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                }
                LOGIN_ROLE_OPTIONS[1] -> {
                    startActivity(
                        intentFor<TeacherNavigationActivity>()
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                }
            }
        }
    }
}

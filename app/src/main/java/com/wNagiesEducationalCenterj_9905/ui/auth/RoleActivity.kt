package com.wNagiesEducationalCenterj_9905.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.LOGIN_ROLE_OPTIONS
import com.wNagiesEducationalCenterj_9905.common.USER_SELECTED_ROLE_PREF_KEY
import kotlinx.android.synthetic.main.activity_role.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick

class RoleActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role)
        btn_parent_role.onClick { navigateToLoginPage(LOGIN_ROLE_OPTIONS[0]) }
        btn_teacher_role.onClick { navigateToLoginPage(LOGIN_ROLE_OPTIONS[1]) }
    }

    private fun navigateToLoginPage(selectedRole: String) {
        startActivity(intentFor<AuthActivity>(USER_SELECTED_ROLE_PREF_KEY to selectedRole))
    }
}

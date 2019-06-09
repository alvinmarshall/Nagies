package com.wNagiesEducationalCenterj_9905.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.google.android.material.snackbar.Snackbar
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.LOGIN_ROLE_OPTIONS
import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import com.wNagiesEducationalCenterj_9905.ui.teacher.TeacherNavigationActivity
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.content_auth.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class AuthActivity : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var btnLogin: Button
    private lateinit var loginOptions: ArrayList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private var snackbar:Snackbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setSupportActionBar(toolbar)
        spinner = sp_login_option
        btnLogin = btn_login
        initLoginSpinner()
        btnLogin.onClick {
            setLoginRoleNavigation()
        }
    }

    private fun initLoginSpinner() {
        loginOptions = LOGIN_ROLE_OPTIONS
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, loginOptions)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
    }

    private fun setLoginRoleNavigation() {
        when (spinner.selectedItemPosition) {
            0 -> {
                snackbar = Snackbar.make(root,"Select a role",Snackbar.LENGTH_SHORT)
                snackbar?.show()
            }
            1 -> startActivity<ParentNavigationActivity>()
            2 -> startActivity<TeacherNavigationActivity>()
        }
    }
}

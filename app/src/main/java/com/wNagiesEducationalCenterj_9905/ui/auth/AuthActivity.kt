package com.wNagiesEducationalCenterj_9905.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseActivity
import com.wNagiesEducationalCenterj_9905.common.LOGIN_ROLE_OPTIONS
import com.wNagiesEducationalCenterj_9905.common.SELECTED_ROLE
import com.wNagiesEducationalCenterj_9905.common.utils.InputValidationProvider
import com.wNagiesEducationalCenterj_9905.ui.auth.viewmodel.AuthViewModel
import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import com.wNagiesEducationalCenterj_9905.ui.teacher.TeacherNavigationActivity
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.content_auth.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import timber.log.Timber
import javax.inject.Inject

class AuthActivity : BaseActivity() {
    @Inject lateinit var inputValidationProvider: InputValidationProvider
    private lateinit var authViewModel: AuthViewModel
    private lateinit var loadingProgress: ProgressBar
    private var snackbar: Snackbar? = null
    private var authRole: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setSupportActionBar(toolbar)
        loadingProgress = progressBar

        loadingProgress.visibility = View.GONE
        val bundle = intent.extras
        val role = bundle?.getString(SELECTED_ROLE)
        authRole = role
        configureViewModel()

        btn_login.onClick {
            authenticateUser()
        }
    }

    private fun authenticateUser() {
        if (!inputValidationProvider.isEditTextFilled(et_username, "invalid username", false)) {
            return
        }
        if (!inputValidationProvider.isEditTextFilled(et_password, "invalid password")) {
            return
        }
        val username: String = et_username.text.toString()
        val password: String = et_password.text.toString()
        subscribeObservable(username, password)
    }

    private fun subscribeObservable(username: String, password: String) {
        authViewModel.authenticatingParent(username, password).observe(this, Observer { resource ->
            resource.let {
                when (it.status) {
                    Status.SUCCESS -> {
                        showProgressBar(false)
                        if (it.data == null) {
                            showFailedMessage()
                            return@let
                        }
                        toast("welcome ${it.data.username}")
                        setLoginRoleNavigation(authRole)
                        Timber.i("user authenticated with id: ${it.data.id}")
                    }
                    Status.ERROR -> {
                        showProgressBar(false)
                        Timber.i(it.message)
                    }
                    Status.LOADING -> {
                        showProgressBar()
                    }
                }
            }
        })
    }

    private fun showFailedMessage(show: Boolean = true) {
        snackbar = Snackbar.make(mv, "username or password incorrect", Snackbar.LENGTH_SHORT)
        if (show) {
            snackbar?.show()
        }
    }

    private fun showProgressBar(show: Boolean = true) {
        if (show) {
            loadingProgress.visibility = View.VISIBLE
        } else {
            loadingProgress.visibility = View.GONE
        }
    }

    private fun configureViewModel() {
        authViewModel = ViewModelProviders.of(this, viewModelFactory)[AuthViewModel::class.java]
    }

    private fun setLoginRoleNavigation(login_role: String?) {
        login_role?.let {
            when (it) {
                LOGIN_ROLE_OPTIONS[0] -> {
                    preferenceProvider.setUserLoginRole(LOGIN_ROLE_OPTIONS[0])
                    startActivity(
                        intentFor<ParentNavigationActivity>()
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                }
                LOGIN_ROLE_OPTIONS[1] -> {
                    preferenceProvider.setUserLoginRole(LOGIN_ROLE_OPTIONS[1])
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

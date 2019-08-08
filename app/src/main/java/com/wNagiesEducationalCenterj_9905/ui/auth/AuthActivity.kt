package com.wNagiesEducationalCenterj_9905.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseActivity
import com.wNagiesEducationalCenterj_9905.common.LOGIN_ROLE_OPTIONS
import com.wNagiesEducationalCenterj_9905.common.SELECTED_ROLE
import com.wNagiesEducationalCenterj_9905.common.UserAccount
import com.wNagiesEducationalCenterj_9905.common.showAnyView
import com.wNagiesEducationalCenterj_9905.common.utils.InputValidationProvider
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import com.wNagiesEducationalCenterj_9905.ui.auth.viewmodel.AuthViewModel
import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import com.wNagiesEducationalCenterj_9905.ui.teacher.TeacherNavigationActivity
import com.wNagiesEducationalCenterj_9905.vo.Resource
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.content_auth.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import timber.log.Timber
import javax.inject.Inject

class AuthActivity : BaseActivity() {
    @Inject
    lateinit var inputValidationProvider: InputValidationProvider
    private lateinit var authViewModel: AuthViewModel
    private var userAccount: UserAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setSupportActionBar(toolbar)

        showErrorMessage()
        showLoadingDialog(false)
        if (!intent.hasExtra(SELECTED_ROLE)) {
            return
        }
        getAccountUser()
        configureViewModel()
        btn_login.onClick {
            authenticateUser()
        }
    }

    private fun getAccountUser() {
        val bundle = intent.extras
        val role = bundle?.getString(SELECTED_ROLE)
        userAccount = when (role) {
            LOGIN_ROLE_OPTIONS[0] -> UserAccount.PARENT
            LOGIN_ROLE_OPTIONS[1] -> UserAccount.TEACHER
            else -> null
        }
    }

    private fun authenticateUser() {
        if (!inputValidationProvider.isEditTextFilled(et_username)) {
            return
        }
        if (!inputValidationProvider.isEditTextFilled(et_password)) {
            return
        }
        val username: String = et_username.text.toString()
        val password: String = et_password.text.toString()
        subscribeObservers(username, password, userAccount)
    }

    private fun subscribeObservers(
        username: String,
        password: String,
        userAccount: UserAccount?
    ) {
        when (userAccount) {
            UserAccount.PARENT -> {
                authViewModel.authenticatingParent(username, password).observe(this, Observer { resource ->
                    if (resource.data?.role != LOGIN_ROLE_OPTIONS[0].toLowerCase()) {
                        showErrorMessage(true)
                        return@Observer
                    }
                    loadDashboard(resource, userAccount)
                })
            }
            UserAccount.TEACHER -> {
                authViewModel.authenticatingTeacher(username, password).observe(this, Observer { resource ->
                    if (resource.data?.role != LOGIN_ROLE_OPTIONS[1].toLowerCase()) {
                        showErrorMessage(true)
                        return@Observer
                    }
                    loadDashboard(resource, userAccount)
                })
            }
            null -> {
                throw NullPointerException("login role option cannot be null")
            }
        }
    }

    private fun loadDashboard(
        resource: Resource<UserEntity>,
        userAccount: UserAccount
    ) {
        when (resource.status) {
            Status.SUCCESS -> {
                showLoadingDialog(false)
                showErrorMessage()
                preferenceProvider.setUserLoginRole(LOGIN_ROLE_OPTIONS[0])
                preferenceProvider.setUserLogin(true, resource.data?.token)
                Timber.i("user authenticated with id: ${resource.data?.id}")
                when (userAccount) {
                    UserAccount.PARENT -> {
                        startActivity(intentFor<ParentNavigationActivity>().newTask().clearTask())
                        finish()
                    }
                    UserAccount.TEACHER -> {
                        startActivity(intentFor<TeacherNavigationActivity>().newTask().clearTask())
                        finish()
                    }
                }
            }
            Status.ERROR -> {
                showLoadingDialog(false)
                showErrorMessage(true)
                toast("${resource.message}")
                Timber.i(resource.message)
            }
            Status.LOADING -> {
                Timber.i("loading...")
                showErrorMessage()
                showLoadingDialog()
            }
        }
    }

    private fun showErrorMessage(show: Boolean = false) {
        if (show) {
            label_msg_error.visibility = View.VISIBLE
            label_msg_error.text = getString(R.string.auth_failed_message)
            return
        }
        label_msg_error.visibility = View.GONE
        label_msg_error.text = ""
    }

    private fun showLoadingDialog(show: Boolean = true) {
        showAnyView(progressBar, null, null, show) { view, _, _, visible ->
            if (visible) {
                (view as ProgressBar).visibility = View.VISIBLE
            } else {
                (view as ProgressBar).visibility = View.GONE
            }
        }
    }

    private fun configureViewModel() {
        authViewModel = ViewModelProviders.of(this, viewModelFactory)[AuthViewModel::class.java]
    }
}

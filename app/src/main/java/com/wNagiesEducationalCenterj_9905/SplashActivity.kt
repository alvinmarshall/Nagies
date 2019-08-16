package com.wNagiesEducationalCenterj_9905

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wNagiesEducationalCenterj_9905.base.BaseActivity
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import com.wNagiesEducationalCenterj_9905.ui.auth.RoleActivity
import com.wNagiesEducationalCenterj_9905.ui.auth.viewmodel.AuthViewModel
import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import com.wNagiesEducationalCenterj_9905.ui.teacher.TeacherNavigationActivity
import com.wNagiesEducationalCenterj_9905.vo.Resource
import com.wNagiesEducationalCenterj_9905.vo.Status
import kotlinx.coroutines.launch
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SplashActivity : BaseActivity() {
    private val getLoginStatus by lazy { preferenceProvider.getUserLoginStatus() }
    @Inject
    lateinit var authViewModel: AuthViewModel
    private var userAccount: UserAccount? = null
    private var shouldFetch: Boolean? = false
    private var extras = "default"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.flags == Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) {
            shouldFetch = getFetchValue()
        }

        configViewModel()
        skipLoginPage()
    }

    private fun getFetchValue(): Boolean {
        if (intent.hasExtra(NOTIFICATION_EXTRA_MESSAGE)) {
            extras = NOTIFICATION_EXTRA_MESSAGE
            return intent.getBooleanExtra(NOTIFICATION_EXTRA_MESSAGE, false)
        }
        if (intent.hasExtra(NOTIFICATION_EXTRA_REPORT)) {
            extras = NOTIFICATION_EXTRA_REPORT
            return intent.getBooleanExtra(NOTIFICATION_EXTRA_REPORT, false)
        }

        if (intent.hasExtra(NOTIFICATION_EXTRA_ASSIGNMENT)) {
            extras = NOTIFICATION_EXTRA_ASSIGNMENT
            return intent.getBooleanExtra(NOTIFICATION_EXTRA_ASSIGNMENT, false)
        }

        if (intent.hasExtra(NOTIFICATION_EXTRA_COMPLAINT)) {
            extras = NOTIFICATION_EXTRA_COMPLAINT
            return intent.getBooleanExtra(NOTIFICATION_EXTRA_COMPLAINT, false)
        }

        if (intent.hasExtra(MESSAGE_RECEIVE_EXTRA)) {
            return intent.getBooleanExtra(MESSAGE_RECEIVE_EXTRA, false)
        }
        return false
    }

    private fun subscribeObserver() {
        authViewModel.cachedUser.observe(this, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    Timber.i("AUTHENTICATED...")
                    loadAccountDashboard(resource)
                }
                Status.ERROR ->{
                    toast("${resource.message}")
                    Timber.i(resource.message)
                }
                Status.LOADING -> {
                }
            }
        })
    }

    private fun loadAccountDashboard(resource: Resource<UserEntity>) {
        userAccount = when (resource.data?.role) {
            LOGIN_ROLE_OPTIONS[0].toLowerCase() -> {
                UserAccount.PARENT
            }
            LOGIN_ROLE_OPTIONS[1].toLowerCase() -> {
                UserAccount.TEACHER
            }
            else -> null
        }
        val userInfo = arrayListOf<String?>()
        userInfo.add(resource.data?.username)
        userInfo.add(resource.data?.photo)
        startDashboard(userInfo, userAccount)
    }

    private fun startDashboard(userInfo: ArrayList<String?>, userAccount: UserAccount?) {
        userAccount?.let { account ->
            when (account) {
                UserAccount.PARENT -> {
                    Timber.i("starting parent dashboard")
                    startActivity(
                        intentFor<ParentNavigationActivity>(
                            USER_INFO to userInfo,
                            extras to shouldFetch
                        )
                    )
                    finish()
                }
                UserAccount.TEACHER -> {
                    Timber.i("starting teachers dashboard")
                    startActivity(
                        intentFor<TeacherNavigationActivity>(
                            USER_INFO to userInfo,
                            extras to shouldFetch
                        )
                    )
                    finish()
                }
            }
        }
    }

    private fun skipLoginPage() = launch {
        when (getLoginStatus) {
            true -> {
                Timber.i("user login status $getLoginStatus ")
                authViewModel.authenticateWithToken()
            }
            false -> {
                Timber.i("user login status $getLoginStatus ")
                startActivity(intentFor<RoleActivity>())
                finish()
            }
        }
    }

    private fun configViewModel() {
        authViewModel = ViewModelProviders.of(this, viewModelFactory)[AuthViewModel::class.java]
        subscribeObserver()
    }
}

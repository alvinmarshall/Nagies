package com.wNagiesEducationalCenterj_9905

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wNagiesEducationalCenterj_9905.base.BaseActivity
import com.wNagiesEducationalCenterj_9905.common.LOGIN_ROLE_OPTIONS
import com.wNagiesEducationalCenterj_9905.common.USER_INFO
import com.wNagiesEducationalCenterj_9905.common.delegate.lazyDeferred
import com.wNagiesEducationalCenterj_9905.ui.auth.RoleActivity
import com.wNagiesEducationalCenterj_9905.ui.auth.viewmodel.AuthViewModel
import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import com.wNagiesEducationalCenterj_9905.ui.teacher.TeacherNavigationActivity
import com.wNagiesEducationalCenterj_9905.vo.AuthStatus
import kotlinx.coroutines.launch
import org.jetbrains.anko.intentFor
import timber.log.Timber
import javax.inject.Inject

class SplashActivity : BaseActivity() {
    @Inject
    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configViewModel()
        skipLoginPage()

    }

    private fun configViewModel() {
        authViewModel = ViewModelProviders.of(this, viewModelFactory)[AuthViewModel::class.java]
    }

    private fun skipLoginPage() = launch {
        val getLoginStatus by lazyDeferred {
            preferenceProvider.getUserLoginStatus()
        }
        val getLoginToken by lazyDeferred {
            preferenceProvider.getUserToken()
        }
        val getLoginRole by lazyDeferred{
            preferenceProvider.getUserLoginRole()
        }
        when (getLoginStatus.await()) {
            true -> {
                Timber.i("login status is true")
                getLoginToken.await()?.let { authViewModel.authenticateWithToken(it) }
                getLoginRole.await()?.let { subscribeObserver(it) }
            }
            false -> {
                Timber.i("login status is false")
                startActivity(intentFor<RoleActivity>())
                finish()
            }
        }
    }

    private fun subscribeObserver(role:String) {
        authViewModel.authCachedUserData().observe(this, Observer { it ->
            it?.let {
                when(it.status){
                    AuthStatus.LOADING -> {}
                    AuthStatus.AUTHENTICATED -> {
                        Timber.i("welcome back ")
                        val userInfo = arrayListOf<String?>()
                        userInfo.add(it.data?.username)
                        userInfo.add(it.data?.photo)
                        startDashboard(role,userInfo)
                    }
                    AuthStatus.ERROR -> {}
                    AuthStatus.LOG_OUT ->{}
                }
            }
        })
    }

    private fun startDashboard(role: String, userInfo: ArrayList<String?>) {
        when(role){
            LOGIN_ROLE_OPTIONS[0] -> {
                startActivity(intentFor<ParentNavigationActivity>(USER_INFO to userInfo))
                finish()
            }
            LOGIN_ROLE_OPTIONS[1] -> {
                startActivity(intentFor<TeacherNavigationActivity>(USER_INFO to userInfo))
                finish()
            }
        }
    }
}

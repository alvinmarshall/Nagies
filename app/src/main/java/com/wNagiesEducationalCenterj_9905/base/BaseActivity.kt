package com.wNagiesEducationalCenterj_9905.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wNagiesEducationalCenterj_9905.SessionManager
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.ui.auth.RoleActivity
import com.wNagiesEducationalCenterj_9905.vo.AuthStatus
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity:DaggerAppCompatActivity(),CoroutineScope{
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var preferenceProvider: PreferenceProvider
    @Inject lateinit var sessionManager: SessionManager
    private var job: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        observeAuthState()
    }

    private fun observeAuthState() {
        sessionManager.getCachedUser().observe(this, Observer {
            it?.let {authResource ->
                when(authResource.status){
                    AuthStatus.LOADING -> Unit
                    AuthStatus.AUTHENTICATED -> {Timber.i("welcome user")}
                    AuthStatus.ERROR -> {}
                    AuthStatus.LOG_OUT -> {redirectToAuthPage()}
                }

            }

        })
    }

    private fun redirectToAuthPage() {
        Timber.i("redirected to role page")
        startActivity(intentFor<RoleActivity>().newTask().clearTask())
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
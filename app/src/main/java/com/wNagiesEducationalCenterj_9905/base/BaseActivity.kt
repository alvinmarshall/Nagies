package com.wNagiesEducationalCenterj_9905.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity:DaggerAppCompatActivity(),CoroutineScope{
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var preferenceProvider: PreferenceProvider

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }
    private var job: Job = Job()

    protected fun showPasswordResetDialog(alertDialog:AlertDialog.Builder?) {
        alertDialog?.setIcon(R.drawable.ic_security_black_24dp)
        alertDialog?.setTitle(getString(R.string.password_reset_notification_dialog_title))
        alertDialog?.setMessage(getString(R.string.password_reset_notification_diaload_body))
        alertDialog?.setPositiveButton("ok") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
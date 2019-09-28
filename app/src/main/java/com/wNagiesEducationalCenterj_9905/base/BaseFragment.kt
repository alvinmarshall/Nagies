package com.wNagiesEducationalCenterj_9905.base

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.common.utils.ConnectionLiveData
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : DaggerFragment(), CoroutineScope {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var preferenceProvider: PreferenceProvider
    private var connectionLiveData: ConnectionLiveData? = null
    private val job = Job()
    private var alertDialog: AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = this.context?.let { ConnectionLiveData(it) }
        alertDialog = context?.let { AlertDialog.Builder(it) }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun getNetworkState(): ConnectionLiveData? {
        return connectionLiveData
    }

    //region Permission
    protected fun showStorageRational(title: String, message: String) {
        alertDialog?.setTitle(title)
        alertDialog?.setMessage(message)
        alertDialog?.setPositiveButton("retry") { dialog, _ ->
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_EXTERNAL_STORAGE
                )
            }
            dialog.dismiss()
        }
        alertDialog?.setNegativeButton("i'm sure", null)
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    protected fun dialogForSettings(title: String, message: String) {
        alertDialog?.setTitle(title)
        alertDialog?.setMessage(message)
        alertDialog?.setPositiveButton("settings") { dialog, _ ->
            goToSettings()
            dialog.dismiss()
        }
        alertDialog?.setNegativeButton("not now", null)
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    private fun goToSettings() {
        val openAppSettings = Intent()
        openAppSettings.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.parse("package:${context?.packageName}")
        openAppSettings.data = uri
        startActivity(openAppSettings)
    }

    //endregion

}
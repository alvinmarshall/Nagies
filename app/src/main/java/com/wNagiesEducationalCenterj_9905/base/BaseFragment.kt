package com.wNagiesEducationalCenterj_9905.base

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.common.utils.ConnectionLiveData
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : DaggerFragment(), CoroutineScope {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var connectionLiveData: ConnectionLiveData? = null
    private val job = Job()
    private var messageReceiver: BroadcastReceiver? = null
    private var fetchMessage: MutableLiveData<Boolean> = MutableLiveData()
    private var fetchAssignment: MutableLiveData<Boolean> = MutableLiveData()
    private var fetchReport: MutableLiveData<Boolean> = MutableLiveData()
    private var fetchComplaint: MutableLiveData<Boolean> = MutableLiveData()
    private var alertDialog: AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = this.context?.let { ConnectionLiveData(it) }
        alertDialog = context?.let { AlertDialog.Builder(it) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        messageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    MESSAGE_BROADCAST_ACTION -> {
                        setFetchValue(intent)
                    }
                }
            }
        }
    }

    private fun setFetchValue(intent: Intent) {
        if (intent.hasExtra(NOTIFICATION_EXTRA_MESSAGE)) {
            fetchMessage.value = intent.getBooleanExtra(NOTIFICATION_EXTRA_MESSAGE, false)
        }
        if (intent.hasExtra(NOTIFICATION_EXTRA_REPORT)) {
            fetchReport.value = intent.getBooleanExtra(NOTIFICATION_EXTRA_REPORT, false)
        }

        if (intent.hasExtra(NOTIFICATION_EXTRA_ASSIGNMENT)) {
            fetchAssignment.value = intent.getBooleanExtra(NOTIFICATION_EXTRA_ASSIGNMENT, false)
        }

        if (intent.hasExtra(NOTIFICATION_EXTRA_COMPLAINT)) {
            fetchComplaint.value = intent.getBooleanExtra(NOTIFICATION_EXTRA_COMPLAINT, false)
        }

        if (intent.hasExtra(MESSAGE_RECEIVE_EXTRA)) {
            fetchMessage.value = intent.getBooleanExtra(MESSAGE_RECEIVE_EXTRA, false)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.registerReceiver(messageReceiver, getReceiver())
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(messageReceiver)
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

    private fun getReceiver(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(MESSAGE_BROADCAST_ACTION)
        return intentFilter
    }

    protected fun getFetchMessage(): LiveData<Boolean> {
        return fetchMessage
    }

    protected fun getFetchAssignment(): LiveData<Boolean> {
        return fetchAssignment
    }

    protected fun getFetchReport(): LiveData<Boolean> {
        return fetchReport
    }

    protected fun getFetchComplaint(): LiveData<Boolean> {
        return fetchComplaint
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
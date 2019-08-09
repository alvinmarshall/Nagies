package com.wNagiesEducationalCenterj_9905.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.common.utils.ConnectionLiveData
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.anko.toast
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : DaggerFragment(), CoroutineScope {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var connectionLiveData: ConnectionLiveData? = null
    private val job = Job()
    private var messageReceiver: BroadcastReceiver? = null
    private var fetch: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = this.context?.let { ConnectionLiveData(it) }
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
            fetch.value = intent.getBooleanExtra(NOTIFICATION_EXTRA_MESSAGE, false)
            return
        }
        if (intent.hasExtra(NOTIFICATION_EXTRA_REPORT)) {
            fetch.value = intent.getBooleanExtra(NOTIFICATION_EXTRA_REPORT, false)
            return
        }

        if (intent.hasExtra(NOTIFICATION_EXTRA_ASSIGNMENT)) {
            fetch.value = intent.getBooleanExtra(NOTIFICATION_EXTRA_ASSIGNMENT, false)
            return
        }

        if (intent.hasExtra(MESSAGE_RECEIVE_EXTRA)) {
            fetch.value = intent.getBooleanExtra(MESSAGE_RECEIVE_EXTRA, false)
            return
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

    protected fun getShouldFetch(): LiveData<Boolean> {
        return fetch
    }


}
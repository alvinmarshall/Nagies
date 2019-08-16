package com.wNagiesEducationalCenterj_9905.notification

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wNagiesEducationalCenterj_9905.App
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.*
import timber.log.Timber

class FirebaseMessageService : FirebaseMessagingService() {
    private var appNotificationManager: AppNotificationManager? = null
    override fun onCreate() {
        super.onCreate()
        appNotificationManager = (applicationContext as App).getAppComponent().appNotification()
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        p0?.data?.let {
            if (it.isNotEmpty()) {
                val extras = when (it["type"]) {
                    getString(R.string.notification_type_message) -> NOTIFICATION_EXTRA_MESSAGE
                    getString(R.string.notification_type_report) -> NOTIFICATION_EXTRA_REPORT
                    getString(R.string.notification_type_assignment) -> NOTIFICATION_EXTRA_ASSIGNMENT
                    getString(R.string.notification_type_complaint) -> NOTIFICATION_EXTRA_COMPLAINT
                    else -> MESSAGE_RECEIVE_EXTRA
                }
                val intent = Intent()
                intent.putExtra(extras, true)
                intent.action = MESSAGE_BROADCAST_ACTION
                sendBroadcast(intent)
                appNotificationManager?.triggerNotification(it["title"], it["message"], it["type"])
            }
        }

        Timber.i("remote message received")
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Timber.i("new token: $p0")
    }

}
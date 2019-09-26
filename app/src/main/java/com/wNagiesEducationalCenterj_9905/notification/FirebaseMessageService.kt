package com.wNagiesEducationalCenterj_9905.notification

import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wNagiesEducationalCenterj_9905.App
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.*
import timber.log.Timber

class FirebaseMessageService : FirebaseMessagingService() {
    private var appNotificationManager: AppNotificationManager? = null
    private var localUsername: String? = null
    private var localLevel: String? = null
    private var preferences: SharedPreferences? = null
    override fun onCreate() {
        super.onCreate()
        preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        localUsername = preferences?.getString(USER_FULL_NAME, null)
        localLevel = preferences?.getString(USER_LEVEL_NAME, null)
        appNotificationManager = (applicationContext as App).getAppComponent().appNotification()
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        val data = p0?.data

        if (!data.isNullOrEmpty()) {
            val type = data["type"]
            val name = data["name"]
            val level = data["level"]
            val title = data["title"]
            val body = data["body"]
            prepareNotification(type, title, body, name, level)
        }
        p0?.notification?.let {
            Timber.i("notification message")
        }
    }


    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Timber.i("new token: $p0")
    }

    private fun prepareNotification(
        type: String?,
        title: String?,
        body: String?,
        name: String?,
        level: String?
    ) {

        val extras = when (type) {
            getString(R.string.notification_type_password_reset) -> {
                if (localUsername == name) {
                    NOTIFICATION_EXTRA_MESSAGE
                } else {
                    null
                }
            }
            getString(R.string.notification_type_message) -> {
                if (localLevel == level) {
                    NOTIFICATION_EXTRA_MESSAGE
                } else {
                    null
                }
            }
            getString(R.string.notification_type_report) -> {
                if (localUsername == name) {
                    NOTIFICATION_EXTRA_REPORT
                } else {
                    null
                }
            }
            getString(R.string.notification_type_assignment) -> {
                if (localLevel == level) {
                    NOTIFICATION_EXTRA_ASSIGNMENT
                } else {
                    null
                }
            }
            getString(R.string.notification_type_complaint) -> {
                if (localUsername == name) {
                    NOTIFICATION_EXTRA_COMPLAINT
                } else {
                    null
                }
            }
            else -> null
        }

        extras?.let {
            val intent = Intent()
            intent.putExtra(it, true)
            intent.action = MESSAGE_BROADCAST_ACTION
            sendBroadcast(intent)
            appNotificationManager?.triggerNotification(title, body, type)
        }


    }
}
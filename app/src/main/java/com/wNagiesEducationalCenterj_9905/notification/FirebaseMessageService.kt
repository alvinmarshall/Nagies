package com.wNagiesEducationalCenterj_9905.notification

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.common.utils.NotificationUtils
import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import com.wNagiesEducationalCenterj_9905.ui.teacher.TeacherNavigationActivity
import com.wNagiesEducationalCenterj_9905.vo.NotificationPayload
import timber.log.Timber

class FirebaseMessageService : FirebaseMessagingService() {
    private var preferences: SharedPreferences? = null
    private var payload: NotificationPayload? = null
    override fun onCreate() {
        super.onCreate()
        preferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        p0.notification?.let {
            Timber.i("push notification message")
        }
        val data = p0.data

        if (data.isNullOrEmpty()) return
        Timber.i("push notification data")

        payload = NotificationPayload(data["type"], data["name"], data["level"], data["title"], data["body"])
        val extras = payload?.getNotificationExtras(baseContext, preferences) ?: return
        handleNotification(extras)
    }

    private fun handleNotification(extras: String?) {
        val isBackground = NotificationUtils.isAppIsInBackground(applicationContext)
        Timber.i("app is in foreground ? $isBackground")
        // app is in foreground
        if (!isBackground) {
            val pushNotification = Intent(FOREGROUND_PUSH_NOTIFICATION)
            pushNotification.putExtra(FOREGROUND_PUSH_NOTIFICATION_EXTRA, extras)
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
            NotificationUtils.playNotificationSound(baseContext)
            return
        }
        // app is in background
        payload?.getCurrentRole(preferences)?.let { role ->
            val resultIntent = when (role) {
                LOGIN_ROLE_OPTIONS[0] -> {
                    Intent(applicationContext, ParentNavigationActivity::class.java)
                }
                LOGIN_ROLE_OPTIONS[1] -> {
                    Intent(applicationContext, TeacherNavigationActivity::class.java)
                }
                else -> null
            }

            resultIntent?.putExtra(NOTIFICATION_MESSAGE_EXTRAS, extras)
            resultIntent?.let { notifyUser(applicationContext, payload?.title, payload?.body, it) }
        }
    }

    private fun notifyUser(applicationContext: Context, title: String?, body: String?, resultIntent: Intent) {
        resultIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        NotificationUtils.showNotificationMessage(applicationContext, title, body, resultIntent)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Timber.i("new token: $p0")
        val editor = preferences?.edit()
        editor?.putString(FIREBASE_TOKEN_ID, p0)
        editor?.apply()
    }

}
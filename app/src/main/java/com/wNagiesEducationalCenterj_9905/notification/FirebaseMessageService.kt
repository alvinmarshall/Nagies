package com.wNagiesEducationalCenterj_9905.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wNagiesEducationalCenterj_9905.App
import timber.log.Timber

class FirebaseMessageService : FirebaseMessagingService() {
    private var appNotificationManager: AppNotificationManager? = null
    override fun onCreate() {
        super.onCreate()
        appNotificationManager = (applicationContext as App).getAppComponent().appNotification()
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        appNotificationManager?.triggerNotification(
            p0?.notification?.title,
            p0?.notification?.body
        )
        Timber.i("remote message received")
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Timber.i("new token: $p0")
    }

}
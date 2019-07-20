package com.wNagiesEducationalCenterj_9905.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.SplashActivity
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class AppNotificationManager @Inject constructor(val context: Application) {

    fun registerNotificationChannel(channelId: String, channelName: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationChannel.description = channelDescription
            notificationChannel.canShowBadge()
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun triggerNotification(title: String?, msg: String?) {
        val intent = Intent(context, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notificationBuilder =
            NotificationCompat.Builder(context, context.getString(R.string.announcement_channel_id))
                .setSmallIcon(R.drawable.ic_business_black_24dp)
                .setContentText(title)
                .setContentTitle(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(100, notificationBuilder)
    }

    fun customNotification(
        targetClass: Class<Any>,
        channelId: String,
        title: String,
        msg: String?,
        priority: Int,
        notificationId: Int
    ) {
        val intent = Intent(context, targetClass::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notificationBuilder =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_business_black_24dp)
                .setContentText(title)
                .setContentTitle(msg)
                .setPriority(priority)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(notificationId, notificationBuilder)
    }
}
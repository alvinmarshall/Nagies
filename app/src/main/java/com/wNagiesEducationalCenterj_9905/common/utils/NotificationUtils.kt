package com.wNagiesEducationalCenterj_9905.common.utils

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.wNagiesEducationalCenterj_9905.R
import kotlin.math.floor

object NotificationUtils {
    fun showNotificationMessage(context: Context, title: String?, message: String?, intent: Intent) {
        val notificationSoundUrl =
            "${ContentResolver.SCHEME_ANDROID_RESOURCE}//${context.packageName}/raw/notification"
        val notificationSound = Uri.parse(notificationSoundUrl)
        val mBuilder =
            NotificationCompat.Builder(context, context.getString(R.string.announcement_channel_id))
        val iconRes: Int = R.mipmap.ic_launcher
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val notificationId = floor(Math.random() * 20).toInt()
        val resultPendingIntent =
            PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        showNotification(
            context,
            mBuilder,
            iconRes,
            title,
            message,
            resultPendingIntent,
            notificationSound,
            notificationId
        )
        playNotificationSound(context)
    }

    fun playNotificationSound(context: Context) {
        try {
            val notificationSoundUrl =
                "${ContentResolver.SCHEME_ANDROID_RESOURCE}//${context.packageName}/raw/notification"
            val notificationSound = Uri.parse(notificationSoundUrl)
            val ringtone: Ringtone = RingtoneManager.getRingtone(context, notificationSound)
            ringtone.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showNotification(
        context: Context,
        mBuilder: NotificationCompat.Builder,
        iconRes: Int,
        title: String?,
        message: String?,
        resultPendingIntent: PendingIntent?,
        notificationSound: Uri?,
        notificationId: Int
    ) {
        val bigTextStyle: NotificationCompat.BigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.bigText(message)
        val notification = mBuilder.setSmallIcon(iconRes).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentIntent(resultPendingIntent)
            .setSound(notificationSound)
            .setStyle(bigTextStyle)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(iconRes)
            .setContentText(message)
            .build()
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notificationId, notification)
    }

    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val activityManager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcess = activityManager.runningAppProcesses
        runningProcess.forEach { processInfo ->
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                processInfo.pkgList.forEach { activeProcess ->
                    if (activeProcess == context.packageName) {
                        isInBackground = false
                    }
                }
            }
        }
        return isInBackground
    }

//    fun clearNotifications(context: Context) {
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.cancelAll()
//    }

    fun registerNotificationChannel(context: Context) {
        val channelId = context.getString(R.string.announcement_channel_id)
        val channelName = context.getString(R.string.announcement_channel)
        val channelDescription = context.getString(R.string.announcement_description)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val sound = Settings.System.DEFAULT_NOTIFICATION_URI
            val attribute = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationChannel.description = channelDescription
            notificationChannel.canShowBadge()
            notificationChannel.setSound(sound, attribute)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}
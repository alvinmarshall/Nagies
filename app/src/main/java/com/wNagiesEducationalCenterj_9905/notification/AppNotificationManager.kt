package com.wNagiesEducationalCenterj_9905.notification

import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import kotlin.math.floor


class AppNotificationManager (val context: Application) {
    private val notificationSoundUrl =
        "${ContentResolver.SCHEME_ANDROID_RESOURCE}//${context.packageName}/raw/notification"
    private val notificationSound = Uri.parse(notificationSoundUrl)
    private val mBuilder =
        NotificationCompat.Builder(context, context.getString(R.string.announcement_channel_id))

    fun registerNotificationChannel(channelId: String, channelName: String, channelDescription: String) {
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

    fun triggerNotification(title: String?, msg: String?, type: String?) {
        val extras = when (type) {
            context.getString(R.string.notification_type_message) -> NOTIFICATION_EXTRA_MESSAGE
            context.getString(R.string.notification_type_report_image) -> NOTIFICATION_EXTRA_REPORT
            context.getString(R.string.notification_type_assignment_image) -> NOTIFICATION_EXTRA_ASSIGNMENT
            context.getString(R.string.notification_type_complaint) -> NOTIFICATION_EXTRA_COMPLAINT
            else -> MESSAGE_RECEIVE_EXTRA
        }
        val intent = Intent(context, ParentNavigationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(extras, true)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder =
            NotificationCompat.Builder(context, context.getString(R.string.announcement_channel_id))
                .setSmallIcon(R.drawable.ic_business_black_24dp)
                .setContentText(title)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        val id = floor(Math.random() * 20).toInt()
        notificationManagerCompat.notify(id, notificationBuilder)
    }

    fun showNotificationMessage(title: String?, message: String?, type: String?, intent: Intent) {
        val iconRes: Int = R.mipmap.ic_launcher
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val resultPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        showNotificationWithNoImage(mBuilder, iconRes, title, message, resultPendingIntent, notificationSound)
        playNotificationSound()

    }

    private fun playNotificationSound() {
        try {
            val ringtone: Ringtone = RingtoneManager.getRingtone(context, notificationSound)
            ringtone.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showNotificationWithNoImage(
        mBuilder: NotificationCompat.Builder,
        iconRes: Int,
        title: String?,
        message: String?,
        resultPendingIntent: PendingIntent?,
        notificationSound: Uri?
    ) {
        val inboxStyle: NotificationCompat.InboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.addLine(message)
        val notification = mBuilder.setSmallIcon(iconRes).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentIntent(resultPendingIntent)
            .setSound(notificationSound)
            .setStyle(inboxStyle)
            .setSmallIcon(iconRes)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, iconRes))
            .setContentText(message)
            .build()
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = floor(Math.random() * 20).toInt()
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
            } else {
                isInBackground = false
            }
        }
        return isInBackground
    }

    fun clearNotifications(context: Context){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

    }



}
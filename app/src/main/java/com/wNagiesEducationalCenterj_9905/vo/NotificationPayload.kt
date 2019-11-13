package com.wNagiesEducationalCenterj_9905.vo

import android.content.Context
import android.content.SharedPreferences
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.*

data class NotificationPayload(
    var type: String?,
    var name: String?,
    var level: String?,
    var title: String?,
    var body: String?
) {

    fun getNotificationExtras(context: Context, preferences: SharedPreferences?): String? {
        val localUsername = preferences?.getString(USER_FULL_NAME_PREF_KEY, null)
        val localLevel = preferences?.getString(USER_LEVEL_NAME_PREF_KEY, null)
        return when (type) {
            context.getString(R.string.notification_type_password_reset) -> {
                if (localUsername == name) {
                    NAVIGATE_TO_DIALOG_RESET_PASSWORD
                } else {
                    null
                }
            }
            context.getString(R.string.notification_type_message) -> {
                if (localLevel == level || localUsername == name) {
                    NAVIGATE_TO_DASHBOARD
                } else {
                    null
                }
            }
            context.getString(R.string.notification_type_report_image) -> {
                if (localUsername == name) {
                    NAVIGATE_TO_REPORT_IMAGE
                } else {
                    null
                }
            }

            context.getString(R.string.notification_type_report_pdf) -> {
                if (localUsername == name) {
                    NAVIGATE_TO_REPORT_PDF
                } else {
                    null
                }
            }


            context.getString(R.string.notification_type_assignment_image) -> {
                if (localLevel == level) {
                    NAVIGATE_TO_ASSIGNMENT_IMAGE
                } else {
                    null
                }
            }

            context.getString(R.string.notification_type_assignment_pdf) -> {
                if (localLevel == level) {
                    NAVIGATE_TO_ASSIGNMENT_PDF
                } else {
                    null
                }
            }
            context.getString(R.string.notification_type_complaint) -> {
                if (localUsername == name) {
                    NAVIGATION_TO_COMPLAINT
                } else {
                    null
                }
            }
            context.getString(R.string.notification_type_announcement) -> {
                NAVIGATE_TO_ANNOUNCEMENT
            }
            else -> null
        }
    }

    fun getCurrentRole(preferences: SharedPreferences?): String? {
        return preferences?.getString(USER_SELECTED_ROLE_PREF_KEY, null)
    }
}
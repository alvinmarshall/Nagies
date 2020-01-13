package com.wNagiesEducationalCenterj_9905.common.utils

import android.app.Application
import android.content.SharedPreferences
import com.wNagiesEducationalCenterj_9905.BuildConfig
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.common.extension.toString
import com.wNagiesEducationalCenterj_9905.vo.SessionData
import java.util.*
import javax.inject.Inject

class PreferenceProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val context: Application
) {
    fun setUserLogin(isLogin: Boolean, token: String?) {
        val preferences = sharedPreferences.edit()
        preferences.putBoolean(USER_LOGIN_STATUS_PREF_KEY, isLogin)
        preferences.putString(USER_TOKEN_PREF_KEY, token)
        preferences.apply()
    }

    fun setUserLoginRole(role: String?) {
        val preferences = sharedPreferences.edit()
        preferences.putString(USER_SELECTED_ROLE_PREF_KEY, role)
        preferences.apply()
    }

    fun setUserBasicInfo(username: String?, name: String?, level: String?, imageUrl: String? = null) {
        val preferences = sharedPreferences.edit()
        preferences.putString(USER_NAME_PREF_KEY, username)
        preferences.putString(USER_FULL_NAME_PREF_KEY, name)
        preferences.putString(USER_LEVEL_NAME_PREF_KEY, level)
        preferences.putString(USER_IMAGE_URL_PREF_KEY, imageUrl)
        preferences.apply()
    }

    fun setNotificationCallback(key: String, extra: Boolean) {
        val preferences = sharedPreferences.edit()
        preferences.putBoolean(key, extra)
        preferences.apply()
    }

    fun getNotificationCallback(key: String): Boolean? {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getUserSessionData(): SessionData {
        val name = sharedPreferences.getString(USER_FULL_NAME_PREF_KEY, null)
        val imageUrl = sharedPreferences.getString(USER_IMAGE_URL_PREF_KEY, null)
        val level = sharedPreferences.getString(USER_LEVEL_NAME_PREF_KEY, null)
        val sessionData = SessionData(name, level, imageUrl)
        sessionData.username = sharedPreferences.getString(USER_NAME_PREF_KEY, null)
        sessionData.token = sharedPreferences.getString(USER_TOKEN_PREF_KEY, null)
        sessionData.loginStatus = sharedPreferences.getBoolean(USER_LOGIN_STATUS_PREF_KEY, false)
        sessionData.userRole = sharedPreferences.getString(USER_SELECTED_ROLE_PREF_KEY, null)
        return sessionData
    }


    fun getUserToken(): String? {
        return sharedPreferences.getString(USER_TOKEN_PREF_KEY, null)
    }

    fun getUserLoginRole(): String? {
        return sharedPreferences.getString(USER_SELECTED_ROLE_PREF_KEY, null)
    }

    fun setFetchDate(type: FetchType) {
        val prefTitle = when (type) {
            FetchType.ASSIGNMENT_PDF -> context.getString(R.string.fetch_assignment_pdf)
            FetchType.ASSIGNMENT_IMAGE -> context.getString(R.string.fetch_assignment_image)
            FetchType.REPORT_PDF -> context.getString(R.string.fetch_report_pdf)
            FetchType.REPORT_IMAGE -> context.getString(R.string.fetch_report_image)
            FetchType.MESSAGE -> context.getString(R.string.fetch_message)
            FetchType.ANNOUNCEMENT -> context.getString(R.string.fetch_announcement)
            FetchType.COMPLAINT -> context.getString(R.string.fetch_complaint)
            FetchType.CLASS_TEACHER -> context.getString(R.string.fetch_class_teacher)
            FetchType.CIRCULAR -> context.getString(R.string.fetch_circular)
            FetchType.BILLING -> context.getString(R.string.fetch_circular)
            FetchType.CLASS_STUDENT -> context.getString(R.string.fetch_class_student)
            FetchType.TIME_TABLE -> context.getString(R.string.fetch_timetable)
        }
        val preferences = sharedPreferences.edit()
        val date = Date().toString(DATE_FORMAT)
        preferences.putString(prefTitle, date)
        preferences.apply()
    }

    fun getFetchType(type: FetchType): Boolean {
        val fetchDate: String?
        return when (type) {
            FetchType.ASSIGNMENT_PDF -> {
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_assignment_pdf), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.ASSIGNMENT_IMAGE -> {
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_assignment_image), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.REPORT_PDF -> {
                true
//                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_report_pdf), null)
//                val minutes = getDifferenceInTime(fetchDate)
//                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.REPORT_IMAGE -> {
                true
//                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_report_image), null)
//                val minutes = getDifferenceInTime(fetchDate)
//                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.MESSAGE -> {
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_message), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.ANNOUNCEMENT -> {
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_announcement), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.COMPLAINT -> {
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_complaint), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.CLASS_TEACHER -> {
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_class_teacher), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.CIRCULAR -> {
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_circular), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.BILLING -> {
                true
//                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_billing), null)
//                val minutes = getDifferenceInTime(fetchDate)
//                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.CLASS_STUDENT -> {
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_class_student), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.TIME_TABLE -> {
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_timetable), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
        }

    }

    private fun getFetchInterval(): Int {
        val interval = sharedPreferences.getString(
            context.getString(R.string.pref_fetch_option_key),
            context.getString(R.string.pref_fetch_option_20_min_value)
        )
        if (BuildConfig.DEBUG) return 5
        return  interval?.toInt() ?: 20
    }


}
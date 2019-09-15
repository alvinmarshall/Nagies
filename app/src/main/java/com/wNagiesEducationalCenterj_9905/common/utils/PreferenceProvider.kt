package com.wNagiesEducationalCenterj_9905.common.utils

import android.app.Application
import android.content.SharedPreferences
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.common.extension.toString
import java.util.*
import javax.inject.Inject

class PreferenceProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val context: Application
) {
    fun setUserLogin(isLogin: Boolean, token: String?) {
        val preferences = sharedPreferences.edit()
        preferences.putBoolean(LOGIN_PREF, isLogin)
        preferences.putString(USER_TOKEN, token)
        preferences.apply()
    }

    fun setUserLoginRole(role: String?) {
        val preferences = sharedPreferences.edit()
        preferences.putString(SELECTED_ROLE, role)
        preferences.apply()
    }

    fun getUserLoginStatus(): Boolean {
        return sharedPreferences.getBoolean(LOGIN_PREF, false)
    }

    fun getUserToken(): String? {
        return sharedPreferences.getString(USER_TOKEN, null)
    }

    fun getUserLoginRole(): String? {
        return sharedPreferences.getString(SELECTED_ROLE, null)
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
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_report_pdf), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.REPORT_IMAGE -> {
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_report_image), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
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
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_billing), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.CLASS_STUDENT -> {
                fetchDate = sharedPreferences.getString(context.getString(R.string.fetch_class_student), null)
                val minutes = getDifferenceInTime(fetchDate)
                (minutes > getFetchInterval() && NetworkStateUtils.isOnline(context))
            }
            FetchType.TIME_TABLE ->{
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
        return interval?.toInt()?:20
    }


}
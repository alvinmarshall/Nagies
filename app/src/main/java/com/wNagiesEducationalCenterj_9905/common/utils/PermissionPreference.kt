package com.wNagiesEducationalCenterj_9905.common.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.wNagiesEducationalCenterj_9905.common.PERMISSION_PREF

object PermissionPreference {
    fun firstTimeAskingPermission(context: Context, permission: String, isFirstTime: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PERMISSION_PREF, MODE_PRIVATE)
        val preferences = sharedPreferences.edit()
        preferences.putBoolean(permission, isFirstTime)
        preferences.apply()
    }

    fun isFirstTimeAskingPermission(context: Context, permission: String): Boolean {
        return context.getSharedPreferences(PERMISSION_PREF, MODE_PRIVATE).getBoolean(permission, true)
    }
}
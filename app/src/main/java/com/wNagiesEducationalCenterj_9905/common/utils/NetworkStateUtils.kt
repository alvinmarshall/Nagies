package com.wNagiesEducationalCenterj_9905.common.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkStateUtils {
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}
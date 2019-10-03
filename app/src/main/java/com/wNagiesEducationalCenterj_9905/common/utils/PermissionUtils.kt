package com.wNagiesEducationalCenterj_9905.common.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale


object PermissionUtils {
    private fun shouldAskPermission():Boolean{
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    }

    private fun shouldAskPermission(context: Context,permission:String):Boolean{
        if (shouldAskPermission()) {
            val permissionResult = ActivityCompat.checkSelfPermission(context, permission)
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    fun checkPermission(context: Context,permission: String,listener:PermissionAskListener){
        if (shouldAskPermission(context, permission)) {
            if (shouldShowRequestPermissionRationale(context as AppCompatActivity, permission)) {
                listener.onPermissionPreviouslyDenied()
            } else {
                if (PermissionPreference.isFirstTimeAskingPermission(context,permission)) {
                    PermissionPreference.firstTimeAskingPermission(context,permission, false)
                    listener.onNeedPermission()
                } else {
                    listener.onPermissionDisabled()
                }
            }
        } else {
            listener.onPermissionGranted()
        }
    }
}

interface PermissionAskListener {
    fun onPermissionPreviouslyDenied()
    fun onNeedPermission()
    fun onPermissionDisabled()
    fun onPermissionGranted()
}

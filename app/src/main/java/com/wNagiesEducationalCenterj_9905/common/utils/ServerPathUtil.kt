package com.wNagiesEducationalCenterj_9905.common.utils

import com.wNagiesEducationalCenterj_9905.BuildConfig
import com.wNagiesEducationalCenterj_9905.common.DEV_SERVER_URL
import com.wNagiesEducationalCenterj_9905.common.SERVER_URL
import timber.log.Timber

object ServerPathUtil {
    fun setCorrectPath(path: String?): String? {
        if (BuildConfig.DEBUG){
            Timber.i("correct mode")
            return path?.let { "$DEV_SERVER_URL$path" }
        }
        return path?.let { "$SERVER_URL$path" }
    }
}
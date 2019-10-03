package com.wNagiesEducationalCenterj_9905.common.utils

import com.wNagiesEducationalCenterj_9905.common.SERVER_URL

object ServerPathUtil {
    fun setCorrectPath(path: String?): String? {
        return path?.let { "$SERVER_URL$path" }
    }
}
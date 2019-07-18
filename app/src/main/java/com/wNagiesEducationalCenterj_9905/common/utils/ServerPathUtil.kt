package com.wNagiesEducationalCenterj_9905.common.utils

import com.wNagiesEducationalCenterj_9905.common.SERVER_URL

object ServerPathUtil {
    fun setCorrectPath(path: String?): String? {
        var imageUrl: String? = null
        var extension: String?
        path?.let {
          if (it.contains(".")){
              extension = it.replaceBeforeLast(".","")
              imageUrl = it.replaceAfterLast(".","")
              imageUrl = imageUrl?.replace(".","")
              imageUrl = "$SERVER_URL$imageUrl$extension"
          }
        }
        return imageUrl
    }
}
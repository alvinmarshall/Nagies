package com.wNagiesEducationalCenterj_9905.common.utils

import com.wNagiesEducationalCenterj_9905.common.FileUploadFormat

object FileTypeUtils {
    @Suppress("SpellCheckingInspection")
    fun getType(path: String): String? {
        if (path.contains("pdf")) {
            return "application/pdf"
        } else if (path.contains(".jpg") || path.contains(".jpeg") || path.contains(".png")) {
            return "image/jpeg"
        } else if (path.contains(".txt")) {
            return "text/plain"
        } else if (path.contains(".gif")) {
            return "image/gif"
        } else if (path.contains(".rtf")) {
            return "application/rtf"
        } else if (path.contains(".doc") || path.contains(".docx")) {
            return "application/msword"
        } else if (path.contains(".xls") || path.contains(".xlsx")) {
            return "application/vnd.ms-excel"
        } else if (path.contains(".pptx") || path.contains(".ppt")) {
            return "application/vnd.ms-powerpoint"
        } else if (path.contains(".wav") || path.contains(".mp3")) {
            return "audio/x-wav"
        } else if (path.contains(".3gp") || path.contains(".mpg") ||
            path.contains(".mpeg") || path.contains(".mpe") || path.contains(
                ".avi"
            )
        ) {
            return "video/*"
        } else
            return "*/*"
    }

    fun getFileFormat(filename: String): FileUploadFormat? {
        return when (getType(filename)) {
            "application/pdf" -> FileUploadFormat.PDF
            "image/jpeg" -> FileUploadFormat.IMAGE
            else -> null
        }
    }
}
package com.wNagiesEducationalCenterj_9905.api.request

import com.wNagiesEducationalCenterj_9905.common.FileUploadFormat
import com.wNagiesEducationalCenterj_9905.common.UploadFileType
import okhttp3.MultipartBody

data class FileUploadRequest(
    val requestBody: MultipartBody.Part?
) {
    var studentInfo: StudentInfo? = null
    var format:FileUploadFormat? = null
    var fileType:UploadFileType? = null

}

data class StudentInfo(
    var studentNo: MultipartBody.Part?,
    var studentName:MultipartBody.Part?
)
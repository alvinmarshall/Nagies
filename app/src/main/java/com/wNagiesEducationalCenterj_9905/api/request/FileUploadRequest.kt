package com.wNagiesEducationalCenterj_9905.api.request

import okhttp3.MultipartBody

data class FileUploadRequest(
    val requestBody: MultipartBody.Part?
) {
    var studentInfo: StudentInfo? = null
}

data class StudentInfo(
    var studentNo: MultipartBody.Part?,
    var studentName:MultipartBody.Part?
)
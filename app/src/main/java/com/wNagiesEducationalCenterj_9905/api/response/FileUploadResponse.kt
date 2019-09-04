package com.wNagiesEducationalCenterj_9905.api.response


data class FileUploadResponse(
    val status: Int,
    val `data`: Data?
)

data class Data(
    val id: Int,
    val fileUrl: String,
    val format: String
)
package com.wNagiesEducationalCenterj_9905.common.utils

import android.content.Context
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.api.request.FileUploadRequest
import com.wNagiesEducationalCenterj_9905.api.request.StudentInfo
import com.wNagiesEducationalCenterj_9905.common.FileUploadFormat
import com.wNagiesEducationalCenterj_9905.common.UploadFileType
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class FileUploadUtil(
    private val context: Context,
    private val path: String?,
    private val isReport: Boolean,
    private val studentArr: Array<String>?
) {

    fun preparingToUpload(): FileUploadRequest? {
        if (path == null) return null
        val file = File(path)
        var requestBody: MultipartBody.Part? = null
        val format = FileTypeUtils.getFileFormat(file.name)
        var studentInfo: StudentInfo? = null
        if (isReport) {
            val studentNo =
                MultipartBody.Part.createFormData(
                    context.getString(R.string.report_form_student_no),
                    studentArr?.get(0)!!
                )
            val studentName =
                MultipartBody.Part.createFormData(
                    context.getString(R.string.report_form_student_name),
                    studentArr[1]
                )
            studentInfo = StudentInfo(studentNo,studentName)
        }
        when (format) {
            FileUploadFormat.PDF -> {
                requestBody = MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    RequestBody.create(MediaType.parse("application/pdf"), file)
                )
            }
            FileUploadFormat.IMAGE -> {
                requestBody = MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    RequestBody.create(MediaType.parse("image/jpeg"), file)
                )
            }
            null -> {
            }
        }

        val request = FileUploadRequest(requestBody)
        request.fileType = if (isReport) UploadFileType.REPORT else UploadFileType.NORMAL
        request.format = format
        request.studentInfo = studentInfo
        return request

    }


}
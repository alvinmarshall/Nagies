package com.wNagiesEducationalCenterj_9905.jobs

import android.app.Application
import android.content.Context
import androidx.work.*
import com.wNagiesEducationalCenterj_9905.common.FileUploadFormat
import com.wNagiesEducationalCenterj_9905.common.UploadFileType
import com.wNagiesEducationalCenterj_9905.common.utils.FileUploadUtil
import com.wNagiesEducationalCenterj_9905.common.utils.NotificationUtils
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.data.repository.TeacherRepository
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class UploadFilesWorker @Inject constructor(
    private val appContext: Application,
    workerParams: WorkerParameters,
    private val preferenceProvider: PreferenceProvider,
    private val teacherRepository: TeacherRepository
) :
    RxWorker(appContext, workerParams) {
    override fun createWork(): Single<Result> {
        val filePath = inputData.getString("upload_file_path")
        val isReport = inputData.getBoolean("upload_isReport", false)
        val fileUploadUtil: FileUploadUtil?
        fileUploadUtil = if (isReport) {
            val studentArr = inputData.getStringArray("student_arr")

            FileUploadUtil(appContext, filePath, isReport, studentArr)
        } else {
            FileUploadUtil(appContext, filePath, isReport, null)
        }
        val uploadData = fileUploadUtil.preparingToUpload()
        uploadData?.let { request ->
            if (request.fileType == UploadFileType.REPORT) {
                when (request.format) {
                    FileUploadFormat.PDF -> {
                        return teacherRepository
                            .uploadReportPDF(
                                preferenceProvider.getUserToken()!!,
                                request
                            )
                            .doOnSuccess {
                                if (it.status == 200) {
                                    NotificationUtils.showWorkerNotificationMessage(
                                        appContext,
                                        "Report upload complete",
                                        "file uploaded successfully"
                                    )
                                    Timber.i("file report upload: ${it.message}")
                                } else {
                                    NotificationUtils.showWorkerNotificationMessage(
                                        appContext,
                                        "Report upload failed",
                                        "file upload failed"
                                    )
                                    Timber.e("file upload status: ${it.status}")
                                }
                            }
                            .doOnError {
                                NotificationUtils.showWorkerNotificationMessage(
                                    appContext,
                                    "Report upload failed",
                                    "file upload failed"
                                )
                                Timber.e(it, "file upload error")
                            }
                            .map { Result.success() }
                    }
                    else -> {
                        return teacherRepository
                            .uploadReportIMAGE(
                                preferenceProvider.getUserToken()!!,
                                request
                            )
                            .doOnSuccess {
                                if (it.status == 200) {
                                    NotificationUtils.showWorkerNotificationMessage(
                                        appContext,
                                        "Report upload complete",
                                        "file uploaded successfully"
                                    )
                                    Timber.i("file report upload: ${it.message}")
                                } else {
                                    NotificationUtils.showWorkerNotificationMessage(
                                        appContext,
                                        "Report upload failed",
                                        "file upload failed"
                                    )
                                    Timber.e("file upload status: ${it.status}")
                                }

                            }
                            .doOnError {
                                NotificationUtils.showWorkerNotificationMessage(
                                    appContext,
                                    "Report upload failed",
                                    "file upload failed"
                                )
                                Timber.e(it, "file upload error")
                            }
                            .map { Result.success() }
                            .onErrorReturnItem(Result.failure())
                    }
                }
            } else {
                when (request.format) {
                    FileUploadFormat.PDF -> {
                        return teacherRepository
                            .uploadAssignmentPDF(
                                preferenceProvider.getUserToken()!!,
                                request.requestBody!!
                            )
                            .doOnSuccess {
                                if (it.status == 200) {
                                    NotificationUtils.showWorkerNotificationMessage(
                                        appContext,
                                        "Assignment upload complete",
                                        "file uploaded successfully"
                                    )
                                    Timber.i("file assignment upload: ${it.message}")
                                } else {
                                    NotificationUtils.showWorkerNotificationMessage(
                                        appContext,
                                        "Assignment upload failed",
                                        "file upload failed"
                                    )
                                    Timber.e("file upload status: ${it.status}")
                                }
                            }
                            .doOnError {
                                NotificationUtils.showWorkerNotificationMessage(
                                    appContext,
                                    "Assignment upload failed",
                                    "file upload failed"
                                )
                                Timber.e(it, "file upload error")
                            }
                            .map { Result.success() }
                            .onErrorReturnItem(Result.failure())

                    }
                    else -> {
                        return teacherRepository
                            .uploadAssignmentIMAGE(
                                preferenceProvider.getUserToken()!!,
                                request.requestBody!!
                            )
                            .doOnSuccess {
                                if (it.status == 200) {
                                    NotificationUtils.showWorkerNotificationMessage(
                                        appContext,
                                        "Assignment upload complete",
                                        "file uploaded successfully"
                                    )
                                    Timber.i("file assignment upload: ${it.message}")
                                } else {
                                    NotificationUtils.showWorkerNotificationMessage(
                                        appContext,
                                        "Assignment upload failed",
                                        "file upload failed"
                                    )
                                    Timber.e("file upload status: ${it.status}")
                                }
                            }
                            .doOnError {
                                NotificationUtils.showWorkerNotificationMessage(
                                    appContext,
                                    "Assignment upload failed",
                                    "file upload failed"
                                )
                                Timber.e(it, "file upload error")
                            }
                            .map { Result.success() }
                            .onErrorReturnItem(Result.failure())
                    }

                }
            }


        } ?: return Single.fromCallable { Result.failure() }
    }

    companion object {
        fun start(
            context: Context,
            filePath: String?,
            isReport: Boolean,
            studentInfo: Array<String>?
        ) {

            val data = Data.Builder()
            if (isReport) {
                data.putString("upload_file_path", filePath)
                    .putBoolean("upload_isReport", isReport)
                    .putStringArray("student_arr", studentInfo!!)
            } else {
                data.putString("upload_file_path", filePath)
                    .putBoolean("upload_isReport", isReport)
            }


            WorkManager.getInstance(context)
                .enqueue(
                    OneTimeWorkRequest
                        .Builder(UploadFilesWorker::class.java)
                        .setInputData(data.build())
                        .build()
                )
        }
    }
}
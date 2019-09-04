package com.wNagiesEducationalCenterj_9905.api

import androidx.lifecycle.LiveData
import com.wNagiesEducationalCenterj_9905.api.request.ChangePasswordRequest
import com.wNagiesEducationalCenterj_9905.api.request.ParentComplaintRequest
import com.wNagiesEducationalCenterj_9905.api.request.TeacherMessageRequest
import com.wNagiesEducationalCenterj_9905.api.response.*
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {
    @POST("users/parent")
    fun getAuthenticatedParent(@Body userEntity: UserEntity)
            : LiveData<ApiResponse<AuthResponse>>

    @POST("users/teacher")
    fun getAuthenticatedTeacher(@Body userEntity: UserEntity)
            : LiveData<ApiResponse<AuthResponse>>

    @GET("students/messages")
    fun getStudentMessages(@Header("Authorization") token: String): LiveData<ApiResponse<MessageResponse>>

    @GET("users/profile")
    fun getStudentProfile(@Header("Authorization") token: String): LiveData<ApiResponse<StudentProfileResponse>>

    @POST("students/complaints")
    fun sendParentComplaint(@Header("Authorization") token: String, @Body parentComplaint: ParentComplaintRequest)
            : Single<ParentComplaintResponse>

    @GET("students/assignment_pdf")
    fun getStudentAssignmentPDF(
        @Header("Authorization")
        token: String
    ): LiveData<ApiResponse<AssignmentResponse>>

    @GET("students/assignment_image")
    fun getStudentAssignmentImage(
        @Header("Authorization")
        token: String
    ): LiveData<ApiResponse<AssignmentResponse>>

    @GET("students/report_pdf")
    fun getStudentReportPDF(@Header("Authorization") token: String): LiveData<ApiResponse<ReportResponse>>

    @GET("students/report_image")
    fun getStudentReportImage(@Header("Authorization") token: String): LiveData<ApiResponse<ReportResponse>>

    @POST("users/change_password")
    fun requestParentAccountPasswordChange(
        @Header("Authorization") token: String,
        @Body changePassRequest: ChangePasswordRequest
    ): Observable<ChangePasswordResponse>

    @GET("students/teachers")
    fun getClassTeacher(@Header("Authorization") token: String): LiveData<ApiResponse<StudentTeachersResponse>>

    @GET("teachers/announcement")
    fun getTeachersAnnouncement(@Header("Authorization") token: String): LiveData<ApiResponse<AnnouncementResponse>>

    @GET("teachers/complaints")
    fun getTeacherComplaint(@Header("Authorization") token: String): LiveData<ApiResponse<TeacherComplaintResponse>>

    @POST("users/change_password")
    fun requestTeacherAccountPasswordChange(
        @Header("Authorization") token: String,
        @Body changePassRequest: ChangePasswordRequest
    ): Observable<ChangePasswordResponse>

    @GET("users/profile")
    fun getTeacherProfile(@Header("Authorization") token: String): LiveData<ApiResponse<TeacherProfileResponse>>

    @POST("teachers/send_message")
    fun sendTeacherMessage(@Header("Authorization") token: String, @Body teacherMessage: TeacherMessageRequest)
            : Single<TeacherMessageResponse>

    @GET("students/circular")
    fun getCircular(@Header("Authorization") token: String): LiveData<ApiResponse<CircularResponse>>

    @GET("students/billing")
    fun getBilling(@Header("Authorization") token: String): LiveData<ApiResponse<BillingResponse>>

    @GET("students/announcement")
    fun getStudentAnnouncement(@Header("Authorization") token: String): LiveData<ApiResponse<AnnouncementResponse>>

    @Multipart
    @POST("teachers/upload_assignment_pdf")
    fun uploadAssignmentPDF(
        @Header("Authorization") token: String, @Part file: MultipartBody.Part
    ): Single<FileUploadResponse>

    @Multipart
    @POST("teachers/upload_assignment_image")
    fun uploadAssignmentIMAGE(
        @Header("Authorization") token: String, @Part file: MultipartBody.Part
    ): Single<FileUploadResponse>

    @GET("teachers/class_student")
    fun fetchClassStudents(@Header("Authorization") token: String): LiveData<ApiResponse<ClassStudentResponse>>

    @Multipart
    @POST("teachers/upload_report_pdf")
    fun uploadReportPDF(
        @Header("Authorization") token: String, @Part file: MultipartBody.Part?, @Part studentNo: MultipartBody.Part?, @Part studentName: MultipartBody.Part?
    ): Single<FileUploadResponse>

    @Multipart
    @POST("teachers/upload_report_image")
    fun uploadReportIMAGE(
        @Header("Authorization") token: String, @Part file: MultipartBody.Part?, @Part studentNo: MultipartBody.Part?, @Part studentName: MultipartBody.Part?
    ): Single<FileUploadResponse>

    @GET("teachers/get_upload")
    fun getUploadedFile(
        @Header("Authorization") token: String,
        @Query(
            "format"
        ) format: String?,
        @Query("type") type: String?
    ): Observable<ExplorerResponse>

    @DELETE("teachers/delete_upload")
    fun deleteUploadedFile(
        @Header("Authorization") token: String,
        @Query("id") id: String?,
        @Query("path") path: String?,
        @Query(
            "format"
        ) format: String?,
        @Query("type") type: String?
    ): Single<ExplorerDeleteResponse>

}
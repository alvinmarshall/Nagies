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
import retrofit2.http.*

interface ApiService {
    @POST("users")
    fun getAuthenticatedUser(
        @Query("role") role: String,
        @Body userEntity: UserEntity
    ): LiveData<ApiResponse<AuthResponse>>

    @GET("message")
    fun getStudentMessages(@Header("Authorization") token: String): LiveData<ApiResponse<MessageResponse>>

    @GET("users/profile")
    fun getStudentProfile(@Header("Authorization") token: String): LiveData<ApiResponse<StudentProfileResponse>>

    @POST("message")
    fun sendParentComplaint(
        @Header("Authorization") token: String,
        @Body parentComplaint: ParentComplaintRequest,
        @Query("to") to: String = "teacher"
    ): Single<ParentComplaintResponse>

    @GET("file/path")
    fun getStudentAssignmentPDF(
        @Header("Authorization") token: String,
        @Query("type") type: String = "assignment",
        @Query("format") format: String = "pdf"
    ): LiveData<ApiResponse<AssignmentResponse>>

    @GET("file/path")
    fun getStudentAssignmentImage(
        @Header("Authorization") token: String,
        @Query("type") type: String = "assignment",
        @Query("format") format: String = "image"
    ): LiveData<ApiResponse<AssignmentResponse>>

    @GET("file/path")
    fun getStudentReportPDF(
        @Header("Authorization") token: String,
        @Query("type") type: String = "report",
        @Query("format") format: String = "pdf"
    ): LiveData<ApiResponse<ReportResponse>>

    @GET("file/path")
    fun getStudentReportImage(
        @Header("Authorization") token: String,
        @Query("type") type: String = "report",
        @Query("format") format: String = "image"
    ): LiveData<ApiResponse<ReportResponse>>

    @POST("users/change_password")
    fun requestParentAccountPasswordChange(
        @Header("Authorization") token: String,
        @Body changePassRequest: ChangePasswordRequest
    ): Observable<ChangePasswordResponse>

    @GET("students/teacher")
    fun getClassTeacher(@Header("Authorization") token: String): LiveData<ApiResponse<StudentTeachersResponse>>

    @GET("message")
    fun getTeachersAnnouncement(
        @Header("Authorization") token: String,
        @Query("from") from: String = "announcement"
    ): LiveData<ApiResponse<AnnouncementResponse>>

    @GET("message")
    fun getComplaint(
        @Header("Authorization") token: String,
        @Query("from") from: String = "complaint"
    ): LiveData<ApiResponse<ComplaintResponse>>

    @POST("users/change_password")
    fun requestTeacherAccountPasswordChange(
        @Header("Authorization") token: String,
        @Body changePassRequest: ChangePasswordRequest
    ): Observable<ChangePasswordResponse>

    @GET("users/profile")
    fun getTeacherProfile(@Header("Authorization") token: String): LiveData<ApiResponse<TeacherProfileResponse>>

    @POST("message")
    fun sendTeacherMessage(
        @Header("Authorization") token: String,
        @Body teacherMessage: TeacherMessageRequest,
        @Query("to") to: String = "parent"
    ): Single<TeacherMessageResponse>

    @GET("file/path")
    fun getCircular(
        @Header("Authorization") token: String,
        @Query("type") type: String = "circular",
        @Query("format") format: String = "image"
    ): LiveData<ApiResponse<CircularResponse>>

    @GET("file/path")
    fun getBilling(
        @Header("Authorization") token: String,
        @Query("type") type: String = "bill",
        @Query("format") format: String = "image"
    ): LiveData<ApiResponse<BillingResponse>>

    @GET("message")
    fun getStudentAnnouncement(
        @Header("Authorization") token: String,
        @Query("from") from: String = "announcement"
    ): LiveData<ApiResponse<AnnouncementResponse>>

    @Multipart
    @POST("file/uploads")
    fun uploadAssignmentPDF(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Query("type") type: String = "assignment"
    ): Single<FileUploadResponse>

    @Multipart
    @POST("file/uploads")
    fun uploadAssignmentIMAGE(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Query("type") type: String = "assignment"
    ): Single<FileUploadResponse>

    @GET("teacher/student")
    fun fetchClassStudents(@Header("Authorization") token: String): LiveData<ApiResponse<ClassStudentResponse>>

    @Multipart
    @POST("file/uploads")
    fun uploadReportPDF(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part?,
        @Part studentNo: MultipartBody.Part?,
        @Part studentName: MultipartBody.Part?,
        @Query("type") type: String = "report"
    ): Single<FileUploadResponse>

    @Multipart
    @POST("file/uploads")
    fun uploadReportIMAGE(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part?,
        @Part studentNo: MultipartBody.Part?,
        @Part studentName: MultipartBody.Part?,
        @Query("type") type: String = "report"
    ): Single<FileUploadResponse>

    @GET("file/path")
    fun getUploadedFile(
        @Header("Authorization") token: String,
        @Query("type") type: String?,
        @Query("format") format: String?
    ): Observable<ExplorerResponse>

    @DELETE("file/{id}")
    fun deleteUploadedFile(
        @Header("Authorization") token: String,
        @Path("id") id: String?,
        @Query("type") type: String?,
        @Query("format") format: String?,
        @Query("path") path: String?
    ): Single<ExplorerDeleteResponse>

    @GET("file/path")
    fun fetchStudentTimetable(
        @Header("Authorization") token: String,
        @Query("type") type: String = "timetable",
        @Query("format") format: String = "image"
    ): LiveData<ApiResponse<TimetableResponse>>

    @DELETE("message/{id}/{type}")
    fun deleteMessage(
        @Header("Authorization") token: String,
        @Path("id") id: Int?,
        @Path("type") type: String?
    ): Single<DeleteMessageResponse>

}
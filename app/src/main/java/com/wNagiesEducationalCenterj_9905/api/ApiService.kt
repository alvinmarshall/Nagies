package com.wNagiesEducationalCenterj_9905.api

import androidx.lifecycle.LiveData
import com.wNagiesEducationalCenterj_9905.api.request.ChangePasswordRequest
import com.wNagiesEducationalCenterj_9905.api.request.ParentComplaintRequest
import com.wNagiesEducationalCenterj_9905.api.request.TeacherMessageRequest
import com.wNagiesEducationalCenterj_9905.api.response.*
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import com.wNagiesEducationalCenterj_9905.vo.DownloadRequest
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
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

    @GET("students/profile")
    fun getStudentProfile(@Header("Authorization") token: String): LiveData<ApiResponse<StudentProfileResponse>>

    @POST("students/complaints")
    fun sendParentComplaint(@Header("Authorization") token: String, @Body parentComplaint: ParentComplaintRequest)
            : Single<ParentComplaintResponse>

    @POST("students/download")
    @Streaming
    fun getFilesFromServer(
        @Header("Authorization") token: String,
        @Body fileUrl: DownloadRequest
    ): Observable<Response<ResponseBody>>

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

    @POST("students/change_password")
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

    @POST("teachers/change_password")
    fun requestTeacherAccountPasswordChange(
        @Header("Authorization") token: String,
        @Body changePassRequest: ChangePasswordRequest
    ): Observable<ChangePasswordResponse>

    @GET("teachers/profile")
    fun getTeacherProfile(@Header("Authorization") token: String): LiveData<ApiResponse<TeacherProfileResponse>>

    @POST("teachers/send_message")
    fun sendTeacherMessage(@Header("Authorization") token: String, @Body teacherMessage: TeacherMessageRequest)
            : Single<TeacherMessageResponse>
}
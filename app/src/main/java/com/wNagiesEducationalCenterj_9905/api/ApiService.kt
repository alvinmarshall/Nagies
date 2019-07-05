package com.wNagiesEducationalCenterj_9905.api

import androidx.lifecycle.LiveData
import com.wNagiesEducationalCenterj_9905.api.response.MessageResponse
import com.wNagiesEducationalCenterj_9905.api.request.ParentComplaintRequest
import com.wNagiesEducationalCenterj_9905.api.response.ParentComplaintResponse
import com.wNagiesEducationalCenterj_9905.api.response.StudentProfileResponse
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import io.reactivex.Single
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
}
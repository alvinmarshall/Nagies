package com.wNagiesEducationalCenterj_9905.api

import androidx.lifecycle.LiveData
import com.wNagiesEducationalCenterj_9905.api.response.MessageResponse
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("users/parent")
    fun getAuthenticatedParent(@Body userEntity: UserEntity)
            : LiveData<ApiResponse<AuthResponse>>

    @POST("users/teacher")
    fun getAuthenticatedTeacher(@Body userEntity: UserEntity)
            : LiveData<ApiResponse<AuthResponse>>

    @GET("students/messages")
    fun getStudentMessages(@Header("Authorization") token:String):LiveData<ApiResponse<MessageResponse>>
}
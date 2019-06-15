package com.wNagiesEducationalCenterj_9905.api

import androidx.lifecycle.LiveData
import com.wNagiesEducationalCenterj_9905.vo.AuthResource
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("students/android_post_parent1.php")
    fun getAuthenticatedParent(@Field("usr") Username:String,@Field("pwd") Password:String )
            :LiveData<ApiResponse<AuthResponse>>

    @FormUrlEncoded
    @POST("teacher/android_post_teacher1.php")
    fun getAuthenticatedTeacher(@Field("usr") Username:String,@Field("pwd") Password:String )
            :LiveData<ApiResponse<AuthResponse>>
}
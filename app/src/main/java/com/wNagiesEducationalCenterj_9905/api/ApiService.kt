package com.wNagiesEducationalCenterj_9905.api

import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("students/android_post_parent1.php")
    fun getAuthenticatedParent(@Field("usr") Username:String,@Field("pwd") Password:String )
            :Flowable<List<AuthResponse>>
}
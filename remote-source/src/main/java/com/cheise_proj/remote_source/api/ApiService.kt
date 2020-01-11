package com.cheise_proj.remote_source.api

import com.cheise_proj.remote_source.model.dto.UserDto
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST
    fun getAuthenticateUser(
        @Query("role") role: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<UserDto>
}
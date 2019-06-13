package com.wNagiesEducationalCenterj_9905.api

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("status")
    val Status:Int,
    @SerializedName("username")
    val Username:String,
    @SerializedName("password")
    val Password:String
)
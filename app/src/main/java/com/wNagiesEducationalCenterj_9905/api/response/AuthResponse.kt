package com.wNagiesEducationalCenterj_9905.api.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("uuid")
    val Id:Int,
    @SerializedName("status")
    val Status:Int,
    @SerializedName("message")
    val Message:String,
    @SerializedName("token")
    val Token:String,
    @SerializedName("imageUrl")
    val image:String,
    val role:String,
    val name:String,
    val level:String
)
package com.wNagiesEducationalCenterj_9905.api.response


import com.google.gson.annotations.SerializedName
import com.wNagiesEducationalCenterj_9905.data.db.Entities.CircularEntity

data class CircularResponse(
    val type: String,
    val message: String,
    val count: Int,
    @SerializedName("Circular")
    val circular: List<CircularEntity>,
    val status: Int
)
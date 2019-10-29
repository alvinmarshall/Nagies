package com.wNagiesEducationalCenterj_9905.api.response


import com.google.gson.annotations.SerializedName
import com.wNagiesEducationalCenterj_9905.data.db.Entities.StudentProfileEntity

data class StudentProfileResponse(
    @SerializedName("uid")
    val id:Int,
    val type: String,
    val status: Int,
    val message: String,
    val count: Int,
    val studentProfile: StudentProfileEntity
)
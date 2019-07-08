package com.wNagiesEducationalCenterj_9905.api.response


import com.google.gson.annotations.SerializedName
import com.wNagiesEducationalCenterj_9905.data.db.Entities.AssignmentEntity

data class AssignmentResponse(
    val type: String,
    val status: Int,
    val message: String,
    val count: Int,
    @SerializedName("Assignment")
    val assignment: List<AssignmentEntity>
)
package com.wNagiesEducationalCenterj_9905.api.response


data class ParentComplaintResponse(
    val status:Int,
    val type: String,
    val message: String,
    val id: String,
    val errors: List<String>
)
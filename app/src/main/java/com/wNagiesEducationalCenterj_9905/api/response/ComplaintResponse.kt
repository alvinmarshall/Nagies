package com.wNagiesEducationalCenterj_9905.api.response

import com.wNagiesEducationalCenterj_9905.data.db.Entities.ComplaintEntity


data class ComplaintResponse(
    val message: String,
    val count: Int,
    val complaints: List<ComplaintEntity>,
    val status: Int
)
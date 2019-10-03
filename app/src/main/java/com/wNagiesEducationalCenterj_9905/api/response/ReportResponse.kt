package com.wNagiesEducationalCenterj_9905.api.response


import com.google.gson.annotations.SerializedName
import com.wNagiesEducationalCenterj_9905.data.db.Entities.ReportEntity

data class ReportResponse(
    val type: String,
    val message: String,
    val count: Int,
    @SerializedName("data")
    val report: List<ReportEntity>,
    val status: Int
)
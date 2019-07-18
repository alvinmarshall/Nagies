package com.wNagiesEducationalCenterj_9905.api.response


import com.wNagiesEducationalCenterj_9905.data.db.Entities.ReportEntity

data class ReportResponse(
    val type: String,
    val message: String,
    val count: Int,
    val report: List<ReportEntity>,
    val status: Int
)
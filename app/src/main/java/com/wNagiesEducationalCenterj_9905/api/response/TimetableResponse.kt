package com.wNagiesEducationalCenterj_9905.api.response


import com.wNagiesEducationalCenterj_9905.data.db.Entities.TimeTableEntity

data class TimetableResponse(
    val status: Int,
    val count: Int,
    val timeTable: List<TimeTableEntity>
)
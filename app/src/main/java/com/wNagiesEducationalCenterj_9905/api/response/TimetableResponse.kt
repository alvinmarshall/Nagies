package com.wNagiesEducationalCenterj_9905.api.response


import com.google.gson.annotations.SerializedName
import com.wNagiesEducationalCenterj_9905.data.db.Entities.TimeTableEntity

data class TimetableResponse(
    val status: Int,
    val count: Int,
    @SerializedName("data")
    val timetable: List<TimeTableEntity>
)
package com.wNagiesEducationalCenterj_9905.api.response


import com.wNagiesEducationalCenterj_9905.data.db.Entities.TeacherComplaintEntity

data class TeacherComplaintResponse(
    val message: String,
    val count: Int,
    val complaints: List<TeacherComplaintEntity>,
    val status: Int
)
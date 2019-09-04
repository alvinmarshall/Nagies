package com.wNagiesEducationalCenterj_9905.api.response


import com.wNagiesEducationalCenterj_9905.data.db.Entities.TeacherProfileEntity

data class TeacherProfileResponse(
    val type: String,
    val message: String,
    val count: Int,
    val teacherProfile: TeacherProfileEntity,
    val status: Int
)
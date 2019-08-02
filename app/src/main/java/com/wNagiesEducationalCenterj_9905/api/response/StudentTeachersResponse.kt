package com.wNagiesEducationalCenterj_9905.api.response


import com.wNagiesEducationalCenterj_9905.data.db.Entities.StudentTeacherEntity

data class StudentTeachersResponse(
    val type: String,
    val message: String,
    val count: Int,
    val studentTeachers: List<StudentTeacherEntity>,
    val status: Int
)
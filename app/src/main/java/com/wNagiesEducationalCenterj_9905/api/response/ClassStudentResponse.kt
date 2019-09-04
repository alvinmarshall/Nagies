package com.wNagiesEducationalCenterj_9905.api.response


import com.wNagiesEducationalCenterj_9905.data.db.Entities.ClassStudentEntity

data class ClassStudentResponse(
    val message: String,
    val count: Int,
    val classStudent: List<ClassStudentEntity>,
    val status: Int
)
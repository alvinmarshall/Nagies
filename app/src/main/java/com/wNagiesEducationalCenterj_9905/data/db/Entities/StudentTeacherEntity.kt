package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_teacher")
data class StudentTeacherEntity(
    val uid: String,
    val teacherName: String,
    val gender: String,
    val contact: String,
    var imageUrl: String?,
    var token:String?
) {
    @PrimaryKey
    var id: Int = 0
}
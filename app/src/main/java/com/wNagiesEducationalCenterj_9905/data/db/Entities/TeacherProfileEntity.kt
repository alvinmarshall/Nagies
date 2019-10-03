package com.wNagiesEducationalCenterj_9905.data.db.Entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teacher_profile")
data class TeacherProfileEntity(
    val ref: String,
    val name: String,
    val dob: String,
    val gender: String,
    val contact: String,
    val admissionDate: String,
    val facultyName: String,
    val level: String,
    val username:String,
    var imageUrl: String?,
    var token: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}